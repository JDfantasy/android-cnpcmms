package com.cnpc.zhibo.app;
//�½�������
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.fragment.Fragment_maintain_apply;
//�½�����������
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Maintain_apply_newapplylistActivity extends MyActivity implements OnClickListener {

	private ImageView back;// ����
	private Button upload;// �ϴ�
	private TextView gasStationname;// ����վ����
	private int totalMoney;// �ܽ��
	private String remarkInfo;// ��ע��Ϣ
	private String id;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_apply_newapplylist);
		setViews();
		set_title_text("�½�������");
		// ��ö�����ȡid
		id=getIntent().getStringExtra("id");
		Toast.makeText(getApplicationContext(), "�������ӱ��������ϵվ����Ӧ��", Toast.LENGTH_LONG).show();

	}

	// �ؼ���ʼ��
	private void setViews() {
		gasStationname=(TextView) findViewById(R.id.maintain_newapplylist_gasStationname);
		gasStationname.setText(getIntent().getStringExtra("gasStationname"));
		// ���÷�
		chailvfeinname01 = (EditText) findViewById(R.id.maintain_newapplylist_chailvfeinname01);
		chailvfeinname02 = (EditText) findViewById(R.id.maintain_newapplylist_chailvfeinname02);
		chailvfeinname03 = (EditText) findViewById(R.id.maintain_newapplylist_chailvfeinname03);
		chailvfeinname04 = (EditText) findViewById(R.id.maintain_newapplylist_chailvfeinname04);
		chailvfeinnamelist.add(chailvfeinname01);
		chailvfeinnamelist.add(chailvfeinname02);
		chailvfeinnamelist.add(chailvfeinname03);
		chailvfeinnamelist.add(chailvfeinname04);
		chailvfeinMoney01=(EditText) findViewById(R.id.maintain_newapplylist_chailvfeinMoney01);
		chailvfeinMoney02=(EditText) findViewById(R.id.maintain_newapplylist_chailvfeinMoney02);
		chailvfeinMoney03=(EditText) findViewById(R.id.maintain_newapplylist_chailvfeinMoney03);
		chailvfeinMoney04=(EditText) findViewById(R.id.maintain_newapplylist_chailvfeinMoney04);
		chailvfeinMoneylist.add(chailvfeinMoney01);
		chailvfeinMoneylist.add(chailvfeinMoney02);
		chailvfeinMoneylist.add(chailvfeinMoney03);
		chailvfeinMoneylist.add(chailvfeinMoney04);
		//�ͷ�
		canfeinname01=(EditText) findViewById(R.id.maintain_newapplylist_canfeinname01);
		canfeinname02=(EditText) findViewById(R.id.maintain_newapplylist_canfeinname02);
		canfeinname03=(EditText) findViewById(R.id.maintain_newapplylist_canfeinname03);
		canfeinnamelist.add(canfeinname01);
		canfeinnamelist.add(canfeinname02);
		canfeinnamelist.add(canfeinname03);
		canfeinMoney01=(EditText) findViewById(R.id.maintain_newapplylist_canfeinMoney01);
		canfeinMoney02=(EditText) findViewById(R.id.maintain_newapplylist_canfeinMoney02);
		canfeinMoney03=(EditText) findViewById(R.id.maintain_newapplylist_canfeinMoney03);
		canfeinMoneylist.add(canfeinMoney01);
		canfeinMoneylist.add(canfeinMoney02);
		canfeinMoneylist.add(canfeinMoney03);
		//�����
		peijianfeinname01=(EditText) findViewById(R.id.maintain_newapplylist_peijianfeinname01);
		peijianfeinname02=(EditText) findViewById(R.id.maintain_newapplylist_peijianfeinname02);
		peijianfeinname03=(EditText) findViewById(R.id.maintain_newapplylist_peijianfeinname03);
		peijianfeinnamelist.add(peijianfeinname01);
		peijianfeinnamelist.add(peijianfeinname02);
		peijianfeinnamelist.add(peijianfeinname03);
		peijianfeinMoney01=(EditText) findViewById(R.id.maintain_newapplylist_peijianfeinMoney01);
		peijianfeinMoney02=(EditText) findViewById(R.id.maintain_newapplylist_peijianfeinMoney02);
		peijianfeinMoney03=(EditText) findViewById(R.id.maintain_newapplylist_peijianfeinMoney03);
		peijianfeinMoneylist.add(peijianfeinMoney01);
		peijianfeinMoneylist.add(peijianfeinMoney02);
		peijianfeinMoneylist.add(peijianfeinMoney03);
		//��������
		qitafeinname01=(EditText) findViewById(R.id.maintain_newapplylist_qitafeinname01);
		qitafeinname02=(EditText) findViewById(R.id.maintain_newapplylist_qitafeinname02);
		qitafeinname03=(EditText) findViewById(R.id.maintain_newapplylist_qitafeinname03);
		qitafeinnamelist.add(qitafeinname01);
		qitafeinnamelist.add(qitafeinname02);
		qitafeinnamelist.add(qitafeinname03);
		qitafeinMoney01=(EditText) findViewById(R.id.maintain_newapplylist_qitafeinMoney01);
		qitafeinMoney02=(EditText) findViewById(R.id.maintain_newapplylist_qitafeinMoney02);
		qitafeinMoney03=(EditText) findViewById(R.id.maintain_newapplylist_qitafeinMoney03);
		qitafeinMoneylist.add(qitafeinMoney01);
		qitafeinMoneylist.add(qitafeinMoney02);
		qitafeinMoneylist.add(qitafeinMoney03);
		// ����
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);// ����
		//�ϴ�
		upload = (Button) findViewById(R.id.maintain_newapplylist_upload);
		upload.setOnClickListener(this);
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
			SysApplication.getInstance().getservicehistoryListHandler().sendEmptyMessage(200);
			finish();
			Myutil.set_activity_close(this);// �����л������Ч��
			break;
		// �ϴ�
		case R.id.maintain_newapplylist_upload:
			upLoad();
			sendsubmit(id);
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

	//�ύ��������Ϣ
	public void upLoad(){
		getlistdata();
		Log.i("tag","totalMoney5="+String.valueOf(totalMoney));
		//String url=Myconstant.MAINTAIN_NEWAPPLYREPORT;
		String url=Myconstant.MAINTAIN_NEWAPPLYREPORT+"id="+id+"&amount="+String.valueOf(totalMoney)+"&remark="+getremarksInfo();
		
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				try {
					System.out.println(response);
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(getApplicationContext(), "����ɹ�", Toast.LENGTH_SHORT).show();
//						finish();
//						Myutil.set_activity_close(Maintain_apply_newapplylistActivity.this);
						SysApplication.getInstance().getservicehistoryListHandler().sendEmptyMessage(200);
						//�޷����±������б�,����
						//SysApplication.getInstance().setListHandler("worker-expenseorder", new Fragment_maintain_apply.Updatelisthandler(new Fragment_maintain_apply()));
						//SysApplication.getInstance().getapplyListHandler().sendEmptyMessage(100);
						startActivity(new Intent(Maintain_apply_newapplylistActivity.this,Maintain_HomeActivity.class));
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
				//Toast.makeText(getApplicationContext(), "����ʧ�ܣ�"+error.toString(), Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(), "����ʧ�ܣ���������", Toast.LENGTH_SHORT).show();
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
	
	
	//���ͷ���
	private void sendsubmit(final String id){
		String url = Myconstant.MAINTAIN_NEWAPPLYSUBMIT;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(getApplicationContext(), "���ͳɹ�", 0).show();
					} else {
						//���ݴ����ԭ��
						Log.i("tag", "content="+js1.getString("content"));
						Toast.makeText(getApplicationContext(), "����ʧ��", 0).show();
						//Toast.makeText(getApplicationContext(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json�����쳣");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				Log.i("tag", "error="+error);
				Toast.makeText(getApplicationContext(), "����ʧ��"+error.getMessage(), 0).show();
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
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}
	

}
