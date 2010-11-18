package com.snda.infrastructure.auth.monitor;

import static com.snda.infrastructure.auth.monitor.Environment.$;

import java.util.Date;

import com.google.common.base.Splitter;
import com.googlecode.functionalcollections.FunctionalIterables;
import com.snda.infrastructure.auth.monitor.listener.EmailListener;
import com.snda.infrastructure.auth.monitor.listener.PersistenceListener;
import com.snda.infrastructure.auth.monitor.plugin.qidian.QidianSeleniumMonitor;
import com.snda.infrastructure.auth.monitor.selenium.SeleniumConfig;

public class AuthMonitorBootstrap {

	private AuthMonitorConsole console = new AuthMonitorConsoleImpl();
	
	public static void main(String[] args) {
		new AuthMonitorBootstrap().start();
	}
	
	public void start() {
		Environment.initialize();
		console.registerMonitor(new QidianSeleniumMonitor(authContext(), seneniumConfig(), expectedText()));
		console.registerListener(new PersistenceListener()).on(AuthResultType.SUCCESS, AuthResultType.FAILED);
		console.registerListener(new EmailListener(MailSenders.create(), from(), receptionist())).on(AuthResultType.SUCCESS, AuthResultType.FAILED);
		console.schedulerAt($("cron"));
		console.start();
	}
	
	public void stop() {
		console.stop();
	}
	
	private AuthContext authContext() {
		return new AuthContext("http://www.qidian.com/", "sdotracker2010", "1234567890", new Date());
	}

	private SeleniumConfig seneniumConfig() {
		return new SeleniumConfig($("selenium.server.host"), Integer.parseInt($("selenium.server.port")), "*firefox");
	}
	
	private String expectedText() {
		return "101116141742200";
	}
	
	private String from() {
		return $("mail.from.address");
	}
	
	private String[] receptionist() {
		return FunctionalIterables.make(Splitter.on(",").split($("mail.to.addresses"))).toArray(String.class);
	}
	
}
