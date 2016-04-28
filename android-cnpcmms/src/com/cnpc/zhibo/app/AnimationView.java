package com.cnpc.zhibo.app;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


//自定义控件：播放动画
public class AnimationView extends View{
	
	private int currentIndex=0;
	private int sleepTime=200;
	private int viewWidth,viewHeight;
	private Bitmap[] bitmaps=null;
	public static boolean isRunning=true;
	private Thread thread;

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//读取xml中的属性
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.animation);
		sleepTime=(int) ta.getFloat(R.styleable.animation_sleep_time, 1);
		
		TypedArray taImage=context.getResources().obtainTypedArray(R.array.animationImages);
		int length=taImage.length();
		bitmaps=new Bitmap[length];
		
		for(int i=0;i<length;i++)
		{
			int imageResId=taImage.getResourceId(i, 0);
			bitmaps[i]=BitmapFactory.decodeResource(getResources(), imageResId);
		}
		
		thread=new Thread(new MyRunnable());
		thread.start();
	}
	
	//Measure测量
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int mode=MeasureSpec.getMode(heightMeasureSpec);
		if (mode==MeasureSpec.AT_MOST){
			int imageWidth=bitmaps[0].getWidth();
			int imageHeight=bitmaps[0].getHeight();
			setMeasuredDimension(imageWidth, imageHeight);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		Bitmap bitmap = bitmaps[currentIndex];
		if (bitmap!=null)
		{
		// 居中
		int x = (viewWidth - bitmap.getWidth()) / 2;
		int y = (viewHeight - bitmap.getHeight()) / 2;
		canvas.drawBitmap(bitmap, x, y, paint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewHeight = h;
		viewWidth = w;
	}

	class MyRunnable implements Runnable {

		@Override
		public void run() {
			while (isRunning) {
				try {
					currentIndex++;
					if (currentIndex >= bitmaps.length) {
						currentIndex = 0;
					}
					postInvalidate();
					Thread.currentThread().sleep(sleepTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
