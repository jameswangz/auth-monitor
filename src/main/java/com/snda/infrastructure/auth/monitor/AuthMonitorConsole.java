package com.snda.infrastructure.auth.monitor;

public interface AuthMonitorConsole {

	AuthMonitorWithContextBuilder registerMonitor(AuthMonitor monitor);
	
	ClassfiedAuthListenerBuilder registerListener(AuthListener authListener);

	AuthMonitorConsole schedulerAt(String cronExpression);
	
	AuthMonitorConsole build();

	void start();

	void stop();

	void fireNow();

}
