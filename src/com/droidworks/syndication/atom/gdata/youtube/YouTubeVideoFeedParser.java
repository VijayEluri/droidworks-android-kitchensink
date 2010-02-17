package com.droidworks.syndication.atom.gdata.youtube;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;

import com.droidworks.xml.Parser;

public class YouTubeVideoFeedParser extends Parser<YouTubeItem> {

	RootElement mRootElement;

	public static final String NS_MEDIA = "http://search.yahoo.com/mrss/";
	public static final String NS_YT = "http://gdata.youtube.com/schemas/2007";

	private Date mDatePublished;
	private String mTitle;
	private String mDescription;
	private String mYouTubeUrl;
	private long mDuration;
	private String mSmallThumbnailUrl;
	private String mLargeThumbnailUrl;

	private final YouTubeFeed mFeed = new YouTubeFeed();

	public YouTubeVideoFeedParser() {
		super("http://www.w3.org/2005/Atom");
	}

	@Override
	protected ContentHandler getContentHandler() {
		return mRootElement.getContentHandler();
	}

	@Override
	protected void setupNodes() {
		mRootElement = new RootElement(getDefaultNamespace(), "feed");

		// 2009-12-04T22:51:39.000-0000
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		Element authorNode = mRootElement.requireChild(getDefaultNamespace(), "author");
		Element authorNameNode = authorNode.requireChild(getDefaultNamespace(), "name");

		Element entryNode = mRootElement.getChild(getDefaultNamespace(), "entry");
		Element pubDateNode = entryNode.getChild(getDefaultNamespace(), "published");
		Element titleNode = entryNode.getChild(getDefaultNamespace(), "title");

		Element mediaGroupNode = entryNode.requireChild(NS_MEDIA, "group");
		Element descriptionNode = mediaGroupNode.requireChild(NS_MEDIA, "description");
		Element youTubeUrlNode = mediaGroupNode.requireChild(NS_MEDIA, "player");
		Element durationNode = mediaGroupNode.requireChild(NS_YT, "duration");
		Element thumbnailNode = mediaGroupNode.requireChild(NS_MEDIA, "thumbnail");

		thumbnailNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				if (attrs.getValue("url").contains("0.jpg"))
					mLargeThumbnailUrl = attrs.getValue("url");
				else if (attrs.getValue("url").contains("1.jpg"))
					mSmallThumbnailUrl = attrs.getValue("url");
			}
		});

		durationNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				mDuration = Long.parseLong(attrs.getValue("seconds"));
			}
		});

		youTubeUrlNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				mYouTubeUrl = attrs.getValue("url");
			}
		});

		descriptionNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mDescription = body;
			}
		});

		titleNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mTitle = body;
			}
		});

		authorNameNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setFeedAuthor(body);
			}
		});

		pubDateNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				 try {
					 String time = body.replace("Z", "-0000");
					mDatePublished = df.parse(time);
				}
				catch (ParseException e) {
					Log.e(getClass().getCanonicalName(), "time parse error", e);
				}
			}
		});

		entryNode.setEndElementListener(new EndElementListener() {
			public void end() {
				final YouTubeItem item = new YouTubeItem(mDatePublished,
						mTitle, mDescription, mYouTubeUrl,
						mDuration, mSmallThumbnailUrl, mLargeThumbnailUrl,
						parseId(mYouTubeUrl));

				// always add item to the feed
				mFeed.addItem(item);

				// notify a listener if there is one.
				if (getListener() != null) {
					getListener().onItemParsed(item);
				}
			}
		});

	}

	private String parseId(String url) {
		Pattern p = Pattern.compile("v=(.+)&");
		Matcher m = p.matcher(url);
		if (m.find())
			return m.group(1);

		return null;
	}

	public YouTubeFeed getFeed() {
		return mFeed;
	}

}
