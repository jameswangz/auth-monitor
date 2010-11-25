package com.snda.infrastructure.auth.monitor.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.snda.infrastructure.auth.monitor.AuthMonitorConsole;
import com.snda.infrastructure.auth.monitor.AuthMonitorConsoleProvider;

public class BootstrapListener implements ServletContextListener {

	public static final String CONSOLE_PROVIDER = BootstrapListener.class + ".consoleProvider";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		AuthMonitorConsoleProvider provider  = new AuthMonitorConsoleProvider();
		AuthMonitorConsole console = provider.get();
		sce.getServletContext().setAttribute(CONSOLE_PROVIDER, console);
		console.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		AuthMonitorConsole console = (AuthMonitorConsole) sce.getServletContext().getAttribute(CONSOLE_PROVIDER);
		console.stop();
	}

}
