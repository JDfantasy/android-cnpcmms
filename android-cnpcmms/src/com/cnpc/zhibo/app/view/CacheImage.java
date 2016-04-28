package com.cnpc.zhibo.app.view;
//带缓存和下载图片的控件
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.util.Http_server_util;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CacheImage extends ImageView {

	private Context context;
	private LruCache<String, Bitmap> cache;
	private File cachePath;

	public CacheImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		if (cache == null) {
			int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
			cache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}

		if (cachePath == null) {
			cachePath = context.getExternalCacheDir();
			if (!cachePath.exists()) {
				cachePath.mkdirs();
			}
		}

	}

	public void setImageUrl(String url) {
		// 看缓存中是否有图片
		Bitmap bitmap = getFromCache(url);
		if (bitmap != null) {
			setImageBitmap(bitmap);
		}
		// 看SDcard中是否有图片
		bitmap = getFromSDCard(url);
		if (bitmap != null) {
			setImageBitmap(bitmap);
		}
		// 都没有，去网路下载
		getFromNet(url);

	}

	public Bitmap loadBitmap(String url) {
		// 看缓存中是否有图片
		Bitmap bitmap = getFromCache(url);
		if (bitmap != null) {
			return bitmap;
		}
		// 看SDcard中是否有图片
		bitmap = getFromSDCard(url);
		if (bitmap != null) {
			return bitmap;
		}
		// 都没有，去网路下载
		getFromNet(url);
		return bitmap;
	}

	// 从缓存中取图片
	private Bitmap getFromCache(String url) {
		return cache.get(url);
	}

	// 将图片存入缓存
	private void saveToCache(String url, Bitmap bitmap) {
		cache.put(url, bitmap);
	}

	// 从SD卡中取图片
	private Bitmap getFromSDCard(String url) {
		int index = url.lastIndexOf('/');
		String pathName = url.substring(index + 1);
		File f = new File(cachePath, pathName);
		if (f.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(pathName);
			return bitmap;
		}
		return null;
	}

	// 将图片存入SD卡
	private void saveToSDCard(String url, Bitmap bitmap) {
		int index = url.lastIndexOf("/");
		String fileName = url.substring(index + 1);
		File f = new File(cachePath, fileName);
		try {
			OutputStream out = new FileOutputStream(f);
			bitmap.compress(CompressFormat.JPEG, 100, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 去网络下载图片
	private void getFromNet(String url) {
		new CacheAsyncTask().execute(url);
	}

	private class CacheAsyncTask extends AsyncTask<String, Void, Bitmap> {

		String url = "";

		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			InputStream is = Http_server_util.Get_inptu_http(params[0]);
			if (is != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				saveToCache(url, result);
				saveToSDCard(url, result);
				setImageBitmap(result);
			} else {
				setImageResource(R.drawable.ic_launcher);
			}
		}
	}
}
