package barriga.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import core.BaseTest;
import io.restassured.RestAssured;

public class SaldoTest extends BaseTest{
		
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
