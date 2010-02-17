package com.droidworks.syndication.atom.gdata.youtube;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

public class YouTubeVideoFeedParserTest extends InstrumentationTestCase {

	public void testParser() throws Exception {

		AssetManager assets = getInstrumentation().getContext().getAssets();

		InputStream input = assets.open("youtubefeed.txt");

		YouTubeVideoFeedParser parser = new YouTubeVideoFeedParser();

		// 2009-12-04T22:51:39.000-0000
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date testDate = df.parse("2009-12-04T22:51:39.000-0000");

		parser.parse(input);

		assertEquals("survivalpodcasting", parser.getFeed().getFeedAuthor());
		YouTubeItem item = parser.getFeed().getItem(0);

		assertEquals(testDate, item.getDatePublished());
		assertEquals("A Country Boy Can Survive on Doves, Bacon and Jalapenos",
				item.getTitle());
		assertEquals("e shot a h", item.getDescription().substring(10, 20));
		assertEquals("http://www.youtube.com/watch?v=HcyY4iAX-AI&feature=youtube_gdata",
			item.getYouTubeUrl());
		assertEquals(158, item.getDuration());
		assertEquals("http://i.ytimg.com/vi/HcyY4iAX-AI/0.jpg", item.getLargeThumbnailUrl());
		assertEquals("HcyY4iAX-AI", item.getYouTubeId());
	}


}
