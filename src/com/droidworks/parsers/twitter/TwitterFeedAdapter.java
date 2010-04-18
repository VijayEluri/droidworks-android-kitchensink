package com.droidworks.parsers.twitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class TwitterFeedAdapter extends BaseAdapter {

	private final LayoutInflater mInflater;
	private final TwitterFeed mFeed;

	public TwitterFeedAdapter(Context context, TwitterFeed feed) {
		super();
		mInflater = (LayoutInflater)
			context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFeed = feed;
	}

	public void addTweet(Tweet tweet) {
		mFeed.addTweet(tweet);
	}

	@Override
	public int getCount() {
		return mFeed.getCount();
	}

	@Override
	public Object getItem(int position) {
		return mFeed.getTweet(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public LayoutInflater getLayoutInflater() {
		return mInflater;
	}

}
