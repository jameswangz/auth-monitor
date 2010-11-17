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
	public void test() throws InterruptedException {
		AuthMonitorConsole console = new AuthMonitorConsoleImpl();
		AuthMonitor qidianMonitor = mock(AuthMonitor.class);
		console.addMonitor(qidianMonitor);
		AuthListener persistenceListener = mock(AuthListener.class);
		AuthListener emailListener = mock(AuthListener.class);
		console.addListener(persistenceListener).on(AuthResultType.SUCCESS, AuthResultType.FAILED);
		console.addListener(emailListener).on(AuthResultType.FAILED);
		console.schedulerAt("0/2 * * * * ?");
		
		AuthResult authResult = new AuthResult(
			new AuthContext("http://www.qidian.com/", "sdotracker2010", new Date()),
			AuthResultType.FAILED,
			"Timed out"
		);
		
		when(qidianMonitor.execute()).thenReturn(authResult);
		
		console.start();
		TimeUnit.SECONDS.sleep(60);
		console.stop();

		verify(persistenceListener, atLeastOnce()).onResult(authResult);
		verify(emailListener, atLeastOnce()).onResult(authResult);
	}
	
}
