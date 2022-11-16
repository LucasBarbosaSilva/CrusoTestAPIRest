package lucas.rest;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;


public class OlaMundoTest {
	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertEquals("Ola Mundo!", response.getBody().asString());
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200); 							
		//throw new RuntimeException(); 						//Gera um erro
		
//		ValidatableResponse validacao = response.then();
//		validacao.statusCode(201); 							//Gera uma falha
	}
}
