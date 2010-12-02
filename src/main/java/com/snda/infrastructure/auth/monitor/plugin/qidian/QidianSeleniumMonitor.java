package com.snda.infrastructure.auth.monitor.plugin.qidian;

import java.util.Date;
import java.util.Iterator;

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
		selenium.setSpeed("2000");
		selenium.setTimeout(seleniumConfig.timeout());
		
		boolean success = false;
		StringBuilder detail = new StringBuilder();
		StepTracker stepTracker = new StepTracker();
		
		try {
			stepTracker.track("打开登录页面");
			selenium.open("LoginFrame.asp?appDomain=qidian.com&CSSURL=http%3A%2F%2Fwww.qidian.com%2Fstyle%2FQidianOALoginBar1_1.css&returnURL=http%3A%2F%2Fwww.qidian.com%2FOALoginJump.aspx%3FreturnURL%3Dhttp%253A%252F%252Fwww.qidian.com%252F&curURL=http%3A%2F%2Fwww.qidian.com%2F&autologinchecked=0&autologintime=14&autologinwarntext=%u4E24%u5468%u5185%u4F1A%u4E3A%u60A8%u81EA%u52A8%u767B%u5F55%u8D77%u70B9%uFF0C%u4E3A%u4FDD%u969C%u60A8%u7684%u8D26%u53F7%u5B89%u5168%uFF0C%u8BF7%u4E0D%u8981%u5728%u7F51%u5427%u7B49%u516C%u5171%u8BA1%u7B97%u673A%u4E0A%u4F7F%u7528%u6B64%u8BBE%u7F6E&from=authmonitor");
			stepTracker.track("输入用户名");
			selenium.type("ptname", authContext.username());
			stepTracker.track("输入密码");
			selenium.type("ptpwd", authContext.password());
			stepTracker.track("点击登录");
			selenium.click("btn_user_login");
			stepTracker.track("等待登录成功页面");
			selenium.waitForPageToLoad(seleniumConfig.timeout());
			stepTracker.track("验证是否出现期望内容");
			success = selenium.isTextPresent(expectedText);
			if (success) {
				stepTracker.track("点击退出");
				selenium.click("link=退出");
//				selenium.waitForPageToLoad(seleniumConfig.timeout());
			}
		} catch (SeleniumException e) {
			logger.error(e.getMessage(), e);
			success = false;
			detail.append(e.getMessage());
		}
		
		if (success) {
			detail.append("Login successfully");
		} else {
			detail.append(stepsInfoOf(stepTracker));
		}
		
		selenium.stop();
		
		return authResultOf(authContext, time, success, detail.toString());
	}

	private String stepsInfoOf(StepTracker step) {
		StringBuilder stepsInfo = new StringBuilder();
		Iterator<String> recorded = step.trace();
		stepsInfo.append(" 登录步骤如下 ( 标记为 {{{ }}} 的为失败步骤 ) : ");
		while (recorded.hasNext()) {
			String stepName = recorded.next();
			if (recorded.hasNext()) {
				stepsInfo.append(stepName);
				stepsInfo.append(" -> ");
			} else {
				stepsInfo.append("{{{ ");
				stepsInfo.append(stepName);
				stepsInfo.append(" }}}");
			}
		}
		return stepsInfo.toString();
	}

	private AuthResult authResultOf(AuthContext authContext, Date time, boolean success, String detail) {
		AuthResultType resultType = success ? AuthResultType.SUCCESS : AuthResultType.FAILED;
		return new AuthResult(authContext, time, resultType, detail);
	}

}
