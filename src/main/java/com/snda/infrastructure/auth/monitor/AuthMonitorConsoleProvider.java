package com.snda.infrastructure.auth.monitor;

import static com.snda.infrastructure.auth.monitor.Environment.$;

import com.google.common.base.Splitter;
import com.googlecode.functionalcollections.FunctionalIterables;
import com.snda.infrastructure.auth.monitor.listener.EmailListener;
import com.snda.infrastructure.auth.monitor.listener.MailSenders;
import com.snda.infrastructure.auth.monitor.listener.PersistenceListener;
import com.snda.infrastructure.auth.monitor.plugin.qidian.QidianSeleniumMonitor;
import com.snda.infrastructure.auth.monitor.selenium.SeleniumConfig;

public class AuthMonitorConsoleProvider {

	private AuthMonitorConsole console = new AuthMonitorConsoleImpl();
	
	public static void main(String[] args) {
		new AuthMonitorConsoleProvider().get().start();
	}
	
	public AuthMonitorConsole get() {
		Environment.initialize();
		console.registerMonitor(new QidianSeleniumMonitor(seneniumConfig(), expectedText())).with(authContext());
		console.registerListener(new PersistenceListener()).on(AuthResultType.any());
		console.registerListener(new EmailListener(MailSenders.create(), from(), receptionist())).on(AuthResultType.FAILED);
		console.schedulerAt($("cron"));
		return console.build();
	}

	private String timeout() {
		return $("page.timeout");
	}
	
	private AuthContext authContext() {
		return new AuthContext("http://61.172.251.13/", $("auth.username"), $("auth.password"));
	}

	private SeleniumConfig seneniumConfig() {
		return new SeleniumConfig(
			$("selenium.server.host"), 
			Integer.parseInt($("selenium.server.port")), 
			"*firefox3",
			timeout()
		);
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
