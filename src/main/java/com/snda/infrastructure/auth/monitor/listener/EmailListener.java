package com.snda.infrastructure.auth.monitor.listener;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.infrastructure.auth.monitor.AuthListener;
import com.snda.infrastructure.auth.monitor.AuthResult;

public class EmailListener implements AuthListener {

	private static Logger logger = LoggerFactory.getLogger(PersistenceListener.class);
	
	private final List<String> receptionist;

	public EmailListener(String... receptionist) {
		this.receptionist = Arrays.asList(receptionist);
	}

	@Override
	public void onResult(AuthResult authResult) {
		logger.error("Sending email to : " + receptionist + ", content : " + authResult);
	}

}
