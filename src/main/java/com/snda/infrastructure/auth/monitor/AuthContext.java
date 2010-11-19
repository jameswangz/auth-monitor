package com.snda.infrastructure.auth.monitor;


public class AuthContext extends ValueObject {

	private final String site;
	private final String username;
	private final String password;

	public AuthContext(String site, String username, String password) {
		this.site = site;
		this.username = username;
		this.password = password;
	}

	public String site() {
		return site;
	}

	public String username() {
		return username;
	}
	
	public String password() {
		return password;
	}

	
}
