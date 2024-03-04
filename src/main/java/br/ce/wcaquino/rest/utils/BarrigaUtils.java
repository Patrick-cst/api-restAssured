package br.ce.wcaquino.rest.utils;

import io.restassured.RestAssured;

public class BarrigaUtils {

	public static Integer getIdContaPeloNome(String nome) {
	    return RestAssured.get("/contas?nome=" + nome)
	        .then()
	        .extract().path("id[0]");
	}
}
