package com.cnpc.zhibo.app;
//搜索维修单的界面，根据单号模糊查询定单

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
import com.cnpc.zhibo.app.adapter.Item_maintain_serviceing_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Maintain_search_Activity extends MyActivity implements OnClickListener {
	
	private EditText sv;//输入框
	private ImageView backImageView;//返回,搜索按钮
	private TextView searchTextview,hint;//点击搜索，提示文字
	private String searchContent;//搜索的内容
	private ListView listView;
	//private int responseCode;//响应码
	private Item_maintain_serviceing_adapter adapter;
	private List<Maintain_startList_item> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_search);
		set_title_text("搜   索");
		//responseCode=getIntent().getIntExtra("responseCode",-1);
		setViews();
		
	}
	

	//控件初始化
	private void setViews() {
		hint=(TextView) findViewById(R.id.maintain_search_repairsenty_hint);
		//返回
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		
		//搜索按钮
		searchTextview=(TextView) findViewById(R.id.maintain_search_textview);
		searchTextview.setOnClickListener(this);
		//搜索框
		sv=(EditText) findViewById(R.id.maintain_search_content);
		
		list=new ArrayList<Maintain_startList_item>();
		adapter=new Item_maintain_serviceing_adapter(this);
		listView=(ListView) findViewById(R.id.maintain_search_listview);
		adapter.setdate(list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(listener);
	}
	
	private AdapterView.OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(Maintain_search_Activity.this,Maintain_home_service_detail_Activity.class);
			String indentId=list.get(position).id;
			intent.putExtra("indentId", indentId);
			startActivity(intent);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 回退按钮
		case R.id.imageView_myacitity_zuo:
			finish();
			break;
		// 搜索按钮
		case R.id.maintain_search_textview:
			//获得搜索框的内容
			searchContent=sv.getText().toString();
			if(searchContent!=null&&searchContent.length()!=0){
					vagueSearchServiceForm(searchContent);
					hint.setVisibility(View.GONE);
			}else {
				Toast.makeText(getApplicationContext(), "输入内容不能为空！", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	
	//根据单号模糊查询维修单
	public void vagueSearchServiceForm(final String searchContent){
		String url = Myconstant.MAINTAIN_SEARCH;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						list.clear();
						adapter.getData().clear();
						adapter.notifyDataSetChanged();
						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getApplicationContext(), "没有找到数据", 0).show();
						} else {
							Toast.makeText(getApplicationContext(), "已找到数据", 0).show();
							
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("description");
								String modifyDate=js3.getString("modifyDate");
								String userName=js3.getJSONObject("reporter").getString("username");
								JSONObject js4=js3.getJSONObject("station");
								Maintain_startList_item m=new Maintain_startList_item(js3.getString("id"), js3.getString("sn"), 
										userName,js4.getString("name"), problem, js3.getString("modifyDate"),js3.getString("workStatus"));
								//星星的个数
								m.starNumber=js3.getString("score");
								JSONArray ary=js3.getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								list.add(m);
								
							}
							
							adapter.setdate(list);
						}

					} else {
						//数据错误的原因
						Log.i("tag", "content="+js1.getString("content"));
						Toast.makeText(getApplicationContext(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//请求失败的原因
				Log.i("tag", "error="+error);
				Toast.makeText(getApplicationContext(), "请求数据失败"+error.getMessage(), 0).show();
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
				map.put("sn", searchContent);
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
		
		
		
	}
	
	

}
