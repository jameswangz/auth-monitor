package com.snda.infrastructure.auth.monitor;

public interface AuthMonitorConsole {

	AuthMonitorConsole addMonitor(AuthMonitor monitor);
	
	ClassfiedAuthListenerBuilder addListener(AuthListener authListener);

	AuthMonitorConsole schedulerAt(String cronExpression);

	void start();

	void stop();

}
