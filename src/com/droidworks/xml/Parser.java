package com.droidworks.xml;

import java.io.InputStream;

import android.content.Context;
import android.widget.ListAdapter;


public abstract class Parser<T extends ListAdapter> {

	protected final T mAdapter;
	protected final Context mContext;
	protected final String mNamespace;

	public Parser(Context context, T adapter, String namespace) {
		mAdapter = adapter;
		mContext = context;
		mNamespace = namespace;

		System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
	}

	public abstract void parse(InputStream stream);
}
