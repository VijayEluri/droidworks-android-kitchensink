package com.droidworks.xml;

import java.io.InputStream;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.os.Handler;
import android.util.Log;
import android.widget.ListAdapter;


public abstract class Parser<T extends ListAdapter> {

	protected final T mAdapter;
	// need a uiHandler
	protected final Handler mUiHandler;
	protected final String mNamespace;

	public Parser(Handler uiHandler, T adapter, String namespace) {
		mAdapter = adapter;
		mUiHandler = uiHandler;
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
