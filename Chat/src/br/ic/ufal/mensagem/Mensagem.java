package br.ic.ufal.mensagem;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Mensagem implements Serializable{
	
    private String nomeCliente;
    private Action action; 
    
    
    public enum Action {
    	CONECTAR, DESCONECTAR, ENVIAR_RESERVADO, ENVIAR_PRA_TODOS, USUARIOS_ONLINES, ENVIAR_ARQUIVO
    }


	public String getNomeCliente() {
		return nomeCliente;
	}


	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	

	public Action getAction() {
		return action;
	}
 
	
	public void setAction(Action action) {
		this.action = action;
	}

}
