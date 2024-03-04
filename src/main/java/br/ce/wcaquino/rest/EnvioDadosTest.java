package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.hamcrest.xml.HasXPath;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class EnvioDadosTest {

	
	@Test
	public void deveENviarValorViaQueryXML() {
		given()
			.log().all()	
		.when()	
			.get("https://restapi.wcaquino.me/v2/users?format=xml")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
		;
	}
	
	@Test
	public void deveENviarValorViaQueryJson() {
		given()
			.log().all()	
		.when()	
			.get("https://restapi.wcaquino.me/v2/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
	}
	
	@Test
	public void deveEnviarValorViaQueryParam() {
	    given()
	        .log().all()
	        .queryParam("format", "xml")
	        .queryParam("outra", "coisa")
	    .when()
	        .get("https://restapi.wcaquino.me/v2/users")
	    .then()
	        .log().all()
	        .statusCode(200)
	        .contentType(ContentType.XML)
	        .contentType(Matchers.containsString("utf-8"));
	}
	
	
	@Test
	public void deveFazerBuscasEmHTML() {
	    given()
	        .log().all()
	    .when()
	        .get("https://restapi.wcaquino.me/v2/users")
	    .then()
	        .log().all()
	        .statusCode(200)
	        .contentType(ContentType.HTML)
	        .body("html.body.div.table.tbody.tr.size()", Matchers.is(3))
	        .body("html.body.div.table.tbody.tr[1].td[2]", Matchers.is("25"))
	        .appendRootPath("html.body.div.table.tbody")
	        .body("tr.find{it.toString().startsWith('2')}.td[1]", Matchers.is("Maria Joaquina"));
	}

	
	@Test  
	public void deveFazerBuscasComXpathEmHTML() {  
	given()  
		.log().all()
	.when()  
	.get("https://restapi.wcaquino.me/v2/users?format=clean")  
	.then()  
	.log().all()
	.statusCode(200)  
	.contentType(ContentType.HTML)  
//	.body(hasXPath("count(//table/tr)",  Matchers.is("4")))  
//	.body(HasXPath("//td[text() = '2']/../td[2]",  Matchers.is("Maria Joaquina"))) 
	;
	}

}
