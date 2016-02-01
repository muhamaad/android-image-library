package com.zarea.dcimagelibrary;

/**
 * Created by zarea on 1/17/16.
 */
public interface ImageLoaderCallback {
    void onSuccess();
    void onError(Exception e);
}
