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

        // short circuit out of here if the item is already in the list.
        for (YouTubeItem existingItem : mItems) {
            if (item.getYouTubeId().equals(existingItem.getYouTubeId()))
                return;
        }

        mItems.add(item);
	}

}
