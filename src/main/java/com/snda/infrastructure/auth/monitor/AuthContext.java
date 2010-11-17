package com.snda.infrastructure.auth.monitor;

import java.util.Date;

public class AuthContext {

	private final String site;
	private final String username;
	private final Date time;

	public AuthContext(String site, String username, Date time) {
		this.site = site;
		this.username = username;
		this.time = time;
	}

}
