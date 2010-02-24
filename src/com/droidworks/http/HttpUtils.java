package com.droidworks.http;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpUtils {

	public static final int TIMEOUT_CONNECT_LONG = 15000;
	public static final int TIMEOUT_CONNECT = 10000;

	public static final int TIMEOUT_READ_LONG = 20000;
	public static final int TIMEOUT_READ = 15000;

	// this method returns a threadsafe httpClient
	public static DefaultHttpClient getThreadSafeClient() {

		DefaultHttpClient client = new DefaultHttpClient();

        ClientConnectionManager mgr = client.getConnectionManager();

        if (!(mgr instanceof ThreadSafeClientConnManager)) {
            HttpParams params = client.getParams();
            client = new DefaultHttpClient(
            		new ThreadSafeClientConnManager(params,
                    mgr.getSchemeRegistry()), params);
        }

		return client;
	}

	/**
	 * Set a connection timeout to an HttpClient object.
	 *
	 * @param client
	 * @param seconds
	 */
	public static void setConnectionTimeout(HttpClient client, int seconds) {
    	// Set the timeout in milliseconds until a connection is established.
    	HttpConnectionParams.setConnectionTimeout(client.getParams(),
    			seconds * 1000);
    	// Set the default socket timeout (SO_TIMEOUT)
    	// in milliseconds which is the timeout for waiting for data.
    	HttpConnectionParams.setSoTimeout(client.getParams(),
    			seconds * 1000);
	}

}
