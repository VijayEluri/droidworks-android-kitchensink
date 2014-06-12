package com.droidworks.xml;

import com.droidworks.syndication.Feed;
import com.droidworks.syndication.FeedItem;

/**
 * Parser designed for parsing syndication feeds. 
 * 
 * @author jasonhudgins
 *
 */
public abstract class FeedParser extends Parser<FeedItem> {

	private final Feed mFeed = new Feed();

    private String mLogTag = "FeedParser";

	public FeedParser(String defaultNamespace) {
		super(defaultNamespace);
	}

	public Feed getFeed() {
		return mFeed;
	}

    public String getLogTag() {
        return (mLogTag != null) ? mLogTag : "FeedParser";
    }

    public void setLogTag(String logTag) {
        mLogTag = logTag;
    }
}
