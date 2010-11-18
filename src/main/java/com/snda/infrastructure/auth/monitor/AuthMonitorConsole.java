package com.snda.infrastructure.auth.monitor;

public interface AuthMonitorConsole {

	AuthMonitorConsole registerMonitor(AuthMonitor monitor);
	
	ClassfiedAuthListenerBuilder registerListener(AuthListener authListener);

	AuthMonitorConsole schedulerAt(String cronExpression);

	void start();

	void stop();

}
