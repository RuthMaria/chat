package br.ic.ufal.cliente.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtNome;
	private JRadioButton radioAzul;
	private JRadioButton radioVermelho;
	private JRadioButton radioVerdeLimao;
	private JRadioButton radioRadioPink;
	private JRadioButton radioAmarelo;
	private JRadioButton radioPreto;
	private JRadioButton radioRosaBebe;
	private JRadioButton radioVerdeAzulado;
	private JRadioButton radioCinza;
	
	public static void main(String[] args) {
		new Login();
	}
	
	/**
	 * Create the frame.
	 */
	public Login() {
		super("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		radioAzul = new JRadioButton("");
		radioAzul.setBackground(Color.BLUE);
		buttonGroup.add(radioAzul);
		radioAzul.setBounds(185, 173, 33, 23);
		contentPane.add(radioAzul);
		
		radioVermelho = new JRadioButton("");
		radioVermelho.setBackground(Color.RED);
		buttonGroup.add(radioVermelho);
		radioVermelho.setBounds(238, 173, 33, 23);
		contentPane.add(radioVermelho);
		
		radioVerdeLimao = new JRadioButton("");
		buttonGroup.add(radioVerdeLimao);
		radioVerdeLimao.setBackground(Color.GREEN);
		radioVerdeLimao.setBounds(289, 173, 33, 23);
		contentPane.add(radioVerdeLimao);
		
		JLabel lblNomeDeUsurio = new JLabel("Nome de Usu\u00E1rio:");
		lblNomeDeUsurio.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNomeDeUsurio.setBounds(74, 132, 106, 14);
		contentPane.add(lblNomeDeUsurio);
		
		txtNome = new JTextField();
		txtNome.setBounds(185, 130, 234, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		radioRadioPink = new JRadioButton("");
		buttonGroup.add(radioRadioPink);
		radioRadioPink.setBackground(Color.MAGENTA);
		radioRadioPink.setBounds(342, 173, 33, 23);
		contentPane.add(radioRadioPink);
		
		radioAmarelo = new JRadioButton("");
		buttonGroup.add(radioAmarelo);
		radioAmarelo.setBackground(Color.YELLOW);
		radioAmarelo.setBounds(386, 173, 33, 23);
		contentPane.add(radioAmarelo);
		
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nomeCliente = txtNome.getText();
				Color cor = corEscolhida();
				
				if(!nomeCliente.isEmpty() && !cor.equals(Color.ORANGE)){					
					fecharFrame();
					new ClienteFrame(nomeCliente, cor);
					
				} else {
					JOptionPane.showMessageDialog(null, "Informe o nome do usuário e selecione uma cor!");
				}
				
			}
		});
		
		btnEntrar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnEntrar.setIcon(new ImageIcon(Login.class.getResource("/br/ic/ufal/cliente/imagens/icon-correto.png")));
		btnEntrar.setBounds(318, 259, 101, 23);
		contentPane.add(btnEntrar);
		
		radioPreto = new JRadioButton("");
		buttonGroup.add(radioPreto);
		radioPreto.setBackground(Color.BLACK);
		radioPreto.setBounds(197, 209, 38, 23);
		contentPane.add(radioPreto);
		
		radioRosaBebe = new JRadioButton("");
		buttonGroup.add(radioRosaBebe);
		radioRosaBebe.setBackground(Color.PINK);
		radioRosaBebe.setBounds(249, 209, 38, 23);
		contentPane.add(radioRosaBebe);
		
		radioVerdeAzulado = new JRadioButton("");
		buttonGroup.add(radioVerdeAzulado);
		radioVerdeAzulado.setBackground(Color.CYAN);
		radioVerdeAzulado.setBounds(302, 209, 38, 23);
		contentPane.add(radioVerdeAzulado);
		
		radioCinza = new JRadioButton("");
		buttonGroup.add(radioCinza);
		radioCinza.setBackground(Color.GRAY);
		radioCinza.setBounds(352, 209, 38, 23);
		contentPane.add(radioCinza);
		
		JLabel lblBemvindoAoChat = new JLabel("BEM-VINDO AO CHAT PADRONIZADO");
		lblBemvindoAoChat.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBemvindoAoChat.setBounds(96, 58, 285, 14);
		contentPane.add(lblBemvindoAoChat);
		
		JLabel lblSelecioneACor = new JLabel("Selecione a cor:");
		lblSelecioneACor.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSelecioneACor.setBounds(74, 182, 106, 14);
		contentPane.add(lblSelecioneACor);
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fecharFrame();
			}
		});
		btnSair.setIcon(new ImageIcon(Login.class.getResource("/br/ic/ufal/cliente/imagens/icon-errado.png")));
		btnSair.setBounds(219, 259, 89, 23);
		contentPane.add(btnSair);
		setVisible(true);
	}
	
    private void fecharFrame(){
    	this.dispose();    
    }
    
	private Color corEscolhida() {		
		if(radioAmarelo.isSelected()){
			 return  Color.YELLOW;
		} else if(radioAzul.isSelected()){
			 return  Color.BLUE;
		} else if(radioCinza.isSelected()){
			 return  Color.GRAY;		
		} else if(radioPreto.isSelected()){
			 return  Color.BLACK;
		} else if(radioRadioPink.isSelected()){
			 return Color.MAGENTA;
		} else if(radioRosaBebe.isSelected()){
			 return  Color.PINK;
		} else if(radioVerdeAzulado.isSelected()){
			 return  Color.CYAN;
		} else if(radioVerdeLimao.isSelected()){
			 return  Color.GREEN;
		} else if(radioVermelho.isSelected()){
			 return  Color.RED;
		}
		return Color.ORANGE;
	}
}
