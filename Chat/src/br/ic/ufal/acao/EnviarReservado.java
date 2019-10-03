package br.ic.ufal.acao;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.ic.ufal.mensagem.Mensagem;
import br.ic.ufal.mensagem.Mensagem.Action;
import br.ic.ufal.mensagem.MensagemReservada;

public class EnviarReservado extends Requisicao {
	private  Map<String, ObjectOutputStream> clientesConectados = new HashMap<String, ObjectOutputStream>();
	private ObjectOutputStream saida;

	public EnviarReservado() {
	}

	public EnviarReservado(Requisicao sucessor, ObjectOutputStream saida, Map<String, ObjectOutputStream> clientesConectados) {
		setSucessor(sucessor);
		this.clientesConectados = clientesConectados;
	}

	@Override
	public Map<String, ObjectOutputStream> atenderRequisicao(Mensagem mensagem) {
		Action action = mensagem.getAction();

		if (action.equals(Action.ENVIAR_RESERVADO)) {
			enviarParaUm(mensagem);
			return clientesConectados;

		} else if (getSucessor() != null) {
			getSucessor().atenderRequisicao(mensagem);
		}
		return clientesConectados;
            
	}
	
	public void enviarParaUm(Mensagem msg){
		MensagemReservada mensagem = (MensagemReservada)msg;
		
		for (Map.Entry<String, ObjectOutputStream> cliente : clientesConectados.entrySet()) {
			if ((cliente.getKey().equals(mensagem.getDestinatario())) || (cliente.getKey().equals(mensagem.getNomeCliente()))){
				try {
					cliente.getValue().writeObject(mensagem);					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
