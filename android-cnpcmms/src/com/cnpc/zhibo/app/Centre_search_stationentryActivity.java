package com.cnpc.zhibo.app;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/*
 * վ����ͨ���ؼ�����������վ�б�Ľ���
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
import com.cnpc.zhibo.app.adapter.Item_string_jyz_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_gasstation_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_search_stationentryActivity extends MyActivity {
	private ImageView back;// ������ť
	private ListView list;
	private List<Centre_gasstation_item> data;// ���ݼ���
	private Item_string_jyz_adapter adapter;
	private TextView seek;// ������ť
	private EditText message;// ��Ϣ
	private TextView hint;// ��ʾ��Ϣ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_search_stationentry);
		setview();// ���ý���ķ���
		list.setOnItemClickListener(listener);
	}

	// �б�ļ����¼�
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter.setcolor(position);// ����ѡ��item�ı�����ɫ
			Intent in = new Intent(Centre_search_stationentryActivity.this, Centre_NewsVerifyActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_search_stationentryActivity.this);
		}
	};

	// ���ý���ķ���
	private void setview() {
		set_title_text("����");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_centre_search_stationentry_message);// ��Ϣ
		seek = (TextView) findViewById(R.id.textview_centre_search_stationentry_seek);// ������ť
		seek.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_centre_search_stationentry_hint);// ��ʾ��Ϣ
		hint.setText("��������վ�б�");
		list = (ListView) findViewById(R.id.listView_centre_search_stationentry_entry);// listview
		adapter = new Item_string_jyz_adapter(Centre_search_stationentryActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_gasstation_item>();
		SysApplication.addActivity(Centre_search_stationentryActivity.this);
	}

	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_search_stationentryActivity.this);

				break;
			case R.id.textview_centre_search_stationentry_seek:// ������ť

				if (message.getText().toString().equals("")) {
					Toast.makeText(Centre_search_stationentryActivity.this, "��������ؼ��ʣ��ڽ�������", 0).show();
				} else {
					hint.setVisibility(View.INVISIBLE);
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
		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.CENTRE_GET_SEARCHSTATIONENTRY, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						data.clear();
						adapter.getData().clear();
						JSONArray js2 = js.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data.add(new Centre_gasstation_item(js3.getString("id"), js3.getString("name")));

						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Centre_search_stationentryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_search_stationentryActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);

	}

}
