package com.snda.infrastructure.auth.monitor;

import java.util.Date;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.snda.infrastructure.auth.monitor.listener.EmailListener;
import com.snda.infrastructure.auth.monitor.listener.PersistenceListener;
import com.snda.infrastructure.auth.monitor.plugin.qidian.QidianSeleniumMonitor;
import com.snda.infrastructure.auth.monitor.selenium.SeleniumConfig;

public class AuthMonitorBootstrap {

	public static void main(String[] args) {
		AuthMonitorConsole console = new AuthMonitorConsoleImpl();
		console.addMonitor(new QidianSeleniumMonitor(authContext(), seneniumConfig(), expectedText()));
		console.addListener(new PersistenceListener()).on(AuthResultType.SUCCESS, AuthResultType.FAILED);
		console.addListener(new EmailListener(mailSender(), from(), receptionist())).on(AuthResultType.SUCCESS, AuthResultType.FAILED);
		console.schedulerAt(cron());
		console.start();
	}

	private static AuthContext authContext() {
		return new AuthContext("http://www.qidian.com/", "sdotracker2010", "1234567890", new Date());
	}

	private static SeleniumConfig seneniumConfig() {
		return new SeleniumConfig("localhost", 4444, "*firefox");
	}
	
	private static String expectedText() {
		return "101116141742200";
	}
	
	private static String cron() {
		return "0/2 * * * * ?";
	}

	private static JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("61.172.242.14");
		mailSender.setPort(25);
		mailSender.setUsername("");
		mailSender.setPassword("");
		Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.timeout", "25000");
//		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
	
	private static String from() {
		return "noreply@snda.com";
	}
	
	private static String[] receptionist() {
		return new String[] {"java2enterprise@gmail.com", "wangzheng.james@snda.com"};
	}
	
}
