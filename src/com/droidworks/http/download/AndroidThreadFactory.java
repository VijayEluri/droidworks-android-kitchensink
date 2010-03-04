package com.droidworks.http.download;

import java.util.concurrent.ThreadFactory;

/**
 * Android compatible thread factory.
 *
 * @author jasonhudgins <jasonleehudgins@gmail.com>
 *
 */
public class AndroidThreadFactory implements ThreadFactory {

	private final int mPriority;

	public AndroidThreadFactory(int threadPriority) {
		mPriority = threadPriority;
	}

	@Override
	public Thread newThread(Runnable r) {
		return new AndroidThread(r, mPriority);
	}

	private static class AndroidThread extends Thread {

		private final int mThreadPriority;

		public AndroidThread(Runnable r, int threadPriority) {
			super(r);
			mThreadPriority = threadPriority;
		}

		@Override
		public void run() {
			android.os.Process.setThreadPriority(mThreadPriority);
			super.run();
		}
	}

}
