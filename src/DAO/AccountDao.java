package DAO;

import java.sql.ResultSet;

public class AccountDao {

	public boolean isValidAccount(String username, String password) {
		try {
			ResultSet resultSet = new DBcon().queryDB("SELECT * FROM account WHERE username = '"+username+"'");
			
			if (resultSet.next()) {
				String pass = resultSet.getString("password");
				if (pass.equals(password)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
