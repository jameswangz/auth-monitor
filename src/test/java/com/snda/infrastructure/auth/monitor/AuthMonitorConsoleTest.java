package com.snda.infrastructure.auth.monitor;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


public class AuthMonitorConsoleTest {
	
	@Test
	public void polling() throws InterruptedException {
		AuthContext authContext = new AuthContext("http://www.qidian.com/", "sdotracker2010", "******");
		AuthMonitorConsole console = new AuthMonitorConsoleImpl();
		AuthMonitor qidianMonitor = mock(AuthMonitor.class);
		console.registerMonitor(qidianMonitor).with(authContext);
		AuthListener persistenceListener = mock(AuthListener.class);
		AuthListener emailListener = mock(AuthListener.class);
		console.registerListener(persistenceListener).on(AuthResultType.any());
		console.registerListener(emailListener).on(AuthResultType.FAILED);
		console.schedulerAt("0/1 * * * * ?");
		
		AuthResult authResult = new AuthResult(
			authContext,
			new Date(), 
			AuthResultType.FAILED,
			"Timed out"
		);
		
		when(qidianMonitor.execute(authContext)).thenReturn(authResult);
		
		console.build().start();
		TimeUnit.SECONDS.sleep(5);
		console.stop();

		verify(persistenceListener, atLeastOnce()).onResult(authResult);
		verify(emailListener, atLeastOnce()).onResult(authResult);
	}

	@Test
	public void eventDriven() {
		AuthContext authContext = new AuthContext("http://www.qidian.com/", "sdotracker2010", "******");
		AuthMonitorConsole console = new AuthMonitorConsoleImpl();
		AuthMonitor qidianMonitor = mock(AuthMonitor.class);
		console.registerMonitor(qidianMonitor).with(authContext);
		AuthListener persistenceListener = mock(AuthListener.class);
		console.registerListener(persistenceListener).on(AuthResultType.any());
		
		AuthResult authResult = new AuthResult(
			authContext,
			new Date(), 
			AuthResultType.SUCCESS,
			null
		);
		
		when(qidianMonitor.execute(authContext)).thenReturn(authResult);
		
		console.build().fireNow();
		
		verify(persistenceListener).onResult(authResult);
	}
	
}
