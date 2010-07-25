package com.droidworks.misc;

import java.util.Map.Entry;

import android.graphics.Bitmap;

public class SimpleBitmapCache extends LruSoftCache<String, Bitmap> {

    @Override
    public void put(String key, Bitmap value) {
        // don't re-add something that's already been cached
        if (cache.containsKey(key))
            return;

        super.put(key, value);
    }

    // provides a rough estimate of the size of the images
    // in the cache.
    public int getRoughSize() {
        int size = 0;

        for (Entry<String, CacheReference<String, Bitmap>> e : cache.entrySet()) {
            Bitmap b = e.getValue().get();
            if (b != null) {
                // a rough estimate of a bitmaps size;
                size += (b.getWidth() * b.getHeight());
            }
        }

        return size;
    }
}
