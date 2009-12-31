package com.droidworks.syndication.atom.gdata.youtube;

import java.io.InputStream;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.view.ViewGroup;

public class YouTubeVideoFeedParserTest extends InstrumentationTestCase {

	public void testParser() throws Exception {

		AssetManager assets = getInstrumentation().getContext().getAssets();

		InputStream input = assets.open("youtubefeed.txt");

		YouTubeVideoFeedParser parser
			= new YouTubeVideoFeedParser(null, testAdapter, "");

		parser.parse(input);
		assertEquals(10, testAdapter.getCount());
	}


	private final YouTubeVideoAdapter testAdapter = new YouTubeVideoAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// dummy impl
			return null;
		}

	};
}
