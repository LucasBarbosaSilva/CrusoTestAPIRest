package barriga.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


import core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import utils.DataUtils;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CenarioReal extends BaseTest{
	public static String conta_nome;
	public static Integer id_conta;
	public static Integer id_transacao;
	
	
	@Test
	public void t02_deveIncluirContaComSucesso() {
		
		
		Map<String, String> conta = new HashMap<String, String>();
		String id = ""+System.currentTimeMillis();
		conta_nome = "Teste1"+id;
		conta.put("nome", conta_nome);
		id_conta = given()
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("nome", is(conta.get("nome")))
			.extract().path("id")
		;
	}
	
	@Test
	public void t03_deveAlterarContaComSucesso() {
		Map<String, String> conta = new HashMap<String, String>();
		String id = ""+System.currentTimeMillis(); 
		conta.put("nome", "Teste2"+id);
		
		int idRecebido = given()
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
		contaAlterada.put("nome", conta_nome+" alterada");
		
		given()
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
	public void t04_naodeveInlcuirContaComNomeRepetido() {
		Map<String, String> conta = new HashMap<String, String>();
		String id = ""+System.currentTimeMillis(); 
		conta.put("nome", "Teste2"+id);
		
		given()
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
		;
		
		given()
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!") )
		;
	}
	
	@Test
	public void t05_deveInlcuirMoviemntacaoComSucesso() {
		Movimentacao movimentacao = getMovimentacaoValida();
		
		id_transacao = given()
			.body(movimentacao)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
			.extract().path("id")
		;
	}
	
	@Test
	public void t06_deveValidarCamposObrigatoriosMoviemntacao() {				
		given()
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
	public void t07_naoDeveCadastrarTransacaoFutura() {
		Movimentacao movimentacao = getMovimentacaoValida();
		String dataFutura = DataUtils.getDataFuturaFormatada(2);
		movimentacao.setData_transacao(dataFutura);
		given()
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
	public void t08_naoDeveRemoverContaComMovimentacao() {
		given()
			.pathParam("id", id_conta)
		.when()
			.delete("/contas/{id}")
		.then()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"))
			
		;
	}
	
	@Test
	public void t09_deveCalcularSaldoContas() {
		given()
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id == "+id_conta+"}.saldo", is("12.00"))
		;
	}
	
	@Test
	public void t10_deveRemoverMovimentacao() {
		given()
			.pathParam("id", id_transacao)
		.when()
			.delete("/transacoes/{id}") 
		.then()
			.statusCode(204)
		;
	}
	
	@Test
	public void t11_naoDeveAcessarAPISemToken() {
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(id_conta);
		movimentacao.setData_pagamento(DataUtils.getDataFuturaFormatada(5));
		movimentacao.setData_transacao(DataUtils.getDataFuturaFormatada(-1));
		movimentacao.setDescricao("Teste");
		movimentacao.setEnvolvido("eu");
		movimentacao.setValor(12.00f);
		movimentacao.setTipo("REC");
		movimentacao.setStatus(true);
		return movimentacao;
	}
}
