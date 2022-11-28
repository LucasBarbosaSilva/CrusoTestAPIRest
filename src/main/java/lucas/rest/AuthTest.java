package lucas.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;



public class AuthTest {
	@Test
	public void deveAcessarSWAPI() {
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void deveObterClima() {
		given()
			.log().all()
			.queryParam("q", "Arapiraca,AL,BR")
			.queryParam("appid", "a002f898793c158b2b62e6ed543688ef")
			.queryParam("unit", "metric")
		.when()
			.get("https://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Arapiraca"))
			.body("coord.lon", is(-36.6611f))
			.body("coord.lat", is(-9.7525f))
		;
	}
	
	@Test
	public void naoDeveAutenticarSemSenha() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica() {
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasicaChallenge() { //Preventiva
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT() { 
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "maricris1497@uorak.com");
		login.put("senha", "1234");
		//Login na API
		//Receber o token
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("https://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
		;
		
		//Receber as contas
		given()
			.log().all()
			.header("Authorization", "JWT "+token)
		.when()
			.get("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Teste"))
		;
	}
	
	@Test
	public void deveAcessarAplicacaoWeb() { 

		String cookie = given()
			.log().all()
			.formParam("email", "maricris1497@uorak.com")
			.formParam("senha", "1234")
			.contentType(ContentType.URLENC.withCharset("utf-8"))
		.when()
			.post("https://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
		;
		
		cookie = cookie.split("=")[1];
		cookie = cookie.split(";")[0];
		
		//Obter a conta
		
		String body = given()
			.log().all()
			.cookie("connect.sid", cookie)
		.when()
			.get("https://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("html.body.table.tbody.tr[0].td[0]", is("Teste"))
			.extract().body().asString()
		;
		
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
	}
}
//a002f898793c158b2b62e6ed543688ef
//maricris1497@uorak.com