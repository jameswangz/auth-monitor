package com.snda.infrastructure.auth.monitor.listener;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.snda.infrastructure.auth.monitor.AuthListener;
import com.snda.infrastructure.auth.monitor.AuthResult;

public class EmailListener implements AuthListener {

	private static Logger logger = LoggerFactory.getLogger(EmailListener.class);

	private final JavaMailSender mailSender;
	private final String from;
	private final String[] receptionist;

	public EmailListener(JavaMailSender mailSender, String from, String... receptionist) {
		this.mailSender = mailSender;
		this.from = from;
		this.receptionist = receptionist;
	}

	@Override
	public void onResult(AuthResult authResult) {
		logger.error("Sending email to : " + Arrays.asList(receptionist) + ", content : " + authResult);
		this.mailSender.send(messageOf(authResult));
		logger.error("Succesuffly sent mail to : " + Arrays.asList(receptionist));
	}

	private SimpleMailMessage messageOf(AuthResult authResult) {
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setFrom(from);
		simpleMessage.setTo(receptionist);
		simpleMessage.setSubject("Error notification from snda auth monitor");
		simpleMessage.setText(
			String.format(
				"This is a notification from snda auth monitor : \n\n " +
				"---------------------------------------------------------- \n\n " +
				"We detected an error while trying to signin [ %s ] at [ %s ],  " +
				"detail message [ %s ], please fix it asap, thanks. \n\n" +
				"---------------------------------------------------------- \n\n ",
				authResult.authContext().site(), 
				df().format(authResult.authContext().time()), 
				authResult.detail()
			)
		);
		return simpleMessage;
	}

	private SimpleDateFormat df() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

}
