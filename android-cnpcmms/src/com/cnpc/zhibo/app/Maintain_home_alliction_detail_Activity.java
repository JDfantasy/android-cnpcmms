package com.cnpc.zhibo.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.platform.comapi.map.r;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.easemob.easeui.EaseConstant;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

//维修端主页分配通知点击列表项进入的报修单详情界面
public class Maintain_home_alliction_detail_Activity extends MyActivity implements OnClickListener {
	private WebView webView;
	private Button phone,chat,process;//电话，会话、处理
	private Button finishIndent,closeIndent,addIndent,canle,enter;//订单已完成，关闭订单，新建维修单
	private ImageView backImageView,popwindowback,phonerightImageview;//返回
	private PopupWindow popProcess;//弹出框
	private String indentId; //关闭维修单的id
	private int finishNumber=0;//完成订单的方法编号
	private int closeNumber=1;//关闭订单方法编号
	private int stateNumber=0;//状态编号
	private String indentNumber;//订单编号
	private String gasAddress;//加油站地址
	private String gasStationname;//加油站名称
	private String mobilephone;//手机号码
	private Maintain_home_alloction_item m;//订单对象
	
	private PopupWindow popCloseandFinish;//关闭或完成的弹窗
	private PopupWindow poprightPhone;//右上角的电话和会话弹窗
	private TextView textviewpoprightphone,textViewpoprightchat;//右上角弹窗的电话，会话
	private EditText remarkEditText;//备注控件
	private String remarkInfo="成功";//备注信息
	
	private LinearLayout bottommenufirstlinearlayout;//详情第一次弹出的底部菜单三个按钮
	private LinearLayout bottommenusecondlinearlayout;//详情第二次弹出的底部菜单三个按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_alloction_detail);
		set_title_text("报修单详情");
		getIntentId();
		setViews();
		
	}
	
	//获得数据
	private void getIntentId() {
		Intent intent=getIntent();
		m=(Maintain_home_alloction_item) intent.getSerializableExtra("m");
		indentId=m.id;
		mobilephone=m.mobilephone;
	}

	//控件初始化
	private void setViews() {
		//返回
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		//右边点击可以会话和电话联系
		phonerightImageview = (ImageView) findViewById(R.id.imageView_myactity_you);
		phonerightImageview.setVisibility(View.VISIBLE);
		phonerightImageview.setOnClickListener(this);
		//电话
		phone=(Button) findViewById(R.id.maintain_alloction_phone);
		phone.setOnClickListener(this);
		//会话
		chat=(Button) findViewById(R.id.maintain_alloction_chat);
		chat.setOnClickListener(this);
		//处理
		process=(Button) findViewById(R.id.maintain_alloction_process);
		process.setOnClickListener(this);
		//webview
		webView=(WebView) findViewById(R.id.maintainhome_alloctionDetail_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		webView.getSettings().setBlockNetworkImage(false);// 可加载图片
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 没有网络时家在缓存
		webView.loadUrl(Myconstant.REPAIRSMESSAGE + indentId);
		//第一次的底部按钮的整体布局
		bottommenufirstlinearlayout=(LinearLayout) findViewById(R.id.maintainhome_alloctionDetail_bottommenufirst);
		bottommenusecondlinearlayout=(LinearLayout) findViewById(R.id.maintainhome_alloctionDetail_bottommenusecond);
		bottommenufirstlinearlayout.setVisibility(View.GONE);
		bottommenusecondlinearlayout.setVisibility(View.VISIBLE);
		//完成
		finishIndent = (Button) findViewById(R.id.maintain_alloction_finishIndent);
		//关闭
		closeIndent = (Button) findViewById(R.id.maintain_alloction_closeIndent);
		//接单
		addIndent = (Button) findViewById(R.id.maintain_alloction_addIndent);
		finishIndent.setOnClickListener(this);
		closeIndent.setOnClickListener(this);
		addIndent.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 回退按钮
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// 设置切换界面的效果
			break;
		// 电话会话联系按钮
		case R.id.imageView_myactity_you:
			rightPhone();
			break;
		// 电话
		case R.id.popup_maintainalloction_contactphone_phonetextview:
			poprightPhone.dismiss();
			//启动拨号需要权限
			  Intent intent=new Intent(Intent.ACTION_CALL);
			  //Data (数据)
			  intent.setData(Uri.parse("tel:"+mobilephone));
			  startActivity(intent);
			break;
		// 会话
		case R.id.popup_maintainalloction_contactphone_chattextview:
			poprightPhone.dismiss();
			Toast.makeText(getApplicationContext(), "跳转到会话", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, m.userName));
			break;
		// 处理	新建维修单
		case R.id.maintain_alloction_process:
			//process();
			bottommenufirstlinearlayout.setVisibility(View.GONE);
			bottommenusecondlinearlayout.setVisibility(View.VISIBLE);
			break;
		// 完成
		case R.id.maintain_alloction_finishIndent:
			try {
				popCloseandFinish();
				//popProcess.dismiss();
				stateNumber=finishNumber;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 关闭
		case R.id.maintain_alloction_closeIndent:
			try {
				popCloseandFinish();
				//popProcess.dismiss();
				stateNumber=closeNumber;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 接单
		case R.id.maintain_alloction_addIndent:
			Intent intent2=new Intent(this,Maintain_home_service_addnewlistActivity.class);
			intent2.putExtra("m", m);
			startActivity(intent2);
			break;
		// 关闭popwindow
		case R.id.popwindow_imageView_back:
			//popProcess.dismiss();
			//bottommenufirstlinearlayout.setVisibility(View.VISIBLE);
			break;
		// 确认
		case R.id.button_popup_maintainhome_enter:
			Toast.makeText(getApplicationContext(), "确认", Toast.LENGTH_SHORT).show();
			if(stateNumber==closeNumber){
				closeAndFinishIndent(closeNumber);
			}
			if(stateNumber==finishNumber){
				closeAndFinishIndent(finishNumber);
			}
			popCloseandFinish.dismiss();
			finish();
			break;
		// 取消
		case R.id.button_popup_maintainhome_canle:
			Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
			popCloseandFinish.dismiss();
			break;
		}

	}
	
	//返回监听
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	bottommenufirstlinearlayout.setVisibility(View.VISIBLE);
			bottommenusecondlinearlayout.setVisibility(View.GONE);
             return true; 
        }else { 
            return super.onKeyDown(keyCode, event); 
        } 
           
    } 
	
	//右上角会话，电话联系窗口
	private void rightPhone(){
		View v=LayoutInflater.from(this).inflate(R.layout.popup_maintainalloction_contactphone, null);
		poprightPhone=new PopupWindow(v,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
		poprightPhone.setBackgroundDrawable(new ColorDrawable());
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
		poprightPhone.showAsDropDown(phonerightImageview, 0, 0);
		//TODO
		textviewpoprightphone=(TextView) v.findViewById(R.id.popup_maintainalloction_contactphone_phonetextview);
		textViewpoprightchat=(TextView) v.findViewById(R.id.popup_maintainalloction_contactphone_chattextview);
		textviewpoprightphone.setOnClickListener(this);
		textViewpoprightchat.setOnClickListener(this);
	}
	
	// 弹出框 完成、关闭、接单
	private void process() {
		/*View v = LayoutInflater.from(this).inflate(
				R.layout.popup_process, null);// 创建view
		popProcess = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
		popProcess.setBackgroundDrawable(new ColorDrawable());
		//popProcess.setOutsideTouchable(true);
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
		//popProcess.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		popProcess.showAsDropDown(webView, 0, 0);
		finishIndent = (Button) v
				.findViewById(R.id.popup_process_finishIndent);
		closeIndent = (Button) v
				.findViewById(R.id.popup_process_closeIndent);
		addIndent = (Button) v
				.findViewById(R.id.popup_process_addIndent);
		popwindowback = (ImageView) v
				.findViewById(R.id.popwindow_imageView_back);
		finishIndent.setOnClickListener(this);
		closeIndent.setOnClickListener(this);
		addIndent.setOnClickListener(this);
		popwindowback.setOnClickListener(this);*/
	}
	
	// 点击关闭或完成再弹窗
	public void popCloseandFinish() {
		View v = LayoutInflater.from(this).inflate(
				R.layout.poppup_mainhome_alloctiondetil, null);// 创建view
		popCloseandFinish = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
		popCloseandFinish.setBackgroundDrawable(new ColorDrawable());
		//popProcess.setOutsideTouchable(true);
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
		popCloseandFinish.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		canle = (Button) v.findViewById(R.id.button_popup_maintainhome_canle);
		enter = (Button) v.findViewById(R.id.button_popup_maintainhome_enter);
		remarkEditText=(EditText) v.findViewById(R.id.editText_popup_maintainhome_content);
		finishIndent.setOnClickListener(this);
		canle.setOnClickListener(this);
		enter.setOnClickListener(this);
	}
	
	public String getRemarkInfo(){
		String str=remarkEditText.getText().toString();
		try {
			str=URLEncoder.encode(str, "UTF-8");
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//关闭订单和完成订单
	public void closeAndFinishIndent(int number){
		remarkInfo=getRemarkInfo();
		String url = null;
		if(number==finishNumber){
			url = Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_FINISH;
			if(remarkInfo!=null&&remarkInfo.length()!=0) remarkInfo="完成订单";
		}else if(number==closeNumber){
			url = Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_CLOSE;
			if(remarkInfo!=null&&remarkInfo.length()!=0) remarkInfo="关闭订单";
		}
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Toast.makeText(getApplicationContext(), "访问失败："+error.toString(), Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(), "访问失败,请检查网络", Toast.LENGTH_SHORT).show();
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
				map.put("id", indentId);//报修单编号
				map.put("remark", remarkInfo);//备注信息
				return map;
			}
		};
		SysApplication.getHttpQueues().add(request);
	}

}
