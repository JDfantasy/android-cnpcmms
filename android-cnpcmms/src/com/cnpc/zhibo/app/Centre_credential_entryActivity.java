package com.cnpc.zhibo.app;

/*
 * 站长端证件管理列表的界面
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
import com.cnpc.zhibo.app.adapter.Item_centre_credenttials_intro_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_credentials_intro;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_credential_entryActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private ListView list;
	private TextView addition;// 添加按钮
	private Item_centre_credenttials_intro_adapter adapter;
	private List<Centre_credentials_intro> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_credential_management);
		setview();
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_credential_entryActivity.this, Centre_credential_itemmessageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("status", adapter.getData().get(position).status);
			startActivity(in);
			Myutil.set_activity_open(Centre_credential_entryActivity.this);
		}
	};

	private void setview() {
		set_title_text("证件管理");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listView_centre_credential_management);
		adapter = new Item_centre_credenttials_intro_adapter(this);
		data = new ArrayList<Centre_credentials_intro>();
		list.setAdapter(adapter);
		addition = (TextView) findViewById(R.id.textview_myacitivity_you);
		addition.setVisibility(View.VISIBLE);
		addition.setText("添加");
		addition.setOnClickListener(l);
		set_loadingdata();

	}

	// 加载数据的方法
	private void set_loadingdata() {

		String url = Myconstant.CENTRE_GET_CERTIFICATEENTRY;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONArray jsa = js.getJSONArray("body");
						for (int i = 0; i < jsa.length(); i++) {
							JSONObject js2 = (JSONObject) jsa.get(i);
							JSONObject certificateType = js2.getJSONObject("certificateType");
							JSONArray certificateImages = js2.getJSONArray("certificateImages");

							if (certificateImages.length() == 0) {
								data.add(new Centre_credentials_intro(certificateType.getString("name"),
										js2.getString("beginDate") + "——" + js2.getString("endDate"),
										js2.getString("accepter"), "", js2.getString("id"), js2.getString("status"),
										js2.getString("alarmDate"), js2.getString("keeper")));
							} else {
								JSONObject js3 = certificateImages.getJSONObject(0);
								data.add(new Centre_credentials_intro(certificateType.getString("name"),
										js2.getString("beginDate") + "——" + js2.getString("endDate"),
										js2.getString("accepter"), Myconstant.WEBPAGEPATHURL + js3.getString("source"),
										js2.getString("id"), js2.getString("status"), js2.getString("alarmDate"),
										js2.getString("keeper")));
							}
						}

						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Centre_credential_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_credential_entryActivity.this, "请求数据失败", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_credential_entryActivity.this);

				break;
			case R.id.textview_myacitivity_you:// 添加证件的按钮
				startActivity(new Intent(Centre_credential_entryActivity.this,
						Centre_credential_addcredentialActivity.class));
				Myutil.set_activity_open(Centre_credential_entryActivity.this);
				break;
			default:
				break;
			}
		}
	};

}
