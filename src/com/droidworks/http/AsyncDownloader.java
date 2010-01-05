package com.droidworks.http;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AsyncDownloader {

	public static final AsyncDownloader mDownloader = new AsyncDownloader();

	private ArrayBlockingQueue<DownloadTask> mTasks
		= new ArrayBlockingQueue<DownloadTask>(500);

	private ArrayList<DownloadLooper> mLoopers
		= new ArrayList<DownloadLooper>();

	private int mMaxThreads = 3;

	public synchronized int getThreadCount() {
		return mLoopers.size();
	}

	public synchronized void addDownloadTask(DownloadTask task) {
		mTasks.add(task);

		// if no threads are running then we always start a thread
		if (mLoopers.size() == 0) {
			DownloadLooper looper = new DownloadLooper();
			new Thread(looper).start();
			mLoopers.add(looper);
		}
		// if we have a backlog and have room for more threads, fire
		// another one up
		else if (mTasks.size() > 1 && mLoopers.size() < mMaxThreads) {
			DownloadLooper looper = new DownloadLooper();
			new Thread(looper).start();
			mLoopers.add(looper);
		}
	}

	public void cancelAll() {
		// TODO cancel EVERYTHING
	}

	public void setMaxThreads(int count) {
		mMaxThreads = count;
	}

	private AsyncDownloader() {
		// prevent instantiation
	}

	public static AsyncDownloader getDownloader() {
		return mDownloader;
	}

	class DownloadLooper implements Runnable {

		private boolean _shutdown = false;

		@Override
		public void run() {
			while (!_shutdown) {

				// grab a task, but only wait 5 seconds;
				try {
					DownloadTask task = mTasks.poll(5, TimeUnit.SECONDS);
					String path = null;

					// shutdown on a null task
					if (task == null) {
						_shutdown = true;
						mLoopers.remove(this);
						return;
					}

					if (task.getUrl() != null) {
						// TODO, do some stuff
					}
					// notify listeners
					for (DownloadCompletedListener l : task.getListeners()) {
						l.onDownloadComplete(path);
					}
				}
				catch (InterruptedException e) {
					// TODO, i need to test this
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static class DownloadTask {

		private final String mUrl;

		private final ArrayList<DownloadCompletedListener> mListeners
			= new ArrayList<DownloadCompletedListener>();

		public DownloadTask(String url) {
			mUrl = url;
		}

		public void addListener(DownloadCompletedListener listener) {
			mListeners.add(listener);
		}

		public String getUrl() {
			return mUrl;
		}

		public ArrayList<DownloadCompletedListener> getListeners() {
			return mListeners;
		}
	}

	public interface DownloadCompletedListener {
		public void onDownloadComplete(String path);
	}

}
