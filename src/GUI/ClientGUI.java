package GUI;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import DAO.HistoryDAO;

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
	private JScrollPane scrollPane_1;
	private JPanel panel_Online;
	private JScrollPane scrollPane_ONLINE;
	private JPanel PANEL_ONLINE;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public ClientGUI(String username, Socket socket, BufferedReader reader, PrintWriter writer) throws UnknownHostException, IOException {
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		this.username = username;
		setTitle(this.username);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 481, 324);
		contentPane.add(panel);
		panel.setLayout(null);

		messageDisplayArea = new JTextArea();
		messageDisplayArea.setEditable(false);
		messageDisplayArea.setForeground(SystemColor.desktop);
		messageDisplayArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		messageDisplayArea.setBounds(10, 11, 466, 222);

		textArea = new JTextArea();
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textArea.setBounds(10, 246, 372, 67);

		btnNewButton = new JButton("SEND");
		btnNewButton.addActionListener(this);
		btnNewButton.setBounds(392, 244, 84, 29);
		panel.add(btnNewButton);

		JScrollPane scrollPane = new JScrollPane(messageDisplayArea);
		scrollPane.setBounds(10, 11, 466, 222);
		panel.add(scrollPane);

		scrollPane_1 = new JScrollPane(textArea);
		scrollPane_1.setBounds(10, 246, 372, 67);
		panel.add(scrollPane_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(486, 0, 180, 44);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel = new JLabel("ĐANG ONLINE");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel.setBounds(0, 11, 180, 22);
		panel_2.add(lblNewLabel);
		

		
		PANEL_ONLINE = new JPanel();
		PANEL_ONLINE.setBounds(486, 50, 180, 274);
		
		scrollPane_ONLINE = new JScrollPane(PANEL_ONLINE);
		PANEL_ONLINE.setLayout(null);
		scrollPane_ONLINE.setBounds(486, 50, 180, 274);
		contentPane.add(scrollPane_ONLINE);


		this.writer.println(this.username);
		new Thread(() -> {
			while (true) {
				try {
					String message = this.reader.readLine();

					if (message != null) {
						if (message.startsWith("ONLINE_USERS:")) {
							System.out.println(message);
							SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
        							updateOnlinePanel(message.substring(13));
                                }
                            });

						} else {
							String dateString = reader.readLine();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date parsedDate = dateFormat.parse(dateString);
							Timestamp time = new Timestamp(parsedDate.getTime());

							String userName = reader.readLine();
							messageDisplayArea.append(time + "\n" + userName + ": " + message + "\n\n");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
					System.out.println("Error parsing date.");
				}
			}
		}).start();

		loadMessage();
	}

	public void updateOnlinePanel(String users) {
		System.out.println("Updating online panel with users: " + users); // In ra console để debug
		PANEL_ONLINE.removeAll();
		String[] userList = users.split(",");
		int x = 4, y = 11, width = 178, height = 25;
		for (String user : userList) {
			JLabel userLabel = new JLabel(user);
			userLabel.setIcon(new ImageIcon("F:\\eclipse-workspace\\DACS\\src\\image\\icons8-dot-20.png"));
			userLabel.setBounds(x, y, width, height);
			userLabel.setHorizontalAlignment(SwingConstants.LEFT);
			PANEL_ONLINE.add(userLabel);
			y = y + height + 10;
			System.out.println("Added user: " + user); // In ra từng user được thêm vào
		}
		PANEL_ONLINE.revalidate();
		PANEL_ONLINE.repaint();
	}

	private void loadMessage() {
		ArrayList<String> messages = new HistoryDAO().getMessage();
		for (String string : messages) {
			messageDisplayArea.append(string + "\n\n");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnNewButton) {
			String message = textArea.getText();
			if (!message.isEmpty()) {
				Timestamp time = new Timestamp(System.currentTimeMillis());
				writer.println(time);
				writer.println(message);
				textArea.setText("");
				messageDisplayArea.append(time + "\n" + username + ": " + message + "\n\n");
			}
		}
	}
}
