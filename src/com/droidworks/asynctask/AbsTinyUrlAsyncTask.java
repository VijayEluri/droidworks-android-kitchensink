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

package com.droidworks.asynctask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.droidworks.http.HttpUtils;

/**
 * AsyncTask implementation that performs url transformations using the
 * tinyurl.com api.
 *
 * @author jasonhudgins <jasonleehudgins@gmail.com>
 *
 */
public abstract class AbsTinyUrlAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TINY_URL_API
    	= "http://tinyurl.com/api-create.php?url=";


	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... url) {
        String result = null;

        try {
			HttpGet getMethod = new HttpGet(TINY_URL_API + url[0]);
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpUtils.setConnectionTimeout(httpClient, 10);
	        HttpResponse response = httpClient.execute(getMethod);
	        result = EntityUtils.toString(response.getEntity());
        }
        catch (Exception e) {
        	Log.e(getClass().getName(), "Error transforming url", e);
        }

		return result;
	}

}
