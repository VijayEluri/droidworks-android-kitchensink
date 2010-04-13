package com.droidworks.http;

import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.HttpGet;

import android.test.InstrumentationTestCase;
import android.util.Log;

public class HttpGetWorkerTest extends InstrumentationTestCase {

	public void testTimeout() {
		// using a url that should timeout
		HttpGet get = new HttpGet("http://www.google.com:52341");

		// create a worker with a brief timeout
		HttpGetWorker worker = new HttpGetWorker(get, null, 2);
		ExecutorService service = new ScheduledThreadPoolExecutor(1);

		try {
			service.submit(worker).get(30, TimeUnit.SECONDS);
			fail();
		}
		catch (Exception e) {
			assertTrue(e.getCause() instanceof SocketTimeoutException);
		}
	}
}
