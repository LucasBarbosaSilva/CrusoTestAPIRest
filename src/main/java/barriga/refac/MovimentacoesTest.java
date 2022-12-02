package barriga.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import barriga.rest.Movimentacao;
import core.BaseTest;
import io.restassured.RestAssured;
import utils.Utils;

public class MovimentacoesTest extends BaseTest {
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
	public void deveInlcuirMoviemntacaoComSucesso() {
		Movimentacao movimentacao = getMovimentacaoValida();
		
		given()
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
	public void naoDeveCadastrarTransacaoFutura() {
		Movimentacao movimentacao = getMovimentacaoValida();
		String dataFutura = Utils.getDataFuturaFormatada(2);
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
			.pathParam("id", getIdContaPeloNome("Conta com movimentacao"))
		.when()
			.delete("/contas/{id}")
		.then()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"))
			
		;
	}
	
	@Test
	public void deveRemoverMovimentacao() {
		given()
			.pathParam("id", getIdTransacaoPeloNome("Movimentacao para exclusao"))
		.when()
			.delete("/transacoes/{id}") 
		.then()
			.statusCode(204)
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
		movimentacao.setData_pagamento(Utils.getDataFuturaFormatada(5));
		movimentacao.setData_transacao(Utils.getDataFuturaFormatada(-1));
		movimentacao.setDescricao("Teste");
		movimentacao.setEnvolvido("eu");
		movimentacao.setValor(12.00f);
		movimentacao.setTipo("REC");
		movimentacao.setStatus(true);
		return movimentacao;
	}
	
	public Integer getIdContaPeloNome(String nomeConta) {
		return RestAssured.get("/contas?nome="+nomeConta).then().extract().path("id[0]");
	}
	
	public Integer getIdTransacaoPeloNome(String nomeTransacao) {
		return RestAssured.get("/transacoes?descricao="+nomeTransacao).then().extract().path("id[0]");
	}
}
