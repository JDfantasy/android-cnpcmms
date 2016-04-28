package com.cnpc.zhibo.app;
/*
 * 站长端查看单个功能的详细介绍的界面
 */

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

public class Centre_See_Function_introduceActivity extends MyActivity {
	private ImageView back;
	private int id = 0;
	private ListView list;
	private Item_imageview_adapter adapter;
	private List<Integer> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre__see__function_introduce);
		setview();
	}

	/*
	 * 设置界面的方法
	 */
	private void setview() {
		set_title_text("功能详情介绍");
		list = (ListView) findViewById(R.id.listview_centre_see_function_introduce);
		adapter = new Item_imageview_adapter(Centre_See_Function_introduceActivity.this);
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
				Myutil.set_activity_close(Centre_See_Function_introduceActivity.this);
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
		case 0:// 我要报修
			data.add(R.drawable.centre_wybx_1);
			break;
		case 1:// 报修单
			data.add(R.drawable.centre_baoxiudan_1);
			break;
		case 2:// 审批管理
			data.add(R.drawable.centre_shenpiguanli_1);
			break;
		case 3:// 公告管理
			data.add(R.drawable.centre_gonggao_1);
			break;
		case 4:// 证件管理
			data.add(R.drawable.centre_zhengjianguanli_1);
			break;
		case 5:// 维修
			data.add(R.drawable.centre_weixiu_1);
			break;
		case 6:// 审批
			data.add(R.drawable.centre_shenpi_1);
			break;

		default:
			break;

		}
		adapter.addDataBottom(data);
	}
}
