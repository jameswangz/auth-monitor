package com.snda.infrastructure.auth.monitor;

import java.util.Date;

public class AuthResult extends ValueObject {

	private final AuthContext authContext;
	private final AuthResultType resultType;
	private final String detail;
	private final Date time;

	public AuthResult(AuthContext authContext, Date time, AuthResultType resultType, String detail) {
		this.authContext = authContext;
		this.time = time;
		this.resultType = resultType;
		this.detail = detail;
	}

	public AuthContext authContext() {
		return authContext;
	}

	public AuthResultType resultType() {
		return resultType;
	}

	public String detail() {
		return detail;
	}

	public Date time() {
		return time;
	}
	
	
}
