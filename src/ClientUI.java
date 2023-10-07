import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class ClientUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JTextField Username;
	public JPasswordField Psw;
	public JButton LoginButton;
	public JButton SignupButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUI frame = new ClientUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Username = new JTextField();
		Username.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		Username.setBounds(150, 61, 146, 21);
		contentPane.add(Username);
		Username.setColumns(10);
		
		JLabel UsernameLabel = new JLabel("\u7528\u6237\u540D");
		UsernameLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		UsernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		UsernameLabel.setBounds(75, 64, 58, 15);
		contentPane.add(UsernameLabel);
		
		JLabel PswLabel = new JLabel("\u5BC6\u7801");
		PswLabel.setHorizontalAlignment(SwingConstants.CENTER);
		PswLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		PswLabel.setBounds(75, 107, 58, 15);
		contentPane.add(PswLabel);
		
		Psw = new JPasswordField();
		Psw.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		Psw.setBounds(150, 106, 146, 21);
		contentPane.add(Psw);
		
		//µÇÂ¼
		LoginButton = new JButton("\u767B \u5F55");
		LoginButton.setFont(new Font("ËÎÌå", Font.PLAIN, 15));
		LoginButton.setBounds(114, 157, 84, 23);
		contentPane.add(LoginButton);
		
		//×¢²á
		SignupButton = new JButton("\u6CE8 \u518C");
		SignupButton.setFont(new Font("ËÎÌå", Font.PLAIN, 15));
		SignupButton.setBounds(246, 157, 84, 23);
		contentPane.add(SignupButton);
	}
}
