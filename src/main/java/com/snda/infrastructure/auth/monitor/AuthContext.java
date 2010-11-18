package com.snda.infrastructure.auth.monitor;

import java.util.Date;

public class AuthContext extends ValueObject {

	private final String site;
	private final String username;
	private final String password;
	private final Date time;

	public AuthContext(String site, String username, String password, Date time) {
		this.site = site;
		this.username = username;
		this.password = password;
		this.time = time;
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

	public Date time() {
		return time;
	}

	
	
}
