package com.cnpc.zhibo.app;

//վ�������õ�ַ�Ľ���
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.adapter.Item_string_area_adapter;
import com.cnpc.zhibo.app.adapter.Item_string_city_adapter;
import com.cnpc.zhibo.app.adapter.Item_string_jyz_adapter;
import com.cnpc.zhibo.app.adapter.Item_string_province_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_Addres_item;
import com.cnpc.zhibo.app.entity.Centre_gasstation_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_AddressActivity extends MyActivity {
	private ImageView back;// ���ؼ���
	private TextView tx_s, tx_q, tx_jyz;
	private ListView sheng, shi, qu, jyz;// ʡ���С���
	private GridView gri;// ����վ
	private List<Centre_Addres_item> data_sheng, data_shi, data_qu;// ʡ���С����ļ���
	private List<Centre_gasstation_item> data_jyz;// ����վ�����ݼ���
	private Item_string_province_adapter adapter_sheng;// ʡ���С���������վ��������
	private Item_string_jyz_adapter adapter_jyz;
	private Item_string_city_adapter adapter_shi;
	private Item_string_area_adapter adapter_qu;
	private RequestQueue mqueQueue;// �������
	private ImageView seek;// ������ť

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		SysApplication.addActivity(this);// �����activity��ӵ��б���
		setview();// �Խ����ϵĿؼ��������õķ���
		setdata_sheng();// ����ʡ�����ݵķ���

	}

	// ������Ŀؼ�����ʵ����������
	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);
		set_title_text("��Ϣ¼��");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);// ���ذ�ť
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		sheng = (ListView) findViewById(R.id.listView_address_sheng);// ʡ����Ϣ
		shi = (ListView) findViewById(R.id.listView_address_shi);// �е���Ϣ
		qu = (ListView) findViewById(R.id.listView_address_qu);// ������Ϣ
		gri = (GridView) findViewById(R.id.gridView_address_jiayouzhan);// ����վ����Ϣ
		adapter_sheng = new Item_string_province_adapter(this);// ʡ
		adapter_shi = new Item_string_city_adapter(this);// ��
		adapter_qu = new Item_string_area_adapter(this);// ��
		adapter_jyz = new Item_string_jyz_adapter(this);// ����վ
		sheng.setAdapter(adapter_sheng);// ʡ��
		shi.setAdapter(adapter_shi);// ��
		qu.setAdapter(adapter_qu);// ��
		gri.setAdapter(adapter_jyz);// ����վ
		sheng.setOnItemClickListener(listener1);
		shi.setOnItemClickListener(listener2);
		qu.setOnItemClickListener(listener3);
		gri.setOnItemClickListener(listener4);
		data_sheng = new ArrayList<Centre_Addres_item>();// ʡ
		data_shi = new ArrayList<Centre_Addres_item>();// ��
		data_qu = new ArrayList<Centre_Addres_item>();// ��
		data_jyz = new ArrayList<Centre_gasstation_item>();// ����վ
		tx_jyz = (TextView) findViewById(R.id.textview_address_jiayouzhan);// ����վ�ı���
		tx_q = (TextView) findViewById(R.id.textview_address_qu);// ���ı���
		tx_s = (TextView) findViewById(R.id.textview_address_shi);// �еı���
		seek = (ImageView) findViewById(R.id.imageView_centre_address_seek);
		seek.setOnClickListener(l);
		Toast.makeText(this, "��¼�������Ϣ���뵽PC��¼��", 0).show();

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();// �رյ�ǰ����
				Myutil.set_activity_close(Centre_AddressActivity.this);
				break;
			case R.id.imageView_centre_address_seek:// ������ť
				startActivity(new Intent(Centre_AddressActivity.this, Centre_search_stationentryActivity.class));
				Myutil.set_activity_open(Centre_AddressActivity.this);

				break;
			default:
				break;
			}

		}
	};
	// ʡ��listview�ĵ���¼�
	private AdapterView.OnItemClickListener listener1 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_sheng.setcolor(position);// ����ѡ��item������ɫ�ķ���
			gri.setVisibility(View.INVISIBLE);// ������վ��gridview�ռ��������
			qu.setVisibility(View.INVISIBLE);// ������listview�ؼ���������
			tx_s.setVisibility(View.VISIBLE);// ���������������ʾ
			tx_q.setVisibility(View.INVISIBLE);// ���������������
			tx_jyz.setVisibility(View.INVISIBLE);// ������վ�����������
			adapter_shi.setcolor(-1);// ��ʼ��ѡ�еĳ��е���ɫ
			setdata_shi(adapter_sheng.getData().get(position).id);// ��������Ϣ�����ݵķ���

		}
	};
	// �е�listview�ĵ���¼�
	private AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_shi.setcolor(position);// ����ѡ�г��е���ɫ
			adapter_qu.setcolor(-1);// ��ʼ��ѡ�е�������ɫ
			setdata_qu(adapter_shi.getData().get(position).id);// �����������ݵķ���

		}
	};
	// ����listview�ĵ���¼�
	private AdapterView.OnItemClickListener listener3 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_qu.setcolor(position);
			shi.setVisibility(View.INVISIBLE);
			qu.setVisibility(View.INVISIBLE);
			setdata_jyz(adapter_qu.getData().get(position).id);// ���ؼ���վ�����ݵķ���

		}
	};
	// ����վ��gridview�ĵ���¼�
	private AdapterView.OnItemClickListener listener4 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_jyz.setcolor(position);// ����ѡ��item�ı�����ɫ
			Intent in = new Intent(Centre_AddressActivity.this, Centre_NewsVerifyActivity.class);
			in.putExtra("id", adapter_jyz.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_AddressActivity.this);
			// finish();

		}
	};

	// ����ʡ�����ݵķ���
	private void setdata_sheng() {

		String url = Myconstant.CENTRE_GETPROVINCE;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject js1 = response.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						System.out.println("ʡ�����ݼ��سɹ�");
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_sheng.add(new Centre_Addres_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_sheng.addDataBottom(data_sheng);

					} else {
						Toast.makeText(Centre_AddressActivity.this, "����ʡ������ʧ��", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("��ȡʡ������ʧ��" + error);

			}
		});
		mqueQueue.add(re);

	}

	// ���������ݵķ���
	private void setdata_shi(final String id) {
		shi.setVisibility(View.VISIBLE);
		adapter_shi.getData().clear();
		data_shi.clear();

		String url = Myconstant.CENTRE_GETCITY + id;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {

					JSONObject js1 = response.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						System.out.println("�������ݼ��سɹ�");
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_shi.add(new Centre_Addres_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_shi.addDataBottom(data_shi);

					} else {
						Toast.makeText(Centre_AddressActivity.this, "���س�������ʧ��", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("��ȡ������ʧ��" + error);

			}
		});
		mqueQueue.add(re);

	}

	// �����������ݵķ���
	private void setdata_qu(String id) {
		tx_s.setVisibility(View.VISIBLE);
		tx_q.setVisibility(View.VISIBLE);
		tx_jyz.setVisibility(View.INVISIBLE);
		qu.setVisibility(View.VISIBLE);
		adapter_qu.getData().clear();
		data_qu.clear();
		String url = Myconstant.CENTRE_GETAREA + id;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject js1 = response.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						System.out.println("�������ݼ��سɹ�");
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_qu.add(new Centre_Addres_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_qu.addDataBottom(data_qu);

					} else {
						Toast.makeText(Centre_AddressActivity.this, "������������ʧ��", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("��ȡ������ʧ��" + error);

			}
		});
		mqueQueue.add(re);

	}

	// ���ؼ���վ���ݵķ���
	private void setdata_jyz(final String id) {
		System.out.println("������id�ǣ�" + id);
		tx_s.setVisibility(View.INVISIBLE);
		tx_q.setVisibility(View.INVISIBLE);
		tx_jyz.setVisibility(View.VISIBLE);
		gri.setVisibility(View.VISIBLE);
		data_jyz.clear();
		adapter_jyz.getData().clear();

		String url = Myconstant.CENTRE_GETSTATIONENTRE;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// ��¼�ɹ�
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONArray js2 = json.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_jyz.add(new Centre_gasstation_item(js3.getString("id"), js3.getString("name")));

						}
						adapter_jyz.addDataBottom(data_jyz);
					} else {
						Toast.makeText(Centre_AddressActivity.this, js1.getString("content"), 0).show();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Centre_AddressActivity.this, "��¼ʧ�ܣ��˺Ų�����", 0).show();
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				map.put("accept", "application/json");
				map.put("api_key", Myconstant.token);
				return map;

			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("areaId", id);
				return map;
			}

		};
		mqueQueue.add(re);

	}

}
