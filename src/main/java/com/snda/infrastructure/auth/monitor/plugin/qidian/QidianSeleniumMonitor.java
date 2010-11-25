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
//			selenium.open("/");
			selenium.open("/LoginFrame.asp?appDomain=qidian.com&CSSURL=http%3A%2F%2Fwww.qidian.com%2Fstyle%2FQidianOALoginBar1_1.css&returnURL=http%3A%2F%2Fwww.qidian.com%2FOALoginJump.aspx%3FreturnURL%3Dhttp%253A%252F%252Fwww.qidian.com%252F&curURL=http%3A%2F%2Fwww.qidian.com%2F&autologinchecked=0&autologintime=14");
			selenium.type("ptname", authContext.username());
			selenium.type("ptpwd", authContext.password());
			selenium.click("btn_user_login");
			selenium.waitForPageToLoad(seleniumConfig.timeout());
			success = selenium.isTextPresent(expectedText);
			if (success) {
				selenium.click("link=退出");
//				selenium.waitForPageToLoad(seleniumConfig.timeout());
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
