package br.ic.ufal.acao;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ic.ufal.mensagem.Mensagem;
import br.ic.ufal.mensagem.MensagemOnlines;
import br.ic.ufal.mensagem.MensagemTexto;
import br.ic.ufal.mensagem.Mensagem.Action;

public class Conectar extends Requisicao{
	private ObjectOutputStream saida;
	private Map<String, ObjectOutputStream> clientesConectados = new HashMap<String, ObjectOutputStream>();
	private EnviarPraTodos todosUsuario = new EnviarPraTodos();
	
	
    public Conectar() {
	}
	
	public Conectar(Requisicao sucessor, ObjectOutputStream saida, Map<String, ObjectOutputStream> listaCliente) {
		setSucessor(sucessor);	
		this.saida = saida;
		this.clientesConectados = listaCliente;
	}
	
	@Override
	public Map<String, ObjectOutputStream> atenderRequisicao(Mensagem mensagem) {
		  Action action = mensagem.getAction();
		  
		  if (action.equals(Action.CONECTAR)){
              	boolean conectado = conectar(mensagem);                  	
          	                    	                    	 
          	if (conectado){
			          clientesConectados.put(mensagem.getNomeCliente(), saida);		
          		      usuariosOnlines(clientesConectados);
          		      todosUsuario.enviarParaTodos(mensagem, clientesConectados);
          		      return clientesConectados;
          	}
          	
          } else if ( getSucessor() != null ){
            	 getSucessor().atenderRequisicao(mensagem);            	 
          }
		  return clientesConectados;
	}
    
	private boolean conectar (Mensagem msg){
		MensagemTexto mensagem = (MensagemTexto)msg;
		
		if (clientesConectados.size() == 0) {
			mensagem.setTextoMensagem("YES");
		    return true;
		}
		
		for (Map.Entry<String, ObjectOutputStream> cliente : clientesConectados.entrySet()) {
			if (cliente.getKey().equals(mensagem.getNomeCliente())) {
				mensagem.setTextoMensagem("NO");
				return false;
		
			}else{
				mensagem.setTextoMensagem("YES");
				return true;
			}			
		}
		
		return false;
	}
	
	public void usuariosOnlines(Map<String, ObjectOutputStream> clientesConectados) {
		List nomeClientes = new ArrayList<String>();

		for (Map.Entry<String, ObjectOutputStream> kv : clientesConectados.entrySet()) {
			nomeClientes.add(kv.getKey());
		}

		MensagemOnlines mensagem = new MensagemOnlines();
		mensagem.setAction(Action.USUARIOS_ONLINES);
		mensagem.setUsuariosConectados(nomeClientes);

		for (Map.Entry<String, ObjectOutputStream> cliente : clientesConectados.entrySet()) {
			mensagem.setNomeCliente(cliente.getKey());
			try {
				cliente.getValue().writeObject(mensagem);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
