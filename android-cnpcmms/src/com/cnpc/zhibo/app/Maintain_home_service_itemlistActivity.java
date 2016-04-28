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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.cnpc.zhibo.app.adapter.Item_maintainhome_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
//维修端主页的维修单界面
//复用开始接单的实体类和adapter
import android.widget.Toast;

public class Maintain_home_service_itemlistActivity extends MyActivity implements
		OnClickListener, OnScrollListener{

	private ImageView back;//返回
	private Item_maintainhome_service_adapter adapter;
	private List<Maintain_startList_item> list;
	private ImageView searchImageView;//搜索
	private int statenumber=0;//维修状态
	//下拉刷新控件
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//下拉刷新
	private ListView listview;
	private boolean refreshFlag=false;//用来判断是否正在进行刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_service_itemlist);
		setViews();
		set_title_text("维修单");
		
	}


	// 控件初始化
	private void setViews() {
		list=new ArrayList<Maintain_startList_item>();
		//下拉刷新
		mPullListView =(PullToRefreshListView) findViewById(R.id.maintainhome_serviceListview);
		listview = mPullListView.getRefreshableView();// 通过框架获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间为当前
		//listview.setOnItemClickListener(this);
		mPullListView.setOnRefreshListener(refreshListener);
		
		listview.setDividerHeight(0);
		listview.setSelector(new BitmapDrawable());
		//listview.setCacheColorHint(0);
		listview.setOnScrollListener(this);//滚动监听
		
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		//搜索
		searchImageView=(ImageView) findViewById(R.id.maintainhome_serviceItemlist_search);
		searchImageView.setOnClickListener(this);
		try {
			startLoadData();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
		}
	}

	//进入时加载的界面
	private void startLoadData() {
		adapter=new Item_maintainhome_service_adapter(this);
		listview.setAdapter(adapter);
		getServiceHistoryandProgress();
	}
	
	// listview的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (refreshFlag==false) {
				refreshFlag=true;//改变刷新的状态
					getServiceHistoryandProgress();
				Toast.makeText(getApplicationContext(), "下拉刷新", Toast.LENGTH_SHORT).show();
			} 
			adapter.setScrollState(false); 
			mPullListView.onPullDownRefreshComplete();//关闭刷新
			refreshFlag=false;//改变是否刷新的状态
			

		}
		//设置下拉刷新是否可以使用
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			
			Toast.makeText(getApplicationContext(), "上拉刷新", Toast.LENGTH_SHORT).show();
			mPullListView.onPullUpRefreshComplete();//关闭刷新
			refreshFlag=false;//改变是否刷新的状态
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// 设置切换界面的效果
			break;
		// 搜索
		case R.id.maintainhome_serviceItemlist_search:
			startActivity(new Intent(this,Maintain_searchhomeservice_Activity.class));
			break;

		}

	}

	// listView监听
	/*@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 点击跳转到详情界面
		Intent intent = new Intent(this,Maintain_home_service_detail_Activity.class);
		String indentId=list.get(position).id;
		intent.putExtra("indentId", indentId);
		startActivity(intent);

	}*/
	
	// 获取维修中的列表
	private void getServiceHistoryandProgress() {
		String url=Myconstant.MAINTAIN_SERVICE_PROGRESS;
		adapter.tag1=0;
		adapter.tag2=0;
		adapter.ck1 = false;
		adapter.ck2 = false;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
								String problem=js3.getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
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
							
							adapter.setdate(list);
							mPullListView.onPullDownRefreshComplete();//关闭刷新
							mPullListView.onPullUpRefreshComplete();
							refreshFlag=false;//改变是否刷新的状态
						}

					} else {
						//数据错误的原因
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
				//Toast.makeText(getApplicationContext(), "请求数据失败"+error.getMessage(), 0).show();
				Toast.makeText(getApplicationContext(), "请求失败，请检查网络", 0).show();
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


	//滚动监听
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState){  
		  
	        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止滚动  
	        {  
	            //设置为停止滚动  
	            adapter.setScrollState(false); 
	            adapter.notifyDataSetChanged();
	            //当前屏幕中listview的子项的个数  
	            break;  
	        }  
	        case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动做出了抛的动作  
	        {  
	            //设置为正在滚动  
	            adapter.setScrollState(true);
	            break;  
	        }  
	
	        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动  
	        {  
	            //设置为正在滚动  
	            adapter.setScrollState(true); 
	            break;  
	        }  
	    }
		
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	

}
