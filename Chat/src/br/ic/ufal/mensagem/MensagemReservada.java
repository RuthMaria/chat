package br.ic.ufal.mensagem;

public class MensagemReservada extends Mensagem{
	
	private String destinatario;
	private String textoMensagem;
	
	
	public String getDestinatario() {
		return destinatario;
	}
	
	
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	
	public String getTextoMensagem() {
		return textoMensagem;
	}
	
	
	public void setTextoMensagem(String textoMensagem) {
		this.textoMensagem = textoMensagem;
	}

}
