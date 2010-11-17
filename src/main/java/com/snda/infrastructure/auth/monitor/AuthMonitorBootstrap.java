package com.snda.infrastructure.auth.monitor;

import com.snda.infrastructure.auth.monitor.listener.EmailListener;
import com.snda.infrastructure.auth.monitor.listener.PersistenceListener;
import com.snda.infrastructure.auth.monitor.plugin.qidian.QidianSeleniumMonitor;
import com.snda.infrastructure.auth.monitor.selenium.SeleniumConfig;

public class AuthMonitorBootstrap {

	public static void main(String[] args) {
		AuthMonitorConsole console = new AuthMonitorConsoleImpl();
		console.addMonitor(new QidianSeleniumMonitor(usernamePassword(), seneniumConfig(), expectedText()));
		console.addListener(new PersistenceListener()).on(AuthResultType.SUCCESS, AuthResultType.FAILED);
		console.addListener(new EmailListener(receptionist())).on(AuthResultType.FAILED);
		console.schedulerAt(cron());
		console.start();
	}

	private static UsernamePassword usernamePassword() {
		return new UsernamePassword("sdotracker2010", "1234567890");
	}
	
	private static SeleniumConfig seneniumConfig() {
		return new SeleniumConfig("localhost", 4444, "*firefox", "http://www.qidian.com/");
	}
	
	private static String expectedText() {
		return "101116141742200";
	}
	
	private static String cron() {
		return "0/2 * * * * ?";
	}

	private static String[] receptionist() {
		return new String[] {"java2enterprise@gmail.com", "auth_monitor@snda.com"};
	}

}
