package br.ic.ufal.acao;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.ic.ufal.mensagem.Mensagem;
import br.ic.ufal.mensagem.MensagemArquivo;
import br.ic.ufal.mensagem.MensagemReservada;
import br.ic.ufal.mensagem.Mensagem.Action;

public class EnviarArquivo extends Requisicao{
	private  Map<String, ObjectOutputStream> clientesConectados = new HashMap<String, ObjectOutputStream>();
	private ObjectOutputStream saida;
    
    public EnviarArquivo() {
	}
    
	public EnviarArquivo(Requisicao sucessor, ObjectOutputStream saida, Map<String, ObjectOutputStream> clientesConectados) {
		setSucessor(sucessor);
		this.clientesConectados = clientesConectados;
	}

	@Override
	public Map<String, ObjectOutputStream> atenderRequisicao(Mensagem msg) {
		Action action = msg.getAction();
		
		if (action.equals(Action.ENVIAR_ARQUIVO)) {
			
			MensagemArquivo mensagem = (MensagemArquivo)msg;
			
			if (mensagem.getDestinatario().equals(""))
		        	enviarTodos(mensagem);
			else
				    enviarReservado(mensagem);
			
			return clientesConectados;

		} else if (getSucessor() != null) {
			getSucessor().atenderRequisicao(msg);
		}
		return clientesConectados;
            
	}
	
	public void enviarTodos(Mensagem msg){
		MensagemArquivo mensagem = (MensagemArquivo)msg;
		
		for (Map.Entry<String, ObjectOutputStream> cliente : clientesConectados.entrySet()) {
				try {
					cliente.getValue().writeObject(mensagem);					
				} catch (IOException e) {
					e.printStackTrace();
				}			
		}
	}
	
	public void enviarReservado(Mensagem msg){
		MensagemArquivo mensagem = (MensagemArquivo)msg;
		
		for (Map.Entry<String, ObjectOutputStream> cliente : clientesConectados.entrySet()) {

			if (cliente.getKey().equals(mensagem.getDestinatario()) || cliente.getKey().equals(mensagem.getNomeCliente())){
				try {
					cliente.getValue().writeObject(mensagem);					
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
	}

}
