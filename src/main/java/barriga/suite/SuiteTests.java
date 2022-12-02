package barriga.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import barriga.refac.AuthTest;
import barriga.refac.ContasTest;
import barriga.refac.MovimentacoesTest;
import barriga.refac.SaldoTest;
import core.BaseTest;
import io.restassured.RestAssured;


@RunWith(Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacoesTest.class,
	SaldoTest.class,
	AuthTest.class 
})
public class SuiteTests extends BaseTest{
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
}
