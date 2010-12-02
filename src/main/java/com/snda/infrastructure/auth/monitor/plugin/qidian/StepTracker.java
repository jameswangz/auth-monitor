package com.snda.infrastructure.auth.monitor.plugin.qidian;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class StepTracker {

	private List<String> trace = Lists.newArrayList();

	public StepTracker track(String name) {
		trace.add(name);
		return this;
	}

	public Iterator<String> trace() {
		return trace.iterator();
	}
	
}
