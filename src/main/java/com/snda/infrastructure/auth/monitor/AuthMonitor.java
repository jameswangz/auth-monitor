package com.snda.infrastructure.auth.monitor;

public interface AuthMonitor {

	AuthResult execute(AuthContext authContext);

}
