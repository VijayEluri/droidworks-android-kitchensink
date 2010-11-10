package com.droidworks.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds a URL based on a base URL and GET params.
 */
public class UrlBuilder {

	private String mBase;
	private Map<String, String> mMap = new HashMap<String, String>();

	public UrlBuilder(String base) {
		mBase = base;
	}

	/**
	 * Append a GET param to the base URL.
	 *
	 * @param key the param name
	 * @param val the param value
	 */
	public UrlBuilder appendParam(String key, String val) {
		if (val != null && val.length() > 0)
			mMap.put(key, val);

		return this;
	}

	/**
	 * Append a GET param to the base URL.
	 *
	 * @param key the param name
	 * @param val the param value
	 */
	public UrlBuilder appendParam(String key, int val) {
		return appendParam(key, Integer.toString(val));
	}

	/**
	 * Gets a standard URL using the base and all appended values.
	 *
	 * @return the fully build URL
	 */
	public String getUrl() {

		if (mMap.size() == 0)
			return mBase;

		StringBuilder url = new StringBuilder(mBase + "?");

		for (String key : mMap.keySet()) {
			try {
				url.append(key + "=" + URLEncoder.encode(mMap.get(key), "UTF-8") + "&");
			} catch (UnsupportedEncodingException e) {}
		}
		String out = url.toString();

		return url.toString().substring(0, out.length() - 1);
	}


	@Override
	public String toString() {
		return getUrl();
	}





}
