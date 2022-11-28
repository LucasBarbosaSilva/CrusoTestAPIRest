package barriga.rest;

import org.junit.Test;

public class CenarioReal {
	@Test
	public void naoDeveAcessarAPISemToken() {
		//get/contas
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
