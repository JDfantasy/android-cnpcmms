package com.cnpc.zhibo.app;

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
import com.cnpc.zhibo.app.adapter.Item_supplier_notice_to_user_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_notice_to_worker;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商端的查看公告未读的界面
 */
public class Supplier_gonggao_seereadActivity extends MyActivity {
	private ImageView fanhui, send;// 返回，发送
	private TextView title, content, time;
	private ListView listView;
	private Item_supplier_notice_to_user_adapter adapter;
	private List<Supplier_notice_to_worker> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_gonggao_seeread);
		set_title_text("已发送公告");
		setview();
		get_notice_record();
	}

	/**
	 * 初始化控件
	 */
	private void setview() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		send = (ImageView) findViewById(R.id.imageView_myactity_you);
		send.setImageResource(R.drawable.send_notice1);
		send.setPadding(0, 0, 50, 0);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		send.setVisibility(View.VISIBLE);
		send.setOnClickListener(l);
		title = (TextView) findViewById(R.id.notice_title1);
		content = (TextView) findViewById(R.id.notice_content1);
		time = (TextView) findViewById(R.id.notice_time1);
		listView = (ListView) findViewById(R.id.listView1);
		adapter = new Item_supplier_notice_to_user_adapter(this);
		data = new ArrayList<Supplier_notice_to_worker>();
		listView.setAdapter(adapter);
		title.setText(getIntent().getStringExtra("title"));
		content.setText(getIntent().getStringExtra("content"));
		time.setText(getIntent().getStringExtra("time"));
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_gonggao_seereadActivity.this);
				break;

			case R.id.imageView_myactity_you:
				startActivity(new Intent(Supplier_gonggao_seereadActivity.this, Supplier_sendmessageActivity.class));
				Myutil.set_activity_open(Supplier_gonggao_seereadActivity.this);
				finish();
				break;

			default:
				break;
			}
		}
	};

	private void get_notice_record() {
		String url = Myconstant.SUPPLIER_RECORD_NOTICE + "?id=" + getIntent().getStringExtra("id");

		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						Toast.makeText(Supplier_gonggao_seereadActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("toUser");
							data.add(new Supplier_notice_to_worker(obj3.getString("id"), obj4.getString("id"),
									obj4.getString("name"), "", obj3.getString("status")));
						}
						adapter.addDataBottom(data);
					} else {
						Toast.makeText(Supplier_gonggao_seereadActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Supplier_gonggao_seereadActivity.this, "网络不好，请稍后再试", Toast.LENGTH_SHORT).show();

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
