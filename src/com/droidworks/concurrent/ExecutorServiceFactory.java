package com.droidworks.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Deprecated
public class ExecutorServiceFactory {

	private static final ExecutorService executor =
		new ScheduledThreadPoolExecutor(2);

	public static ExecutorService getService() {
		return executor;
	}

}
