package br.ic.ufal.cliente.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Random;

import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import br.ic.ufal.mensagem.Mensagem;
import br.ic.ufal.mensagem.Mensagem.Action;
import br.ic.ufal.mensagem.MensagemArquivo;
import br.ic.ufal.mensagem.MensagemOnlines;
import br.ic.ufal.mensagem.MensagemReservada;
import br.ic.ufal.mensagem.MensagemTexto;

public class ClienteFrame extends JFrame {
	private JPanel contentPane;
	private JTextArea textoEnviado;
	private Socket cliente;
	private ObjectOutputStream saida;
	private JTextArea textoRecebido;
	private ObjectInputStream entrada;
	private JLabel nomeUsuario;
    private Mensagem mensagem; // creator
    private JList listOnlines;
    private String nomeCliente;
    private Color cor;
    
	/**
	 * Create the frame.
	 */
	public ClienteFrame(String nomeCliente, Color cor) {
		super("Chat");
		this.nomeCliente = nomeCliente;
		this.cor = cor;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 609, 472);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 394, 65);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnSair = new JButton("Sair");
		btnSair.setIcon(new ImageIcon(ClienteFrame.class.getResource("/br/ic/ufal/cliente/imagens/icon-errado.png")));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectar();				
			}
		});
		btnSair.setBounds(295, 21, 89, 23);
		panel.add(btnSair);
		
		nomeUsuario = new JLabel(this.nomeCliente);
		nomeUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));
		nomeUsuario.setBounds(10, 25, 213, 14);		
		panel.add(nomeUsuario);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 87, 394, 336);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setIcon(new ImageIcon(ClienteFrame.class.getResource("/br/ic/ufal/cliente/imagens/icon-correto.png")));
		btnEnviar.addActionListener(new EnviarMensagem());
		btnEnviar.setBounds(291, 302, 103, 23);
		panel_1.add(btnEnviar);

		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.setIcon(new ImageIcon(ClienteFrame.class.getResource("/br/ic/ufal/cliente/imagens/limpar.png")));
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textoEnviado.setText("");
			}
		});
		btnLimpar.setBounds(174, 302, 107, 23);
		panel_1.add(btnLimpar);
		
		textoEnviado = new JTextArea();
		JScrollPane scrollPane1 = new JScrollPane(textoEnviado);
		scrollPane1.setBounds(10, 190, 384, 91);
		panel_1.add(scrollPane1);
		
		textoRecebido = new JTextArea();
		textoRecebido.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textoRecebido);
		scrollPane.setBounds(10, 5, 384, 174);
		panel_1.add(scrollPane);
		
		JButton btnArquivo = new JButton("Enviar arquivo");
		btnArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviarArquivo();
			}
		});
		btnArquivo.setIcon(new ImageIcon(ClienteFrame.class
				.getResource("/br/ic/ufal/cliente/imagens/diretorio.png")));
		btnArquivo.setBounds(20, 302, 144, 23);
		panel_1.add(btnArquivo);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Onlines",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(421, 11, 162, 412);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		listOnlines = new JList();
		listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOnlines.setBounds(10, 38, 142, 363);
		panel_2.add(listOnlines);
		ConexaoServidor();
		conectar();
		corEscolhida(cor);
		setVisible(true);
	}

	private void ConexaoServidor() {
		try {
			cliente = new Socket("127.0.0.1", 8406);
			saida = new ObjectOutputStream(cliente.getOutputStream());	
			new Thread(new EscutaServidor()).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class EnviarMensagem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {			

		    if(listOnlines.isSelectionEmpty()){
			    mensagem = enviarPraTodos();   		
				   
			} else {
				System.out.println("Nome selecionado: "+(String)listOnlines.getSelectedValue());
				mensagem = enviarPraUm((String)listOnlines.getSelectedValue());
				listOnlines.clearSelection();
			}
		    
			try {
				saida.writeObject(mensagem);				
				saida.flush();
				saida.reset(); 
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			textoEnviado.setText("");
			textoEnviado.requestFocus(); 

		}

	}
	
	private class EscutaServidor implements Runnable {

		@Override
		public void run() {
			
			try {						
				entrada = new ObjectInputStream(cliente.getInputStream()); 
				Mensagem texto = null;
				
				while ((texto = (Mensagem)entrada.readObject()) != null)  {	
					 Action action = texto.getAction();
	                 System.out.println("Chegou no cliente");
	                 
					 if (action.equals(Action.ENVIAR_PRA_TODOS)){
						 MensagemTexto conteudo = (MensagemTexto)texto;
						 corEscolhida(cor);
						 textoRecebido.append(conteudo.getNomeCliente()+" diz :  "+conteudo.getTextoMensagem()+"\n");
						 
					 } else if(action.equals(Action.CONECTAR)){
						 conectado(texto);
						 
					 } else if (action.equals(Action.USUARIOS_ONLINES)){
						 atualizarLista(texto);
						 
					 } else if(action.equals(Action.ENVIAR_RESERVADO)){
						 MensagemReservada conteudo = (MensagemReservada)texto;						 
						 corEscolhida(cor);
						 textoRecebido.append(conteudo.getNomeCliente()+" diz :  "+conteudo.getTextoMensagem()+"\n");
						 
					 } else if (action.equals(Action.ENVIAR_ARQUIVO)){
						      MensagemArquivo conteudo = (MensagemArquivo)texto;
						      FileReader arquivo = new FileReader(conteudo.getArquivo());
						      BufferedReader lerArquivo = new BufferedReader(arquivo);
						      String linha;
						      textoRecebido.append(conteudo.getNomeCliente()+" te enviou o arquivo "+ 
						                           conteudo.getArquivo().getName()+"\n");
						      salvarArquivo(conteudo);
						      while ((linha = lerArquivo.readLine()) != null){
						               textoRecebido.append(linha+"\n");
						      }
						      
					 } else if (action.equals(Action.DESCONECTAR)){
						      MensagemTexto mensagem = (MensagemTexto)texto;
						      textoRecebido.append(mensagem.getTextoMensagem());
					 }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
	}
	
	public void desconectado() {
		this.dispose();
	}
	public void desconectar(){
		MensagemTexto mensagem = new MensagemTexto();
		mensagem.setAction(Action.DESCONECTAR);
		mensagem.setNomeCliente(nomeCliente);
		
		try {
			saida.writeObject(mensagem);	
			saida.flush();
			saida.reset(); 
			this.dispose();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	
	public void salvarArquivo(MensagemArquivo mensagem) {

		try {
			Thread.sleep(new Random().nextInt(1000));

			long tempo = System.currentTimeMillis();

			File diretorio = new File("c:\\arquivos");

			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}

			FileInputStream origem = new FileInputStream(mensagem.getArquivo());
			FileOutputStream destino = new FileOutputStream("c:\\arquivos\\"
					+ tempo + "_" + mensagem.getArquivo().getName());

			FileChannel canalArquivo = origem.getChannel();
			FileChannel salvarArquivo = destino.getChannel();

			long tamanho = canalArquivo.size();
			canalArquivo.transferTo(0, tamanho, salvarArquivo);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private void enviarArquivo() {
		JFileChooser arquivoEscolhido = new JFileChooser();
		int opcao = arquivoEscolhido.showOpenDialog(null);
		
		if (opcao == JFileChooser.APPROVE_OPTION){			
			MensagemArquivo mensagem = new MensagemArquivo();
			mensagem.setArquivo(arquivoEscolhido.getSelectedFile());
			mensagem.setAction(Action.ENVIAR_ARQUIVO);	
			mensagem.setNomeCliente(nomeCliente);
			mensagem.setDestinatario("");
			
			if (!listOnlines.isSelectionEmpty()){
				mensagem.setDestinatario((String)listOnlines.getSelectedValue());	
			}
			
			try {
				saida.writeObject(mensagem);		
				listOnlines.clearSelection();
				saida.flush();
				saida.reset(); 
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	public Mensagem enviarPraTodos() {
		 MensagemTexto mensagem = new MensagemTexto();
		 mensagem.setNomeCliente(nomeCliente);
		 mensagem.setAction(Action.ENVIAR_PRA_TODOS);
		 mensagem.setTextoMensagem(textoEnviado.getText());	
		 return mensagem;
	}
	
	public Mensagem enviarPraUm(String destinatario){
		 MensagemReservada mensagem = new MensagemReservada();
		 mensagem.setNomeCliente(nomeCliente);
		 mensagem.setAction(Action.ENVIAR_RESERVADO);
		 mensagem.setDestinatario(destinatario);
		 mensagem.setTextoMensagem(textoEnviado.getText());	
		 return mensagem;
	}
	
	private void conectado(Mensagem msg){
	     MensagemTexto mensagem = (MensagemTexto)msg;
		
		 if(mensagem.getTextoMensagem().equals("YES")){
			     corEscolhida(cor);
		         textoRecebido.append(mensagem.getNomeCliente()+" entrou no chat . . .\n");
		 }
		 
		 else{
			     JOptionPane.showMessageDialog(this, "Informe outro nome de usuário, este já está em uso!");
			     this.dispose();
			     new Login().setVisible(true);
		 }
	}
	
	private void conectar(){
		MensagemTexto mensagem = new MensagemTexto();
		mensagem.setNomeCliente(nomeCliente);
		mensagem.setTextoMensagem("YES");
	    mensagem.setAction(Action.CONECTAR);		
		
		try {
			saida.writeObject(mensagem);
			saida.flush();
			saida.reset(); 
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void atualizarLista(Mensagem msg){
		
		    MensagemOnlines mensagem = (MensagemOnlines)msg;
			List listaCliente = mensagem.getUsuariosConectados();
			listaCliente.remove(mensagem.getNomeCliente());
			String[] array = (String[])listaCliente.toArray(new String[listaCliente.size()]);			
			listOnlines.setListData(array);				
	}
	
	private void corEscolhida(Color cor) {		
		if(cor.equals(Color.YELLOW)){
			nomeUsuario.setForeground(Color.YELLOW);
			textoRecebido.setForeground(Color.YELLOW);
		} else if(cor.equals(Color.BLUE)){
			nomeUsuario.setForeground(Color.BLUE);
			textoRecebido.setForeground(Color.BLUE);
		} else if(cor.equals(Color.GRAY)){
			nomeUsuario.setForeground(Color.GRAY);	
			textoRecebido.setForeground(Color.GRAY);
		} else if(cor.equals(Color.BLACK)){
			nomeUsuario.setForeground(Color.BLACK);
			textoRecebido.setForeground(Color.BLACK);
		} else if(cor.equals(Color.MAGENTA)){
			nomeUsuario.setForeground(Color.MAGENTA);
			textoRecebido.setForeground(Color.MAGENTA);
		} else if(cor.equals(Color.PINK)){
			nomeUsuario.setForeground(Color.PINK);
			textoRecebido.setForeground(Color.PINK);
		} else if(cor.equals(Color.CYAN)){
			nomeUsuario.setForeground(Color.CYAN);
			textoRecebido.setForeground(Color.CYAN);
		} else if(cor.equals(Color.GREEN)){
			nomeUsuario.setForeground(Color.GREEN);
			textoRecebido.setForeground(Color.GREEN);
		} else if(cor.equals(Color.RED)){
			nomeUsuario.setForeground(Color.RED);
			textoRecebido.setForeground(Color.RED);
		}
	}
	
	public static void main(String[] args) {
		 new ClienteFrame("Ruth Maria", Color.RED);
	}
}
