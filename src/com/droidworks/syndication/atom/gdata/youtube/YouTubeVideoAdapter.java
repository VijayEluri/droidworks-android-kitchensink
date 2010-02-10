package com.droidworks.syndication.atom.gdata.youtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;


public abstract class YouTubeVideoAdapter extends BaseAdapter {

	private final YouTubeFeed mFeed;
	private final LayoutInflater mLayoutInflater;

	public YouTubeVideoAdapter(Context context, YouTubeFeed feed) {
		mLayoutInflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFeed = feed;
	}

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

	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

}
