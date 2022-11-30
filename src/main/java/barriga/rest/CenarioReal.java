package barriga.rest;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import core.BaseTest;

public class CenarioReal extends BaseTest{
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
		//post/sigin
		//post/contas
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
		//post/sigin
		//put/contas/:id
	}
	
	@Test
	public void naodeveInlcuirContaComNomeRepetido() {
		//post/sigin
		//post/contas
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
