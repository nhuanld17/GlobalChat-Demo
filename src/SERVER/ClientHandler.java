package SERVER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;

import DAO.HistoryDAO;

public class ClientHandler extends Thread{
	private BufferedReader reader;
	private PrintWriter writer;
	private HashMap<String, ClientHandler> clients;
	private String usename;
	
	public ClientHandler(String username, BufferedReader reader, PrintWriter writer, HashMap<String, ClientHandler> clients) {
		this.usename = username;
		this.reader = reader;
		System.out.println(username+" hea");
		this.writer = writer;
		this.clients = clients;
		clients.put(username, this);
		start();
	}
	
	public void sendMessage(String message, Timestamp time, String usename2) {
		writer.println(message);
		writer.println(time);
		writer.println(usename2);
	}

	@Override
	public void run() {
	    try {
//	    	this.usename = reader.readLine();
	    	Server.updateUserList();
	        while (true) {
	        	Timestamp time = Timestamp.valueOf(reader.readLine());
	            String message = reader.readLine();
	            if (message == null) {
	                throw new IOException("Client disconnected");
	            }
	            new HistoryDAO().addToHistory(usename, time, message);

	            String mess = time + "\n"
	            		+ usename +": "+message;
	            System.out.println(mess);
	            Server.broadCast(time,message,usename, this); // Phát tin nhắn tới các client khác
	        }
	    } catch (IOException e) {
	        System.out.println("Error handling client " + usename);
	        e.printStackTrace();
	    } finally {
	        clients.remove(usename);
	        Server.updateUserList();
	        try {
	            reader.close();
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void sendOnlineList(String onlineUser) {
		writer.println("ONLINE_USERS:"+onlineUser);
	}
}
