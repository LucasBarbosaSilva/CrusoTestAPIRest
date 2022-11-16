package lucas.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
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
	
	@Test
	public void devoConhecerMatchersHamcrest() {
		assertThat("Maria", Matchers.is("Maria"));
						//Atual    Esperado
		assertThat(128, Matchers.is(128));
		assertThat(128, Matchers.isA(Integer.class));
		assertThat(128d, Matchers.isA(Double.class));
		assertThat(128d, Matchers.greaterThan(120d));
		assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares, containsInAnyOrder(1,3,5,9, 7));
		//assertThat(impares, containsInAnyOrder(1,3,5,9)); //False - Precisa conter todos os elementos da lista
		assertThat(impares, hasItem(1));
		assertThat(impares, hasItems(1, 3, 5));
		assertThat("Maria", is(not("Joao")));
		assertThat("Maria", not("Joao"));
//		assertThat("Maria", not("Maria"));
//		assertThat("João", anyOf(is(not("João")), is("Maria")));
		assertThat("João", anyOf(is("João"), is("Maria")));
		assertThat("João Maria", allOf(startsWith("João"), endsWith("Maria")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}
}
