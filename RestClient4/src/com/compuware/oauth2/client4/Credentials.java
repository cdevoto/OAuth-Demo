package com.compuware.oauth2.client4;

public class Credentials {
	private String userName;
	private String password;

	public Credentials(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

}
