package com.cnpc.zhibo.app;

//站长端设置地址的界面
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
	private ImageView back;// 返回键、
	private TextView tx_s, tx_q, tx_jyz;
	private ListView sheng, shi, qu, jyz;// 省、市、区
	private GridView gri;// 加油站
	private List<Centre_Addres_item> data_sheng, data_shi, data_qu;// 省、市、区的集合
	private List<Centre_gasstation_item> data_jyz;// 加油站的数据集合
	private Item_string_province_adapter adapter_sheng;// 省、市、区、加油站的适配器
	private Item_string_jyz_adapter adapter_jyz;
	private Item_string_city_adapter adapter_shi;
	private Item_string_area_adapter adapter_qu;
	private RequestQueue mqueQueue;// 请求对象
	private ImageView seek;// 搜索按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		SysApplication.addActivity(this);// 将这个activity添加到列表中
		setview();// 对界面上的控件进行设置的方法
		setdata_sheng();// 加载省的数据的方法

	}

	// 将界面的控件进行实例化和设置
	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);
		set_title_text("信息录入");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);// 返回按钮
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		sheng = (ListView) findViewById(R.id.listView_address_sheng);// 省的信息
		shi = (ListView) findViewById(R.id.listView_address_shi);// 市的信息
		qu = (ListView) findViewById(R.id.listView_address_qu);// 区的信息
		gri = (GridView) findViewById(R.id.gridView_address_jiayouzhan);// 加油站的信息
		adapter_sheng = new Item_string_province_adapter(this);// 省
		adapter_shi = new Item_string_city_adapter(this);// 市
		adapter_qu = new Item_string_area_adapter(this);// 区
		adapter_jyz = new Item_string_jyz_adapter(this);// 加油站
		sheng.setAdapter(adapter_sheng);// 省份
		shi.setAdapter(adapter_shi);// 市
		qu.setAdapter(adapter_qu);// 区
		gri.setAdapter(adapter_jyz);// 加油站
		sheng.setOnItemClickListener(listener1);
		shi.setOnItemClickListener(listener2);
		qu.setOnItemClickListener(listener3);
		gri.setOnItemClickListener(listener4);
		data_sheng = new ArrayList<Centre_Addres_item>();// 省
		data_shi = new ArrayList<Centre_Addres_item>();// 市
		data_qu = new ArrayList<Centre_Addres_item>();// 区
		data_jyz = new ArrayList<Centre_gasstation_item>();// 加油站
		tx_jyz = (TextView) findViewById(R.id.textview_address_jiayouzhan);// 加油站的标题
		tx_q = (TextView) findViewById(R.id.textview_address_qu);// 区的标题
		tx_s = (TextView) findViewById(R.id.textview_address_shi);// 市的标题
		seek = (ImageView) findViewById(R.id.imageView_centre_address_seek);
		seek.setOnClickListener(l);
		Toast.makeText(this, "想录入更多信息，请到PC端录入", 0).show();

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();// 关闭当前界面
				Myutil.set_activity_close(Centre_AddressActivity.this);
				break;
			case R.id.imageView_centre_address_seek:// 搜索按钮
				startActivity(new Intent(Centre_AddressActivity.this, Centre_search_stationentryActivity.class));
				Myutil.set_activity_open(Centre_AddressActivity.this);

				break;
			default:
				break;
			}

		}
	};
	// 省的listview的点击事件
	private AdapterView.OnItemClickListener listener1 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_sheng.setcolor(position);// 设置选中item背景颜色的方法
			gri.setVisibility(View.INVISIBLE);// 将加油站的gridview空间进行隐藏
			qu.setVisibility(View.INVISIBLE);// 将区的listview控件进行隐藏
			tx_s.setVisibility(View.VISIBLE);// 将市区标题进行显示
			tx_q.setVisibility(View.INVISIBLE);// 将区标题进行隐藏
			tx_jyz.setVisibility(View.INVISIBLE);// 将加油站标题进行隐藏
			adapter_shi.setcolor(-1);// 初始化选中的城市的颜色
			setdata_shi(adapter_sheng.getData().get(position).id);// 加载市信息的数据的方法

		}
	};
	// 市的listview的点击事件
	private AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_shi.setcolor(position);// 设置选中城市的颜色
			adapter_qu.setcolor(-1);// 初始化选中的区的颜色
			setdata_qu(adapter_shi.getData().get(position).id);// 加载区的数据的方法

		}
	};
	// 区的listview的点击事件
	private AdapterView.OnItemClickListener listener3 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_qu.setcolor(position);
			shi.setVisibility(View.INVISIBLE);
			qu.setVisibility(View.INVISIBLE);
			setdata_jyz(adapter_qu.getData().get(position).id);// 加载加油站的数据的方法

		}
	};
	// 加油站的gridview的点击事件
	private AdapterView.OnItemClickListener listener4 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter_jyz.setcolor(position);// 设置选中item的背景颜色
			Intent in = new Intent(Centre_AddressActivity.this, Centre_NewsVerifyActivity.class);
			in.putExtra("id", adapter_jyz.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_AddressActivity.this);
			// finish();

		}
	};

	// 加载省份数据的方法
	private void setdata_sheng() {

		String url = Myconstant.CENTRE_GETPROVINCE;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject js1 = response.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						System.out.println("省份数据加载成功");
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_sheng.add(new Centre_Addres_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_sheng.addDataBottom(data_sheng);

					} else {
						Toast.makeText(Centre_AddressActivity.this, "加载省份数据失败", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("获取省份数据失败" + error);

			}
		});
		mqueQueue.add(re);

	}

	// 加载市数据的方法
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
						System.out.println("城市数据加载成功");
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_shi.add(new Centre_Addres_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_shi.addDataBottom(data_shi);

					} else {
						Toast.makeText(Centre_AddressActivity.this, "加载城市数据失败", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("获取市数据失败" + error);

			}
		});
		mqueQueue.add(re);

	}

	// 加载区的数据的方法
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
						System.out.println("市区数据加载成功");
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_qu.add(new Centre_Addres_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_qu.addDataBottom(data_qu);

					} else {
						Toast.makeText(Centre_AddressActivity.this, "加载市区数据失败", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("获取市数据失败" + error);

			}
		});
		mqueQueue.add(re);

	}

	// 加载加油站数据的方法
	private void setdata_jyz(final String id) {
		System.out.println("城区的id是：" + id);
		tx_s.setVisibility(View.INVISIBLE);
		tx_q.setVisibility(View.INVISIBLE);
		tx_jyz.setVisibility(View.VISIBLE);
		gri.setVisibility(View.VISIBLE);
		data_jyz.clear();
		adapter_jyz.getData().clear();

		String url = Myconstant.CENTRE_GETSTATIONENTRE;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// 登录成功
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
				Toast.makeText(Centre_AddressActivity.this, "登录失败，账号不存在", 0).show();
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
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
