package com.droidworks.http;

import android.test.InstrumentationTestCase;

import com.droidworks.http.download.AsyncDownloader;
import com.droidworks.http.download.WritableStorageDownloadTask;
import com.droidworks.http.download.DownloadTask.DownloadCompletedListener;

public class AsyncDownloaderTest extends InstrumentationTestCase {

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
		AsyncDownloader downloader = AsyncDownloader.getDownloader();
		downloader.setPollingDuration(1);
		// create a task that should never complete
		WritableStorageDownloadTask task = new WritableStorageDownloadTask("http://www.google.com:8234/test/1/");
		// set a long timeout value
		task.setTimeout(20);
		// create a task that should never complete
		WritableStorageDownloadTask task2 = new WritableStorageDownloadTask("http://www.google.com:8234/test/2");
		// set a long timeout value
		task2.setTimeout(20);
		downloader.addDownloadTask(task);
		// add it a again
		downloader.addDownloadTask(task2);
		// make sure our thread count is good
		assertEquals(2, downloader.getThreadCount());
		// wait 1 second, give these workers a chance to start working
		Thread.sleep(1000);
		// now cancel everything
		downloader.cancelAll();
		// give a couple seconds to shut everything down
		long started = System.currentTimeMillis();
		while (downloader.getThreadCount() != 0 && System.currentTimeMillis() - started < 5000) {
			// spin until we get what we want or we run out of time
		}
		// check thread count
		assertEquals(0, downloader.getThreadCount());
	}

    public void testNullUrlTask() throws Exception {
    	AsyncDownloader downloader = AsyncDownloader.getDownloader();
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
    	AsyncDownloader downloader = AsyncDownloader.getDownloader();
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

		assertTrue(mOutputFile.contains("/sdcard/tmp/ad_"));
		assertEquals(0, mResultCode);
    }
}
