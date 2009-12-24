package com.droidworks.xml;

import java.io.InputStream;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.app.Activity;
import android.util.Log;
import android.widget.ListAdapter;


public abstract class Parser<T extends ListAdapter> {

	protected final T mAdapter;
	// we need runOnUiThread, i'm not sure what's better, doing this or
	// passing in a handler,
	protected final Activity mActivity;
	protected final String mNamespace;

	public Parser(Activity activity, T adapter, String namespace) {
		mAdapter = adapter;
		mActivity = activity;
		mNamespace = namespace;

		System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");

		setupNodes();
	}

	protected abstract void setupNodes();

	protected abstract ContentHandler getContentHandler();

	public void parse(InputStream stream) {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(getContentHandler());
			reader.parse(new InputSource(stream));
			stream.close();
		}
		catch (Exception e) {
			Log.e(getClass().getCanonicalName(),
					"Failure parsing document", e);
		}
	}
}
