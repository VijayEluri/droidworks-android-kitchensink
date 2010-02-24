package com.droidworks.http;

import android.test.InstrumentationTestCase;

import com.droidworks.http.download.ConcurrentDownloadManager;
import com.droidworks.http.download.DownloadManager;
import com.droidworks.http.download.DownloadTask;
import com.droidworks.http.download.WritableStorageDownloadTask;
import com.droidworks.http.download.DownloadTask.DownloadCompletedListener;

public class ConcurrentDownloadManagerTest extends InstrumentationTestCase {

	private static final String TEST_DL_URL
		= "http://www.droidworks.com/images/trivial-gps.jpg";
	private boolean mJobCompleted = false;
	private int mResultCode;
	private String mOutputFile;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mJobCompleted = false;
	}

	public void testCancelAllTasks() throws Exception {
		DownloadManager downloader = new ConcurrentDownloadManager();
		downloader.setTaskTimeout(20);

		// create a task that should never complete
		WritableStorageDownloadTask task = new WritableStorageDownloadTask("http://www.google.com:8234/test/1/");
		// create a task that should never complete
		WritableStorageDownloadTask task2 = new WritableStorageDownloadTask("http://www.google.com:8234/test/2");
		downloader.addDownloadTask(task);
		// add it a again
		downloader.addDownloadTask(task2);
		// now cancel everything
		downloader.cancelAll();

		// sleep a bit
		Thread.sleep(1000);
		// check thread count
		assertTrue(task.getStatus() == DownloadTask.STATUS_CANCELLED
				|| task.getStatus() == DownloadTask.STATUS_UNPROCESSED);
		assertTrue(task2.getStatus() == DownloadTask.STATUS_CANCELLED
				|| task2.getStatus() == DownloadTask.STATUS_UNPROCESSED);
	}

    public void testNullUrlTask() throws Exception {
    	DownloadManager downloader = new ConcurrentDownloadManager();
		downloader.setPollingDuration(1);
		WritableStorageDownloadTask task = new WritableStorageDownloadTask(null);

		task.addListener(new DownloadCompletedListener<String>() {
			public void onDownloadComplete(String path, int resultCode) {
				mJobCompleted = true;
			}
		});

		downloader.addDownloadTask(task);

		long started = System.currentTimeMillis();
		while (!mJobCompleted && System.currentTimeMillis() - started < 5000) {
			// spin until we get what we want or we run out of time
		}

		if (!mJobCompleted)
			fail("Job wasn't completed?");
    }

    public void testActualDownload() {
    	DownloadManager downloader = new ConcurrentDownloadManager();
		downloader.setPollingDuration(1);
    	WritableStorageDownloadTask task = new WritableStorageDownloadTask(TEST_DL_URL);

		task.addListener(new DownloadCompletedListener<String>() {
			public void onDownloadComplete(String path, int resultCode) {
				mJobCompleted = true;
				mOutputFile = path;
				mResultCode = resultCode;
			}
		});

		downloader.addDownloadTask(task);

		long started = System.currentTimeMillis();
		while (!mJobCompleted && System.currentTimeMillis() - started < 30000) {
			// spin until we get what we want or we run out of time
		}

		if (!mJobCompleted)
			fail("Job wasn't completed?");

		assertEquals(0, mResultCode);
		assertNotNull(mOutputFile);
		assertTrue(mOutputFile.contains("/sdcard/tmp/ad_"));
    }
}
