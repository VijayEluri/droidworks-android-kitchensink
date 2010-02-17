package com.droidworks.misc;

import android.graphics.drawable.Drawable;

public class SimpleDrawableCache extends LruCache<String, Drawable> {

    @Override
    public void put(String key, Drawable value) {
        // don't re-add something that's already been cached
        if (cache.containsKey(key))
            return;

        super.put(key, value);
    }

	public synchronized boolean containsKey(String key) {
		return super.contains(key);
	}


}