package com.snda.infrastructure.auth.monitor.selenium;

public class SeleniumConfig {

	private final String serverHost;
	private final int serverPort;
	private final String browserStartCommand;
	private final String timeout;

	public SeleniumConfig(String serverHost, int serverPort, String browserStartCommand, String timeout) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.browserStartCommand = browserStartCommand;
		this.timeout = timeout;
	}

	public String serverHost() {
		return serverHost;
	}

	public int serverPort() {
		return serverPort;
	}

	public String browserStartCommand() {
		return browserStartCommand;
	}

	public String timeout() {
		return timeout;
	}
	
	

}
