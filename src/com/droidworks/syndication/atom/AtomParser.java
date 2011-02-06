package com.droidworks.syndication.atom;

import java.io.InputStream;
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

import com.droidworks.syndication.rss.Feed;
import com.droidworks.syndication.rss.FeedItem;
import com.droidworks.syndication.rss.FeedItemFactory;
import com.droidworks.xml.FeedParser;
import com.droidworks.xml.Parser;

public class AtomParser extends FeedParser {

	RootElement mRootElement;

	private FeedItem mFeedItem;
	
	private Date mDatePublished;
	private String mTitle;
	private String mDescription;
	private String mYouTubeUrl;
	private long mDuration;
	private String mSmallThumbnailUrl;
	private String mLargeThumbnailUrl;
	private FeedItemFactory mFeedItemFactory;
	
	public AtomParser() {
		super("http://www.w3.org/2005/Atom");	
	}
	
	public AtomParser(FeedItemFactory feedItemFactory) {
		super("http://www.w3.org/2005/Atom");		
		mFeedItemFactory = feedItemFactory;
	}
	
	@Override
	protected ContentHandler getContentHandler() {
		return mRootElement.getContentHandler();
	}

	@Override
	protected void setupNodes() {
		mRootElement = new RootElement(getDefaultNamespace(), "feed");

		// atom uses RFC3339 format ex: 2011-02-06T08:26:07-08:00
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		Element entryNode = mRootElement.getChild(getDefaultNamespace(), "entry");
		
		Element entryAutherNode = entryNode.getChild(getDefaultNamespace(), "author");
		Element entryAuthorNameNode = entryAutherNode.getChild(getDefaultNamespace(), "name");
		
		Element entryPubDateNode = entryNode.getChild(getDefaultNamespace(), "updated");
		Element entryItemGuidNode = entryNode.getChild(getDefaultNamespace(), "id");
		Element entryItemLinkNode = entryNode.getChild(getDefaultNamespace(), "link");
		Element entryItemTitleNode = entryNode.getChild(getDefaultNamespace(), "title");
		Element entryItemContentNode = entryNode.getChild(getDefaultNamespace(), "content");
		Element entryItemCategoryNode = entryNode.getChild(getDefaultNamespace(), "category");
		
		entryItemCategoryNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				mFeedItem.setCategory(attrs.getValue("term"));
			}
		});
		
		entryItemContentNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setDescription(body);
			}
		});
		
		entryItemTitleNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setTitle(body);
			}
		});
		
		entryItemLinkNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				mFeedItem.setLink(attrs.getValue("href"));
			}
		});
		
		entryItemGuidNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setGuid(body);
			}
		});
		
		entryPubDateNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				try {
					mFeedItem.setPubDate(df.parse(body));
				}
				catch (ParseException e) {
					Log.e(getClass().getCanonicalName(), "Error parsing pubDate: " + body, e);
				}
			}
		});		
		
		Element pubDateNode = entryNode.getChild(getDefaultNamespace(), "published");
		Element titleNode = entryNode.getChild(getDefaultNamespace(), "title");

		entryAuthorNameNode.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String text) {
				mFeedItem.setAuthorNode(text);
			}
		});		
		
		// DEBUG only
		entryAutherNode.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attrs) {
				Log.d("DEBUGDEBUG", "read author node");
			}
		});
		
		// DEBUG only
		mRootElement.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attrs) {
				Log.d("DEBUGDEBUG", "read feed node");
			}
		});		
		

		
		entryNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attributes) {
				
				Log.d("DEBUGDEBUG", "read an entry node");
				
				if (mFeedItemFactory != null)
					mFeedItem = mFeedItemFactory.create();
				else
					mFeedItem = new FeedItem();
			}
		});
		
		entryNode.setEndElementListener(new EndElementListener() {
			public void end() {
				

				
				final FeedItem tmpItem = mFeedItem;
				mFeedItem = null;

				// only add feeditem if the pubdate isn't null,
				// null pubdates cause a crash, and we can't sort
				// without it..
				if (tmpItem.getPubDate() != null)
					getFeed().addItem(tmpItem);

				// notify a listener if present
				if (getListener() != null) {
					getListener().onItemParsed(tmpItem);
				}
			}
		});
	}

}
