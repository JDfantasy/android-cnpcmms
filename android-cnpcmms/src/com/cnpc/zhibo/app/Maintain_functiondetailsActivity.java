package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_imageview_adapter;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;

//维修端功能介绍详情界面
public class Maintain_functiondetailsActivity extends MyActivity {

	private ImageView fanhui;
	private String value;
	private ListView listView;
	private Item_imageview_adapter adapter;
	private List<Integer> list=new ArrayList<Integer>();
	private int[] servicelistarray=new int[]{R.drawable.maintain_servicelistfunction};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_functiondetails);
		value=getIntent().getStringExtra("functionName");
		//Log.i("tag", "value="+value);
		//functiondetails=(ImageView)findViewById(R.id.maintain_functionimageview);
		//functiondetails.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		adapter=new Item_imageview_adapter(this);
		listView= (ListView) findViewById(R.id.maintain_functionlistview);
		listView.setDividerHeight(0);
		list.clear();
		adapter.getData().clear();
		if(value.equals("servicelist")){
			//functiondetails.setImageDrawable(getResources().getDrawable(R.drawable.ease_app_panel_video_icon));
			//functiondetails.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			list.add(R.drawable.maintain_servicelistfunction);
			listView.setAdapter(adapter);
			adapter.setdate(list);
		}else if(value.equals("alloction")){
			list.add(R.drawable.maintain_alloctionfunction1);
			list.add(R.drawable.maintain_alloctionfunction2);
			list.add(R.drawable.maintain_alloctionfunction3);
			list.add(R.drawable.maintain_alloctionfunction4);
			listView.setAdapter(adapter);
			adapter.setdate(list);
		}else if(value.equals("notice")){
			list.add(R.drawable.maintain_noticemagerfunction);
			listView.setAdapter(adapter);
			adapter.setdate(list);
		}else if(value.equals("service")){
			list.add(R.drawable.maintain_servicefunction_2);
			list.add(R.drawable.maintain_servicefunction_1);
			listView.setAdapter(adapter);
			adapter.setdate(list);
		}else if(value.equals("apply")){
			list.add(R.drawable.maintain_applyfunction_1);
			list.add(R.drawable.maintain_applyfunction_2);
			listView.setAdapter(adapter);
			adapter.setdate(list);
		}
		setViews();
	}
	
	private void setViews() {
		set_title_text("详情介绍");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
	}
	
	private View.OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Maintain_functiondetailsActivity.this);
				break;
			default:
				break;
			}
		}
	};
	
}
