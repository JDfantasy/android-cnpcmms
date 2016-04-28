package com.cnpc.zhibo.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.cnpc.zhibo.app.adapter.Item_centre_supplier_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_message;
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

/**
 * ��Ӧ�̶˵�ѡ���Լ���˾�Ľ���
 */
public class Supplier_select_firmActivity extends MyActivity {
	private ImageView back;// ���ذ�ť
	private EditText message;// �������Ϣ
	private TextView seek;// ������ť
	private ListView list;// �б����
	private String keyword = "";// �洢�������Ϣ
	private List<Supplier_message> data;
	private Item_centre_supplier_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_select_firm);
		setview();// ���ý���ķ���
		list.setOnItemClickListener(listener);

	}

	// �б�ļ����¼�
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			supplier_selectprovider(adapter.getData().get(position).id);// ��Ӧ��ѡ��ѡ���Լ��Ĺ�˾

		}
	};

	// ���ý���ķ���
	private void setview() {
		set_title_text("��Ϣ����");
		seek = (TextView) findViewById(R.id.textview_supplier_select_firm_seek);
		seek.setOnClickListener(l);
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_supplier_select_firm_message);
		list = (ListView) findViewById(R.id.listView_supplier_select_firm_list);
		adapter = new Item_centre_supplier_adapter(Supplier_select_firmActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Supplier_message>();
		Toast.makeText(this, "��¼�������Ϣ���뵽PC��¼��", 0).show();
	}

	// �����ķ���
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Supplier_select_firmActivity.this);

				break;
			case R.id.textview_supplier_select_firm_seek:
				search_supplierlist();// ��ȡ��Ӧ�̵��б�

				break;
			default:
				break;
			}

		}
	};

	// ���ݹ�Ӧ������ģ����ѯ��Ӧ���б�
	private void search_supplierlist() {
		
		
		if (message.getText().toString().equals("")) {
			Toast.makeText(Supplier_select_firmActivity.this, "��������ؼ����ڲ�ѯ", 0).show();
			return;
		}

		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.SEARCH_SUPPLIERLIST, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(Supplier_select_firmActivity.this, "�Բ���û���ѵ�����Ҫ��ѯ�Ĺ�Ӧ�̹�˾���뼰ʱ���̨�ͷ���ϵ��", 0)
									.show();
						} else {
							data.clear();
							adapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								data.add(new Supplier_message(js3.getString("id"), js3.getString("name")));
							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Supplier_select_firmActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Supplier_select_firmActivity.this, "��������ʧ��", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
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

	// ��Ӧ��ѡ���Լ��Ĺ�˾�ķ���
	private void supplier_selectprovider(final String id) {

		String url = Myconstant.SUPPLIER_SELECTPROVIDER;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Supplier_select_firmActivity.this, "ѡ��ɹ�", 0).show();
						startActivity(new Intent(Supplier_select_firmActivity.this, Supplier_HomeActivity.class));
						Myutil.set_activity_open(Supplier_select_firmActivity.this);
						finish();
					} else {
						Toast.makeText(Supplier_select_firmActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Supplier_select_firmActivity.this, "��������ʧ��", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("providerId", id);
				return map;
			}
		};
		SysApplication.getHttpQueues().add(re);

	}

}
