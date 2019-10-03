package br.ic.ufal.mensagem;

import java.util.ArrayList;
import java.util.List;

public class MensagemOnlines extends Mensagem{
	private List<String> usuariosConectados;
	
	
	public MensagemOnlines() {
		usuariosConectados = new ArrayList<String>();
	}

	
	public List<String> getUsuariosConectados() {
		return usuariosConectados;
	}

	
	public void setUsuariosConectados(List<String> usuariosConectados) {
		this.usuariosConectados = usuariosConectados;
	}

}
