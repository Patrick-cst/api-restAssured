package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

public class AuthTest {

	
	@Test
	public void validarApiReal() {
		given()
		    .log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
    public void deveObterClima(){
        given()
//            .spec(reqSpec)
            .queryParam("q", "Fortaleza")
            .queryParam("appid", "67aeb962099868f850e143c3fa0be8e8")
            .queryParam("units", "metric")
        .when()
            .get("http://api.openweathermap.org/data/2.5/weather")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Fortaleza"))
            .body("coord.lon", is(-46.64f))
            .body("main.temp", greaterThan(20f))
        ;
    }
	
	
	@Test
	public void naoDeveAcessarSeMsenha() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	//a02212af2a8e498359ccb288ef7ebd64
	
	@Test
	public void deveAcessarComAuthBasica() {
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveAcessarComAuthBasica2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveAcessarComAuthBasica3() {
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	///////
	@Test
	public void cadastrarSeuBarriga() {
		given()
			.log().all()
//			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("http://seubarriga.wcaquino.me/cadastro")
		.then()
			.log().all()
			.statusCode(200)
			
		;
	}
	///////////
	
	@Test
	public void deveFazerAutenticacaoComTOkenJWT() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "pac-cs@hotmail.com");
		login.put("senha", "teste");
		
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")	
	;
		
		System.out.println(token);
		
		given()
			.log().all()
			.header("Authorization", "JWT "+ token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Patrick "))
			;
	}
	
	
	@Test
	public void deveFazerAutenticacaoComTOkenJWTWeb() {
		
		String cookie = given()
			.log().all()
			.formParam("email", "pac-cs@hotmail.com")
			.formParam("senha", "teste")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
			;
		
		cookie = cookie.split("=")[1].split(";")[0];
		
		String body = given()
			.log().all()
			.cookie("connect.sid", cookie)
		.when()
			.get("http://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("html.body.table.tbody.tr[0].td[0]", is("Patrick "))
			.extract().body().asString()
//			.extract().path("html.body.table.tbody.tr[0].td[0]")
		;
		//
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
	}
}
