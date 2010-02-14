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
import org.apache.http.client.methods.HttpGet;

import android.util.Log;

import com.droidworks.http.HttpGetWorker;

// TODO, this needs lots of changes, but first and foremost it should not
// be a singleton.
public class AsyncDownloader {

	public static final AsyncDownloader mDownloader = new AsyncDownloader();

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

		public DownloadLooper(String name) {
			super(name);
		}

		public void cancel() {
			_shutdown = true;

			if (_worker != null) {
				_worker.cancel();
			}
		}

//		private void writeFile(InputStream is) {
//			if (!AndroidUtils.hasStorage(true)) {
//				_resultCode = STATUS_NO_STORAGE;
//				return;
//			}
//
//	        String directoryName = Environment
//        	.	getExternalStorageDirectory().toString() + "/tmp";
//
//	        File directory = new File(directoryName);
//
//	        if (!directory.isDirectory()) {
//	            if (!directory.mkdirs()) {
//	            	Log.e(LOG_LABEL, "can't create directory: " + directoryName);
//					_resultCode = STATUS_FILE_WRITE_ERROR;
//					return;
//	            }
//	        }
//	        try {
//				File tmpFile = File.createTempFile("ad_", null, directory);
//				FileOutputStream os = new FileOutputStream(tmpFile);
//				byte[] data = new byte[512];
//				int bytesRead = 0;
//				while ((bytesRead = is.read(data)) != -1) {
//					os.write(data, 0, bytesRead);
//
//					if (_shutdown) {
//						_resultCode = STATUS_CANCELLED;
//						return;
//					}
//				}
//
//				_resultCode = STATUS_OK;
//				is.close();
//			}
//	        catch (IOException e) {
//				Log.e(LOG_LABEL, "Exception creating temp file", e);
//				_resultCode = STATUS_FILE_WRITE_ERROR;
//				return;
//	        }
//		}

		@Override
		public void run() {
			while (!_shutdown) {

				DownloadTask<?> streamHandler;

				// grab a task
				try {
					synchronized (mDownloader) {
						_resultCode = -1;
						streamHandler = mTasks.poll(mPollDuration, TimeUnit.SECONDS);

						// shutdown on a null task
						if (streamHandler == null || _shutdown) {
							_shutdown = true;
							return;
						}

						// if we have a valid task, init a worker
						if (streamHandler.getUrl() != null) {
							HttpGet get = new HttpGet(streamHandler.getUrl());
							_worker = new HttpGetWorker(get, null, streamHandler.getTimeout());
						}
					}

					// if we have a worker, then we can do the download.
					if (_worker != null) {
						try {
							HttpResponse response = mExecutor.submit(_worker)
								.get(mDownloadCompleteTimeout, TimeUnit.SECONDS);

							_resultCode = streamHandler.processStream(
									response.getEntity().getContent());
						}
						catch (ExecutionException e) {
							if (e.getCause() instanceof SocketTimeoutException) {
								_resultCode = DownloadTask.STATUS_TIMED_OUT;
							}
							// cancelled tasks are receiving an EE
							Log.e(LOG_LABEL, "Caught execution exception on task: " + streamHandler.getUrl(), e );
						} catch (TimeoutException e) {
							_resultCode = DownloadTask.STATUS_TIMED_OUT;
							Log.e(LOG_LABEL, "Caught timeout exception task: " + streamHandler.getUrl(), e );
						} catch (IllegalStateException e) {
							_resultCode = DownloadTask.STATUS_GENERAL_ERROR;
							Log.e(LOG_LABEL, "Caught exception: " + streamHandler.getUrl(), e );
						} catch (IOException e) {
							_resultCode = DownloadTask.STATUS_GENERAL_ERROR;
							Log.e(LOG_LABEL, "Caught exception: " + streamHandler.getUrl(), e );
						}
					}

					// TODO, this probaby won't work either, need to modify
					// for flexability.
					// notify listeners
					streamHandler.notifyListeners(_resultCode);
				}
				catch (InterruptedException ignored) {
					_shutdown = true;
				}
			}
		}
	}

}
