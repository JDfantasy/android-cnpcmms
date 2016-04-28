package com.cnpc.zhibo.app;
//����ά�޵��Ľ��棬���ݵ���ģ����ѯ����

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
import com.cnpc.zhibo.app.adapter.Item_maintain_apply_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_apply_item;

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

public class Maintain_searchapplylist_Activity extends MyActivity implements OnClickListener {
	
	private EditText sv;//�����
	private ImageView backImageView;//����,������ť
	private TextView searchTextview,hint;//�����������ʾ����
	private String searchContent;//����������
	private ListView listView;
	//private int responseCode;//��Ӧ��
	private Item_maintain_apply_adapter adapter;// ������
	private List<Maintain_apply_item> list;// ����������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_searchapplylist);
		set_title_text("��   ��");
		//responseCode=getIntent().getIntExtra("responseCode",-1);
		setViews();
		
	}
	

	//�ؼ���ʼ��
	private void setViews() {
		hint=(TextView) findViewById(R.id.maintain_searchapplylist_repairsenty_hint);
		//����
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		
		//������ť
		searchTextview=(TextView) findViewById(R.id.maintain_searchapplylist_textview);
		searchTextview.setOnClickListener(this);
		//������
		sv=(EditText) findViewById(R.id.maintain_searchapplylist_content);
		
		list=new ArrayList<Maintain_apply_item>();
		adapter=new Item_maintain_apply_adapter(this);
		listView=(ListView) findViewById(R.id.maintain_searchapplylist_listview);
		adapter.setdate(list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listener);
	}
	
	private AdapterView.OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(Maintain_searchapplylist_Activity.this,Maintain_apply_DetailActivity.class);
			String indentId=list.get(position).id;
			intent.putExtra("indentId", indentId);
			startActivity(intent);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���˰�ť
		case R.id.imageView_myacitity_zuo:
			finish();
			break;
		// ������ť
		case R.id.maintain_searchapplylist_textview:
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
		String url = Myconstant.MAINTAINHOME_SEARCHAPPLYLIST;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getApplicationContext(), "û���ҵ�����", 0).show();
						} else {
							Toast.makeText(getApplicationContext(), "���ҵ�����", 0).show();
							list.clear();
							adapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								String modifyDate=js3.getString("modifyDate");
								JSONObject js4=js3.getJSONObject("fixOrder").getJSONObject("station");
								Maintain_apply_item m=new Maintain_apply_item(js3.getString("id"), js3.getString("sn"),
										js4.getString("name"), problem, modifyDate, js3.getString("amount"));
								m.applyState=js3.getString("status");
								Log.i("tag", "applystate="+m.applyState);
								JSONArray ary=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONArray("repairOrderImages");
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
						//���ݴ����ԭ��
						Toast.makeText(getApplicationContext(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json��ȡ���ݴ���");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				Log.i("tag", "error="+error);
				Toast.makeText(getApplicationContext(), "��������ʧ��"+error.getMessage(), 0).show();
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
