package com.cnpc.zhibo.app;
import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_centre_function_introduce_select_adapter;
import com.cnpc.zhibo.app.entity.Centre_Function_introduce;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * 中石油端的功能介绍
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class Petrochina_function_introduceActivity extends MyActivity {
	private ImageView back;
	private ListView list;
	private List<Centre_Function_introduce> data;
	private Item_centre_function_introduce_select_adapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_function_introduce);
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
		list = (ListView) findViewById(R.id.listView_petrochina_function_introduce_entry);
		adapter = new Item_centre_function_introduce_select_adapter(Petrochina_function_introduceActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_Function_introduce>();
		setclodingdata();
		//list.setDividerHeight(1);
		list.setSelector(new BitmapDrawable());
		list.setOnItemClickListener(listener);
	}
	/*
	 * 列表的行点击事件
	 */
	private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in=new Intent(Petrochina_function_introduceActivity.this, Petrochina_See_function_introduceActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_function_introduceActivity.this);
			
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
             Myutil.set_activity_close(Petrochina_function_introduceActivity.this);
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
		data.add(new Centre_Function_introduce(R.drawable.petrochina_station, 0, "加油站信息"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_suppliermessage, 1, "供应商信息"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_shenpi, 2, "审批"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_notice, 3, "公告管理"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_weixiulv, 4, "维修率查询"));
		adapter.addDataBottom(data);
	}
}
