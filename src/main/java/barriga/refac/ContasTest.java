package barriga.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import core.BaseTest;
import utils.BarrigaUtils;

public class ContasTest extends BaseTest{
	
	@Test
	public void deveIncluirContaComSucesso() {
		
		
		Map<String, String> conta = new HashMap<String, String>();
		String conta_nome = "Conta inserida";
		conta.put("nome", conta_nome);
		given()
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("nome", is(conta.get("nome")))
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		Map<String, String> contaAlterada = new HashMap<String, String>();
		contaAlterada.put("nome", "Conta para alterar");
		Integer CONTA_ID =  BarrigaUtils.getIdContaPeloNome("Conta para alterar");
		
		given()
			.body(contaAlterada)
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}") 
		.then()
			.statusCode(200)
			.body("id", is(CONTA_ID))
			.body("nome", is(contaAlterada.get("nome")))
		;
	}
	
	@Test
	public void naodeveInlcuirContaComNomeRepetido() {
		Map<String, String> conta = new HashMap<String, String>();
		conta.put("nome", "Conta mesmo nome");
		
		given()
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!") )
		;
	}
	
	
}
