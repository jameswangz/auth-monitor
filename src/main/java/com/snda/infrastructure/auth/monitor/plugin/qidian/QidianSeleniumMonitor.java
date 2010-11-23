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
	
	private final SeleniumConfig seleniumConfig;
	private final String expectedText;

	public QidianSeleniumMonitor(
		SeleniumConfig seleniumConfig, 
		String expectedText) {
		this.seleniumConfig = seleniumConfig;
		this.expectedText = expectedText;
	}

	@Override
	public AuthResult execute(AuthContext authContext) {
		Date time = new Date();
		
		Selenium selenium = new DefaultSelenium(
			seleniumConfig.serverHost(), 
			seleniumConfig.serverPort(), 
			seleniumConfig.browserStartCommand(), 
			authContext.site()
		);
		
		selenium.start();
		
		boolean success = false;
		String detail = null;
		
		try {
			selenium.setSpeed("2000");
			selenium.setTimeout(seleniumConfig.timeout());
			selenium.open("/");
			selenium.type("ptname", authContext.username());
			selenium.type("ptpwd", authContext.password());
			selenium.click("btn_user_login");
			selenium.waitForPageToLoad(seleniumConfig.timeout());
			success = selenium.isTextPresent(expectedText);
			if (success) {
				selenium.click("link=退出");
				selenium.waitForPageToLoad(seleniumConfig.timeout());
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
		
		return authResultOf(authContext, time, success, detail);
	}

	private AuthResult authResultOf(AuthContext authContext, Date time, boolean success, String detail) {
		AuthResultType resultType = success ? AuthResultType.SUCCESS : AuthResultType.FAILED;
		return new AuthResult(authContext, time, resultType, detail);
	}

}
