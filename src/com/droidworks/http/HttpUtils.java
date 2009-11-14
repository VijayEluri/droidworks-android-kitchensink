package com.droidworks.http;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class HttpUtils {

	public static final int TIMEOUT_CONNECT_LONG = 15000;
	public static final int TIMEOUT_CONNECT = 10000;

	public static final int TIMEOUT_READ_LONG = 20000;
	public static final int TIMEOUT_READ = 15000;

	public static DefaultHttpClient client;

	// this method returns a threadsafe httpClient
	public synchronized static DefaultHttpClient getThreadSafeClient() {

        if (client != null)
        	return client;

        client = new DefaultHttpClient();

        ClientConnectionManager mgr = client.getConnectionManager();

        if (!(mgr instanceof ThreadSafeClientConnManager)) {
            HttpParams params = client.getParams();
            client = new DefaultHttpClient(
            		new ThreadSafeClientConnManager(params,
                    mgr.getSchemeRegistry()), params);
        }

		return client;
	}

}
