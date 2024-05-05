package SERVER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

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
				ClientHandler clientHandler = new ClientHandler(userName, reader, writer, clients);
			} catch (Exception e) {
				socket.close();
				e.printStackTrace();
			}
		}
	}
	
	public static void broadCast(String message, ClientHandler sender) {
		clients.forEach((key, value) ->{
			if(value != sender) {
				value.sendMessage(message);
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		new Server();
	}
}

class ClientHandler extends Thread{
	private BufferedReader reader;
	private PrintWriter writer;
	private HashMap<String, ClientHandler> clients;
	private String usename;
	
	public ClientHandler(String username, BufferedReader reader, PrintWriter writer, HashMap<String, ClientHandler> clients) {
		this.usename = username;
		this.reader = reader;
		this.writer = writer;
		this.clients = clients;
		clients.put(username, this);
		start();
	}
	
	public void sendMessage(String message) {
		writer.println(message);
	}

	@Override
	public void run() {
	    try {
	        while (true) {
	            String message = reader.readLine();
	            if (message == null) {
	                throw new IOException("Client disconnected");
	            }
	            System.out.println(usename + ": " + message);
	            Server.broadCast(usename + ": " + message, this); // Phát tin nhắn tới các client khác
	        }
	    } catch (IOException e) {
	        System.out.println("Error handling client " + usename);
	        e.printStackTrace();
	    } finally {
	        clients.remove(usename);
	        try {
	            reader.close();
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
