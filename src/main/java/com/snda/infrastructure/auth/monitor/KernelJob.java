package com.snda.infrastructure.auth.monitor;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public final class KernelJob implements Job {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		List<AuthMonitor> monitors = (List<AuthMonitor>) jobDataMap.get(AuthMonitorConsoleImpl.MONITORS_KEY);
		List<ClassfiedAuthListener> listeners = (List<ClassfiedAuthListener>) jobDataMap.get(AuthMonitorConsoleImpl.LISTENERS_KEY);
		try {
			for (AuthMonitor monitor : monitors) {
				AuthResult result = monitor.execute();
				for (ClassfiedAuthListener listener : listeners) {
					if (listener.types().contains(result.resultType())) {
						listener.authListener().onResult(result);
					}
				}	
			}
		} catch (RuntimeException e) {
			throw new JobExecutionException(e);
		}
	}
	
}