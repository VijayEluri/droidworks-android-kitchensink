package com.droidworks.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.client.methods.HttpGet;

import android.os.Environment;
import android.util.Log;

import com.droidworks.util.AndroidUtils;

// TODO, this needs lots of changes, but first and foremost it should not
// be a singleton.
public class AsyncDownloader {

	public static int STATUS_OK = 0;
	public static int STATUS_TIMED_OUT = 1;
	public static int STATUS_NO_STORAGE = 2;
	public static int STATUS_FILE_WRITE_ERROR = 3;
	public static int STATUS_CANCELLED = 4;
	public static int STATUS_GENERAL_ERROR = 5;

	public static final AsyncDownloader mDownloader = new AsyncDownloader();

	public static final String LOG_LABEL = "AsyncDownloader";

	private LinkedBlockingQueue<DownloadTask> mTasks
		= new LinkedBlockingQueue<DownloadTask>(500);

	private LinkedList<DownloadLooper> mLoopers
		= new LinkedList<DownloadLooper>();

	private int mMaxThreads = 10;
	private int mPollDuration = 5;
	private int mDownloadCompleteTimeout = 3600;

	private static int mLooperCount = 0;

	private ExecutorService mExecutor;

	/**
	 * The number of seconds a thread will wait for a new task before
	 * shutting itself down.  You probably don't want to change this
	 * under normal conditions.
	 *
	 * @param seconds
	 */
	public synchronized void setPollingDuration(int seconds) {
		mPollDuration = seconds;
	}

	/**
	 * Returns the number of active download threads.
	 *
	 * @return
	 */
	public synchronized int getThreadCount() {
		int count = 0;
		for (DownloadLooper l : mLoopers) {
			if (l.getState() != Thread.State.TERMINATED)
				count++;
		}
		return count;
	}

	public synchronized void addDownloadTask(DownloadTask task) {
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

	public synchronized void cancelAll() {
		for (DownloadLooper l : mLoopers) {
			l.cancel();
			l.interrupt();
		}
	}

	public synchronized void setMaxThreads(int count) {
		mMaxThreads = count;
	}

	// private to prevent instantiation
	private AsyncDownloader() {
		mExecutor = Executors.newCachedThreadPool();
	}

	public static AsyncDownloader getDownloader() {
		return mDownloader;
	}

	class DownloadLooper extends Thread {

		private boolean _shutdown = false;
		private HttpGetWorker _worker;

		private int _resultCode;
		private String _outputFile;

		public DownloadLooper(String name) {
			super(name);
		}

		public void cancel() {
			_shutdown = true;

			if (_worker != null) {
				_worker.cancel();
			}
		}

		private void writeFile(InputStream is) {
			if (!AndroidUtils.hasStorage(true)) {
				_resultCode = STATUS_NO_STORAGE;
			}

	        String directoryName = Environment
        	.	getExternalStorageDirectory().toString() + "/tmp";

	        File directory = new File(directoryName);

	        if (!directory.isDirectory()) {
	            if (!directory.mkdirs()) {
	            	Log.e(LOG_LABEL, "can't create directory: " + directoryName);
					_resultCode = STATUS_FILE_WRITE_ERROR;
					return;
	            }
	        }
	        try {
				File tmpFile = File.createTempFile("ad_", null, directory);
				FileOutputStream os = new FileOutputStream(tmpFile);
				byte[] data = new byte[512];
				int bytesRead = 0;
				while ((bytesRead = is.read(data)) != -1) {
					os.write(data, 0, bytesRead);

					if (_shutdown) {
						_resultCode = STATUS_CANCELLED;
						return;
					}
				}

				// if we get here we suceeeded
				_outputFile = tmpFile.getAbsolutePath();
				_resultCode = STATUS_OK;
				is.close();
			}
	        catch (IOException e) {
				Log.e(LOG_LABEL, "Exception creating temp file", e);
				_resultCode = STATUS_FILE_WRITE_ERROR;
				return;
	        }
		}

		@Override
		public void run() {
			while (!_shutdown) {

				DownloadTask task;

				// grab a task
				try {
					synchronized (mDownloader) {
						_resultCode = -1;
						_outputFile = null;
						task = mTasks.poll(mPollDuration, TimeUnit.SECONDS);

						// shutdown on a null task
						if (task == null || _shutdown) {
							_shutdown = true;
							return;
						}

						// if we have a valid task, init a worker
						if (task.getUrl() != null) {
							HttpGet get = new HttpGet(task.getUrl());
							_worker = new HttpGetWorker(get, null, task.getTimeout());
						}
					}

					// if we have a worker, then we can do the download.
					if (_worker != null) {
						try {
							HttpResponse response = mExecutor.submit(_worker)
								.get(mDownloadCompleteTimeout, TimeUnit.SECONDS);

							writeFile(response.getEntity().getContent());
						}
						catch (ExecutionException e) {
							if (e.getCause() instanceof SocketTimeoutException) {
								_resultCode = AsyncDownloader.STATUS_TIMED_OUT;
							}
							// cancelled tasks are receiving an EE
							Log.e(LOG_LABEL, "Caught execution exception on task: " + task.getUrl(), e );
						} catch (TimeoutException e) {
							_resultCode = STATUS_TIMED_OUT;
							Log.e(LOG_LABEL, "Caught timeout exception task: " + task.getUrl(), e );
						} catch (IllegalStateException e) {
							_resultCode = STATUS_GENERAL_ERROR;
							Log.e(LOG_LABEL, "Caught exception: " + task.getUrl(), e );
						} catch (IOException e) {
							_resultCode = STATUS_GENERAL_ERROR;
							Log.e(LOG_LABEL, "Caught exception: " + task.getUrl(), e );
						}
					}

					// notify listeners
					for (DownloadCompletedListener l : task.getListeners()) {
						l.onDownloadComplete(_outputFile, _resultCode);
					}
				}
				catch (InterruptedException ignored) {
					_shutdown = true;
				}
			}
		}
	}


	public static class DownloadTask {

		private final String mUrl;
		private int mTimeOut = 10;  // default to a 10 second connection timeout

		private final ArrayList<DownloadCompletedListener> mListeners
			= new ArrayList<DownloadCompletedListener>();

		public void setTimeout(int seconds) {
			mTimeOut = seconds;
		}

		public int getTimeout() {
			return mTimeOut;
		}

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
		public void onDownloadComplete(String path, int resultCode);
	}

}
