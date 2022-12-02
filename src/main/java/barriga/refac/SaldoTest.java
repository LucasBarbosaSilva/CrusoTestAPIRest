package barriga.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import core.BaseTest;
import io.restassured.RestAssured;

public class SaldoTest extends BaseTest{
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "maricris1497@uorak.com");
		login.put("senha", "1234");
		
		String TOKEN = given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token")
		;
		
		RestAssured.requestSpecification.header("Authorization", "JWT "+TOKEN);
		
		RestAssured.get("/reset").then().statusCode(200);
	}
	
	@Test
	public void deveCalcularSaldoContas() {
		given()
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id == "+getIdContaPeloNome("Conta para saldo")+"}.saldo", is("534.00"))
		;
	}
	
	public Integer getIdContaPeloNome(String nomeConta) {
		return RestAssured.get("/contas?nome="+nomeConta).then().extract().path("id[0]");
	}
}
