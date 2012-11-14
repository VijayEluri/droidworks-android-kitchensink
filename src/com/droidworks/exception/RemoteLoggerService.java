/**
 * Copyright 2010 Jason Hudgins, All Rights Reserved.
 */
package com.droidworks.exception;

import java.util.ArrayList;

import android.text.TextUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.droidworks.exception.RemoteLoggingExceptionHandler.Keys;
import com.droidworks.util.GuidManager;
import com.droidworks.util.StringUtils;

/**
 * @author jasonhudgins
 *
 */
public class RemoteLoggerService extends Service {

	public static final String EXTRA_MESSAGE = "message";
	public static final String EXTRA_STACK_TRACE = "stack_trace";
	public static final String EXTRA_REMOTE_HOST_URL = "remote_host_url";
	public static final String EXTRA_PACKAGE = "package";

	private String mMessage;
	private String mTrace;
	private String mUrl;
	private String mPackage;

	private final String mProduct = Build.PRODUCT;
	private final String mDevice = Build.DEVICE;
	private final String mSDK = Build.VERSION.SDK;
	private final String mCodename = Build.VERSION.CODENAME;
	private String mAppVersionName;
	private String mAppVersionCode;
	private String mGuid;

	public RemoteLoggerService() {
		super();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		mMessage = intent.getStringExtra(EXTRA_MESSAGE);
		mTrace = intent.getStringExtra(EXTRA_STACK_TRACE);
		mUrl = intent.getStringExtra(EXTRA_REMOTE_HOST_URL);
		mPackage = intent.getStringExtra(EXTRA_PACKAGE);

		// quick sanity check
		if (TextUtils.isEmpty(mTrace) || TextUtils.isEmpty(mUrl) || TextUtils.isEmpty(mPackage)) {
			return;
		}

		mGuid = new GuidManager(this).getGuid();

		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager()
				.getPackageInfo(mPackage, 0);
		}
		catch (NameNotFoundException e) {
				Log.e(getClass().getName(), "package not found: " + mPackage, e);
		}

		mAppVersionName = packageInfo.versionName;
		mAppVersionCode = Integer.toString(packageInfo.versionCode);

		new Thread(new ErrorReporter()).start();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class ErrorReporter implements Runnable {

		public void run() {

			HttpPost post = new HttpPost(mUrl);

			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair(Keys.GUID, mGuid));
			data.add(new BasicNameValuePair(Keys.APP_VERSION, mAppVersionName));
			data.add(new BasicNameValuePair(Keys.VERSION_CODE, mAppVersionCode));
			data.add(new BasicNameValuePair(Keys.MESSAGE,
					(StringUtils.hasChars(mMessage))
						? mMessage
					    : "An Exception Occured"));
			data.add(new BasicNameValuePair(Keys.TRACE, mTrace));
			data.add(new BasicNameValuePair(Keys.PRODUCT, mProduct));
			data.add(new BasicNameValuePair(Keys.DEVICE, mDevice));
			data.add(new BasicNameValuePair(Keys.BUILD_CODENAME, mCodename));
			data.add(new BasicNameValuePair(Keys.PLATFORM_SDK, mSDK));

			try {
				UrlEncodedFormEntity form = new UrlEncodedFormEntity(data, "utf-8");
				post.setEntity(form);
				HttpResponse response = new DefaultHttpClient().execute(post);
			}
			catch (Exception e) {
				Log.e(getClass().getName(),
					"Failure transmitting exception data", e);
			}
		}
	}


}
