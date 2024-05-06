package SERVER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Server {
	private static HashMap<String, ClientHandler> clients = new HashMap<>();
	
	public Server() throws IOException {
		ServerSocket serverSocket = new ServerSocket(8088);
		System.out.println("Server started");
		
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("Client connected");
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				
				String userName = reader.readLine();
				System.out.println("Server nhận được "+ userName);
				if (isDuplicateLogin(userName)) {
					writer.println("DUPLICATE_LOGIN");
					continue;
				}else {
					writer.println("OKE");
					ClientHandler clientHandler = new ClientHandler(userName, reader, writer, clients);
				}
			} catch (Exception e) {
				socket.close();
				e.printStackTrace();
			}
		}
	}
	
	private boolean isDuplicateLogin(String userName) {
		for (String username : clients.keySet()) {
			if (username.equals(userName)) {
				return true;
			}
		}
		return false;
	}

	public static void broadCast(Timestamp time, String message, String usename, ClientHandler sender) {
		clients.forEach((key, value) ->{
			if(value != sender) {
				value.sendMessage(message, time, usename);
			}
		});
	}
	
	public static void sendOnlineUsers() {
		String onlineUser = clients.keySet().stream().collect(Collectors.joining(","));
		for (ClientHandler client : clients.values()) {
			client.sendOnlineList(onlineUser);
		}
	}
	
	public static void updateUserList() {
		sendOnlineUsers();
	}
	
	public static void main(String[] args) throws IOException {
		new Server();
	}
}

