package com.cnpc.zhibo.app;
/*
 * 站长端的功能列表介绍的界面
 */
import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_centre_function_introduce_select_adapter;
import com.cnpc.zhibo.app.entity.Centre_Function_introduce;
import com.cnpc.zhibo.app.util.Myutil;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class Centre_Function_IntroduceActivity extends MyActivity {
	private ImageView back;
	private ListView list;
	private List<Centre_Function_introduce> data;
	private Item_centre_function_introduce_select_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitycentre_function_introduce);
		setview();
	}

	/*
	 * 设置界面的方法
	 */
	private void setview() {
		set_title_text("功能介绍");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listView_centre_function_introduce_entry);
		adapter = new Item_centre_function_introduce_select_adapter(Centre_Function_IntroduceActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_Function_introduce>();
		setclodingdata();
		//list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		list.setOnItemClickListener(listener);
	}
	/*
	 * 列表的行点击事件
	 */
	private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in=new Intent(Centre_Function_IntroduceActivity.this, Centre_See_Function_introduceActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_Function_IntroduceActivity.this);
			
		}
	};

	/*
	 * 控件的监听事件
	 */
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
             finish();
             Myutil.set_activity_close(Centre_Function_IntroduceActivity.this);
				break;

			default:
				break;
			}

		}
	};

	/*
	 * 加载数据的方法
	 */
	private void setclodingdata() {
		data.add(new Centre_Function_introduce(R.drawable.woyaobaoxiu, 0, "我要报修"));	
		data.add(new Centre_Function_introduce(R.drawable.copy_file, 1, "报修单"));
		data.add(new Centre_Function_introduce(R.drawable.shenpiguanli, 2, "审批管理"));
		data.add(new Centre_Function_introduce(R.drawable.gonggaoguanli, 3, "公告管理"));
		data.add(new Centre_Function_introduce(R.drawable.zhengjianguanli, 4, "证件管理"));
		data.add(new Centre_Function_introduce(R.drawable.centre_service_check, 5, "维修"));
		data.add(new Centre_Function_introduce(R.drawable.centre_examine_check, 6, "审批"));
		adapter.addDataBottom(data);
	}
}
