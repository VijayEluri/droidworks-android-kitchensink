/*
 *  Copyright 2010 Jason L. Hudgins
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at :
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

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
