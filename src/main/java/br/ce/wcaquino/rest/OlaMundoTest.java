package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	
	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
		assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		assertTrue(response.statusCode() == 200);
		assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}

	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		get("https://restapi.wcaquino.me/ola").then().statusCode(200);
		
		given()
		//Pré condições
		.when() 
			.get("https://restapi.wcaquino.me/ola")
		.then()	
			.statusCode(200);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void devoConhecerMatcherHamcrest() {
		assertThat("Maria", Matchers.is("Maria"));
		assertThat(128, Matchers.is(128));
		//Matchers.greaterThan
		assertThat(128d, Matchers.greaterThan(120d));
		assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,2,3,4,5);
		
		//quantidade de itens que tem na lista
		assertThat(impares, hasSize(5));
		
		//validar se todos itens da lista
		assertThat(impares, contains(1,2,3,4,5));
		
		//Validar se a lista contem todos os itens, independente da ordem
		assertThat(impares, containsInAnyOrder(1,4, 2,3,5));
		
		assertThat(impares, hasItem(1));
		//Valida se tem o item ou os itens na lista
		assertThat(impares, hasItems(1,2));
		
		assertThat("Maria", is(not("Joao")));
		
		assertThat("Maria", anyOf(is("Joao"), is("Valentina")));
		
		assertThat("Maria", allOf(startsWith("Joao"), endsWith("Valentina"), containsString("qui")));
	}
	
	@Test
	public void validarBody() {
		given()
		//Pré condições
		.when() 
			.get("https://restapi.wcaquino.me/ola")
		.then()	
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}
}
