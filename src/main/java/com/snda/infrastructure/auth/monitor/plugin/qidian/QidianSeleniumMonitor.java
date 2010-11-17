package com.snda.infrastructure.auth.monitor.plugin.qidian;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.infrastructure.auth.monitor.AuthContext;
import com.snda.infrastructure.auth.monitor.AuthMonitor;
import com.snda.infrastructure.auth.monitor.AuthResult;
import com.snda.infrastructure.auth.monitor.AuthResultType;
import com.snda.infrastructure.auth.monitor.UsernamePassword;
import com.snda.infrastructure.auth.monitor.selenium.SeleniumConfig;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

public class QidianSeleniumMonitor implements AuthMonitor {

	private static Logger logger = LoggerFactory.getLogger(QidianSeleniumMonitor.class);
	
	private final UsernamePassword usernamePassword;
	private final SeleniumConfig seleniumConfig;
	private final Selenium selenium;
	private final String expectedText;

	public QidianSeleniumMonitor(UsernamePassword usernamePassword, SeleniumConfig seleniumConfig, String expectedText) {
		this.usernamePassword = usernamePassword;
		this.seleniumConfig = seleniumConfig;
		this.selenium = new DefaultSelenium(
			seleniumConfig.serverHost(), 
			seleniumConfig.serverPort(), 
			seleniumConfig.browserStartCommand(), 
			seleniumConfig.browserURL()
		);
		this.expectedText = expectedText;
	}

	@Override
	public AuthResult execute() {
		Date time = new Date();
		
		selenium.start();
		
		selenium.open("/");
		selenium.type("ptname", usernamePassword.username());
		selenium.type("ptpwd", usernamePassword.password());
		selenium.click("btn_user_login");
		
		boolean success = false;
		String detail = null;
		
		try {
			selenium.waitForPageToLoad("30000");
			success = selenium.isTextPresent(expectedText);
		} catch (SeleniumException e) {
			logger.error(e.getMessage(), e);
			success = false;
			detail = "Login failed due to : " + e.getMessage();
		}
		
		if (detail == null) {
			detail = success ? "Login successfully" : "Login failed";
		}
		
		selenium.stop();
		
		AuthContext authContext = new AuthContext(seleniumConfig.serverHost(), usernamePassword.username(), time);
		AuthResultType resultType = success ? AuthResultType.SUCCESS : AuthResultType.FAILED;
		return new AuthResult(authContext, resultType, detail);
	}

}
