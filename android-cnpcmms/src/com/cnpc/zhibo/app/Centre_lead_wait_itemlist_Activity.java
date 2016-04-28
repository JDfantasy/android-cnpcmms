package com.cnpc.zhibo.app;

/*
 * վ���˵������������
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.adapter.Item_centre_page_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.util.Myutil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Centre_lead_wait_itemlist_Activity extends MyActivity {
	private ListView list;
	private ImageView back;// ���ذ�ť
	private Item_centre_page_service_adapter adapter;
	private List<Centre_page_service> data;// ���ݼ���
	private RequestQueue mqueQueue;// �������
	/*
	 * �����б�
	 */
	private Handler noticehandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					get_waitapprvoeentry();// �������ݵķ���
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_lead_wait_itemlist_);
		set_view();// ���ý���ķ���
		get_waitapprvoeentry();// ��ȡ����
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler("site-approve", noticehandler);
	}

	
	// listview��
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_lead_wait_itemlist_Activity.this, Centre_See_examine_itemActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);

			startActivity(in);
			Myutil.set_activity_open(Centre_lead_wait_itemlist_Activity.this);
			// finish();

		}
	};

	// ���ý����ϵĿؼ�
	private void set_view() {
		mqueQueue = Volley.newRequestQueue(this);// ��ʼ���������
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listView_Centre_service_wait_itemlist_message);
		adapter = new Item_centre_page_service_adapter(this);
		data = new ArrayList<Centre_page_service>();
		list.setAdapter(adapter);
		set_title_text("��������");
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Centre_lead_wait_itemlist_Activity.this);
				break;

			default:
				break;
			}
		}
	};

	// ��ȡ��Ҫ�����ύ������ά�޵��б�ķ���
	private void get_waitapprvoeentry() {
		data.clear();
		adapter.getData().clear();
		String url = Myconstant.CENTRE_GET_WAITAPPROVEENTRY;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						adapter.getData().clear();
						data.clear();
						adapter.setinitializedata();
						Toast.makeText(Centre_lead_wait_itemlist_Activity.this, js1.getString("content"), 0).show();
						JSONArray jsa = js.getJSONArray("body");
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
										""));
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
										Myconstant.WEBPAGEPATHURL + jsphone.getString("source")));

							}
						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Centre_lead_wait_itemlist_Activity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_lead_wait_itemlist_Activity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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

		};
		mqueQueue.add(re);

	}

	// �ж�ά�޵���״̬�ķ���
	private String getjudgestatus(String name) {
		String str = "�޷�״̬";
		if (name.equals("approved")) {
			str = "����ͨ��";
		}
		if (name.equals("unsubmited")) {
			str = "�ȴ�����";
		}
		if (name.equals("unapproved")) {
			str = "��������";
		}
		return str;
	}
}