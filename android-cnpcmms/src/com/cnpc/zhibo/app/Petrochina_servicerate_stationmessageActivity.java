package com.cnpc.zhibo.app;

/*
 * 中石油端的维修率模块中查看加油站信息的界面
 */
import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_petrochina_station_seerepairsitem;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_station_seerepairsitem;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Petrochina_servicerate_stationmessageActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private TextView stationmonads, stationmessage;// 查看维修项目
	private ListView list;// 显示加油站项目的列表
	private Item_petrochina_station_seerepairsitem adapter;
	private List<Petrochina_station_seerepairsitem> data;
	private WebView web;
	private LinearLayout line;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_servicerate_stationmessage);
		setview();
		list.setOnItemClickListener(listener);
	}

	// listview的行点击事件的监听
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Petrochina_servicerate_stationmessageActivity.this,
					Petrochina_see_station_repairsmessageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_servicerate_stationmessageActivity.this);

		}
	};

	// 设置界面上的控件的方法
	private void setview() {
		set_title_text("加油站信息");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		stationmonads = (TextView) findViewById(R.id.textview_petrochina_servicerate_stationseemessage_stationmonads);
		stationmessage = (TextView) findViewById(R.id.textview_petrochina_servicerate_stationseemessage_stationmessage);
		web = (WebView) findViewById(R.id.webview_petrochina_servicerate_stationseemessage);
		line = (LinearLayout) findViewById(R.id.linearlayout_petrochina_servicerate_stationseemessage);
		// web.loadUrl(Myconstant.STATIONMESSAGE +
		// getIntent().getStringExtra("id"));

		web.loadUrl(Myconstant.STATIONMESSAGE + "5");
		stationmonads.setOnClickListener(l);
		stationmessage.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listview_petrochina_servicerate_stationseemessage_stationmonads);
		adapter = new Item_petrochina_station_seerepairsitem(this);
		list.setAdapter(adapter);
		data = new ArrayList<Petrochina_station_seerepairsitem>();
		set_entryaddddata();// 加载数据的方法
		stationmessage.setTextColor(getResources().getColor(R.color.baise));
		stationmonads.setTextColor(getResources().getColor(R.color.dingbubeijingse));
		stationmessage.setBackgroundColor(getResources().getColor(R.color.dingbubeijingse));
		stationmonads.setBackgroundColor(getResources().getColor(R.color.baise));
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回键
				finish();
				Myutil.set_activity_close(Petrochina_servicerate_stationmessageActivity.this);

				break;
			case R.id.textview_petrochina_servicerate_stationseemessage_stationmonads:// 查看加油站的维修项目的列表
				line.setVisibility(View.VISIBLE);
				web.setVisibility(View.INVISIBLE);
				stationmessage.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				stationmonads.setTextColor(getResources().getColor(R.color.baise));
				stationmessage.setBackgroundColor(getResources().getColor(R.color.baise));
				stationmonads.setBackgroundColor(getResources().getColor(R.color.dingbubeijingse));
				break;
			case R.id.textview_petrochina_servicerate_stationseemessage_stationmessage:// 查看加油站信息
				stationmessage.setTextColor(getResources().getColor(R.color.baise));
				stationmonads.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				stationmessage.setBackgroundColor(getResources().getColor(R.color.dingbubeijingse));
				stationmonads.setBackgroundColor(getResources().getColor(R.color.baise));
				line.setVisibility(View.INVISIBLE);
				web.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}

		}
	};

	// 加载数据的方法
	private void set_entryaddddata() {
		data.add(new Petrochina_station_seerepairsitem("维修单号：12345652", "2016-02-12  12:20", "紫竹加油站   加油机  不出油", "通过审批",
				"共耗时：0天6小时22分钟", "花费900元", "145"));
		data.add(new Petrochina_station_seerepairsitem("维修单号：12345679", "2016-02-15  8:20", "紫竹加油站   加油机  不出油", "通过审批",
				"共耗时：0天6小时22分钟", "花费900元", "143"));
		data.add(new Petrochina_station_seerepairsitem("维修单号：12345568", "2016-02-16  15:20", "紫竹加油站   加油机  不出油", "通过审批",
				"共耗时：0天6小时22分钟", "花费900元", "144"));
		data.add(new Petrochina_station_seerepairsitem("维修单号：12345662", "2016-02-19  6:20", "紫竹加油站   加油机  不出油", "通过审批",
				"共耗时：0天6小时22分钟", "花费900元", "142"));
		adapter.addDataBottom(data);
	}
}
