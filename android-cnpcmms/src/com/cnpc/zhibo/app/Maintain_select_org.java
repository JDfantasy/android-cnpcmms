package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.WorkerBean;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.util.Node;
import com.cnpc.zhibo.app.util.adapter.TreeListViewAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 维修工首次登陆后选择所在组织机构部门列表
 * @author xicunyou
 */
public class Maintain_select_org extends MyActivity implements OnClickListener{
	
	private ListView listview;
	private Dialog mDialog;
	private List<WorkerBean> mDatas= new ArrayList<WorkerBean>();
	private int defaultExpandLevel=1;
	private TreeListViewAdapter<WorkerBean> adapter;
	private String parem="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parem= getIntent().getStringExtra("supplier_id");//从上一个界面传来的供应商id
		initDialog();
		initViews();
		initDatas();
	}
	
	private void initDialog() {
		mDialog = new Dialog(this,R.style.MyProgressDialogStyle);
		mDialog.setContentView(R.layout.progress_dialog);
	}
	
	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentView(R.layout.activity_maintain_select_org);
		View view = findViewById(R.id.imageView_myacitity_zuo);
		view.setOnClickListener( this);//设置返回
		view.setVisibility(View.VISIBLE);
		set_title_text("所属部门");
		listview = (ListView) findViewById(R.id.id_listview);
		initAdapter();
	}
	private void initAdapter() {
		try {
			adapter = new TreeListViewAdapter<WorkerBean>(listview,this,mDatas,defaultExpandLevel) {
				@Override
				public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
					ViewHolder holder = null;
					if (convertView == null)
					{
						convertView = mInflater.inflate(R.layout.tree_listview_item, parent, false);
						holder = new ViewHolder();
						holder.mIcon = (ImageView) convertView.findViewById(R.id.id_item_icon);
						holder.uIcon = (ImageView) convertView.findViewById(R.id.id_item_usericon);
						holder.cIcon = (TextView) convertView.findViewById(R.id.id_item_chaceicon);
						holder.mText = (TextView) convertView.findViewById(R.id.id_item_text);
						convertView.setTag(holder);
					} else
					{
						holder = (ViewHolder) convertView.getTag();
					}
					final WorkerBean workerBean = getBeanByNode(node);
					if(workerBean!=null){
						holder.mIcon.setVisibility(View.VISIBLE);
						holder.mIcon.setImageResource(node.getIcon()==-1?R.drawable.tree_ec:(node.getIcon()));
						holder.uIcon.setImageResource(R.drawable.icon_password);
						if(node.getIcon()==-1){//有子节点
							convertView.setBackgroundResource(R.color.baise);//背景白色
						}else{//无子节点
							convertView.setBackgroundResource(R.drawable.item_bg_selector);//可变化背景色
						}
					}
						holder.cIcon.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog(workerBean);
							}
						});
					
					holder.mText.setText(node.getName());
					return convertView;
				}
				class ViewHolder
				{
					ImageView mIcon;
					ImageView uIcon;
					TextView cIcon;
					TextView mText;
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		} 
		listview.setAdapter(adapter);
	}
	
	private WorkerBean getBeanByNode(Node node){
		WorkerBean workerBean = null;
		for(WorkerBean bean : mDatas){
			if(node.getId()==bean.getId())
				workerBean = bean;
		}
		return workerBean;
	}
	
	protected void dialog(final WorkerBean worker) {
		AlertDialog.Builder builder = new Builder(Maintain_select_org.this);
		builder.setMessage("确认选择"+worker.getName()+"吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//确认提交接口
				submitDatas(worker);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				worker.setSelected(false);
				adapter.notifyDataSetChanged();
			}
		})
		.create().show();
		}
	
	private void initDatas(){
		mDialog.show();
		String url = Myconstant.ORGANIZATION_TREE_ORG+"?id="+parem;//参数为供应商id
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, url,null,new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				mDialog.dismiss();
				try {
					String type = response.getJSONObject("response").getString("type");
					if("success".equals(type)){
						JSONArray array =response.getJSONArray("body");
						mDatas.clear();
						System.out.println(array.toString());
						for(int i= 0;i<array.length();i++){
							JSONObject object = array.getJSONObject(i);
							WorkerBean worker = new WorkerBean(object.getInt("id"), object.getInt("parent"), object.getString("name"));
							worker.setType(object.getString("type"));
							mDatas.add(worker);
						}
						adapter.changeDatas(mDatas, defaultExpandLevel);
					}else{
						Toast.makeText(Maintain_select_org.this, "请求数据失败", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new  Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mDialog.dismiss();
				Toast.makeText(Maintain_select_org.this, "访问失败："+error.getMessage(), Toast.LENGTH_LONG).show();
			}
		}){
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
		};
		SysApplication.getHttpQueues().add(request);
	}
	private Map<String,String> map;
	private void submitDatas(WorkerBean bean){
		mDialog.show();
		String url = Myconstant.WORKER_SELECT;
		map = new HashMap<String, String>();
		map.put("providerId", parem);//供应商序号
		map.put("orgId", bean.getId()+"");//所属部门序号
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				mDialog.dismiss();
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Maintain_select_org.this, "提交成功", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(Maintain_select_org.this, Maintain_HomeActivity.class));
						finish();
						Myutil.set_activity_close(Maintain_select_org.this);
					} else {
						Toast.makeText(Maintain_select_org.this, "提交数据失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mDialog.dismiss();
				Toast.makeText(Maintain_select_org.this, "访问失败："+error.toString(), Toast.LENGTH_SHORT).show();
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
				return map;
			}
		};
		SysApplication.getHttpQueues().add(re);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(Maintain_select_org.this);
			break;
		}
	}
}
