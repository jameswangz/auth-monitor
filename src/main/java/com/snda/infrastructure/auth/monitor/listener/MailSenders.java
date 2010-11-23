package com.snda.infrastructure.auth.monitor.listener;

import static com.snda.infrastructure.auth.monitor.Environment.$;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public abstract class MailSenders {

	public static JavaMailSender create() {		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost($("mail.smtp.server.host"));
		mailSender.setPort(Integer.parseInt($("mail.smtp.server.port")));
		mailSender.setUsername($("mail.smtp.username"));
		mailSender.setPassword($("mail.smtp.password"));
		Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.smtp.auth", $("mail.smtp.auth"));
		javaMailProperties.setProperty("mail.smtp.timeout", $("mail.smtp.timeout"));
//		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

}
