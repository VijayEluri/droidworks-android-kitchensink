/**
 * Copyright 2010 Jason L. Hudgins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droidworks.misc;



import java.util.HashMap;
import java.util.Map.Entry;


/**
 *
 * Straightforward LRU cache implementation.  Does not use soft/weak references,
 * for that implementation visit LruSoftCache.
 *
 * @author jasonhudgins
 *
 * @param <K>
 * @param <V>
 */
public class LruCache<K, V> {

	private int mMaxSize = 100;

    public void setMaxSize(int maxSize) {
        mMaxSize = maxSize;
    }

    protected final HashMap<K, CacheReference<K,V>> cache =
            new HashMap<K, CacheReference<K,V>>();


    public synchronized void put(K key, V value) {
        // if we have hit max size, then we need to
        // remove an entry first
        if ((cache.size() + 1) > mMaxSize) {
            K oldestEntry = findOldest();
            remove(oldestEntry);
        }

        CacheReference<K, V> ref = new CacheReference<K,V>(key, value);
        cache.put(key, ref);
    }

    public synchronized V get(K key) {
        CacheReference<K, V> ref = cache.get(key);
        if (ref != null) {
            ref.lastRead = System.currentTimeMillis();
            return ref.get();
        }

        return null;
    }

    public synchronized boolean contains(K key) {
        return cache.containsKey(key);
    }

    public synchronized void remove(K key) {
            cache.remove(key);
    }

    public synchronized int size() {
        return cache.size();
    }

    // cahce refernce object holds key, value, and a last read time.
    public static class CacheReference<K, V> {

        public final K _key;
        public final V _value;
        private long lastRead;

        public CacheReference(K key, V value) {
            _key = key;
            _value = value;
            lastRead = System.currentTimeMillis();
        }

        public V get() {
			return _value;
		}

		public long getLastRead() {
            return lastRead;
        }

        public void setLastRead(long lastRead) {
            this.lastRead = lastRead;
        }
    }

    private synchronized K findOldest() {
        long oldestAge = Long.MAX_VALUE;
        K oldestKey = null;

        for (Entry<K, CacheReference<K,V>> e : cache.entrySet()) {
            if (e.getValue().lastRead < oldestAge) {
                oldestAge = e.getValue().lastRead;
                oldestKey = e.getKey();

            }
        }
        return oldestKey;
    }

}
