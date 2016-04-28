package com.cnpc.zhibo.app;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/*
 * ��ʯ�Ͷ�������Ӧ���б�Ľ���
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
import com.cnpc.zhibo.app.adapter.Item_petrochina_station_message_adapter;
import com.cnpc.zhibo.app.adapter.Item_petrochina_suppliers_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_station_message;
import com.cnpc.zhibo.app.entity.Petrochina_suppliers_entry;
import com.cnpc.zhibo.app.util.Myutil;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_search_suppliersentryActivity extends MyActivity {
	private ImageView back;
	private ListView list;
	private Item_petrochina_suppliers_adapter adapter;
	private List<Petrochina_suppliers_entry> data;
	private TextView seek;// ������ť
	private EditText message;// ��Ϣ
	private TextView hint;// ��ʾ��Ϣ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_search_suppliersentry);
		set_view();
	}

	// ���ý���ķ���
	private void set_view() {
		set_title_text("����");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_petrochina_search_suppliersntry_message);// ��Ϣ
		seek = (TextView) findViewById(R.id.textview_petrochina_search_suppliersntry_seek);// ������ť
		seek.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_petrochina_search_suppliersntry_hint);// ��ʾ��Ϣ
		hint.setText("����ά�޵��б�");
		list = (ListView) findViewById(R.id.listView_petrochina_search_suppliersntry_entry);// listview
		adapter = new Item_petrochina_suppliers_adapter(this);
		data = new ArrayList<Petrochina_suppliers_entry>();
		list.setAdapter(adapter);
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// �б���м����¼�
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Petrochina_search_suppliersentryActivity.this,
					Petrochina_seesuppliers_message_Activity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_search_suppliersentryActivity.this);
		}
	};

	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Petrochina_search_suppliersentryActivity.this);
				break;
			case R.id.textview_petrochina_search_suppliersntry_seek:// ������ť
				hint.setVisibility(View.INVISIBLE);
				if (message.getText().toString().equals("")) {
					Toast.makeText(Petrochina_search_suppliersentryActivity.this, "��������ؼ��ʣ��ڽ�������", 0).show();
				} else {
					searchexamineentry();
				}

				break;
			default:
				break;
			}

		}
	};

	// ����ά�޷��õ����ݵķ���
	private void searchexamineentry() {
		data.clear();
		adapter.getData().clear();
	
		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.PETROCHINA_GET_SEARCHPROVIDERENTRYSTATS, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// ��Ӧ������
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_search_suppliersentryActivity.this, js1.getString("content"), 0)
								.show();
						JSONArray body = js.getJSONArray("body");
						if (body.length() == 0) {
							Toast.makeText(Petrochina_search_suppliersentryActivity.this, "�Բ���û����������Ҫ��ѯ�Ĺ�Ӧ��", 0).show();
						} else {
							for (int i = 0; i < body.length(); i++) {
								JSONObject js2 = body.getJSONObject(i);
								data.add(new Petrochina_suppliers_entry(js2.getString("name"),
										"ά�޴���:" + js2.getString("count"), "��������:" + js2.getString("score"),
										js2.getString("id")));
							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Petrochina_search_suppliersentryActivity.this, js1.getString("content"), 0)
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
				System.out.println(error);
				Toast.makeText(Petrochina_search_suppliersentryActivity.this, "���Ѿ����ߣ������µ�¼", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				map.put("keyword", message.getText().toString());
				map.put("endDate", Myutil.get_time_ymd());
				map.put("beginDate", Myutil.get_month_time());
				if (getIntent().getBooleanExtra("state", false)) {
					map.put("direction1", "desc");
				} else {
					map.put("direction2", "desc");
				}
				return map;
			}
		};
		SysApplication.getHttpQueues().add(re);

	}
}
