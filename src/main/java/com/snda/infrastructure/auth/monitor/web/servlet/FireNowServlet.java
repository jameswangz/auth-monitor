package com.snda.infrastructure.auth.monitor.web.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.snda.infrastructure.auth.monitor.AuthMonitorConsole;
import com.snda.infrastructure.auth.monitor.web.listener.BootstrapListener;

public class FireNowServlet extends HttpServlet {

	private static final long serialVersionUID = 1981236832470912598L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {
		ServletContext servletContext = req.getSession().getServletContext();
		AuthMonitorConsole console = (AuthMonitorConsole) servletContext.getAttribute(BootstrapListener.CONSOLE_PROVIDER);
		console.fireNow();
		resp.getWriter().write("Fired successfully.");
	}

}
