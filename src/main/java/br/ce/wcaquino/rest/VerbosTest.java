package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class VerbosTest {

	@Test  
	public void devesalvarUsuario() { 
	given()
		.log().all()
		.contentType("application/json")  
		.body("{\"name\":\"Patrick\", \"age\": 28}")
	.when()
		.post("https://restapi.wcaquino.me/users")  
	.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))  
		.body("name", is("Patrick"))  
		.body("age", is(50)) 
		;

	}
	
	@Test  
	public void devesalvarUsuarioSemNome() { 
	given()
		.log().all()
		.contentType("application/json")  
		.body("{\"age\": 28}")
	.when()
		.post("https://restapi.wcaquino.me/users")  
	.then()
		.log().all()
		.statusCode(400)
		.body("id", is(nullValue()))  
		.body("error", is("Name é um atributo obrigatório"))
		;

	}
	
	@Test  
	public void devesalvarUsuarioComXML() { 
	given()
		.log().all()
		.contentType(ContentType.XML)  
		.body("<user><name>Patrick</name><age>30</age></user>")
	.when()
		.post("https://restapi.wcaquino.me/usersXML")  
	.then()
		.log().all()
		.statusCode(201)
		.body("user.id", is(notNullValue()))  
		.body("user.name", is("Patrick"))  
		.body("user.age", is("30")) 
		;

	}
	
	@Test  
	public void deveAlterarUsuarioComPUT() { 
	given()
		.log().all()
		.contentType("application/json")  
		.body("{\"name\":\"Patrick alterado\", \"age\": 58}")
	.when()
		.put("https://restapi.wcaquino.me/users/1")  
	.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1)) 
		.body("name", is("Patrick alterado"))  
		.body("age", is(58)) 
		.body("salary", is(1234.5678f))
		;

	}
	
	@Test  
	public void deveDeletar() { 
	given()
		.log().all()
	.when()
		.delete("https://restapi.wcaquino.me/users/1")  
	.then()
		.log().all()
		.statusCode(204)
//		.body("id", ) 
		;

	}
	
	@Test  
	public void naoDeveDeletarUsuarioInexistente() { 
	given()
		.log().all()
	.when()
		.delete("https://restapi.wcaquino.me/users/1000")  
	.then()
		.log().all()
		.statusCode(400)
		.body("error", is("Registro inexistente"))
		
		;

	}
	
	@Test  
	public void deveSalvarUsuarioUsandoMap() { 
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("name", "Usuario via map");
	params.put("age", 28);
		
	given()
		.log().all()
		.contentType("application/json")  
		.body(params)
	.when()
		.post("https://restapi.wcaquino.me/users")  
	.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))  
		.body("name", is("Usuario via map"))  
		.body("age", is(28)) 
		;

	}
	
	@Test  
	public void deveSalvarUsuarioUsandoObjetivo() { 
	Users user = new Users("Usuario via objeto", 35);
		
	given()
		.log().all()
		.contentType("application/json")  
		.body(user)
	.when()
		.post("https://restapi.wcaquino.me/users")  
	.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))  
		.body("name", is("Usuario via objeto"))  
		.body("age", is(35)) 
		;

	}
	
	@SuppressWarnings("deprecation")
	@Test  
	public void deveDeserializarObjetivoSalvarUsuario() { 
	Users user = new Users("Usuario deserializado", 35);
		
	Users usuarioInserido = given()
		.log().all()
		.contentType("application/json")  
		.body(user)
	.when()
		.post("https://restapi.wcaquino.me/users")  
	.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(Users.class)
		;

	System.out.println(usuarioInserido.toString());
	assertThat(usuarioInserido.getId(), notNullValue());
	assertEquals("Usuario deserializado", usuarioInserido.getName());
	assertThat(usuarioInserido.getAge(), is(35));
	}
	
	@Test  
	public void deveSalvarUsuarioComXMLUsandoObjeto() { 
		
	Users user = new Users("Usuario xml", 40);
	given()
		.log().all()
		.contentType(ContentType.XML)  
		.body(user)
	.when()
		.post("https://restapi.wcaquino.me/usersXML")  
	.then()
		.log().all()
		.statusCode(201)
		.body("user.id", is(notNullValue()))  
		.body("user.name", is("Usuario xml"))  
		.body("user.age", is("40")) 
		;

	}
	
	@Test  
	public void deveSalvarUsuarioDeserializadoComXML() { 
		
	Users user = new Users("Usuario xml", 40);
	
	User userInserido =
	given()
		.log().all()
		.contentType(ContentType.XML)  
		.body(user)
	.when()
		.post("https://restapi.wcaquino.me/usersXML")  
	.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(User.class)
		;
		
	System.out.println(userInserido);
	}
	
}

