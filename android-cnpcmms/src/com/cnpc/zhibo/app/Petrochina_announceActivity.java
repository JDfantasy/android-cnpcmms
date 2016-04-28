package com.cnpc.zhibo.app;
/*
 * 中石油段公告管理界面
 */
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.adapter.Item_supplier_notice_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_notice;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Petrochina_announceActivity extends MyActivity {
	private ImageView back, send;// 返回，发送
	private ListView listView;
	private Item_supplier_notice_adapter adapter;
	private List<Supplier_notice> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_announce);
		setview();
	}

	private void setview() {
		set_title_text("公告管理");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		send = (ImageView) findViewById(R.id.imageView_myactity_you);
		send.setImageResource(R.drawable.send_notice1);
		back.setVisibility(View.VISIBLE);
		send.setVisibility(View.VISIBLE);
		adapter = new Item_supplier_notice_adapter(this);
		listView = (ListView) findViewById(R.id.listView_petrochina_announce_entry);
		back.setOnClickListener(l);
		send.setOnClickListener(l);
		data = new ArrayList<Supplier_notice>();
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);
		listView.setSelector(new BitmapDrawable());
		listView.setOnItemClickListener(listener);
		adapter.setviewstate();
		get_notice_history();

	}
	private AdapterView.OnItemClickListener listener=new  AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in=new Intent(Petrochina_announceActivity.this, Petrochina_seeannounce_messageActivity.class);
			in.putExtra("noticeId", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_announceActivity.this);
			
		}
		
	};

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Petrochina_announceActivity.this);
				break;
			case R.id.imageView_myactity_you:
				startActivity(new Intent(Petrochina_announceActivity.this, Petrochina_sendallActivity.class));
				Myutil.set_activity_open(Petrochina_announceActivity.this);
				break;

			default:
				break;
			}

		}
	};
	/**
	 * 获取所有历史公告列表
	 */
	private void get_notice_history() {
		String url = Myconstant.PETROCHINA_GETHISTORYNOTICEENTRY + "?beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();

		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("reporter");
							data.add(new Supplier_notice(obj3.getString("id"), obj3.getString("title"),
									obj3.getString("content"), obj3.getString("unread"), obj3.getString("createDate")));
						}
						if (obj2.length() > 0) {
							Toast.makeText(Petrochina_announceActivity.this, obj1.getString("content"), Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(Petrochina_announceActivity.this, "没有查到公告列表", Toast.LENGTH_SHORT).show();
						}
						adapter.addDataBottom(data);
					} else {
						Toast.makeText(Petrochina_announceActivity.this, obj1.getString("content"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Petrochina_announceActivity.this, "网络出问题了，请稍后再试...", Toast.LENGTH_LONG).show();

			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
		};
		SysApplication.getHttpQueues().add(re);
	}
}
