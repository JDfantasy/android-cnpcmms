package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_imageview_adapter;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class Petrochina_See_function_introduceActivity extends MyActivity {
	private ImageView back;
	private int id = 0;
	private ListView list;
	private Item_imageview_adapter adapter;
	private List<Integer> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ptrochina__see_function_introduce);
		setview();
	}

	/*
	 * 设置界面的方法
	 */
	private void setview() {
		set_title_text("功能详情介绍");
		list = (ListView) findViewById(R.id.listview_petrochina_see_function_introduce);
		adapter = new Item_imageview_adapter(Petrochina_See_function_introduceActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Integer>();
		id = getIntent().getIntExtra("id", 0);
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		selectimageview();
	}

	/*
	 * 控件的点击事件的监听
	 */
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_See_function_introduceActivity.this);
				break;

			default:
				break;
			}

		}
	};

	/*
	 * 判断上个界面传递过来的参数，打开相应的图片
	 */
	private void selectimageview() {
		switch (id) {
		case 0:// 加油站信息
			data.add(R.drawable.petrochian_jyz_1);
			break;
		case 1:// 供应商信息
			data.add(R.drawable.petrochina_gys_1);
			break;
		case 2:// 审批
			data.add(R.drawable.petrochina_shenpi_1);
			break;
		case 3:// 公告管理
			data.add(R.drawable.petrochina_gonggao_1);
			break;
		case 4:// 维修率查询
			data.add(R.drawable.petrochina_weixiulv_1);
			break;

		default:
			break;
		}
		adapter.addDataBottom(data);
	}
}
