package barriga.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import barriga.rest.Movimentacao;
import core.BaseTest;
import utils.BarrigaUtils;
import utils.DataUtils;

public class MovimentacoesTest extends BaseTest {
	
	
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
			.pathParam("id", BarrigaUtils.getIdContaPeloNome("Conta com movimentacao"))
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
			.pathParam("id", BarrigaUtils.getIdTransacaoPeloNome("Movimentacao para exclusao"))
		.when()
			.delete("/transacoes/{id}") 
		.then()
			.statusCode(204)
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta_id(BarrigaUtils.getIdContaPeloNome("Conta para movimentacoes"));
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
