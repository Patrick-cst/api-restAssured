package br.ce.wcaquino.rest.tests.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.utils.BarrigaUtils;
import io.restassured.RestAssured;

public class SaldoTest extends BaseTest {
	
	
	@Test
	public void T09_deveCalcularSaldoContas() {
		Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para saldo");
		given()

		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", Matchers.is("534.00"))
			.log().all()
			
			;
	}
	
//	public Integer getSaldo() {
//	    return RestAssured.get("find{it.conta_id == "+getIdContaPeloNome("Conta para saldo")+"}.saldo")
//	        .then()
//	        .extract().path("saldo");
//	}
//	
	
	
}
