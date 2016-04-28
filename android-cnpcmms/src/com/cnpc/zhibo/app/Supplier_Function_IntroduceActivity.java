package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_supplier_function_adapter;
import com.cnpc.zhibo.app.entity.Centre_Function_introduce;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * 供应商的功能介绍
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Supplier_Function_IntroduceActivity extends MyActivity {
	private ImageView back;
	private ListView lv;
	private Item_supplier_function_adapter adapter;
	private List<Centre_Function_introduce> imgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier__function__introduce);
		setViews();

	}

	private void setViews() {
		set_title_text("功能介绍");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.imageView_myacitity_zuo:// 返回按钮
					finish();
					Myutil.set_activity_close(Supplier_Function_IntroduceActivity.this);
					break;

				default:
					break;
				}

			}
		});
		
		lv = (ListView)findViewById(R.id.supplier_function_listView);
		addimg();
		adapter = new Item_supplier_function_adapter(imgs, this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Centre_Function_introduce a = (Centre_Function_introduce) adapter.getItem(position);
				startActivity(new Intent(Supplier_Function_IntroduceActivity.this,
						Supplier_See_Function_introduceActivity.class).putExtra("id", a.id));
				Myutil.set_activity_open(Supplier_Function_IntroduceActivity.this);
			}
		});
	}

	private void addimg() {
		imgs = new ArrayList<Centre_Function_introduce>();
		imgs.add(new Centre_Function_introduce(R.drawable.dingdanfenpei, 0, "订单分配"));
		imgs.add(new Centre_Function_introduce(R.drawable.fenpeiguanli, 1, "分配管理"));
		imgs.add(new Centre_Function_introduce(R.drawable.gonggaoguanli, 2, "公告管理"));
		imgs.add(new Centre_Function_introduce(R.drawable.centre_service_check, 3, "维修"));
		imgs.add(new Centre_Function_introduce(R.drawable.supplier_baoxiao1, 4, "报销"));
	}
}
