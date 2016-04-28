package com.cnpc.zhibo.app.view;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class BrowseView extends ImageView {
	private int Img_ID;
	private float startDis;// 开始距离
	private PointF midPoint;// 中间点
	private float oldRotation = 0;
	private float rotation = 0;
	private PointF startPoint = new PointF();
	private Matrix matrix = new Matrix();
	private Matrix currentMatrix = new Matrix();
	private Activity mActivity;
	private boolean is_Editable=false;

	/**
	 * 模式 NONE：无 DRAG：拖拽. ZOOM:缩放
	 * 
	 * @author zhangjia
	 * 
	 */
	private enum MODE {
		NONE, DRAG, ZOOM

	};

	private MODE mode = MODE.NONE;// 默认模式

	/** 构造方法 **/
	public BrowseView(Context context) {
		super(context);
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}
	
	/** 控制图片的可编辑性**/
	public void setis_Editable(boolean is_Editable) {
		this.is_Editable=is_Editable;
	}
	
	public boolean getis_Editable() {
		return this.is_Editable;
	}

	public BrowseView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	

	/**
	 * 计算两点之间的距离
	 * 
	 * @param event
	 * @return
	 */
	public static float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 计算两点之间的中间点
	 * 
	 * @param event
	 * @return
	 */
	public static PointF mid(MotionEvent event) {
		float midX = (event.getX(1) + event.getX(0)) / 2;
		float midY = (event.getY(1) + event.getY(0)) / 2;
		return new PointF(midX, midY);
	}

	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);

	}
	
	/***
	 * touch 事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/** 处理单点、多点触摸 **/
		if(is_Editable==true){
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 手指压下屏幕
				mode = MODE.DRAG;
				
				currentMatrix.set(this.getImageMatrix());// 记录ImageView当前的移动位置
				matrix.set(currentMatrix);
				startPoint.set(event.getX(), event.getY());
				postInvalidate();
				break;
				
			case MotionEvent.ACTION_POINTER_DOWN:// 当屏幕上还有触点（手指），再有一个手指压下屏幕
				mode = MODE.ZOOM;
				oldRotation = rotation(event);
				startDis = distance(event);
				if (startDis > 10f) {
					midPoint = mid(event);
					currentMatrix.set(this.getImageMatrix());// 记录ImageView当前的缩放倍数
				}
				break;

			case MotionEvent.ACTION_MOVE:// 手指在屏幕移动，该 事件会不断地触发
				if (mode == MODE.DRAG) {
					float dx = event.getX() - startPoint.x;// 得到在x轴的移动距离
					float dy = event.getY() - startPoint.y;// 得到在y轴的移动距离
					matrix.set(currentMatrix);// 在没有进行移动之前的位置基础上进行移动
					matrix.postTranslate(dx, dy);
				} else if (mode == MODE.ZOOM) {// 缩放与旋转
					float endDis = distance(event);// 结束距离
					rotation = (rotation(event) - oldRotation);
					if (endDis > 10f) {
						float scale = endDis / startDis;// 得到缩放倍数
						matrix.set(currentMatrix);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
						matrix.postRotate(rotation, midPoint.x, midPoint.y);
					}
				}
				break;

			case MotionEvent.ACTION_UP:// 手指离开屏
				
				break;
			case MotionEvent.ACTION_POINTER_UP:// 有手指离开屏幕,但屏幕还有触点（手指）
				mode = MODE.NONE;
				break;
			}
			this.setImageMatrix(matrix);
		}
		return true;
	}

}