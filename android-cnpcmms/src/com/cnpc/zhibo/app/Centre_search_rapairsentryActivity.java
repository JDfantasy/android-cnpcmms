package com.cnpc.zhibo.app;

/*
 * վ�����������޵��б�Ľ���
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
import com.cnpc.zhibo.app.adapter.Item_centre_page_appove_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_search_rapairsentryActivity extends MyActivity {
	private ImageView back;// ���ذ�ť
	private ListView list;
	private Item_centre_page_appove_adapter adapter;// ������
	private TextView seek;// ������ť
	private EditText message;// ��Ϣ
	private List<Centre_page_approve> data;// ���ݼ���
	private TextView hint;// ��ʾ��Ϣ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_search_rapairsentry);
		setview();
	}

	// ���ý���ķ���
	private void setview() {
		set_title_text("����");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		seek = (TextView) findViewById(R.id.textview_centre_search_repairsentry_seek);// ������ť
		seek.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_centre_search_repairsentry_message);// ��Ϣ
		message.addTextChangedListener(tx_listener);

		hint = (TextView) findViewById(R.id.textView_centre_search_repairsenty_hint);
		hint.setText("�������޵��б�");
		list = (ListView) findViewById(R.id.listView_centre_search_repairsentry_entry);
		adapter = new Item_centre_page_appove_adapter(Centre_search_rapairsentryActivity.this);
		data = new ArrayList<Centre_page_approve>();
		list.setAdapter(adapter);
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	/*
	 * �����ļ���
	 */
	private TextWatcher tx_listener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (message.getText().toString().length() != 0) {
				hint.setVisibility(View.INVISIBLE);
				searchentry();

			} else {
				adapter.getData().clear();
				adapter.notifyDataSetChanged();
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_search_rapairsentryActivity.this, Centre_See_repairs_messageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("state", adapter.getData().get(position).state);
			startActivity(in);
			Myutil.set_activity_open(Centre_search_rapairsentryActivity.this);

		}
	};
	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_search_rapairsentryActivity.this);

				break;
			case R.id.textview_centre_search_repairsentry_seek:// ������ť

				adapter.getData().clear();
				adapter.notifyDataSetChanged();
				message.setText("");
				break;
			default:
				break;
			}

		}
	};

	// ��ȡ�б����ݵķ���
	private void searchentry() {

		String url = Myconstant.CENTRE_GET_SEARCHREPAIRSMONADENTRY;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						JSONArray jsa = js.getJSONArray("body");
						if (jsa.length() == 0) {
							Toast.makeText(Centre_search_rapairsentryActivity.this, "�Բ���û���ҵ���Ҫ��ѯ�ı��޵�", 0).show();
						} else {

							for (int i = 0; i < jsa.length(); i++) {
								JSONObject js2 = jsa.getJSONObject(i);
								JSONObject project = js2.getJSONObject("project");
								JSONArray repairOrderImages = js2.getJSONArray("repairOrderImages");
								if (repairOrderImages.length() == 0) {
									// û��ͼƬʱ
									data.add(new Centre_page_approve(js2.getString("sn"),
											js2// ���
													.getString("modifyDate"),
											Myutil.set_cutString(
													project.getString("name") + "  " + js2.getString("description")),
											js2.getString("status"),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), ""));
								} else {
									// ��ͼƬʱ
									JSONObject js3 = (JSONObject) repairOrderImages.get(0);
									data.add(new Centre_page_approve(js2.getString("sn"),
											js2// ���
													.getString("modifyDate"),
											Myutil.set_cutString(
													project.getString("name") + "  " + js2.getString("description")),
											getjudgestatus(js2.getString("status")),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), Myconstant.WEBPAGEPATHURL + js3.getString("source")));
								}

							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Centre_search_rapairsentryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_search_rapairsentryActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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

	// �жϱ��޵���״̬�ķ���
	private String getjudgestatus(String name) {
		if (name.equals("wait")) {
			return "�ȴ�ά��....";
		} else if (name.equals("completed")) {
			return "�����";

		} else {
			return "�ѹر�";
		}

	}

}
