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

/**
 * @author Jason Hudgins <jasonleehudgins@gmail.com>
 */
public class HttpGetWorker implements Callable<HttpResponse> {

    private final HttpGet method;
    private final Set<Integer> acceptStatusCodes;
    private final HttpClient client;

    public HttpGetWorker(HttpGet method, Set<Integer> codes) {
        this.method = method;
        this.acceptStatusCodes = codes;
        this.client = new DefaultHttpClient();
    }

    public HttpGetWorker(HttpGet method, Set<Integer> codes, 
    		HttpClient client) {
    	
        this.method = method;
        this.acceptStatusCodes = codes;
        this.client = client;
    }

    public HttpResponse call() throws HttpException, ClientProtocolException {
        
        HttpResponse response = null;
			try {
				response = client.execute(this.method);
			} 
			catch (IOException e) {
				throw new RuntimeException("Server communication failure", e);
			}

        int code = response.getStatusLine().getStatusCode();

        if (acceptStatusCodes == null) {
            if (code != HttpStatus.SC_OK) {
                throw new HttpException(Integer.toString(code)); 
            }
        } else {
            if (!acceptStatusCodes.contains(code)) {
                throw new HttpException(Integer.toString(code)); 
            }
        }

        return response;
    }
}
