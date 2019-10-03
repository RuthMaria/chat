package br.ic.ufal.acao;

import java.io.ObjectOutputStream;
import java.util.Map;

import br.ic.ufal.mensagem.Mensagem;

public abstract class Requisicao {
    private Requisicao sucessor;
	
	public void setSucessor(Requisicao sucessor) {
		this.sucessor = sucessor;
	}
	
	public Requisicao getSucessor(){
		return sucessor;
	}
	
	public abstract Map<String, ObjectOutputStream> atenderRequisicao(Mensagem mensagem);

}
