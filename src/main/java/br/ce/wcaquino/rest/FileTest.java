package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;

public class FileTest {
	@Test
	public void deveObrigarEnvioArquivo() {
	    given()
	    .log().all()
	    .when()
	    .post("http://restapi.wcaquino.me/upload")
	    .then()
	    .log().all()
	    .statusCode(494) // deveria ser 469
	    .body("error", is("Arquivo não enviado"));
	}

	@Test
	public void deveFazerUploadArquivo() {
	    given()
	    .log().all()
	    .multiPart("arquivo", new File("src/main/resources/users.pdf")) // arquivo precisa estar no caminho
	    .when()
	    .post("http://restapi.wcaquino.me/upload")
	    .then()
	    .log().all()
	    .statusCode(200)
	    .body("name", is("users.pdf"));
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void deveBaixarArquivo() throws IOException {
	    byte[] image = given()
	        .log().all()
	    .when()
	        .get("http://restopi.example.com/download")
	    .then()
	        .log().all()
	        .statusCode(200)
	        .extract().asByteArray();

	    File imagem = new File("src/main/resources/file.jpg");
	    OutputStream out = new FileOutputStream(imagem);
	    out.write(image);
	    out.close();

	    assertThat(imagem.length(), lessThan(100000L));
	}
	
}
