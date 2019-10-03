package br.ic.ufal.mensagem;

import java.io.File;

public class MensagemArquivo extends Mensagem{	
	private String destinatario;
	private File arquivo; 
	
	public String getDestinatario() {
		return destinatario;
	}
	
	
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}


	public File getArquivo() {
		return arquivo;
	}


	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}
	
	

}
