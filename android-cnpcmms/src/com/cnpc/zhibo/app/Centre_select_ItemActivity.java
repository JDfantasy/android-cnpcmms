package com.cnpc.zhibo.app;

//站长端的保修项目中选择要报修项目的界面
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.adapter.Item_repairs_item_adapter;
import com.cnpc.zhibo.app.adapter.Item_repairs_select_adapter;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_Repairs_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_select_ItemActivity extends MyActivity {
	private ImageView fanhui,grabble;// 返回、搜索按钮
	private GridView items;// 可选的保修项目、已选的保修项目
	private Item_repairs_item_adapter adapter_i;// 要选择的项目的adapter
	private List<Centre_Repairs_item> data_i;// 要选择的项目的集合
	private RequestQueue mQueue;// 请求对象
	private boolean ck = false;// 判断用户是否选择项目

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_item);
		setview();// 设置界面上控件的方法
		items.setOnItemClickListener(listener_i);// 为可选的gridview设置点击监听事件
		//items.setDividerHeight(0);
		items.setSelector(new BitmapDrawable());
	}

	// 可选列表的点击事件
	private AdapterView.OnItemClickListener listener_i = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			Intent in = new Intent(Centre_select_ItemActivity.this, Centre_ItemDetailsActivity.class);
			
			in.putExtra("name", adapter_i.getData().get(position).name);
			in.putExtra("id",adapter_i.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_select_ItemActivity.this);
			
		}
	};

	

	private void setview() {
		mQueue = Volley.newRequestQueue(this);
		set_title_text("报修项目");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		grabble = (ImageView) findViewById(R.id.imageView_myactity_you);// 搜索按钮
		grabble.setVisibility(View.VISIBLE);
		grabble.setImageResource(R.drawable.search_icon);
		grabble.setOnClickListener(l);
		items = (GridView) findViewById(R.id.gridView_centre_item_items);
		adapter_i = new Item_repairs_item_adapter(Centre_select_ItemActivity.this);// gridview实例化
		items.setAdapter(adapter_i);// 为gridview加载适配器
		setdata();// 加载项目的集合

	}

	// 将所有可以保修的项目进行加载
	private void setdata() {
		Toast.makeText(this, "正在加载数据请稍后....", 0).show();// 提醒用户正在加载数据
		data_i = new ArrayList<Centre_Repairs_item>();
		String url = Myconstant.CENTRE_GETREPAIRSITEM;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject js1 = response.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_i.add(new Centre_Repairs_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_i.addDataBottom(data_i);
					} else {
						Toast.makeText(Centre_select_ItemActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("获取数据错误");

			}
		});
		mQueue.add(re);

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_select_ItemActivity.this);

				break;
			
			case R.id.imageView_myactity_you:// 搜索按钮
				startActivity(new Intent(Centre_select_ItemActivity.this, Centre_search_itemActivity.class));// 跳转到搜索界面
				Myutil.set_activity_open(Centre_select_ItemActivity.this);// 设置界面切换的效果
				break;

			default:
				break;
			}
		}
	};

	

}
