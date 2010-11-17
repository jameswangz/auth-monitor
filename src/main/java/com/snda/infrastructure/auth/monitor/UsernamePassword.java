package com.snda.infrastructure.auth.monitor;

public class UsernamePassword {

	private final String username;
	private final String password;

	public UsernamePassword(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String username() {
		return username;
	}

	public String password() {
		return password;
	}
	
	
	
	
}
