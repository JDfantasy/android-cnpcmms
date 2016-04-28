package com.cnpc.zhibo.app;

/*
 * վ���˲鿴ά�޵����б�Ľ���
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
import com.cnpc.zhibo.app.adapter.Item_centre_page_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
/*
 * վ���˵ײ��˵���ά�ޡ�������������������򿪵Ľ���
 */
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_search_serviceentryActivity extends MyActivity {
	private ImageView back;// ������ť
	private ListView list;
	private List<Centre_page_service> data;// ���ݼ���
	private Item_centre_page_service_adapter adapter;
	private TextView seek;// ������ť
	private EditText message;// ��Ϣ
	private TextView hint;// ��ʾ��Ϣ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_page_search);
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
			if (getIntent().getStringExtra("activityname").equals("repairs")) {
				// �򿪲鿴ά�޵�����Ľ���
				in.setClass(Centre_search_serviceentryActivity.this, Centre_page_maintainActivity.class);

			} else {
				// �򿪲鿴����������Ľ���
				in.setClass(Centre_search_serviceentryActivity.this, Centre_page_examine_Activity.class);
				in.putExtra("state", adapter.getData().get(position).WorkStatus);
			}

			in.putExtra("id", adapter.getData().get(position).id);

			startActivity(in);
			Myutil.set_activity_open(Centre_search_serviceentryActivity.this);

		}
	};

	// ���ý���ķ���
	private void setview() {
		set_title_text("����");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_centre_search_serviceentry_message);// ��Ϣ
		seek = (TextView) findViewById(R.id.textview_centre_search_serviceentry_seek);// ������ť
		seek.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_centre_search_servicesentry_hint);// ��ʾ��Ϣ
		hint.setText("����ά�޵��б�");
		list = (ListView) findViewById(R.id.listView_centre_search_serviceentry_entry);// listview
		adapter = new Item_centre_page_service_adapter(this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_page_service>();
		adapter.servicestate();
	}

	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_search_serviceentryActivity.this);

				break;
			case R.id.textview_centre_search_serviceentry_seek:// ������ť
				hint.setVisibility(View.INVISIBLE);
				if (message.getText().toString().equals("")) {
					Toast.makeText(Centre_search_serviceentryActivity.this, "��������ؼ��ʣ��ڽ�������", 0).show();
				} else {
					searchentry();
				}

				break;
			default:
				break;
			}

		}
	};

	// ��ȡ�б����ݵķ���
	private void searchentry() {

		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.CENTRE_GET_SEARCHSERVICEMONADENTRY , new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						Toast.makeText(Centre_search_serviceentryActivity.this, js1.getString("content"), 0).show();
						JSONArray jsa = js.getJSONArray("body");
						if (jsa.length() == 0) {
							Toast.makeText(Centre_search_serviceentryActivity.this, "�Բ���û���ҵ���Ҫ��ѯ��ά�޵�", 0).show();
						} else {

							adapter.getData().clear();
							data.clear();
							for (int i = 0; i < jsa.length(); i++) {

								JSONObject js2 = jsa.getJSONObject(i);
								JSONObject js4 = js2.getJSONObject("fixter");
								JSONObject js3 = js2.getJSONObject("repairOrder").getJSONObject("project");
								JSONObject repairOrder = js2.getJSONObject("repairOrder");
								JSONArray repairOrderImages = repairOrder.getJSONArray("repairOrderImages");
								if (repairOrderImages.length() == 0) {
									// û��ͼƬʱ
									data.add(new Centre_page_service(js2.getString("sn"),
											js2// ���
													.getString("createDate"),
											Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
											js2.getString("workStatus"),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
											"", js2.getString("score")));
								} else {
									// ��ͼƬʱ
									JSONObject jsphone = (JSONObject) repairOrderImages.get(0);

									data.add(new Centre_page_service(js2.getString("sn"),
											js2// ���
													.getString("createDate"),
											Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
											js2.getString("workStatus"),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
											Myconstant.WEBPAGEPATHURL + jsphone.getString("source"),
											js2.getString("score")));

								}

							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Centre_search_serviceentryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_search_serviceentryActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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
