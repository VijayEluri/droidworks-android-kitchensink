package com.droidworks.syndication.atom.gdata.youtube;

import java.util.ArrayList;

public class YouTubeFeed {

	private String mFeedAuthor;
	private ArrayList<YouTubeItem> mItems = new ArrayList<YouTubeItem>();
	private String mNextLink;

	public String getNextLink() {
		return mNextLink;
	}

	public void setNextLink(String nextLink) {
		mNextLink = nextLink;
	}

	public String getFeedAuthor() {
		return mFeedAuthor;
	}

	public void setFeedAuthor(String feedAuthor) {
		mFeedAuthor = feedAuthor;
	}

	public ArrayList<YouTubeItem> getItems() {
		return mItems;
	}

	public void setitems(ArrayList<YouTubeItem> items) {
		mItems = items;
	}

	public YouTubeItem getItem(int i) {
		return mItems.get(i);
	}

	public void addItem(YouTubeItem item) {
		mItems.add(item);
	}

}
