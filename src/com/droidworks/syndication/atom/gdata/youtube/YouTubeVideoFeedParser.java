package com.droidworks.syndication.atom.gdata.youtube;

import org.xml.sax.ContentHandler;

import android.os.Handler;

import com.droidworks.xml.Parser;

public class YouTubeVideoFeedParser extends Parser<YouTubeVideoAdapter> {

	public YouTubeVideoFeedParser(Handler uiHandler, YouTubeVideoAdapter adapter,
			String namespace) {

		super(uiHandler, adapter, namespace);
	}

	@Override
	protected ContentHandler getContentHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setupNodes() {
		// TODO Auto-generated method stub
	}

}
