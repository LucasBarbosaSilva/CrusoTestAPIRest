package lucas.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.Test;


public class FileTest {
	@Test
	public void deveObrigarEnvioArquivo() {
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)  //Deveria ser 400
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveFazerEnvioArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/java/resources/pdf.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)  
			.body("name", is("pdf.pdf"))
		;
	}
	

	@Test
	public void naoDeveFazerEnvioArquivoGrande() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/java/resources/14 - 2.bmp"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(1200L))
			.statusCode(413)
		;
	}
}
