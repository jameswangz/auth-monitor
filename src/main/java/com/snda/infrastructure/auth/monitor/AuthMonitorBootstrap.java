package com.snda.infrastructure.auth.monitor;

import static com.snda.infrastructure.auth.monitor.Environment.$;

import com.google.common.base.Splitter;
import com.googlecode.functionalcollections.FunctionalIterables;
import com.snda.infrastructure.auth.monitor.listener.EmailListener;
import com.snda.infrastructure.auth.monitor.listener.MailSenders;
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
		console.registerMonitor(new QidianSeleniumMonitor(seneniumConfig(), expectedText())).with(authContext());
		console.registerListener(new PersistenceListener()).on(AuthResultType.any());
		console.registerListener(new EmailListener(MailSenders.create(), from(), receptionist())).on(AuthResultType.FAILED);
		console.schedulerAt($("cron"));
		console.start();
	}

	private String timeout() {
		return $("page.timeout");
	}
	
	public void stop() {
		console.stop();
	}
	
	private AuthContext authContext() {
		return new AuthContext("http://www.sdo.com/", $("auth.username"), $("auth.password"));
	}

	private SeleniumConfig seneniumConfig() {
		return new SeleniumConfig(
			$("selenium.server.host"), 
			Integer.parseInt($("selenium.server.port")), 
			"*firefox",
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
