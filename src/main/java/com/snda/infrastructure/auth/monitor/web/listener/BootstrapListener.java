package com.snda.infrastructure.auth.monitor.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.snda.infrastructure.auth.monitor.AuthMonitorBootstrap;

public class BootstrapListener implements ServletContextListener {

	private AuthMonitorBootstrap bootstrap  = new AuthMonitorBootstrap();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		bootstrap.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		bootstrap.stop();
	}

}
