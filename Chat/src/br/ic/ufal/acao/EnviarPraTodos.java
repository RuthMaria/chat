package br.ic.ufal.acao;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.ic.ufal.mensagem.Mensagem;
import br.ic.ufal.mensagem.Mensagem.Action;

public class EnviarPraTodos extends Requisicao {
	private Map<String, ObjectOutputStream> clientesConectados = new HashMap<String, ObjectOutputStream>();

	public EnviarPraTodos() {
	}

	public EnviarPraTodos(Requisicao sucessor, Map<String, ObjectOutputStream> listaCliente) {
		setSucessor(sucessor);
		this.clientesConectados = listaCliente;
	}

	@Override
	public Map<String, ObjectOutputStream> atenderRequisicao(Mensagem mensagem) {
		Action action = mensagem.getAction();

		if (action.equals(Action.ENVIAR_PRA_TODOS)) {
			enviarParaTodos(mensagem, clientesConectados);
			return clientesConectados;

		} else if (getSucessor() != null) {
			getSucessor().atenderRequisicao(mensagem);
		}
		return clientesConectados;
	}

	public void enviarParaTodos(Mensagem mensagem, Map<String, ObjectOutputStream> clientesConectados) {

		for (Map.Entry<String, ObjectOutputStream> cliente : clientesConectados.entrySet()) {
			try {
				cliente.getValue().writeObject(mensagem);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
