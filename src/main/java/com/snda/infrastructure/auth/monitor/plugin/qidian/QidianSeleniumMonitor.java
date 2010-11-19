package com.snda.infrastructure.auth.monitor.plugin.qidian;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.infrastructure.auth.monitor.AuthContext;
import com.snda.infrastructure.auth.monitor.AuthMonitor;
import com.snda.infrastructure.auth.monitor.AuthResult;
import com.snda.infrastructure.auth.monitor.AuthResultType;
import com.snda.infrastructure.auth.monitor.selenium.SeleniumConfig;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

public class QidianSeleniumMonitor implements AuthMonitor {

	private static Logger logger = LoggerFactory.getLogger(QidianSeleniumMonitor.class);
	
	private final AuthContext authContext;
	private final Selenium selenium;
	private final String expectedText;

	public QidianSeleniumMonitor(AuthContext authContext, SeleniumConfig seleniumConfig, String expectedText) {
		this.authContext = authContext;
		this.selenium = new DefaultSelenium(
			seleniumConfig.serverHost(), 
			seleniumConfig.serverPort(), 
			seleniumConfig.browserStartCommand(), 
			authContext.site()
		);
		this.selenium.setSpeed("2000");
		this.expectedText = expectedText;
	}

	@Override
	public AuthResult execute() {
		Date time = new Date();
		selenium.start();
		
		boolean success = false;
		String detail = null;
		
		try {
			selenium.open("/");
			selenium.type("ptname", authContext.username());
			selenium.type("ptpwd", authContext.password());
			selenium.click("btn_user_login");
			selenium.waitForPageToLoad("50000");
			success = selenium.isTextPresent(expectedText);
			if (success) {
				selenium.click("link=退出");
				selenium.waitForPageToLoad("50000");
			}
		} catch (SeleniumException e) {
			logger.error(e.getMessage(), e);
			success = false;
			detail = "Login failed due to : " + e.getMessage();
		}
		
		if (detail == null) {
			detail = success ? "Login successfully" : "Login failed";
		}
		
		selenium.stop();
		
		return authResultOf(time, success, detail);
	}

	private AuthResult authResultOf(Date time, boolean success, String detail) {
		AuthResultType resultType = success ? AuthResultType.SUCCESS : AuthResultType.FAILED;
		return new AuthResult(authContext, time, resultType, detail);
	}

}
