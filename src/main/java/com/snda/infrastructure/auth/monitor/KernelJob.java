package com.snda.infrastructure.auth.monitor;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public final class KernelJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Runnable command = (Runnable) jobDataMap.get(AuthMonitorConsoleImpl.COMMAND_KEY);
		try {
			command.run();
		} catch (RuntimeException e) {
			throw new JobExecutionException(e);
		}
	}
	
}
