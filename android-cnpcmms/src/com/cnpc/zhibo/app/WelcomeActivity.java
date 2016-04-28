package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class WelcomeActivity extends Activity {
	private List<ImageView> list = new ArrayList<ImageView>();
	private int prePos;
	private ImageView experience;// 体验

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		experience = (ImageView) findViewById(R.id.activity_welcome_imageView);
		experience.setOnClickListener(l);
		setViewPager();
		// 设置页面的导航
		setPagerIndicator();

	}

	private LinearLayout indicatorLayout;

	private void setPagerIndicator() {
		indicatorLayout = (LinearLayout) findViewById(R.id.linearid);
		for (int i = 0; i < list.size(); i++) {
			// Log.i("i", "" + i);
			View view = new View(this);
			view.setEnabled(true);
			view.setBackgroundResource(R.drawable.point);
			LayoutParams params = new LayoutParams(10, 10);
			params.rightMargin = 10;
			view.setLayoutParams(params);
			indicatorLayout.addView(view);
		}
		indicatorLayout.getChildAt(0).setEnabled(false);

	}

	private void setViewPager() {
		// 在初始化viewpager之前先加载图片显示出来
		prepare();

		ViewPager vpid = (ViewPager) findViewById(R.id.vpid);

		ImagePagerAdapter adapter = new ImagePagerAdapter();

		vpid.setAdapter(adapter);
		vpid.setOnPageChangeListener(new PagerListener());
	}

	/**
	 * 设置图片
	 */
	private void prepare() {
		ImageView imageView01 = new ImageView(this);
		imageView01.setImageResource(R.drawable.a00);
		imageView01.setScaleType(ScaleType.FIT_XY);
		list.add(imageView01);
		ImageView imageView02 = new ImageView(this);
		imageView02.setImageResource(R.drawable.a01);
		imageView02.setScaleType(ScaleType.FIT_XY);
		list.add(imageView02);
		ImageView imageView03 = new ImageView(this);
		imageView03.setImageResource(R.drawable.a02);
		imageView03.setScaleType(ScaleType.FIT_XY);
		list.add(imageView03);
		ImageView imageView04 = new ImageView(this);
		imageView04.setImageResource(R.drawable.a03);
		imageView04.setScaleType(ScaleType.FIT_XY);
		list.add(imageView04);
	}

	class ImagePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView item = list.get(position);
			container.addView(item);
			return item;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

	}

	class PagerListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int pos) {
			setIndicatorEnable(pos);
			setButtonVisiable(pos);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
	}

	private View.OnClickListener l=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WelcomeActivity.this, EnterintoActivity.class);
			startActivity(intent);
			finish();
		}
	};

	private void setButtonVisiable(int pos) {
		if (pos == 3) {
			experience.setVisibility(View.VISIBLE);
		} else {
			experience.setVisibility(View.GONE);
		}
	}

	private void setIndicatorEnable(int pos) {
		indicatorLayout.getChildAt(prePos).setEnabled(true);
		indicatorLayout.getChildAt(pos).setEnabled(false);
		prePos = pos;
	}

	/**
	 * 运行是不能回退
	 */

	@Override
	public void onBackPressed() {
	}

}
