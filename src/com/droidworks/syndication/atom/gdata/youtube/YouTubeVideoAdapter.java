package com.droidworks.syndication.atom.gdata.youtube;

import android.widget.BaseAdapter;


public abstract class YouTubeVideoAdapter extends BaseAdapter {

	private YouTubeFeed mFeed;

	@Override
	public int getCount() {
		return mFeed.getItems().size();
	}

	@Override
	public Object getItem(int position) {
		return mFeed.getItems().get(position);
	}

	@Override
	public long getItemId(int position) {
		return mFeed.getItems().get(position).getYouTubeId().hashCode();
	}

	public YouTubeFeed getFeed() {
		return mFeed;
	}

	public void setFeed(YouTubeFeed feed) {
		mFeed = feed;
	}

}
