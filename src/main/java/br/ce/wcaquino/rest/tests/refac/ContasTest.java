package br.ce.wcaquino.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.BarrigaTest;
import br.ce.wcaquino.rest.utils.BarrigaUtils;
import io.restassured.RestAssured;

public class ContasTest extends BaseTest {
	
	
	
	@Test
	public void T01_IncluirUmaContaComSuceso() {	
		given()
			.body("{ \"nome\": \"Conta inserida\"}")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
				
			;

	}
	
	@Test
	public void deveAlterarContaComSucesso() {
	    Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para alterar");
	    given()
	        .body("{\"nome\": \"Conta alterada\"}")
	        .pathParam("id", CONTA_ID)
	    .when()
	        .put("/contas/{id}")
	    .then()
	        .statusCode(200)
	        .body("nome", is("Conta alterada"));
	}

	public void naoDeveInserirContaMesmoNome() {
	    given()
	        .body("{\"nome\": \"Conta mesmo nome\"}")
	    .when()
	        .post("/contas")
	    .then()
	        .statusCode(400)
	        .body("error", is("Já existe uma conta com esse nome!"));
	}

	
	
}
