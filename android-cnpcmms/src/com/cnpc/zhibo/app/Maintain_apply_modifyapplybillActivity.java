package com.cnpc.zhibo.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.Message.Body;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.cnpc.zhibo.app.adapter.Item_maintain_apply_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_apply_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextDirectionHeuristic;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Maintain_apply_modifyapplybillActivity extends MyActivity implements OnClickListener {


	private ImageView back;// ����
	private Button upload;// �ϴ�
	private TextView gasStationname;// ����վ����
	private int totalMoney;// �ܽ��
	private String remarkInfo;// ��ע��Ϣ
	private String id;//��������id
	
	//����ͨ��Ѱ�Ҳ��������½��
	private Item_maintain_apply_adapter adapter;// ������
	private List<Maintain_apply_item> list;// ����������
	private ListView listView;
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//����ˢ��
	// ���÷�
	private EditText chailvfeinname01, chailvfeinMoney01;
	private EditText chailvfeinname02, chailvfeinMoney02;
	private EditText chailvfeinname03, chailvfeinMoney03;
	private EditText chailvfeinname04, chailvfeinMoney04;
	// �ͷ�
	private EditText canfeinname01, canfeinMoney01;
	private EditText canfeinname02, canfeinMoney02;
	private EditText canfeinname03, canfeinMoney03;
	// �������
	private EditText peijianfeinname01, peijianfeinMoney01;
	private EditText peijianfeinname02, peijianfeinMoney02;
	private EditText peijianfeinname03, peijianfeinMoney03;
	// ��������
	private EditText qitafeinname01, qitafeinMoney01;
	private EditText qitafeinname02, qitafeinMoney02;
	private EditText qitafeinname03, qitafeinMoney03;
	// ���÷Ѽ���
	private List<EditText> chailvfeinnamelist = new ArrayList<EditText>();// ���Ƽ���
	private List<EditText> chailvfeinMoneylist = new ArrayList<EditText>();// ����
	// �ͷѼ���
	private List<EditText> canfeinnamelist = new ArrayList<EditText>();// ���Ƽ���
	private List<EditText> canfeinMoneylist = new ArrayList<EditText>();// ����
	// ����Ѽ���
	private List<EditText> peijianfeinnamelist = new ArrayList<EditText>();// ���Ƽ���
	private List<EditText> peijianfeinMoneylist = new ArrayList<EditText>();// ����
	// �������ü���
	private List<EditText> qitafeinnamelist = new ArrayList<EditText>();// ���Ƽ���
	private List<EditText> qitafeinMoneylist = new ArrayList<EditText>();// ����
	
	//�������ݼ���
	private List<String> travenamellist=new ArrayList<String>();//���÷����Ƽ���
	private List<String> mealsnamelist=new ArrayList<String>();//�ͷ����Ƽ���
	private List<String> sparenamelist=new ArrayList<String>();//��������Ƽ���
	private List<String> othernamelist=new ArrayList<String>();//�����������Ƽ���
	private List<String> travemoneyllist=new ArrayList<String>();//���÷ѽ���
	private List<String> mealsmoneylist=new ArrayList<String>();//�ͷѽ���
	private List<String> sparemoneylist=new ArrayList<String>();//����ѽ���
	private List<String> othermoneylist=new ArrayList<String>();//�������ý���

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_apply_modifyapplybill);
		set_title_text("�޸ı�����");
		// ��ö�����ȡid
		id=getIntent().getStringExtra("applybillid");
		setViews();
		getapplybilldata(getIntent().getStringExtra("applybillid"));//��ñ�����������
		Toast.makeText(getApplicationContext(), "�������ӱ��������ϵվ����Ӧ��", Toast.LENGTH_LONG).show();

	}

	// �ؼ���ʼ��
	private void setViews() {
		//�������Զ���������
//		View view=View.inflate(this, R.layout.fragment_maintain_apply, null);
//		mPullListView=(PullToRefreshListView) view.findViewById(R.id.maintainApply_fragment_applyListview);
//		listView = mPullListView.getRefreshableView();// ͨ����ܻ�ȡlistview
//		list=new ArrayList<Maintain_apply_item>();//���������ݼ���
//		adapter = new Item_maintain_apply_adapter(this);// ʵ����������
//		listView.setAdapter(adapter);
		
		gasStationname=(TextView) findViewById(R.id.maintain_modifyapplybill_gasStationname);
		gasStationname.setText(getIntent().getStringExtra("gasStationname"));
		// ���÷�
		chailvfeinname01 = (EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinname01);
		chailvfeinname02 = (EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinname02);
		chailvfeinname03 = (EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinname03);
		chailvfeinname04 = (EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinname04);
		chailvfeinnamelist.add(chailvfeinname01);
		chailvfeinnamelist.add(chailvfeinname02);
		chailvfeinnamelist.add(chailvfeinname03);
		chailvfeinnamelist.add(chailvfeinname04);
		chailvfeinMoney01=(EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinMoney01);
		chailvfeinMoney02=(EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinMoney02);
		chailvfeinMoney03=(EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinMoney03);
		chailvfeinMoney04=(EditText) findViewById(R.id.maintain_modifyapplybill_chailvfeinMoney04);
		chailvfeinMoneylist.add(chailvfeinMoney01);
		chailvfeinMoneylist.add(chailvfeinMoney02);
		chailvfeinMoneylist.add(chailvfeinMoney03);
		chailvfeinMoneylist.add(chailvfeinMoney04);
		//�ͷ�
		canfeinname01=(EditText) findViewById(R.id.maintain_modifyapplybill_canfeinname01);
		canfeinname02=(EditText) findViewById(R.id.maintain_modifyapplybill_canfeinname02);
		canfeinname03=(EditText) findViewById(R.id.maintain_modifyapplybill_canfeinname03);
		canfeinnamelist.add(canfeinname01);
		canfeinnamelist.add(canfeinname02);
		canfeinnamelist.add(canfeinname03);
		canfeinMoney01=(EditText) findViewById(R.id.maintain_modifyapplybill_canfeinMoney01);
		canfeinMoney02=(EditText) findViewById(R.id.maintain_modifyapplybill_canfeinMoney02);
		canfeinMoney03=(EditText) findViewById(R.id.maintain_modifyapplybill_canfeinMoney03);
		canfeinMoneylist.add(canfeinMoney01);
		canfeinMoneylist.add(canfeinMoney02);
		canfeinMoneylist.add(canfeinMoney03);
		//�����
		peijianfeinname01=(EditText) findViewById(R.id.maintain_modifyapplybill_peijianfeinname01);
		peijianfeinname02=(EditText) findViewById(R.id.maintain_modifyapplybill_peijianfeinname02);
		peijianfeinname03=(EditText) findViewById(R.id.maintain_modifyapplybill_peijianfeinname03);
		peijianfeinnamelist.add(peijianfeinname01);
		peijianfeinnamelist.add(peijianfeinname02);
		peijianfeinnamelist.add(peijianfeinname03);
		peijianfeinMoney01=(EditText) findViewById(R.id.maintain_modifyapplybill_peijianfeinMoney01);
		peijianfeinMoney02=(EditText) findViewById(R.id.maintain_modifyapplybill_peijianfeinMoney02);
		peijianfeinMoney03=(EditText) findViewById(R.id.maintain_modifyapplybill_peijianfeinMoney03);
		peijianfeinMoneylist.add(peijianfeinMoney01);
		peijianfeinMoneylist.add(peijianfeinMoney02);
		peijianfeinMoneylist.add(peijianfeinMoney03);
		//��������
		qitafeinname01=(EditText) findViewById(R.id.maintain_modifyapplybill_qitafeinname01);
		qitafeinname02=(EditText) findViewById(R.id.maintain_modifyapplybill_qitafeinname02);
		qitafeinname03=(EditText) findViewById(R.id.maintain_modifyapplybill_qitafeinname03);
		qitafeinnamelist.add(qitafeinname01);
		qitafeinnamelist.add(qitafeinname02);
		qitafeinnamelist.add(qitafeinname03);
		qitafeinMoney01=(EditText) findViewById(R.id.maintain_modifyapplybill_qitafeinMoney01);
		qitafeinMoney02=(EditText) findViewById(R.id.maintain_modifyapplybill_qitafeinMoney02);
		qitafeinMoney03=(EditText) findViewById(R.id.maintain_modifyapplybill_qitafeinMoney03);
		qitafeinMoneylist.add(qitafeinMoney01);
		qitafeinMoneylist.add(qitafeinMoney02);
		qitafeinMoneylist.add(qitafeinMoney03);
		// ����
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);// ����
		//�ϴ�
		upload = (Button) findViewById(R.id.maintain_modifyapplybill_upload);
		upload.setOnClickListener(this);
		
	}
	
	//��д��������
	public void fillinapplybillcontent(){
		//���÷�����
		if(travenamellist.size()!=0){
			for(int i=0;i<travenamellist.size();i++){
				chailvfeinnamelist.get(i).setText(travenamellist.get(i));
			}
		}
		//�ͷ�����
		if(mealsnamelist.size()!=0){
			for(int i=0;i<mealsnamelist.size();i++){
				canfeinnamelist.get(i).setText(mealsnamelist.get(i));
			}
		}
		//���������
		if(sparenamelist.size()!=0){
			for(int i=0;i<sparenamelist.size();i++){
				peijianfeinnamelist.get(i).setText(sparenamelist.get(i));
			}
		}
		//������������
		if(othernamelist.size()!=0){
			for(int i=0;i<othernamelist.size();i++){
				qitafeinnamelist.get(i).setText(othernamelist.get(i));
			}
		}
		//���÷ѽ��
		if(travemoneyllist.size()!=0){
			for(int i=0;i<travemoneyllist.size();i++){
				chailvfeinMoneylist.get(i).setText(travemoneyllist.get(i));
			}
		}
		//�ͷѽ��
		if(mealsmoneylist.size()!=0){
			for(int i=0;i<mealsmoneylist.size();i++){
				canfeinMoneylist.get(i).setText(mealsmoneylist.get(i));
			}
		}
		//����ѽ��
		if(sparemoneylist.size()!=0){
			for(int i=0;i<sparemoneylist.size();i++){
				peijianfeinMoneylist.get(i).setText(sparemoneylist.get(i));
			}
		}
		//������������
		if(othermoneylist.size()!=0){
			for(int i=0;i<othermoneylist.size();i++){
				qitafeinMoneylist.get(i).setText(othermoneylist.get(i));
			}
		}
		
	}
	
	//�������
	public String getlistdata(){
		JSONArray array = new JSONArray();
		//���÷�
		for(int i=0;i<chailvfeinnamelist.size();i++){
			String str1=chailvfeinnamelist.get(i).getText().toString();
			String str2=chailvfeinMoneylist.get(i).getText().toString();
			//if(str1!=null&&str1.length()!=0&&str2!=null&&str2.length()!=0){
			if(str2!=null&&str2.length()!=0){
				try {
					JSONObject obj = new JSONObject();
					int money=Integer.parseInt(str2);
					Log.i("tag","str21="+money);
					obj.put("amount", money);
					obj.put("name", str1);
					obj.put("order", i);
					obj.put("type", "travel");
					array.put(obj);
					totalMoney+=money;
					Log.i("tag","totalMoney1="+totalMoney);
					Log.i("tag","ary="+array.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		//�ͷ�
		for(int i=0;i<canfeinnamelist.size();i++){
			String str1=canfeinnamelist.get(i).getText().toString();
			String str2=canfeinMoneylist.get(i).getText().toString();
			//if(str1!=null&&str1.length()!=0&&str2!=null&&str2.length()!=0){
			if(str2!=null&&str2.length()!=0){
				try {
					JSONObject obj = new JSONObject();
					int money=Integer.parseInt(str2);
					Log.i("tag","str22="+money);
					obj.put("amount", money);
					obj.put("name", str1);
					obj.put("order", i);
					obj.put("type", "meals");
					array.put(obj);
					totalMoney+=money;
					Log.i("tag","totalMoney2="+totalMoney);
					Log.i("tag","ary="+array.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		//�������
		for(int i=0;i<peijianfeinnamelist.size();i++){
			String str1=peijianfeinnamelist.get(i).getText().toString();
			String str2=peijianfeinMoneylist.get(i).getText().toString();
			//if(str1!=null&&str1.length()!=0&&str2!=null&&str2.length()!=0){
			if(str2!=null&&str2.length()!=0){
				try {
					JSONObject obj = new JSONObject();
					int money=Integer.parseInt(str2);
					Log.i("tag","str23="+money);
					obj.put("amount", money);
					obj.put("name", str1);
					obj.put("order", i);
					obj.put("type", "spare");
					array.put(obj);
					totalMoney+=money;
					Log.i("tag","totalMoney3="+totalMoney);
					Log.i("tag","ary="+array.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		//��������
		for(int i=0;i<qitafeinnamelist.size();i++){
			String str1=qitafeinnamelist.get(i).getText().toString();
			String str2=qitafeinMoneylist.get(i).getText().toString();
			//if(str1!=null&&str1.length()!=0&&str2!=null&&str2.length()!=0){
			if(str2!=null&&str2.length()!=0){
				try {
					JSONObject obj = new JSONObject();
					int money=Integer.parseInt(str2);
					Log.i("tag","str24="+money);
					obj.put("amount", money);
					obj.put("name", str1);
					obj.put("order", i);
					obj.put("type", "other");
					array.put(obj);
					totalMoney+=money;
					
					Log.i("tag","totalMoney4="+totalMoney);
					Log.i("tag","ary="+array.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if(array.toString()!=null&&array.toString().length()!=0) return array.toString();
		else return null;
	}

	// �������
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// �����л������Ч��
			break;
		// �ϴ�
		case R.id.maintain_modifyapplybill_upload:
			upLoad();
			break;
		}

	}
	
	//��ñ�ע��Ϣ������
	public String getremarksInfo(){
		//String str=remarksInfoEditText.getText().toString();
		String str="����";
		try {
			str=URLEncoder.encode(str, "UTF-8");
			Log.i("tag", "str="+str);
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*private Handler handler;
	//���ø��µ�handler
	public void setListHandler(Handler handler){
		this.handler=handler;
	}*/

	//�ύ��������Ϣ
	public void upLoad(){
		getlistdata();
		String url=Myconstant.MAINTAIN_MODIFYAPPLYBILL+"id="+id+"&amount="+String.valueOf(totalMoney)+"&remark="+getremarksInfo();
		
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				try {
					System.out.println(response);
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(getApplicationContext(), "����ɹ�", Toast.LENGTH_SHORT).show();
						//getApplyProgress();
						//handler.sendEmptyMessage(100);
						SysApplication.getInstance().getapplyListHandler().sendEmptyMessage(100);
						//Log.i("tag", ""+Thread.currentThread().getId());//���߳� 
						startActivity(new Intent(Maintain_apply_modifyapplybillActivity.this,Maintain_HomeActivity.class));
					} else {
						Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), "����ʧ�ܣ�"+error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("Content-Type", "application/json");
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
			
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", id);
				map.put("amount", String.valueOf(totalMoney));
				map.put("remark", getremarksInfo());
				return map;
			}
			
			@Override
			public byte[] getBody() throws AuthFailureError {
				StringBuilder builder=new StringBuilder();
				builder.append(getlistdata());
				Log.i("tag", builder.toString());
				return builder.toString().getBytes();
			}
		};
		SysApplication.getHttpQueues().add(request);
		
	}

	
	//��ñ���������
	public void getapplybilldata(String id){
		String url=Myconstant.MAINTAIN_GETAPPLYBILLDETAIL+id;
		Log.i("tag", "MAINTAIN_GETAPPLYBILLDETAIL="+url);
		StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(getApplicationContext(), "�ҵ�����", Toast.LENGTH_SHORT).show();
						JSONObject js2=json.getJSONObject("body");
						//Log.i("tag", "body="+js2.toString());
						JSONArray array=js2.getJSONArray("details");
						//Log.i("tag", "array="+array.length());
						if(array.length()!=0){
							for(int i=0;i<array.length();i++){
								JSONObject js3=array.getJSONObject(i);
								String type=js3.getString("type");
								String name=js3.getString("name");
								int money=js3.getInt("amount");
								int order=js3.getInt("order");
								if(type.equals("travel")){//���÷�
									for(int j=0;j<4;j++){
										if(order==j){
											travenamellist.add(name);
											travemoneyllist.add(String.valueOf(money));
										}
									}
								}else if(type.equals("meals")){//�ͷ�
									for(int j=0;j<3;j++){
										if(order==j){
											mealsnamelist.add(name);
											mealsmoneylist.add(String.valueOf(money));
										}
									}
								}else if(type.equals("spare")){//�����
									for(int j=0;j<3;j++){
										if(order==j){
											sparenamelist.add(name);
											sparemoneylist.add(String.valueOf(money));
										}
									}
								}else if(type.equals("other")){//��������
									for(int j=0;j<3;j++){
										if(order==j){
											othernamelist.add(name);
											othermoneylist.add(String.valueOf(money));
										}
									}
								}
							}
						}
						//������������
						fillinapplybillcontent();
					} else {
						Toast.makeText(getApplicationContext(), "û���ҵ�����", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json�����쳣");
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), "����ʧ�ܣ�"+error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("Content-Type", "application/json");
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
		};
		
		SysApplication.getHttpQueues().add(request);
		
	}
	
	//��ñ���������   ***�޷�����***
	private void getApplyProgress() {
		String url=Myconstant.MAINTAIN_APPLY_PROGRESS;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
										js4.getString("name"), problem, modifyDate, js3.getString("amount"));//�ܽ��
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

		};
		SysApplication.getHttpQueues().add(re);

	}

	
}
