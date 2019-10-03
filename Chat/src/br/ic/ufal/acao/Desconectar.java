package br.ic.ufal.acao;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.ic.ufal.mensagem.Mensagem;
import br.ic.ufal.mensagem.Mensagem.Action;
import br.ic.ufal.mensagem.MensagemTexto;

public class Desconectar extends Requisicao {
	private Map<String, ObjectOutputStream> clientesConectados = new HashMap<String, ObjectOutputStream>();
	private ObjectOutputStream saida;

	public Desconectar() {
	}
    
	public Desconectar(Map<String, ObjectOutputStream> listaCliente){
		this.clientesConectados = listaCliente;
		
	}
	public Desconectar(Requisicao sucessor) {
		setSucessor(sucessor);
	}

	@Override
	public Map<String, ObjectOutputStream> atenderRequisicao(Mensagem mensagem) {
		Action action = mensagem.getAction();

		if (action.equals(Action.DESCONECTAR)) {
			desconectar(mensagem);
			return clientesConectados;

		} else if (getSucessor() != null) {
			getSucessor().atenderRequisicao(mensagem);
		}
		return clientesConectados;
	}
	
	private void desconectar (Mensagem msg){
		MensagemTexto mensagem = (MensagemTexto)msg;
		
		clientesConectados.remove(mensagem.getNomeCliente());
		mensagem.setTextoMensagem(mensagem.getNomeCliente()+" saiu do Chat . . .\n");
		new EnviarPraTodos().enviarParaTodos(mensagem, clientesConectados);	
		new Conectar().usuariosOnlines(clientesConectados);
	}
	

}
