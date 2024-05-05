package DAO;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class HistoryDAO {

	public void addToHistory(String usename, Timestamp time, String message) {
		String query = "INSERT INTO chat.message VALUE('"+usename+"','"+message+"','"+time+"')";
		try {
			new DBcon().updateDB(query);
			System.out.println("Đã thêm");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String> getMessage() {
		ArrayList<String> mess = new ArrayList<String>();
		String query = "SELECT * FROM chat.message";
		try {
			ResultSet resultSet = new DBcon().queryDB(query);
			
			while (resultSet.next()) {
				String username = resultSet.getString("sender");
				String message = resultSet.getString("message");
				Timestamp time = resultSet.getTimestamp("time");
				
				String str = time + "\n"
						+ username+": "+message;
				mess.add(str);
			}
			
			return mess;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
