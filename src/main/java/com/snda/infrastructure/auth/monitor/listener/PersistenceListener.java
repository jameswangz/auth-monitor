package com.snda.infrastructure.auth.monitor.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.infrastructure.auth.monitor.AuthListener;
import com.snda.infrastructure.auth.monitor.AuthResult;

public class PersistenceListener implements AuthListener {
	
	private static Logger logger = LoggerFactory.getLogger(PersistenceListener.class);
	
	@Override
	public void onResult(AuthResult authResult) {
		logger.debug("Persisting authResult : " + authResult);
	}

}
