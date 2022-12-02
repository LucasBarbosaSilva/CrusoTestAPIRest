package utils;

import io.restassured.RestAssured;

public class BarrigaUtils {
	public static Integer getIdContaPeloNome(String nomeConta) {
		return RestAssured.get("/contas?nome="+nomeConta).then().extract().path("id[0]");
	}
	
	public static Integer getIdTransacaoPeloNome(String nomeTransacao) {
		return RestAssured.get("/transacoes?descricao="+nomeTransacao).then().extract().path("id[0]");
	}
}
