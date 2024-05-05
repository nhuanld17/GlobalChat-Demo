package DTO;

import java.sql.Timestamp;

public class message {
	private String sender;
	private String message;
	private Timestamp time;
	
	public message(String sender, String message, Timestamp time) {
		this.sender = sender;
		this.message = message;
		this.time = time;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
