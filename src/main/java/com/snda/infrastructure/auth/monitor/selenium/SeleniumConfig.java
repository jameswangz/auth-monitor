package com.snda.infrastructure.auth.monitor.selenium;

public class SeleniumConfig {

	private final String serverHost;
	private final int serverPort;
	private final String browserStartCommand;

	public SeleniumConfig(String serverHost, int serverPort, String browserStartCommand) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.browserStartCommand = browserStartCommand;
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

}
