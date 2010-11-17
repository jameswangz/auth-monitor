package com.snda.infrastructure.auth.monitor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.SeleniumException;


public class QidianAuthMonitorTest extends SeleneseTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(QidianAuthMonitorTest.class);
	
	String username = "sdotracker2010";
	String password = "1234567890";

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://www.qidian.com/");
	}

	@Test
	public void testUntitled() throws Exception {
		selenium.start();
		
		selenium.open("/");
		selenium.type("ptname", username);
		selenium.type("ptpwd", password);
		selenium.click("btn_user_login");
		
		boolean success = false;
		
		try {
			selenium.waitForPageToLoad("10000");
			success = selenium.isTextPresent("101116141742200");
		} catch (SeleniumException e) {
			logger.error(e.getMessage(), e);
			success = false;
		}
		
		logger.debug("Login success : " + success);
		selenium.stop();
	}

	@After
	public void tearDown() throws Exception {

	}
	
}
