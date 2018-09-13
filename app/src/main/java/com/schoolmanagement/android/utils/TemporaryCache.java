package com.schoolmanagement.android.utils;

import android.util.LruCache;

/**
 * Acts as an ephemeral data structure.
 * Wrapper implementation of {@link LruCache} with initial size of 20
 * It is a reusable component that can be used to pass the data in/out classes just by using the memory.
 * Can be used in where the objects need to be made parcelable and then passed across into the intent.
 * <p>
 */
public class TemporaryCache {

    private static TemporaryCache sCacheInstance;
    private LruCache<String, Object> cache = new LruCache<>(20);

    public static final TemporaryCache getInstance() {
        if (sCacheInstance == null) {
            sCacheInstance = new TemporaryCache();
        }

        return sCacheInstance;
    }

    /**
     * Store temporary data into the cached for the parameter key.
     *
     * @param key   for against which the data is stored.
     * @param stuff The data to be stored. Marshalling and unmarshelling the data into the correct
     *              type is the responsibility of the caller.
     * @return
     */
    public boolean put(String key, Object stuff) {
        try {
            cache.put(key, stuff);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Get the cached value.
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return cache.get(key);
    }

    /**
     * Remove the cached value.
     *
     * @param key
     * @return
     */
    public Object remove(String key) {

        if (key == null) {
            return null;
        }

        return cache.remove(key);
    }
}