package com.snda.infrastructure.auth.monitor;

public class AuthMonitorWithContext {

	private final AuthMonitor monitor;
	private final AuthContext authContext;

	public AuthMonitorWithContext(AuthMonitor monitor, AuthContext authContext) {
		this.monitor = monitor;
		this.authContext = authContext;
	}

	public AuthResult execute() {
		return monitor.execute(authContext);
	}
	
}
