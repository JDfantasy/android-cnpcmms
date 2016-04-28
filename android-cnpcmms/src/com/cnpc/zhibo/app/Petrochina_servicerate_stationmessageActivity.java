package com.cnpc.zhibo.app;

/*
 * ��ʯ�Ͷ˵�ά����ģ���в鿴����վ��Ϣ�Ľ���
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
	private ImageView back;// ���ذ�ť
	private TextView stationmonads, stationmessage;// �鿴ά����Ŀ
	private ListView list;// ��ʾ����վ��Ŀ���б�
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

	// listview���е���¼��ļ���
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

	// ���ý����ϵĿؼ��ķ���
	private void setview() {
		set_title_text("����վ��Ϣ");
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
		set_entryaddddata();// �������ݵķ���
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
			case R.id.imageView_myacitity_zuo:// ���ؼ�
				finish();
				Myutil.set_activity_close(Petrochina_servicerate_stationmessageActivity.this);

				break;
			case R.id.textview_petrochina_servicerate_stationseemessage_stationmonads:// �鿴����վ��ά����Ŀ���б�
				line.setVisibility(View.VISIBLE);
				web.setVisibility(View.INVISIBLE);
				stationmessage.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				stationmonads.setTextColor(getResources().getColor(R.color.baise));
				stationmessage.setBackgroundColor(getResources().getColor(R.color.baise));
				stationmonads.setBackgroundColor(getResources().getColor(R.color.dingbubeijingse));
				break;
			case R.id.textview_petrochina_servicerate_stationseemessage_stationmessage:// �鿴����վ��Ϣ
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

	// �������ݵķ���
	private void set_entryaddddata() {
		data.add(new Petrochina_station_seerepairsitem("ά�޵��ţ�12345652", "2016-02-12  12:20", "�������վ   ���ͻ�  ������", "ͨ������",
				"����ʱ��0��6Сʱ22����", "����900Ԫ", "145"));
		data.add(new Petrochina_station_seerepairsitem("ά�޵��ţ�12345679", "2016-02-15  8:20", "�������վ   ���ͻ�  ������", "ͨ������",
				"����ʱ��0��6Сʱ22����", "����900Ԫ", "143"));
		data.add(new Petrochina_station_seerepairsitem("ά�޵��ţ�12345568", "2016-02-16  15:20", "�������վ   ���ͻ�  ������", "ͨ������",
				"����ʱ��0��6Сʱ22����", "����900Ԫ", "144"));
		data.add(new Petrochina_station_seerepairsitem("ά�޵��ţ�12345662", "2016-02-19  6:20", "�������վ   ���ͻ�  ������", "ͨ������",
				"����ʱ��0��6Сʱ22����", "����900Ԫ", "142"));
		adapter.addDataBottom(data);
	}
}
