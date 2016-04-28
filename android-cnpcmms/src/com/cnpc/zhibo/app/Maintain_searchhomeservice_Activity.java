package com.cnpc.zhibo.app;
//����ά�޵��Ľ��棬���ݵ���ģ����ѯ����

import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.cnpc.zhibo.app.adapter.Item_maintain_alloction_adapter;
import com.cnpc.zhibo.app.adapter.Item_maintain_serviceing_adapter;
import com.cnpc.zhibo.app.adapter.Item_maintainhome_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class Maintain_searchhomeservice_Activity extends MyActivity implements OnClickListener {
	
	private EditText sv;//�����
	private ImageView backImageView;//����,������ť
	private TextView searchTextview,hint;//�����������ʾ����
	private String searchContent;//����������
	private ListView listView;
	//private int responseCode;//��Ӧ��
	private Item_maintainhome_service_adapter adapter;
	private List<Maintain_startList_item> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_searchhomeservice);
		set_title_text("��   ��");
		//responseCode=getIntent().getIntExtra("responseCode",-1);
		setViews();
		
	}
	

	//�ؼ���ʼ��
	private void setViews() {
		hint=(TextView) findViewById(R.id.maintain_searchhomeservice_repairsenty_hint);
		//����
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		
		//������ť
		searchTextview=(TextView) findViewById(R.id.maintain_searchhomeservice_textview);
		searchTextview.setOnClickListener(this);
		//������
		sv=(EditText) findViewById(R.id.maintain_searchhomeservice_content);
		
		list=new ArrayList<Maintain_startList_item>();
		adapter=new Item_maintainhome_service_adapter(this);
		listView=(ListView) findViewById(R.id.maintain_searchhomeservice_listview);
		adapter.setdate(list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���˰�ť
		case R.id.imageView_myacitity_zuo:
			finish();
			break;
		// ������ť
		case R.id.maintain_searchhomeservice_textview:
			//��������������
			searchContent=sv.getText().toString();
			if(searchContent!=null&&searchContent.length()!=0){
					vagueSearchServiceForm(searchContent);
					hint.setVisibility(View.GONE);
			}else {
				Toast.makeText(getApplicationContext(), "�������ݲ���Ϊ�գ�", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	
	//���ݵ���ģ����ѯά�޵�
	public void vagueSearchServiceForm(final String searchContent){
		String url = Myconstant.MAINTAINHOME_SEARCHING;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						list.clear();
						adapter.getData().clear();
						adapter.notifyDataSetChanged();
						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getApplicationContext(), "û���ҵ�����", 0).show();
						} else {
							Toast.makeText(getApplicationContext(), "���ҵ�����", 0).show();
							
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("description");
								String modifyDate=js3.getString("modifyDate");
								String userName=js3.getJSONObject("reporter").getString("username");
								JSONObject js4=js3.getJSONObject("station");
								Maintain_startList_item m=new Maintain_startList_item(js3.getString("id"), js3.getString("sn"), 
										userName,js4.getString("name"), problem, js3.getString("modifyDate"),js3.getString("workStatus"));
								JSONArray ary=js3.getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								list.add(m);
								
							}
							/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							int count=0;
							for(int i=0;i<list.size();i++){
								try {
									Date d = df.parse(list.get(i).time);
									//double diff = System.currentTimeMillis()/1000000 - d.getTime()/1000000;
									double diff = new Date().getTime()/1000000.0 - d.getTime()/1000000.0;
									//1��=86 400 000���� 1Сʱ3.6 ��Сʱ1.8
									if(diff>=86.4*7){
										count++;
									}
								} catch (Exception e) {
								}
							}
							if(count==0) list.get(0).timeState="weekBefore";
							if(count!=0&&list.size()==count) list.get(0).timeState="weekAfter";
							if(count!=0&&list.size()>count){
								list.get(0).timeState="weekBefore";
								list.get(list.size()-count).timeState="weekAfter";
							} */
							//list.get ��ý���ά�޵�
							adapter.setdate(list);
						}

					} else {
						//���ݴ����ԭ��
						Toast.makeText(getApplicationContext(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json���ݽ����쳣");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				Log.i("tag", "error="+error);
				Toast.makeText(getApplicationContext(), "���������������ʧ��"+error.getMessage()+"�������µ�¼", 0).show();
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
				map.put("sn", searchContent);
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}
	
	

}
