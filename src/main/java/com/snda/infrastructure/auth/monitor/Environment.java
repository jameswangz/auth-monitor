package com.snda.infrastructure.auth.monitor;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public abstract class Environment {

	private static Logger logger = LoggerFactory.getLogger(Environment.class);
	
	private static Properties properties = new Properties();
	
	public static void initialize() {
		String configFile = "environment.properties";
		try {
			properties.load(
				Preconditions.checkNotNull(
					Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile),
					configFile + "missing!"
				)
			);
		} catch (IOException e) {
			logger.error("Failed to load config : " + configFile, e);
			Throwables.propagate(e);
		}
	}

	public static String $(String key) {
		return properties.getProperty(key);
	}

}
