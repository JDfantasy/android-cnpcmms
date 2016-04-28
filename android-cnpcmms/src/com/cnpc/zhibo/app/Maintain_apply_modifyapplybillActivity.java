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


	private ImageView back;// 返回
	private Button upload;// 上传
	private TextView gasStationname;// 加油站名称
	private int totalMoney;// 总金额
	private String remarkInfo;// 备注信息
	private String id;//报销单的id
	
	//设置通过寻找布局来更新金额
	private Item_maintain_apply_adapter adapter;// 适配器
	private List<Maintain_apply_item> list;// 报销单集合
	private ListView listView;
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//下拉刷新
	// 差旅费
	private EditText chailvfeinname01, chailvfeinMoney01;
	private EditText chailvfeinname02, chailvfeinMoney02;
	private EditText chailvfeinname03, chailvfeinMoney03;
	private EditText chailvfeinname04, chailvfeinMoney04;
	// 餐费
	private EditText canfeinname01, canfeinMoney01;
	private EditText canfeinname02, canfeinMoney02;
	private EditText canfeinname03, canfeinMoney03;
	// 配件费用
	private EditText peijianfeinname01, peijianfeinMoney01;
	private EditText peijianfeinname02, peijianfeinMoney02;
	private EditText peijianfeinname03, peijianfeinMoney03;
	// 其他费用
	private EditText qitafeinname01, qitafeinMoney01;
	private EditText qitafeinname02, qitafeinMoney02;
	private EditText qitafeinname03, qitafeinMoney03;
	// 差旅费集合
	private List<EditText> chailvfeinnamelist = new ArrayList<EditText>();// 名称集合
	private List<EditText> chailvfeinMoneylist = new ArrayList<EditText>();// 金额集合
	// 餐费集合
	private List<EditText> canfeinnamelist = new ArrayList<EditText>();// 名称集合
	private List<EditText> canfeinMoneylist = new ArrayList<EditText>();// 金额集合
	// 配件费集合
	private List<EditText> peijianfeinnamelist = new ArrayList<EditText>();// 名称集合
	private List<EditText> peijianfeinMoneylist = new ArrayList<EditText>();// 金额集合
	// 其他费用集合
	private List<EditText> qitafeinnamelist = new ArrayList<EditText>();// 名称集合
	private List<EditText> qitafeinMoneylist = new ArrayList<EditText>();// 金额集合
	
	//回填数据集合
	private List<String> travenamellist=new ArrayList<String>();//差旅费名称集合
	private List<String> mealsnamelist=new ArrayList<String>();//餐费名称集合
	private List<String> sparenamelist=new ArrayList<String>();//配件费名称集合
	private List<String> othernamelist=new ArrayList<String>();//其他费用名称集合
	private List<String> travemoneyllist=new ArrayList<String>();//差旅费金额集合
	private List<String> mealsmoneylist=new ArrayList<String>();//餐费金额集合
	private List<String> sparemoneylist=new ArrayList<String>();//配件费金额集合
	private List<String> othermoneylist=new ArrayList<String>();//其他费用金额集合

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_apply_modifyapplybill);
		set_title_text("修改报销单");
		// 获得对象，提取id
		id=getIntent().getStringExtra("applybillid");
		setViews();
		getapplybilldata(getIntent().getStringExtra("applybillid"));//获得报销单的数据
		Toast.makeText(getApplicationContext(), "如需增加报销项，请联系站长或供应商", Toast.LENGTH_LONG).show();

	}

	// 控件初始化
	private void setViews() {
		//保存完自动更新数据
//		View view=View.inflate(this, R.layout.fragment_maintain_apply, null);
//		mPullListView=(PullToRefreshListView) view.findViewById(R.id.maintainApply_fragment_applyListview);
//		listView = mPullListView.getRefreshableView();// 通过框架获取listview
//		list=new ArrayList<Maintain_apply_item>();//报销单数据集合
//		adapter = new Item_maintain_apply_adapter(this);// 实例化适配器
//		listView.setAdapter(adapter);
		
		gasStationname=(TextView) findViewById(R.id.maintain_modifyapplybill_gasStationname);
		gasStationname.setText(getIntent().getStringExtra("gasStationname"));
		// 差旅费
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
		//餐费
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
		//配件费
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
		//其他费用
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
		// 返回
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);// 返回
		//上传
		upload = (Button) findViewById(R.id.maintain_modifyapplybill_upload);
		upload.setOnClickListener(this);
		
	}
	
	//填写数据内容
	public void fillinapplybillcontent(){
		//差旅费名称
		if(travenamellist.size()!=0){
			for(int i=0;i<travenamellist.size();i++){
				chailvfeinnamelist.get(i).setText(travenamellist.get(i));
			}
		}
		//餐费名称
		if(mealsnamelist.size()!=0){
			for(int i=0;i<mealsnamelist.size();i++){
				canfeinnamelist.get(i).setText(mealsnamelist.get(i));
			}
		}
		//配件费名称
		if(sparenamelist.size()!=0){
			for(int i=0;i<sparenamelist.size();i++){
				peijianfeinnamelist.get(i).setText(sparenamelist.get(i));
			}
		}
		//其他费用名称
		if(othernamelist.size()!=0){
			for(int i=0;i<othernamelist.size();i++){
				qitafeinnamelist.get(i).setText(othernamelist.get(i));
			}
		}
		//差旅费金额
		if(travemoneyllist.size()!=0){
			for(int i=0;i<travemoneyllist.size();i++){
				chailvfeinMoneylist.get(i).setText(travemoneyllist.get(i));
			}
		}
		//餐费金额
		if(mealsmoneylist.size()!=0){
			for(int i=0;i<mealsmoneylist.size();i++){
				canfeinMoneylist.get(i).setText(mealsmoneylist.get(i));
			}
		}
		//配件费金额
		if(sparemoneylist.size()!=0){
			for(int i=0;i<sparemoneylist.size();i++){
				peijianfeinMoneylist.get(i).setText(sparemoneylist.get(i));
			}
		}
		//其他费用名称
		if(othermoneylist.size()!=0){
			for(int i=0;i<othermoneylist.size();i++){
				qitafeinMoneylist.get(i).setText(othermoneylist.get(i));
			}
		}
		
	}
	
	//获得数据
	public String getlistdata(){
		JSONArray array = new JSONArray();
		//差旅费
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
		//餐费
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
		//配件费用
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
		//其他费用
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

	// 点击监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// 设置切换界面的效果
			break;
		// 上传
		case R.id.maintain_modifyapplybill_upload:
			upLoad();
			break;
		}

	}
	
	//获得备注信息的内容
	public String getremarksInfo(){
		//String str=remarksInfoEditText.getText().toString();
		String str="报销";
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
	//设置更新的handler
	public void setListHandler(Handler handler){
		this.handler=handler;
	}*/

	//提交报销单信息
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
						Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
						//getApplyProgress();
						//handler.sendEmptyMessage(100);
						SysApplication.getInstance().getapplyListHandler().sendEmptyMessage(100);
						//Log.i("tag", ""+Thread.currentThread().getId());//主线程 
						startActivity(new Intent(Maintain_apply_modifyapplybillActivity.this,Maintain_HomeActivity.class));
					} else {
						Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), "访问失败："+error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
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

	
	//获得报销单数据
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
						Toast.makeText(getApplicationContext(), "找到数据", Toast.LENGTH_SHORT).show();
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
								if(type.equals("travel")){//差旅费
									for(int j=0;j<4;j++){
										if(order==j){
											travenamellist.add(name);
											travemoneyllist.add(String.valueOf(money));
										}
									}
								}else if(type.equals("meals")){//餐费
									for(int j=0;j<3;j++){
										if(order==j){
											mealsnamelist.add(name);
											mealsmoneylist.add(String.valueOf(money));
										}
									}
								}else if(type.equals("spare")){//配件费
									for(int j=0;j<3;j++){
										if(order==j){
											sparenamelist.add(name);
											sparemoneylist.add(String.valueOf(money));
										}
									}
								}else if(type.equals("other")){//其他费用
									for(int j=0;j<3;j++){
										if(order==j){
											othernamelist.add(name);
											othermoneylist.add(String.valueOf(money));
										}
									}
								}
							}
						}
						//回填数据内容
						fillinapplybillcontent();
					} else {
						Toast.makeText(getApplicationContext(), "没有找到数据", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json解析异常");
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), "访问失败："+error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
				headers.put("Content-Type", "application/json");
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
		};
		
		SysApplication.getHttpQueues().add(request);
		
	}
	
	//获得报销单数据   ***无法更新***
	private void getApplyProgress() {
		String url=Myconstant.MAINTAIN_APPLY_PROGRESS;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getApplicationContext(), "没有找到数据", 0).show();
						} else {
							Toast.makeText(getApplicationContext(), "已找到数据", 0).show();
							list.clear();
							adapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								String modifyDate=js3.getString("modifyDate");
								JSONObject js4=js3.getJSONObject("fixOrder").getJSONObject("station");
								Maintain_apply_item m=new Maintain_apply_item(js3.getString("id"), js3.getString("sn"),
										js4.getString("name"), problem, modifyDate, js3.getString("amount"));//总金额
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
						//数据错误的原因
						Toast.makeText(getApplicationContext(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json获取数据错误");
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
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

		};
		SysApplication.getHttpQueues().add(re);

	}

	
}
