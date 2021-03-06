package com.droidworks.syndication.rss;

import java.io.InputStream;

import com.droidworks.syndication.FeedItem;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

public class Rss2ParserTest extends InstrumentationTestCase {

	AssetManager assetManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		assetManager = getInstrumentation().getContext().getAssets();
	}

	public void testParser() throws Exception {
		InputStream stream = assetManager.open("testfeed.txt");
		assertNotNull(stream);

		Rss2Parser parser = new Rss2Parser();
		parser.parse(stream);
		assertEquals(60, parser.getFeed().getItems().size());
		FeedItem item = parser.getFeed().getItem(0);
		assertTrue(item.getItunesDuration() > 0);
	}


    public void testParser2() throws Exception {

        InputStream stream = assetManager.open("debugme.rss");
        assertNotNull(stream);

        Rss2Parser parser = new Rss2Parser();
        parser.parse(stream);
        assertEquals(30, parser.getFeed().getItems().size());
        FeedItem item = parser.getFeed().getItem(0);
        assertEquals("Re: BOMBSHELL: BIG PHARMA WORLD BANK DOCTORS W/O BORDERS PROOF", item.getTitle());
    }


}
