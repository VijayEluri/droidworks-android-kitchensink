package com.droidworks.parsers.twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.xml.sax.ContentHandler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;

import com.droidworks.xml.Parser;

public class TwitterFeedParser extends Parser<Tweet> {

	private static final String DEFAULT_NAMESPACE = "";

	private String mTweetContent;
	private long mTweetTime;
	private final TwitterFeed mFeed = new TwitterFeed();
	private RootElement mRootElement;

	public TwitterFeedParser() {
		super(DEFAULT_NAMESPACE);
	}

	@Override
	protected ContentHandler getContentHandler() {
		return mRootElement.getContentHandler();
	}

	@Override
	protected void setupNodes() {
        final SimpleDateFormat sdf = new SimpleDateFormat(
        		"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		mRootElement = new RootElement(DEFAULT_NAMESPACE, "statuses");
		Element tweetNode = mRootElement.getChild(DEFAULT_NAMESPACE, "status");
		Element textNode = tweetNode.getChild(DEFAULT_NAMESPACE, "text");
		Element timeNode = tweetNode.getChild(DEFAULT_NAMESPACE, "created_at");

		textNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mTweetContent = body;
			}
		});

		timeNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				try {
					mTweetTime = sdf.parse(body).getTime();
				}
				catch (ParseException e) {
					mTweetTime = 0;
				}
			}
		});

		tweetNode.setEndElementListener(new EndElementListener() {
			public void end() {
				mFeed.addTweet(new Tweet(mTweetTime, mTweetContent));
			}
		});
	}

	public TwitterFeed getFeed() {
		return mFeed;
	}
}
