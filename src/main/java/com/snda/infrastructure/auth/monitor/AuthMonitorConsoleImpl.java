package com.snda.infrastructure.auth.monitor;

import java.util.Date;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class AuthMonitorConsoleImpl implements AuthMonitorConsole {

	private static Logger logger = LoggerFactory.getLogger(AuthMonitorConsoleImpl.class);
	
	public static final String COMMAND_KEY = "command";
	
	private boolean built = false;
	private SchedulerFactory schedFact = new StdSchedulerFactory();
	private Scheduler scheduler;
	private String cronExpression;
	private List<AuthMonitorWithContextBuilder> monitorBuilders = Lists.newArrayList();
	private List<AuthMonitorWithContext> monitors = Lists.newArrayList();
	private List<ClassfiedAuthListenerBuilder> listenerBuilders = Lists.newArrayList();
	private List<ClassfiedAuthListener> listeners = Lists.newArrayList();

	@Override
	public AuthMonitorWithContextBuilder registerMonitor(AuthMonitor monitor) {
		AuthMonitorWithContextBuilder monitorBuilder = new AuthMonitorWithContextBuilderImpl(monitor);
		this.monitorBuilders.add(monitorBuilder);
		return monitorBuilder;
	}
	
	@Override
	public ClassfiedAuthListenerBuilder registerListener(AuthListener authListener) {
		ClassfiedAuthListenerBuilder listenerBuilder = new ClassfiedAuthListenerBuilderImpl(authListener);
		this.listenerBuilders.add(listenerBuilder);
		return listenerBuilder;
	}

	@Override
	public AuthMonitorConsole schedulerAt(String cronExpression) {
		this.cronExpression = cronExpression;
		return this;
	}
	
	@Override
	public AuthMonitorConsole build() {
		buildMonitors();
		buildListeners();
		built = true;
		return this;
	}

	private void buildMonitors() {
		for (AuthMonitorWithContextBuilder builder : monitorBuilders) {
			this.monitors.add(builder.build());
		}
	}
	
	private void buildListeners() {
		for (ClassfiedAuthListenerBuilder builder : listenerBuilders) {
			this.listeners.add(builder.build());
		}
	}
	
	@Override
	public void start() {
		checkBuilt();
		secheduleJob();
	}
	
	private void checkBuilt() {
		Preconditions.checkState(built , "Hasn't built yet.");
	}

	private void secheduleJob() {
		try {
			scheduler = schedFact.getScheduler();
			scheduler.start();
			JobDetail jobDetail = new JobDetail("kernel-job", "kernel-job-group", KernelJob.class);
			jobDetail.getJobDataMap().put(COMMAND_KEY, command());
			Trigger trigger = new CronTrigger("kernel-job-trigger", "kernel-job-trigger-group", cronExpression);
			trigger.setStartTime(new Date());
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("Failed to start quartz job : " + e.getMessage(), e);
			Throwables.propagate(e);
		}
	}
	
	private Runnable command() {
		return new Runnable() {
			@Override
			public void run() {
				for (AuthMonitorWithContext monitorWithContext : monitors) {
					AuthResult result = monitorWithContext.execute();
					for (ClassfiedAuthListener listener : listeners) {
						if (listener.types().contains(result.resultType())) {
							listener.authListener().onResult(result);
						}
					}	
				}
			}
		};
	}

	@Override
	public void stop() {
		try {
			scheduler.shutdown();
			scheduler = null;
		} catch (SchedulerException e) {
			Throwables.propagate(e);
		}
	}

	@Override
	public void fireNow() {
		checkBuilt();
		command().run();
	}


}
