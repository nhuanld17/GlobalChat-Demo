package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBcon {
	private String status;
	private String url = "jdbc:mysql://localhost:3306/chat";
	private String user = "root";
	private String pwd = "nhuan.142857";
	Connection con;
	
	public DBcon() {
		try {
			con = DriverManager.getConnection(url,user,pwd);
			status = "OK";
		} catch (Exception e) {
			status = "KO";
			e.printStackTrace();
		}
	}
	public Connection getCon() {
		return con;
	}
	public ResultSet queryDB(String query) throws Exception {
		Statement statement = this.getCon().createStatement();
		return statement.executeQuery(query);
	}
	public int updateDB(String query) throws Exception {
		Statement statement = this.getCon().createStatement();
		return statement.executeUpdate(query);
	}
}
