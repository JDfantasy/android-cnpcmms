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
	private float startDis;// ��ʼ����
	private PointF midPoint;// �м��
	private float oldRotation = 0;
	private float rotation = 0;
	private PointF startPoint = new PointF();
	private Matrix matrix = new Matrix();
	private Matrix currentMatrix = new Matrix();
	private Activity mActivity;
	private boolean is_Editable=false;

	/**
	 * ģʽ NONE���� DRAG����ק. ZOOM:����
	 * 
	 * @author zhangjia
	 * 
	 */
	private enum MODE {
		NONE, DRAG, ZOOM

	};

	private MODE mode = MODE.NONE;// Ĭ��ģʽ

	/** ���췽�� **/
	public BrowseView(Context context) {
		super(context);
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}
	
	/** ����ͼƬ�Ŀɱ༭��**/
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
	 * ��������֮��ľ���
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
	 * ��������֮����м��
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
	 * touch �¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/** �����㡢��㴥�� **/
		if(is_Editable==true){
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// ��ָѹ����Ļ
				mode = MODE.DRAG;
				
				currentMatrix.set(this.getImageMatrix());// ��¼ImageView��ǰ���ƶ�λ��
				matrix.set(currentMatrix);
				startPoint.set(event.getX(), event.getY());
				postInvalidate();
				break;
				
			case MotionEvent.ACTION_POINTER_DOWN:// ����Ļ�ϻ��д��㣨��ָ��������һ����ָѹ����Ļ
				mode = MODE.ZOOM;
				oldRotation = rotation(event);
				startDis = distance(event);
				if (startDis > 10f) {
					midPoint = mid(event);
					currentMatrix.set(this.getImageMatrix());// ��¼ImageView��ǰ�����ű���
				}
				break;

			case MotionEvent.ACTION_MOVE:// ��ָ����Ļ�ƶ����� �¼��᲻�ϵش���
				if (mode == MODE.DRAG) {
					float dx = event.getX() - startPoint.x;// �õ���x����ƶ�����
					float dy = event.getY() - startPoint.y;// �õ���y����ƶ�����
					matrix.set(currentMatrix);// ��û�н����ƶ�֮ǰ��λ�û����Ͻ����ƶ�
					matrix.postTranslate(dx, dy);
				} else if (mode == MODE.ZOOM) {// ��������ת
					float endDis = distance(event);// ��������
					rotation = (rotation(event) - oldRotation);
					if (endDis > 10f) {
						float scale = endDis / startDis;// �õ����ű���
						matrix.set(currentMatrix);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
						matrix.postRotate(rotation, midPoint.x, midPoint.y);
					}
				}
				break;

			case MotionEvent.ACTION_UP:// ��ָ�뿪��
				
				break;
			case MotionEvent.ACTION_POINTER_UP:// ����ָ�뿪��Ļ,����Ļ���д��㣨��ָ��
				mode = MODE.NONE;
				break;
			}
			this.setImageMatrix(matrix);
		}
		return true;
	}

}