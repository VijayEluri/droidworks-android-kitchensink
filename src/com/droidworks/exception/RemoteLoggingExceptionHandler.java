/**
 * Copyright 2010 Jason L. Hudgins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droidworks.exception;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Exception Handler that tries to log exceptions with a remote server.
 *
 * @author jasonhudgins
 *
 */
public class RemoteLoggingExceptionHandler
	implements Thread.UncaughtExceptionHandler {

	private final Thread.UncaughtExceptionHandler mDefaultHandler;
	private final String mLoggerUrl;
	private final Context mContext;
	private final String mPackageName;

	public static final class Keys {
		public static final String GUID = "guid";
		public static final String APP_VERSION = "app_version";
		public static final String VERSION_CODE = "version_code";
		public static final String MESSAGE = "message";
		public static final String TRACE = "dump";
		public static final String PRODUCT = "product";
		public static final String DEVICE = "device";
		public static final String BUILD_CODENAME = "build_codename";
		public static final String PLATFORM_SDK = "platform_sdk";
	};

	public RemoteLoggingExceptionHandler(Context context, String logUrl,
			String packageName, Thread.UncaughtExceptionHandler defaultHandler) {

			mLoggerUrl = logUrl;
			mDefaultHandler = defaultHandler;
			mContext = context;
			mPackageName = packageName;
	}

	public void uncaughtException(Thread thread, Throwable ex) {

		// marshall params, start service, run default handler
		Intent intent = new Intent(mContext, RemoteLoggerService.class);
		intent.putExtra(RemoteLoggerService.EXTRA_MESSAGE, ex.getMessage());
		intent.putExtra(RemoteLoggerService.EXTRA_REMOTE_HOST_URL, mLoggerUrl);
		intent.putExtra(RemoteLoggerService.EXTRA_STACK_TRACE,
				android.util.Log.getStackTraceString(ex));
		intent.putExtra(RemoteLoggerService.EXTRA_PACKAGE, mPackageName);
		mContext.startService(intent);

		mDefaultHandler.uncaughtException(thread, ex);
	}

}
