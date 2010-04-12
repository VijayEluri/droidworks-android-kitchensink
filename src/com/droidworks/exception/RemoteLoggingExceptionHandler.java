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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

/**
 * Exception Handler that tries to log exceptions with a remote server.
 *
 * @author jasonhudgins
 *
 */
public class RemoteLoggingExceptionHandler
	implements Thread.UncaughtExceptionHandler {

	private final String mProduct = Build.PRODUCT;
	private final String mDevice = Build.DEVICE;
	private final String mSDK = Build.VERSION.SDK;
	private final String mCodename = Build.VERSION.CODENAME;
	private final String mAppVersionName;
	private final String mAppVersionCode;
	private final String mGuid;
	private final Thread.UncaughtExceptionHandler mDefaultHandler;
	private final String mLoggerUrl;

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

	public RemoteLoggingExceptionHandler(PackageInfo packageInfo,
			String logUrl, String guid,
			Thread.UncaughtExceptionHandler defaultHandler) {

			mLoggerUrl = logUrl;
			mGuid = guid;
			mAppVersionName = packageInfo.versionName;
			mAppVersionCode = Integer.toString(packageInfo.versionCode);
			mDefaultHandler = defaultHandler;
	}

	public void uncaughtException(Thread thread, Throwable ex) {

		Log.d("DEBUGDEBUG", "Exception caught, attempting to log");

		 ErrorReporter reporter = new ErrorReporter(ex);
		 new Thread(reporter).start();
		 mDefaultHandler.uncaughtException(thread, ex);
	}

	private class ErrorReporter implements Runnable {

		private final Throwable ex;

		public ErrorReporter(Throwable ex) {
			this.ex = ex;
		}

		public void run() {

			Log.d("DEBUGDEBUG", "reporter is running");

			HttpPost post = new HttpPost(mLoggerUrl);

			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair(Keys.GUID, mGuid));
			data.add(new BasicNameValuePair(Keys.APP_VERSION, mAppVersionName));
			data.add(new BasicNameValuePair(Keys.VERSION_CODE, mAppVersionCode));
			data.add(new BasicNameValuePair(Keys.MESSAGE,
					(ex.getMessage() == null)
						? "An Exception Occured"
					    : ex.getMessage()));
			data.add(new BasicNameValuePair(Keys.TRACE, getStackTrace()));
			data.add(new BasicNameValuePair(Keys.PRODUCT, mProduct));
			data.add(new BasicNameValuePair(Keys.DEVICE, mDevice));
			data.add(new BasicNameValuePair(Keys.BUILD_CODENAME, mCodename));
			data.add(new BasicNameValuePair(Keys.PLATFORM_SDK, mSDK));

			try {
				UrlEncodedFormEntity form = new UrlEncodedFormEntity(data, "utf-8");
				post.setEntity(form);
				Log.d("DEBUGDEBUG", "executing http request");
				HttpResponse response = new DefaultHttpClient().execute(post);
				Log.d("DEBUGDEBUG", "response returned: " + response.getStatusLine());
			}
			catch (Exception e) {
				Log.e(getClass().getName(),
					"Failure transmitting exception data", e);
			}
		}

		private String getStackTrace() {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			ex.printStackTrace(ps);
			return os.toString();
		}
	}

}
