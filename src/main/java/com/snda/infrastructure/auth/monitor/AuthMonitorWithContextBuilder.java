package com.snda.infrastructure.auth.monitor;

public interface AuthMonitorWithContextBuilder {

	void with(AuthContext authContext);

	AuthMonitorWithContext build();

}
