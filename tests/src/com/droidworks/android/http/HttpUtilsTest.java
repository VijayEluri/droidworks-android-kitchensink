package com.droidworks.android.http;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.test.InstrumentationTestCase;

public class HttpUtilsTest extends InstrumentationTestCase {

	public void testSetTimeout() {
        DefaultHttpClient client = new DefaultHttpClient();
		HttpUtils.setConnectionTimeout(client, 3);

		assertEquals(3000,
				HttpConnectionParams.getConnectionTimeout(client.getParams()));
		assertEquals(3000,
				HttpConnectionParams.getSoTimeout(client.getParams()));
	}

}
