package com.cnpc.zhibo.app.util;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;

public class ImageFileCache implements ImageCache {

	@Override
	public Bitmap getBitmap(String url) {

		return ImageFileCacheUtils.getInstance().getImage(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		ImageFileCacheUtils.getInstance().saveBitmap(bitmap, url);

	}

}
