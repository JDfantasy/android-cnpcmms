package com.cnpc.zhibo.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * ��ʯ�Ͷ�����ά�޵��Ľ���
 */
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
import com.cnpc.zhibo.app.adapter.Item_petrochina_examine_message_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_examine_message;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_search_serviceentryActivity extends MyActivity {
	private ImageView back;// ������ť
	private ListView list;
	private Item_petrochina_examine_message_adapter adapter;
	private List<Petrochina_examine_message> data;
	private TextView seek;// ������ť
	private EditText message;// ��Ϣ
	private TextView hint;// ��ʾ��Ϣ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_search_serviceentry);
		setview();// ���ý���ķ���
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// �б�ļ����¼�
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent();
			in.setClass(Petrochina_search_serviceentryActivity.this, Petrochina_examine_messageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("workStatus", adapter.getData().get(position).workStatus);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_search_serviceentryActivity.this);
		}
	};

	// ���ý���ķ���
	private void setview() {
		set_title_text("����");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_petrochina_search_serviceentry_message);// ��Ϣ
		seek = (TextView) findViewById(R.id.textview_petrochina_search_serviceentry_seek);// ������ť
		seek.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_petrochina_search_servicesentry_hint);// ��ʾ��Ϣ
		hint.setText("����ά�޵��б�");
		list = (ListView) findViewById(R.id.listView_petrochina_search_serviceentry_entry);// listview
		adapter = new Item_petrochina_examine_message_adapter(this);
		list.setAdapter(adapter);
		data = new ArrayList<Petrochina_examine_message>();
	}

	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Petrochina_search_serviceentryActivity.this);

				break;
			case R.id.textview_petrochina_search_serviceentry_seek:// ������ť
				hint.setVisibility(View.INVISIBLE);
				if (message.getText().toString().equals("")) {
					Toast.makeText(Petrochina_search_serviceentryActivity.this, "��������ؼ��ʣ��ڽ�������", 0).show();
				} else {
					searchexamineentry();
				}

				break;
			default:
				break;
			}

		}
	};

	// ��ʯ�Ͷ�,���ݵ���ģ����ѯά�޵�
	private void searchexamineentry() {
		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.PETROCHINA_SEARCHEXAMINEENTRY, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_search_serviceentryActivity.this, js1.getString("content"), 0).show();
						data.clear();
						adapter.getData().clear();
						JSONArray jsa = js.getJSONArray("body");
						for (int i = 0; i < jsa.length(); i++) {
							JSONObject js2 = jsa.getJSONObject(i);
							JSONObject station = js2.getJSONObject("station");// ����վ��Ϣ
							JSONObject repairOrder = js2.getJSONObject("repairOrder");
							JSONObject project = repairOrder.getJSONObject("project");
							JSONArray repairOrderImages = repairOrder.getJSONArray("repairOrderImages");

							if (repairOrderImages.length() == 0) {
								data.add(new Petrochina_examine_message(js2.getString("sn"), station.getString("name"),
										project.getString("name") + " " + repairOrder.getString("name"), "900Ԫ", "",
										js2.getString("id"), js2.getString("createDate"), js2.getString("workStatus")));
							} else {
								JSONObject phone = (JSONObject) repairOrderImages.get(0);
								data.add(new Petrochina_examine_message(js2.getString("sn"), station.getString("name"),
										project.getString("name") + " " + repairOrder.getString("name"), "900Ԫ",
										Myconstant.WEBPAGEPATHURL + phone.getString("source"), js2.getString("id"),
										js2.getString("createDate"), js2.getString("workStatus")));
							}

						}
						adapter.addDataBottom(data);
					} else {
						Toast.makeText(Petrochina_search_serviceentryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_search_serviceentryActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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
				map.put("sn", message.getText().toString());
				return map;
			}
		};
		SysApplication.getHttpQueues().add(re);
	}

}
