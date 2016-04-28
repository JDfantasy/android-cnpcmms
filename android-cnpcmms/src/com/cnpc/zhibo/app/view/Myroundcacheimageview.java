package com.cnpc.zhibo.app.view;
/*
 * 自定义volley的networkimageview空间（自带下载护和图片优化）
 */
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.util.ImageFileCache;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class Myroundcacheimageview extends NetworkImageView {

	private ImageLoader imageLoader;

	public Myroundcacheimageview(Context context) {
		super(context);
		this.imageLoader = new ImageLoader(SysApplication.getHttpQueues(), new ImageFileCache());
	}

	public Myroundcacheimageview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.imageLoader = new ImageLoader(SysApplication.getHttpQueues(), new ImageFileCache());
	}

	public Myroundcacheimageview(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.imageLoader = new ImageLoader(SysApplication.getHttpQueues(), new ImageFileCache());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addRoundRect(new RectF(0, 0, w, h), 10.0f, 10.0f, Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}

	

	public void setImageUrl(String url) {

		super.setImageUrl(url, imageLoader);
	}
}
