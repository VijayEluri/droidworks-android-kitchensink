package com.droidworks.syndication.atom.gdata.youtube;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.view.ViewGroup;

public class YouTubeVideoFeedParserTest extends InstrumentationTestCase {

	public void testParser() throws Exception {

		AssetManager assets = getInstrumentation().getContext().getAssets();

		InputStream input = assets.open("youtubefeed.txt");

		YouTubeVideoFeedParser parser
			= new YouTubeVideoFeedParser(null, testAdapter, "http://www.w3.org/2005/Atom");

		// 2009-12-04T22:51:39.000-0000
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date testDate = df.parse("2009-12-04T22:51:39.000-0000");

		parser.parse(input);
		assertEquals("survivalpodcasting", testAdapter.getAuthor());

		YouTubeItem item = (YouTubeItem) testAdapter.getItem(0);

		assertEquals(testDate, item.datePublished);
		assertEquals("A Country Boy Can Survive on Doves, Bacon and Jalapenos",
				item.title);
		assertEquals("e shot a h", item.description.substring(10, 20));
		assertEquals("http://www.youtube.com/watch?v=HcyY4iAX-AI&feature=youtube_gdata",
			item.youtubeUrl);
		assertEquals(158, item.duration);
		assertEquals("http://i.ytimg.com/vi/HcyY4iAX-AI/0.jpg", item.thumbnailUrl);
		assertEquals("HcyY4iAX-AI", item.youtubeId);
	}


	private final YouTubeVideoAdapter testAdapter = new YouTubeVideoAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// dummy impl
			return null;
		}

	};
}
