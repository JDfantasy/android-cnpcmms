package com.cnpc.zhibo.app.view;

import com.cnpc.zhibo.app.R;
import android.annotation.SuppressLint;
/*
 * 自定义虚线控件
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class DashedLine extends View {
	private final String namespace = "http://com.smartmap.driverbook";
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private Rect mRect;

	public DashedLine(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(getResources().getColor(R.color.zitiyanse5));
		Path path = new Path();
		path.moveTo(0, 6);
		path.lineTo(getDisplay().getWidth(), 6);
		PathEffect effects = new DashPathEffect(new float[] { 10, 10, 10, 10 }, 1);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
	}
}
