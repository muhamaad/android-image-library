package com.zarea.dcimagelibrary;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by zarea on 1/17/16.
 */
public class ImageCache extends LruCache<String, Bitmap> {
    /**
     * set max size for memory cash
     * @param maxSize
     */
    public ImageCache( int maxSize ) {
        super( maxSize );
    }

    @Override
    protected int sizeOf( String key, Bitmap value ) {
        return value.getByteCount();
    }

    @Override
    protected void entryRemoved( boolean evicted, String key, Bitmap oldValue, Bitmap newValue ) {
        oldValue.recycle();
    }

}