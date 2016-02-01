package com.zarea.dcimagelibrary;

import android.graphics.Bitmap;

/**
 * Created by zarea on 1/17/16.
 */
public interface ImageLoaderAsyncCallback {
    void onProcessFinish(Bitmap bitmap);
}
