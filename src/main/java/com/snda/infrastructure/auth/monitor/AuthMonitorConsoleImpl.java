package com.snda.infrastructure.auth.monitor;

import java.util.ArrayList;
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

import com.google.common.base.Throwables;

public class AuthMonitorConsoleImpl implements AuthMonitorConsole {

	private static Logger logger = LoggerFactory.getLogger(AuthMonitorConsoleImpl.class);
	
	public static final String MONITORS_KEY = "monitors";
	public static final String LISTENERS_KEY = "listeners";
	
	private SchedulerFactory schedFact = new StdSchedulerFactory();
	private Scheduler scheduler;

	private List<AuthMonitor> monitors = new ArrayList<AuthMonitor>();
	private String cronExpression;
	private List<ClassfiedAuthListenerBuilderImpl> builders = new ArrayList<ClassfiedAuthListenerBuilderImpl>();
	private List<ClassfiedAuthListener> listeners = new ArrayList<ClassfiedAuthListener>();

	@Override
	public AuthMonitorConsole registerMonitor(AuthMonitor monitor) {
		this.monitors.add(monitor);
		return this;
	}
	
	@Override
	public ClassfiedAuthListenerBuilder registerListener(AuthListener authListener) {
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
		secheduleJob();
	}

	private void secheduleJob() {
		try {
			scheduler = schedFact.getScheduler();
			scheduler.start();
			JobDetail jobDetail = new JobDetail("kernel-job", "kernel-job-group", KernelJob.class);
			jobDetail.getJobDataMap().put(MONITORS_KEY, monitors);
			jobDetail.getJobDataMap().put(LISTENERS_KEY, listeners);
			Trigger trigger = new CronTrigger("kernel-job-trigger", "kernel-job-trigger-group", cronExpression);
			trigger.setStartTime(new Date());
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("Failed to start quartz job : " + e.getMessage(), e);
			Throwables.propagate(e);
		}
	}

	private void buildListeners() {
		for (ClassfiedAuthListenerBuilderImpl impl : builders) {
			ClassfiedAuthListener listener = impl.build();
			this.listeners.add(listener);
		}
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



}
