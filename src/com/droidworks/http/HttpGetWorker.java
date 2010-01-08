package com.droidworks.http;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

// TODO, added connnection timeout support, needs to be tested

/**
 * @author Jason Hudgins <jasonleehudgins@gmail.com>
 */
public class HttpGetWorker implements Callable<HttpResponse> {

    private final HttpGet mMethod;
    private final Set<Integer> mAccecptedHttpStatusCodes;
    private final HttpClient mClient;

    public HttpGetWorker(HttpGet method, Set<Integer> codes) {
        mMethod = method;
        mAccecptedHttpStatusCodes = codes;
        mClient = new DefaultHttpClient();
    }

    public HttpGetWorker(HttpGet method, Set<Integer> codes,
    		HttpClient client) {

        mMethod = method;
        mAccecptedHttpStatusCodes = codes;
        mClient = client;
    }

    public HttpGetWorker(HttpGet method, Set<Integer> codes,
    		int connectionTimeoutSeconds) {
        mMethod = method;
        mAccecptedHttpStatusCodes = codes;
        mClient = new DefaultHttpClient();
		HttpUtils.setConnectionTimeout(mClient, connectionTimeoutSeconds);
    }


    public HttpResponse call() throws HttpException, ClientProtocolException, IOException {

        HttpResponse response = null;
        response = mClient.execute(this.mMethod);

        int code = response.getStatusLine().getStatusCode();

        if (mAccecptedHttpStatusCodes == null) {
            if (code != HttpStatus.SC_OK) {
                throw new HttpException(Integer.toString(code));
            }
        } else {
            if (!mAccecptedHttpStatusCodes.contains(code)) {
                throw new HttpException(Integer.toString(code));
            }
        }

        return response;
    }

}
