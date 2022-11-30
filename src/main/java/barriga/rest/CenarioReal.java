package barriga.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import core.BaseTest;
import io.restassured.http.ContentType;

public class CenarioReal extends BaseTest{
	private String TOKEN;
	@Before
	public void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "maricris1497@uorak.com");
		login.put("senha", "1234");
		
		TOKEN = given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token")
		;
	}
	@Test
	public void naoDeveAcessarAPISemToken() {
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	
	@Test
	public void deveIncluirContaComSucesso() {
		
		
		Map<String, String> conta = new HashMap<String, String>();
		String id = ""+System.currentTimeMillis(); 
		conta.put("nome", "Teste2"+id);
		given()
			.header("Authorization", "JWT "+TOKEN)
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
		Map<String, String> conta = new HashMap<String, String>();
		String id = ""+System.currentTimeMillis(); 
		conta.put("nome", "Teste2"+id);
		
		int idRecebido = given()
			.header("Authorization", "JWT "+TOKEN)
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("nome", is(conta.get("nome")))
			.extract().path("id")
		;
		
		Map<String, String> contaAlterada = new HashMap<String, String>();
		id = ""+System.currentTimeMillis(); 
		contaAlterada.put("nome", "Teste2"+id);
		
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body(contaAlterada)
		.when()
			.put("/contas/"+idRecebido) 
		.then()
			.statusCode(200)
			.body("id", is(idRecebido))
			.body("nome", is(contaAlterada.get("nome")))
		;
	}
	
	@Test
	public void naodeveInlcuirContaComNomeRepetido() {
		Map<String, String> conta = new HashMap<String, String>();
		String id = ""+System.currentTimeMillis(); 
		conta.put("nome", "Teste2"+id);
		
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
		;
		
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!") )
		;
	}
	
	@Test
	public void deveInlcuirMoviemntacaoComSucesso() {
		//post/sigin
		//post/transacoes 
	}
	
	@Test
	public void deveValidarCamposObrigatoriosMoviemntacao() {
		//post/sigin
		//post/transacoes 
	}
	
	@Test
	public void deveCadastrarTransacaoFutura() {
		//post/sigin
		//post/transacoes 
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		//post/sigin
		//delete/contas/:id 
	}
	
	@Test
	public void deveCalcularSaldoContas() {
		//post/sigin
		//get/saldo
	}
	
	@Test
	public void deveRemoverMovimentacao() {
		//post/sigin
		//get/saldo
	}
	
	
}
