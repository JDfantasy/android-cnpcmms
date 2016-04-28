package com.cnpc.zhibo.app;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.CornerUtil;
//中石油端的主界面
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Petrochina_HomeActivity extends MyActivity {
	private RelativeLayout fbgg, examine, suppliermessage, jyzmessage, servicenumber,aboutus;
	// 发布公告、审批、供应商信息、加油站信息、维修率查询、关于我们
	private ImageView back;//返回按键
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_home);
		setview();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}

		return false;

	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	// 设置界面的方法
	private void setview() {
		set_title_text("中国石油");
		fbgg = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_pushnotice);// 发布公告
		examine = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_examine);// 审批
		suppliermessage = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_suppliermessage);// 供应商
		jyzmessage = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_jyzrmessage);// 加油站信息
		servicenumber = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_servicenumber);// 维修率查询
		aboutus = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_home_aboutus);// 维修率查询
		setBadgerView(examine, "customer-approveorder",examine_helper);// 给审批按钮设置数字
		fbgg.setOnClickListener(l);
		examine.setOnClickListener(l);
		suppliermessage.setOnClickListener(l);
		jyzmessage.setOnClickListener(l);
		servicenumber.setOnClickListener(l);
		aboutus.setOnClickListener(l);
		back=(ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);
	}
	/*
	 * 实现审批数字显示赋值的接口
	 */
	private CornerUtil.OrderNumsHelper examine_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			get_untreated_examinenumber(badgeView);

		}
	};
	/*
	 * 获取未审批的审批单的数量
	 */
	private void get_untreated_examinenumber(final BadgeView badgeView){
		String url = Myconstant.PETROCHINA_GET_COUNTWAIT_RENTRYSTATS;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						String number=js.getString("body");
						if ("0".equals(number)) {
							badgeView.hide();
						} else {
							badgeView.setText(number);
							badgeView.show();
						}
					} else {
						Toast.makeText(Petrochina_HomeActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
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

		};
		SysApplication.getHttpQueues().add(re);
	}
	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {
		Intent in = new Intent();

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relativelayout_petrochina_hone_pushnotice:// 发布公告
				in.setClass(Petrochina_HomeActivity.this, Petrochina_announceActivity.class);
				break;
			case R.id.relativelayout_petrochina_hone_examine:// 审批
				//clearNums("customer-approve");// 取消审批按钮上的数字
				in.setClass(Petrochina_HomeActivity.this, Petrochina_examine_entryActivity.class);
				break;

			case R.id.relativelayout_petrochina_hone_suppliermessage:// 供应商信息
				in.setClass(Petrochina_HomeActivity.this, Petrochina_suppliers_entryActivity.class);
				break;

			case R.id.relativelayout_petrochina_hone_jyzrmessage:// 加油站信息
				in.setClass(Petrochina_HomeActivity.this, Petrochina_station_entryActivity.class);
				break;

			case R.id.relativelayout_petrochina_hone_servicenumber:// 维修率查询
				in.setClass(Petrochina_HomeActivity.this, Petrochina_project_entryActivity.class);
				break;
			case R.id.imageView_myactity_you://查看个人信息
				in.setClass(Petrochina_HomeActivity.this, User_messagectivity.class);
				break;
			case R.id.relativelayout_petrochina_home_aboutus://关于我们
				in.setClass(Petrochina_HomeActivity.this, AboutUsActivity.class);
				in.putExtra("activity", "customer");
				break;
			default:
				break;
			}
			startActivity(in);
			Myutil.set_activity_open(Petrochina_HomeActivity.this);

		}
	};

}
