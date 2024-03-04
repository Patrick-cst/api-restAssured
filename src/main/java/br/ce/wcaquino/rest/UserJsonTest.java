package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import groovy.util.logging.Log;
import groovyjarjarantlr.collections.List;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
//		RestAssured.port = 443;
//		RestAssured.basePath = "/v2";
	}
	
	@Test
	public void validarPrimeiroNivel() {
		
		given()
		.log().all()
		.when()
			.get("/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18));
	}
	
	@Test
	public void validarPrimeiroNivelOutraForma() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");
	
		
		assertEquals(response.path("id"), 1);
		////
		assertEquals(1, response.path("%s", "id"));
		//////////
		JsonPath jpath = new JsonPath(response.asString());
		
		assertEquals(1, jpath.getInt("id"));
		///////////////////
		int id = JsonPath.from(response.asString()).getInt("id");
		
		assertEquals(1, id);
	}
	
	@Test
	public void validarSegundoNivel() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"));
	}
	
	@Test
	public void validarTerceiroNivelComLista() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			.body("filhos.rua", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			//verificar se possui o item LUIZINHO -ABAIXO
			.body("filhos.name", hasItem("Luizinho"))
			.body("filhos.name", hasItems("Luizinho", "Zezinho"))
			;
	}
	
	@Test
	public void validarMensagemDeErro() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"))
			;
	}

	@Test
	public void validarListRaiz() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null))
			;
	}
	
	@Test
	public void devoFazerVerificacoesAvancadas() {

		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)

			.body("S", hasSize(3))

			.body("age.findAll(it <= 25}.size()", is(2))

			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))

			.body("findAll{it.age <= 25 && it.age 20].name", hasItem("Maria Joaquina"))

			.body("findAllfit.age <= 25][0].name", is("Maria Joaquina"))

			.body("findAll(it.age <= 25}[-1].name", is("Ana Júlia"))

			.body("findlit.age 25} name", is("Maria Joaquina"))

			.body("findAll(it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))

			.body("findAll[it.name.length() > 10) name", hasItems("João da Silva", "Maria Joaquina"))

			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')].collect{it.toUpperCase()).toArray()",allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))

			.body("age.collect{it * 2}", hasItems(60, 50, 40))

			.body("id.max()", is(3)).body("salary.min()", is(1234.5678f))

			.body("salary.findAll(it != null) sum", is(closeTo(3734.5678, 0.001)))
			.body("salary.findAll(it != null) sum()", allOf(greaterThan(3000d), lessThan(5000d)));
		}
	
	@Test
	public void devoUnirJsonPathComJAVA() {
//		 List<String> teste = List.of("teste", "ola", "tdbem");
		
		ArrayList<String> names = 
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.extract().path("name.findAll{it.startsWith('Maria')}")
			;
		assertEquals(1, names.size());
		assertTrue(names.get(0).equalsIgnoreCase("Maria Joaquina"));
		assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
//		names.addAll(teste);
		assertTrue(names.contains("Maria Joaquina"));
		System.out.println(names);
		;
	}
	
	@Test
	public void validarXML() {
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
		.statusCode(200)
		.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))

		.body("users.user.@id", hasItems ("1", "2","3")) 
		.body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))

		.body("users.user.findAll{it.name.toString().contains('n')}. name", hasItems ("Maria Joaquina", "Ana Julia"))

		.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))

		.body("users.user.age.collect{it.toInteger() * 2}", hasItems(40, 50, 60))

		.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"));
	}
	
	@Test
	public void validarXMLejava() {
		
		ArrayList<NodeImpl> nomes = given()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
		.statusCode(200)
		
		.extract().path("users.user.findAll{it.name.toString().contains('n')}")
		;
		System.out.println("");
		
		assertEquals(2, nomes.size() );
		assertTrue("Ana Julia".equalsIgnoreCase(nomes.get(1).toString()));
	
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXPath() {

	given()

	.when()

	.get("https://restapi.wcaquino.me/usersXML")

	.then()

		.statusCode(200)

		.body(hasXPath("count(/users/user)", is("3")))

		.body(hasXPath("/users/user[@id = '1']"))

		.body(hasXPath("//user[@id = '2']"))
		
		.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))

		.body (hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))

		.body(hasXPath("/users/user/name", is("João da Silva")))

		.body (hasXPath("//name", is("João da Silva")))

		.body (hasXPath("/users/user[2]/name", is("Maria Joaquina"))) .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))

		.body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
	
		.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))

		.body (hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))

		.body (hasXPath("//user[age > 20][age <30]/name", is("Maria Joaquina")))
	
		;
		
		
	
	}
}
