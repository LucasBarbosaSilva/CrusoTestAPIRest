package lucas.rest;

import static io.restassured.RestAssured.*;

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
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		//Modo Fluente ----------
		//Given When Then
		given()
			//Pré-condições
		.when()
			//Ação que vamos testar
			.get("http://restapi.wcaquino.me/ola")
		.then()
			//Teste
			//.assertThat() //Não faz nada, é só para legibilidade
			.statusCode(200);
	}
}
