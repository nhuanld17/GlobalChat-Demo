package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String username;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private Scanner k = new Scanner(System.in);
	private JTextArea textArea;
	private JButton btnNewButton;
	private JTextArea messageDisplayArea;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public ClientGUI(String username) throws UnknownHostException, IOException {
		this.username = username;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 486, 324);
		contentPane.add(panel);
		panel.setLayout(null);
		
		messageDisplayArea = new JTextArea();
		messageDisplayArea.setBounds(10, 11, 466, 222);
		panel.add(messageDisplayArea);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 246, 372, 67);
		panel.add(textArea);
		
		btnNewButton = new JButton("SEND");
		btnNewButton.addActionListener(this);
		btnNewButton.setBounds(392, 244, 84, 29);
		panel.add(btnNewButton);
		
		socket = new Socket("localhost", 8088);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream(), true);
		
		new Thread(() -> {
			while (true) {
				String message;
				try {
					message = reader.readLine();
					if (message != null) {
						messageDisplayArea.append(message + "\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		writer.println(username);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnNewButton) {
			String message = textArea.getText();
			if (!message.isEmpty()) {
				writer.println(message);
				textArea.setText("");
				messageDisplayArea.append("Me: "+message +"\n");
			}
		}
	}
}
