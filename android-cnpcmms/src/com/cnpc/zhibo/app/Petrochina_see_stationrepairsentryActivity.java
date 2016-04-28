package com.cnpc.zhibo.app;
/*
 * 中石油端查看加油站维修项目的列表
 */
import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_petrochina_station_seerepairsitem;
import com.cnpc.zhibo.app.entity.Petrochina_station_seerepairsitem;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


public class Petrochina_see_stationrepairsentryActivity extends MyActivity {
	private ImageView back;
	private ListView list;
	private Item_petrochina_station_seerepairsitem adapter;
	private List<Petrochina_station_seerepairsitem> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_see_stationrepairsentry);
		
	}
	
	
}
