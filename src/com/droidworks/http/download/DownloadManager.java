package com.droidworks.http.download;

public interface DownloadManager {

	/**
	 * The number of seconds a thread will wait for a new task before
	 * shutting itself down.  You probably don't want to change this
	 * under normal conditions.
	 *
	 * @param seconds
	 */
	public abstract void setPollingDuration(int seconds);

	/**
	 * Returns the number of active download threads.
	 *
	 * @return
	 */
	public abstract int getThreadCount();

	public abstract boolean isQueued(DownloadTask<?> task);

	public abstract void addDownloadTask(DownloadTask<?> task);

	public abstract void cancelAll();

	public abstract void setMaxThreads(int count);

	public abstract void setTaskTimeout(int seconds);

}