package lucas.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.print.attribute.standard.OutputDeviceAssigned;

import org.junit.Assert;
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
	
	@Test
	public void deveBaixarArquivo() throws IOException {
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			.log().all()
			.statusCode(200)
			.extract().asByteArray()
		;
		
		File imagem = new File("src/main/java/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		System.out.println(imagem.length());
		Assert.assertThat(imagem.length(), lessThan(100000L));
	}
}
