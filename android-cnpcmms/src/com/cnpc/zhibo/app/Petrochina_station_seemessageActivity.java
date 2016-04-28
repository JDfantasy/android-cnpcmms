package com.cnpc.zhibo.app;

/*
 * ��ʯ�Ͷ˵ļ���վ��Ϣģ��鿴����վ������Ϣ�Ľ���
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.adapter.Item_centre_news_verify_adapter;
import com.cnpc.zhibo.app.adapter.Item_petrochina_station_seerepairsitem;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_newsverify_item;
import com.cnpc.zhibo.app.entity.Petrochina_station_seerepairsitem;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_station_seemessageActivity extends MyActivity {
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
		setContentView(R.layout.activity_petrochina_station_seemessage);
		setview();
		list.setOnItemClickListener(listener);
	}

	// listview���е���¼��ļ���
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Petrochina_station_seemessageActivity.this,
					Petrochina_see_station_repairsmessageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_station_seemessageActivity.this);

		}
	};

	// ���ý����ϵĿؼ��ķ���
	private void setview() {
		set_title_text("����վ��Ϣ");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		stationmonads = (TextView) findViewById(R.id.textview_petrochina_station_seemessage_stationmonads);
		stationmessage = (TextView) findViewById(R.id.textview_petrochina_station_seemessage_stationmessage);
		web = (WebView) findViewById(R.id.webview_petrochina_station_seemessage);
		line = (LinearLayout) findViewById(R.id.linearlayout_petrochina_station_seemessage);
		web.loadUrl(Myconstant.STATIONMESSAGE + getIntent().getStringExtra("id"));

		stationmonads.setOnClickListener(l);
		stationmessage.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listview_petrochina_station_seemessage_stationmonads);
		adapter = new Item_petrochina_station_seerepairsitem(this);
		list.setAdapter(adapter);
		data = new ArrayList<Petrochina_station_seerepairsitem>();
		set_entryaddddata();// �������ݵķ���
		
		stationmessage.setTextColor(getResources().getColor(R.color.blue));
		stationmessage.setText(Html.fromHtml("<u>" + "����վ��Ϣ" + "</u>"));
		stationmonads.setTextColor(getResources().getColor(R.color.zitiyanse5));
		stationmonads.setText("ά�޼�¼");
		
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ؼ�
				finish();
				Myutil.set_activity_close(Petrochina_station_seemessageActivity.this);

				break;
			case R.id.textview_petrochina_station_seemessage_stationmonads:// �鿴����վ��ά����Ŀ���б�
				line.setVisibility(View.VISIBLE);
				web.setVisibility(View.INVISIBLE);
				stationmonads.setTextColor(getResources().getColor(R.color.blue));
				stationmonads.setText(Html.fromHtml("<u>" + "ά�޼�¼" + "</u>"));
				stationmessage.setTextColor(getResources().getColor(R.color.zitiyanse5));
				stationmessage.setText("����վ��Ϣ");
				break;
			case R.id.textview_petrochina_station_seemessage_stationmessage:// �鿴����վ��Ϣ

				stationmessage.setTextColor(getResources().getColor(R.color.blue));
				stationmessage.setText(Html.fromHtml("<u>" + "����վ��Ϣ" + "</u>"));
				stationmonads.setTextColor(getResources().getColor(R.color.zitiyanse5));
				stationmonads.setText("ά�޼�¼");
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
		String url = Myconstant.PETROCHINA_GET_STATIONNTRYFIXORDER + "id=" + getIntent().getStringExtra("id")
				+ "&beginDate=" + Myutil.get_month_time() + "&endDate=" + Myutil.get_time_ymd();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// ��Ӧ������
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_station_seemessageActivity.this, js1.getString("content"), 0).show();
						JSONArray body = js.getJSONArray("body");
						for (int i = 0; i < body.length(); i++) {
							JSONObject js2 = body.getJSONObject(i);
							JSONObject repairOrder = js2.getJSONObject("repairOrder");
							JSONObject project = repairOrder.getJSONObject("project");
							JSONObject provider = repairOrder.getJSONObject("provider");
							JSONObject station = repairOrder.getJSONObject("station");
							JSONArray repairOrderImages = repairOrder.getJSONArray("repairOrderImages");

							if (repairOrderImages.length() == 0) {
								data.add(new Petrochina_station_seerepairsitem(js2.getString("sn"),
										js2.getString("completeDate"),
										project.getString("name") + " " + repairOrder.getString("name"),
										Myutil.getjudgestatus(js2.getString("workStatus")),
										Myutil.get_delta_t(js2.getString("completeDate"),
												provider.getString("createDate")),
										js2.getString("amount") + "Ԫ", js2.getString("id"), station.getString("name"),
										""));
							} else {
								JSONObject phone = (JSONObject) repairOrderImages.get(0);
								data.add(new Petrochina_station_seerepairsitem(js2.getString("sn"),
										js2.getString("completeDate"),
										project.getString("name") + " " + repairOrder.getString("name"),
										Myutil.getjudgestatus(js2.getString("workStatus")),
										Myutil.get_delta_t(js2.getString("completeDate"),
												provider.getString("createDate")),
										js2.getString("amount") + "Ԫ", js2.getString("id"), station.getString("name"),
										Myconstant.WEBPAGEPATHURL + phone.getString("source")));
							}

						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Petrochina_station_seemessageActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_station_seemessageActivity.this, "���Ѿ����ߣ������µ�¼", 0).show();
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
		SysApplication.getHttpQueues().add(re);

	}

}
