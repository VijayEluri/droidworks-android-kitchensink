package com.droidworks.xml;

import org.xml.sax.ContentHandler;

import com.droidworks.syndication.rss.Feed;
import com.droidworks.syndication.rss.FeedItem;

public abstract class FeedParser extends Parser<FeedItem> {

	private final Feed mFeed = new Feed();
	
	public FeedParser(String defaultNamespace) {
		super(defaultNamespace);
	}

	public Feed getFeed() {
		return mFeed;
	}

}
