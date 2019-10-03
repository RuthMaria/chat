package br.ic.ufal.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import br.ic.ufal.acao.*;
import br.ic.ufal.mensagem.Mensagem;

public class Servidor {

	private Map<String, ObjectOutputStream> clientesConectados = new HashMap<>(); 
	
	public Servidor() throws IOException{
		
        ServerSocket servidor = null;
        Socket conexaoCliente;
        
		try {
			servidor = new ServerSocket(8406);
			
	    	while (true) { 
	    		  System.out.println("Aguardando conexão");
		          conexaoCliente = servidor.accept(); 
		          
		          new Thread(new EscutaCliente(conexaoCliente)).start(); 		          
	    	}
	    	
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	private class EscutaCliente implements Runnable{
		private ObjectInputStream leitura;
		private ObjectOutputStream saida;
		
		public EscutaCliente(Socket cliente) {
			try {
				leitura =   new ObjectInputStream(cliente.getInputStream());				
				saida = new ObjectOutputStream(cliente.getOutputStream());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			Requisicao Desconectar = new Desconectar(clientesConectados);
			Requisicao enviarArquivo = new EnviarArquivo(Desconectar, saida, clientesConectados);
			Requisicao enviarReservado = new EnviarReservado(enviarArquivo, saida, clientesConectados);
			Requisicao enviarPraTodos = new EnviarPraTodos(enviarReservado, clientesConectados);
			Requisicao conectar = new Conectar(enviarPraTodos, saida, clientesConectados);	            
			
			try{
				Mensagem mensagem = null;
				
				while((mensagem = (Mensagem)leitura.readObject()) != null ){      
        				clientesConectados = conectar.atenderRequisicao(mensagem);       			
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) throws IOException{
		new Servidor();
	}
}
