package com.snda.infrastructure.auth.monitor;

public class AuthResult {

	private final AuthContext authContext;
	private final AuthResultType resultType;
	private final String detail;

	public AuthResult(AuthContext authContext, AuthResultType resultType, String detail) {
		this.authContext = authContext;
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
	
	
	
}
