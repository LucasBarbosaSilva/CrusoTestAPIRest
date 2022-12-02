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
			.log().all()
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
		Movimentacao movimentacao = getMovimentacaoValida();
		
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void deveValidarCamposObrigatoriosMoviemntacao() {				
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(8))
			.body("msg", hasItems(
							"Data da Movimentação é obrigatório",
							"Data do pagamento é obrigatório",
							"Descrição é obrigatório",
							"Interessado é obrigatório",
							"Valor é obrigatório",
							"Valor deve ser um número",
							"Conta é obrigatório",
							"Situação é obrigatório"
					))			
		;
	}
	
	@Test
	public void deveCadastrarTransacaoFutura() {
		Movimentacao movimentacao = getMovimentacaoValida();
		movimentacao.setData_transacao("10/01/2023");
		given()
			.header("Authorization", "JWT "+TOKEN)
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
		;
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		given()
			.header("Authorization", "JWT "+TOKEN)
		.when()
			.delete("/contas/1485261")
		.then()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"))
			
		;
	}
	
	@Test
	public void deveCalcularSaldoContas() {
		given()
			.header("Authorization", "JWT "+TOKEN)
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id == 1485261}.saldo", is("24.00"))
		;
	}
	
	@Test
	public void deveRemoverMovimentacao() {
		//post/sigin
		//get/saldo
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(1485261);
		movimentacao.setData_pagamento("01/11/2022");
		movimentacao.setData_transacao("01/01/2022");
		movimentacao.setDescricao("Teste");
		movimentacao.setEnvolvido("eu");
		movimentacao.setValor(12.00f);
		movimentacao.setTipo("REC");
		movimentacao.setStatus(true);
		return movimentacao;
	}
}
