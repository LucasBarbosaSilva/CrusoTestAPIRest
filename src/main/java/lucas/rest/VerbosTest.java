package lucas.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Jose\", \"age\": 50}")
		.when()
			.post("http://restapi.wcaquino.me/users") 
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 50}")
		.when()
			.post("http://restapi.wcaquino.me/users") 
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
		;
	}
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("http://restapi.wcaquino.me/usersXML") 
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
		;
	}
	
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Claudio\", \"age\": 30}")
		.when()
			.put("http://restapi.wcaquino.me/users/1") 
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Claudio"))
			.body("age", is(30))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveCustomizarURL() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Claudio\", \"age\": 30}")
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userId}", "users", "1") 
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Claudio"))
			.body("age", is(30))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveCustomizarURLParte2() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Claudio\", \"age\": 30}")
			.pathParam("entidade", "users")
			.pathParam("userId", "1")
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userId}") 
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Claudio"))
			.body("age", is(30))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/4")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
	
	
}