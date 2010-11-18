package com.snda.infrastructure.auth.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthMonitorConsoleImpl implements AuthMonitorConsole {

	
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	private List<AuthMonitor> monitors = new ArrayList<AuthMonitor>();
	private String cronExpression;
	private List<ClassfiedAuthListenerBuilderImpl> builders = new ArrayList<ClassfiedAuthListenerBuilderImpl>();
	private List<ClassfiedAuthListener> listeners = new ArrayList<ClassfiedAuthListener>();


	@Override
	public AuthMonitorConsole addMonitor(AuthMonitor monitor) {
		this.monitors.add(monitor);
		return this;
	}
	
	@Override
	public ClassfiedAuthListenerBuilder addListener(AuthListener authListener) {
		ClassfiedAuthListenerBuilderImpl classfiedAuthListenerBuilderImpl 
			= new ClassfiedAuthListenerBuilderImpl(authListener);
		this.builders.add(classfiedAuthListenerBuilderImpl);
		return classfiedAuthListenerBuilderImpl;
	}

	@Override
	public AuthMonitorConsole schedulerAt(String cronExpression) {
		this.cronExpression = cronExpression;
		return this;
	}

	@Override
	public void start() {
		buildListeners();
		//FIXME use quartz instead of scheduler, otherwise cronExpression doesn't make sense
//		scheduler.scheduleAtFixedRate(command(), 1, 30, TimeUnit.SECONDS);
		command().run();
	}

	private void buildListeners() {
		for (ClassfiedAuthListenerBuilderImpl impl : builders) {
			ClassfiedAuthListener listener = impl.build();
			this.listeners.add(listener);
		}
	}

	private Runnable command() {
		return new Runnable() {
			@Override
			public void run() {
				for (AuthMonitor monitor : monitors) {
					try {
						AuthResult result = monitor.execute();
						for (ClassfiedAuthListener listener : listeners) {
							if (listener.types().contains(result.resultType())) {
								listener.authListener().onResult(result);
							}
						}	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	@Override
	public void stop() {
//		scheduler.shutdown();
	}



}
