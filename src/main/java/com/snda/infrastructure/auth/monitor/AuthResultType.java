package com.snda.infrastructure.auth.monitor;

public enum AuthResultType {
	
	SUCCESS, FAILED;

	public static AuthResultType[] any() {
		return AuthResultType.values();
	}

}
