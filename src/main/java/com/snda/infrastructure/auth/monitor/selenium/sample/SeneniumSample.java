package com.snda.infrastructure.auth.monitor.selenium.sample;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class SeneniumSample {

	
	public static void main(String[] args) {
		Selenium selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://www.sdo.com/");
		selenium.start();

		selenium.open("/LoginFrame.asp?appDomain=qidian.com");
		selenium.type("ptname", "sdotracker2010");
		selenium.type("ptpwd", "1234567890");
		selenium.click("btn_user_login");
		selenium.waitForPageToLoad("30000");
		selenium.selectFrame("iframeLogin");
		System.out.println(selenium.isTextPresent("sdo****"));

		selenium.stop();
	}
	

	
}
