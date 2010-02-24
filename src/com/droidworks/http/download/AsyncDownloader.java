package com.droidworks.http.download;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;

import com.droidworks.http.HttpGetWorker;
import com.droidworks.http.HttpUtils;

// TODO: this thing is a piece of crap, we need to create an interface
// and then rebuild something that works better..
public class AsyncDownloader implements DownloadManager {

	public static final String LOG_LABEL = "AsyncDownloader";

	private LinkedBlockingQueue<DownloadTask<?>> mTasks
		= new LinkedBlockingQueue<DownloadTask<?>>(500);

	private LinkedList<DownloadLooper> mLoopers
		= new LinkedList<DownloadLooper>();

	private int mMaxThreads = 10;
	private int mPollDuration = 5;
	private int mDownloadCompleteTimeout = 3600;

	private static int mLooperCount = 0;

	private ExecutorService mExecutor;

	/* (non-Javadoc)
	 * @see com.droidworks.http.download.DownloadManager#setPollingDuration(int)
	 */
	public synchronized void setPollingDuration(int seconds) {
		mPollDuration = seconds;
	}

	/* (non-Javadoc)
	 * @see com.droidworks.http.download.DownloadManager#getThreadCount()
	 */
	public int getThreadCount() {
		int count = 0;
		for (DownloadLooper l : mLoopers) {
			if (l.getState() != Thread.State.TERMINATED)
				count++;
		}
		return count;
	}

	/* (non-Javadoc)
	 * @see com.droidworks.http.download.DownloadManager#isQueued(com.droidworks.http.download.DownloadTask)
	 */
	public boolean isQueued(DownloadTask<?> task) {

		if (mTasks.contains(task))
			return true;

		return false;
	}

	/* (non-Javadoc)
	 * @see com.droidworks.http.download.DownloadManager#addDownloadTask(com.droidworks.http.download.DownloadTask)
	 */
	public void addDownloadTask(DownloadTask<?> task) {
		mTasks.add(task);

		// if no threads are running then we always start a thread
		if (getThreadCount() == 0) {
			startNewLooper();
		}
		// if we have a backlog and have room for more threads, fire
		// another one up
		else if (mTasks.size() > 1 && getThreadCount() < mMaxThreads) {
			startNewLooper();
		}

		drainLoopers();
	}

	// drain off dead loopers
	private void drainLoopers() {
		ArrayList<DownloadLooper> removeList = new ArrayList<DownloadLooper>();
		for (DownloadLooper l : mLoopers) {
			if (l.getState() == Thread.State.TERMINATED) {
				removeList.add(l);
			}
		}
		for (DownloadLooper l : removeList) {
			mLoopers.remove(l);
		}
	}

	private void startNewLooper() {
		DownloadLooper looper = new DownloadLooper("Looper " + ++mLooperCount);
		looper.start();
		mLoopers.add(looper);
	}

	/* (non-Javadoc)
	 * @see com.droidworks.http.download.DownloadManager#cancelAll()
	 */
	public void cancelAll() {
		for (DownloadLooper l : mLoopers) {
			l.cancel();
			l.interrupt();
		}
	}

	/* (non-Javadoc)
	 * @see com.droidworks.http.download.DownloadManager#setMaxThreads(int)
	 */
	public synchronized void setMaxThreads(int count) {
		mMaxThreads = count;
	}

	// private to prevent instantiation
	public AsyncDownloader() {
		mExecutor = Executors.newCachedThreadPool();
	}

	class DownloadLooper extends Thread {

		private boolean _shutdown = false;
		private HttpGetWorker _worker;

		private final HttpClient _client
			= HttpUtils.getThreadSafeClient();

		private int _resultCode;

		public DownloadLooper(String name) {
			super(name);
		}

		public void cancel() {
			_shutdown = true;

			if (_worker != null) {
				_worker.cancel();
			}
		}

		@Override
		public void run() {
			android.os.Process.setThreadPriority(
					android.os.Process.THREAD_PRIORITY_LOWEST);

			while (!_shutdown) {

				DownloadTask<?> task;

				// grab a task
				try {
					synchronized (AsyncDownloader.this) {
						_resultCode = -1;
						task = mTasks.poll(mPollDuration, TimeUnit.SECONDS);

						// shutdown on a null task
						if (task == null || _shutdown) {
							_shutdown = true;
							return;
						}

						// if we have a valid task, init a worker
						if (task.getUrl() != null) {
							HttpGet get = new HttpGet(task.getUrl());

							// TODO, resetting the timeout each time is kind of lame
							// it makes more sense for the task's all to have the
							// same timeout value..
							HttpUtils.setConnectionTimeout(_client, task.getTimeout());
							_worker = new HttpGetWorker(get, null, _client);
						}
					}

					// if we have a worker, then we can do the download.
					if (_worker != null) {
						try {
							HttpResponse response = mExecutor.submit(_worker)
								.get(mDownloadCompleteTimeout, TimeUnit.SECONDS);

							task.processStream(response.getEntity().getContent());
						}
						catch (ExecutionException e) {
							if (e.getCause() instanceof SocketTimeoutException) {
								_resultCode = DownloadTask.STATUS_TIMED_OUT;
							}
							// cancelled tasks are receiving an EE
							Log.e(LOG_LABEL, "Caught execution exception on task: " + task.getUrl(), e );
						} catch (TimeoutException e) {
							_resultCode = DownloadTask.STATUS_TIMED_OUT;
							Log.e(LOG_LABEL, "Caught timeout exception task: " + task.getUrl(), e );
						} catch (IllegalStateException e) {
							_resultCode = DownloadTask.STATUS_GENERAL_ERROR;
							Log.e(LOG_LABEL, "Caught exception: " + task.getUrl(), e );
						} catch (IOException e) {
							_resultCode = DownloadTask.STATUS_GENERAL_ERROR;
							Log.e(LOG_LABEL, "Caught exception: " + task.getUrl(), e );
						}
					}

					task.notifyListeners();
				}
				catch (InterruptedException ignored) {
					_shutdown = true;
				}
			}
		}
	}

	@Override
	public void setTaskTimeout(int seconds) {
		// ignored
	}

}
