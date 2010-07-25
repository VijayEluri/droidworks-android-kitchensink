package com.droidworks.misc;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map.Entry;

public class LruSoftCache<K, V> {
	private int maxSize = 100;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    protected final HashMap<K, CacheReference<K,V>> cache =
            new HashMap<K, CacheReference<K,V>>();

    private final ReferenceQueue<V> refQueue
            = new ReferenceQueue<V>();

    public synchronized void put(K key, V value) {
        // if we have hit max size, then we need to
        // remove an entry first
        if ((cache.size() + 1) > maxSize) {
            K oldestEntry = findOldest();
            remove(oldestEntry);
        }

        CacheReference<K, V> ref
                = new CacheReference<K,V>(key, value, refQueue);
        cache.put(key, ref);
        drain();
    }

    public synchronized V get(K key) {
        drain();

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

    public static class CacheReference<K, V> extends SoftReference<V> {

        public final K key;
        private long lastRead;

        public CacheReference(K key, V value, ReferenceQueue<V> rq) {
            super(value, rq);
            this.key = key;
            lastRead = System.currentTimeMillis();
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

    @SuppressWarnings("all")
    private synchronized void drain() {
        CacheReference ref = (CacheReference) refQueue.poll();

        while (ref != null) {
            cache.remove(ref.key);
            ref = (CacheReference) refQueue.poll();
        }
    }

}
