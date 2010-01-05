package com.droidworks.http;

import android.test.InstrumentationTestCase;

import com.droidworks.http.AsyncDownloader.DownloadCompletedListener;
import com.droidworks.http.AsyncDownloader.DownloadTask;

public class AsyncDownloaderTest extends InstrumentationTestCase {

	private boolean mJobCompleted = false;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mJobCompleted = false;
	}



	public void testDownloader() throws Exception {
		AsyncDownloader downloader = AsyncDownloader.getDownloader();

		// create a null task
		DownloadTask task = new DownloadTask(null);

		task.addListener(new DownloadCompletedListener() {
			public void onDownloadComplete(String path) {
				mJobCompleted = true;
			}
		});

		downloader.addDownloadTask(task);

		// wait a miniscule amount of time
		Thread.sleep(100);

		// test result
		assertTrue(mJobCompleted);

		assertEquals(1, downloader.getThreadCount());
		// sleep a little more
		Thread.sleep(7000);
		assertEquals(0, downloader.getThreadCount());
	}

}
