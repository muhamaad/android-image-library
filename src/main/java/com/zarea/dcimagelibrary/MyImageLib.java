package com.zarea.dcimagelibrary;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by zarea on 1/17/16.
 */
public class MyImageLib {

    private ImageLoader imageLoader;
    private ImageCache mMemoryCache;
    private WeakReference<ImageView> viewReference;
    private ImageLoaderCallback imageLoaderSuccess;

    /**
     * @param cashMaxSize
     */
    public MyImageLib(int cashMaxSize) {
        mMemoryCache = new ImageCache(cashMaxSize);
    }

    /**
     * get image in background, cache the image in memory
     *
     * @param imageUrl
     * @param imageView
     * @throws Exception
     */
    public void getImageFromUrl(final String imageUrl, ImageView imageView){

        if (imageUrl != null && !imageUrl.equals("")) {

            viewReference = new WeakReference<>(imageView);

            if (!checkIfImageExist(imageUrl)) {
                imageLoader = new ImageLoader();
                imageLoader.execute(imageUrl);
                final ImageView finalImageView = imageView;
                imageLoader.callback = new ImageLoaderAsyncCallback() {
                    @Override
                    public void onProcessFinish(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        if (finalImageView != null) {
                            addBitmapToMemoryCache(imageUrl, bitmap);
                            finalImageView.setImageBitmap(getBitmapFromMemCache(imageUrl));
                        }
                    }
                };
            } else {
                //get bitmap from cache
                imageView = viewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(getBitmapFromMemCache(imageUrl));
                }
            }
        } else {
            try {
                throw new Exception("empty image url");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get image in background, cache the image in memory with callback
     *
     * @param imageUrl
     * @param imageView
     * @param imageLoaderSuccess
     * @throws Exception
     */
    public void getImageFromUrl(final String imageUrl, ImageView imageView, final ImageLoaderCallback imageLoaderSuccess) {
        if (imageUrl != null && !imageUrl.equals("")) {
            viewReference = new WeakReference<>(imageView);

            if (!checkIfImageExist(imageUrl)) {
                imageLoader = new ImageLoader();
                imageLoader.execute(imageUrl);
                final ImageView finalImageView = imageView;
                imageLoader.callback = new ImageLoaderAsyncCallback() {
                    @Override
                    public void onProcessFinish(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        if (finalImageView != null) {
                            addBitmapToMemoryCache(imageUrl, bitmap);
                            finalImageView.setImageBitmap(getBitmapFromMemCache(imageUrl));
                            imageLoaderSuccess.onSuccess();
                        }
                    }
                };

            } else {
                //get bitmap from cache
                imageView = viewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(getBitmapFromMemCache(imageUrl));
                }
            }
        } else {
            imageLoaderSuccess.onError(new Exception("empty image url"));

        }
    }

    private Boolean checkIfImageExist(String url) {
        return getBitmapFromMemCache(url) != null;
    }


    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private void removeImageFromCach(String url) {
        if (checkIfImageExist(url)) {
            mMemoryCache.remove(url);
        }
    }

    private void removeUnusedImage() {
        mMemoryCache.trimToSize(mMemoryCache.maxSize());
    }

    /**
     * stop download image
     * @return
     */
    public Boolean cancelImageDownload() {
        imageLoader.cancel(true);
        return true;
    }
}
