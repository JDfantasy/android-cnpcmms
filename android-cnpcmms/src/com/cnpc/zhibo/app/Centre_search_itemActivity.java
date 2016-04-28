package com.cnpc.zhibo.app;

//站长端报修项目的搜索界面
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
import com.cnpc.zhibo.app.adapter.Item_repairs_item_adapter;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_Repairs_item;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Centre_search_itemActivity extends MyActivity {
	private GridView list;// 列表对象
	private Item_repairs_item_adapter adapter;// 适配器
	private List<Centre_Repairs_item> data;// 集合
	private ImageView fanhui;// 返回按钮
	private RequestQueue mQueue;// 请求对象
	private EditText message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grabble);
		set_title_text("报修项目");
		mQueue = Volley.newRequestQueue(this);// 初始化请求对象
		list = (GridView) findViewById(R.id.gridView_centre_grabble);// 初始化listview
		data = new ArrayList<Centre_Repairs_item>();
		adapter = new Item_repairs_item_adapter(this);
		list.setAdapter(adapter);// 为列表设置适配器
		list.setSelector(new BitmapDrawable());
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_centre_search_grabble_message);

		message.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (message.getText().toString().length() != 0) {
					setdata();

				} else {
					adapter.getData().clear();
					adapter.notifyDataSetChanged();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		list.setOnItemClickListener(listener);
	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_search_itemActivity.this, Centre_ItemDetailsActivity.class);
			in.putExtra("name", adapter.getData().get(position).name);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_search_itemActivity.this);

		}
	};

	// 返回按钮的监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
			Myutil.set_activity_close(Centre_search_itemActivity.this);// 设置界面跳转的效果

		}

	};

	// 为listview加载站长端可以选择的项目列表
	private void setdata() {
		data = new ArrayList<Centre_Repairs_item>();

		String url = Myconstant.COMMONALITY_SEARCHREPAIRSITEM;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(Centre_search_itemActivity.this, "对不起没有查找到该项目", 0).show();
						} else {
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								data.add(new Centre_Repairs_item(js3.getString("id"), js3.getString("name")));
							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Centre_search_itemActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("azy", error.toString());
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

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("keyword", message.getText().toString());
				return map;
			}

		};

		mQueue.add(re);

	}

}
