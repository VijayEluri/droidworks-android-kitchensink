package com.droidworks.http;

import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.HttpGet;

import android.test.InstrumentationTestCase;

import com.droidworks.concurrent.ExecutorServiceFactory;

public class HttpGetWorkerTest extends InstrumentationTestCase {

	public void testTimeout() {
		HttpGet get = new HttpGet("http://www.google.com:52341");
		// create a worker with a 3 second timeout
		HttpGetWorker worker = new HttpGetWorker(get, null, 3);
		ExecutorService service = ExecutorServiceFactory.getService();

		try {
			service.submit(worker).get(5, TimeUnit.SECONDS);
			fail();
		}
		catch (Exception e) {
			if (!(e.getCause() instanceof SocketTimeoutException)) {
				fail();
			}
		}
	}
}
