package com.droidworks.http.download;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.droidworks.http.HttpUtils;

public class ConcurrentDownloadManager implements DownloadManager {

	private final ThreadPoolExecutor mThreadPool;

	// TODO, do i need a reference to this?
	private final LinkedBlockingQueue<Runnable> mWorkQueue
		= new LinkedBlockingQueue<Runnable>();
	private final DefaultHttpClient mClient;

	// quick and dirty hack to tell us if an item has been q'd or not.
	private final HashSet<String> mQueued
		= new HashSet<String>();

	public ConcurrentDownloadManager() {
		mThreadPool = new ThreadPoolExecutor(0, 6, 10, TimeUnit.SECONDS,
				mWorkQueue);
		mClient = HttpUtils.getThreadSafeClient();
	}

	@Override
	public void addDownloadTask(DownloadTask<?> task) {
		mQueued.add(task.getUrl());
		mThreadPool.submit(new TaskRunnable(task));
	}

	private class TaskRunnable implements Runnable {

		private final DownloadTask<?> mTask;

		public TaskRunnable(DownloadTask<?> task) {
			mTask = task;
		}

		public void run() {
			try {
				HttpResponse response = mClient.execute(new HttpGet(mTask.getUrl()));
				mTask.processStream(response.getEntity().getContent());
			}
			catch (Exception e) {
				Log.e(getClass().getCanonicalName(), "Download Exception", e);
			}

			mQueued.remove(mTask.getUrl());
			mTask.notifyListeners();
		}
	}

	@Override
	public void cancelAll() {
		// shutdown the threadpool
		mThreadPool.shutdownNow();
	}

	@Override
	public int getThreadCount() {
		return mThreadPool.getPoolSize();
	}

	@Override
	public boolean isQueued(DownloadTask<?> task) {
		return mQueued.contains(task.getUrl());
	}

	@Override
	public void setMaxThreads(int count) {
		mThreadPool.setMaximumPoolSize(count);
	}

	@Override
	public void setPollingDuration(int seconds) {
		// ignored
	}

	@Override
	public void setTaskTimeout(int seconds) {
		HttpUtils.setConnectionTimeout(mClient, seconds);
	}

}
