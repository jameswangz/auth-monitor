package com.snda.infrastructure.auth.monitor.selenium;

public class SeleniumConfig {

	private final String serverHost;
	private final int serverPort;
	private final String browserStartCommand;
	private final String browserURL;

	public SeleniumConfig(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.browserStartCommand = browserStartCommand;
		this.browserURL = browserURL;
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

	public String browserURL() {
		return browserURL;
	}
	
}
