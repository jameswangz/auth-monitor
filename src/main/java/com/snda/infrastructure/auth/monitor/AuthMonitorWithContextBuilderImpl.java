package com.snda.infrastructure.auth.monitor;

import com.google.common.base.Preconditions;

public class AuthMonitorWithContextBuilderImpl implements AuthMonitorWithContextBuilder {

	private final AuthMonitor monitor;
	private AuthContext authContext;

	public AuthMonitorWithContextBuilderImpl(AuthMonitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void with(AuthContext authContext) {
		this.authContext = authContext;
	}

	@Override
	public AuthMonitorWithContext build() {
		compile();
		return new AuthMonitorWithContext(monitor, authContext);
	}

	private void compile() {
		Preconditions.checkNotNull(authContext, "authContext required");
	}

}
