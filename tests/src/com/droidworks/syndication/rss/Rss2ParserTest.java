package com.droidworks.syndication.rss;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.view.ViewGroup;

public class Rss2ParserTest extends InstrumentationTestCase {

	AssetManager assetManager;
	FeedAdapter adapter;

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		assetManager.close();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		assetManager = getInstrumentation().getContext().getAssets();
		adapter = new TestFeedAdapter(getInstrumentation().getTargetContext());
	}

	public void testParser() throws Exception {
		InputStream feed = assetManager.open("testfeed.txt");
		assertNotNull(feed);

		Rss2Parser parser = new Rss2Parser(null, adapter);
		parser.parse(feed);
		assertEquals(60, adapter.getCount());
		FeedItem item = (FeedItem) adapter.getItem(0);
		assertTrue(item.getItunesDuration() > 0);
	}

	private class TestFeedAdapter extends FeedAdapter {

		public TestFeedAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// stub
			return null;
		}

	}

}
