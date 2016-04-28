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
import com.cnpc.zhibo.app.fragment.Fragment_centre_examine;
import com.cnpc.zhibo.app.fragment.Fragment_centre_home;
import com.cnpc.zhibo.app.fragment.Fragment_centre_repairs;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class Centre_HomeActivity extends MyActivity {

	private Fragment_centre_home f_centre;// 首页
	private Fragment_centre_examine f_examine;// 审批
	private Fragment_centre_repairs f_repairs;// 报修
	private Fragment f0;// 最初的fragment对象
	private FragmentManager fm;// fragment的管理类对象
	private RelativeLayout centre, examine, repairs;// 首页、审批、报修
	private ImageView ima[];
	private TextView tx[];
	private ImageView back;// 返回按键

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_home);
		set_title_text("站长的主页");
		setview();// 将界面上的控件进行实例化
	}

	// 设置界面上的控件的方法
	private void setview() {
		centre = (RelativeLayout) findViewById(R.id.relativelayout_centre_home_shouye);// 首页
		examine = (RelativeLayout) findViewById(R.id.relativelayout_centre_home_shenpi);// 审批
		repairs = (RelativeLayout) findViewById(R.id.relativelayout_centre_home_weixiu);// 维修
		centre.setOnClickListener(l);
		examine.setOnClickListener(l);
		repairs.setOnClickListener(l);
		setBadgerView(repairs, "site-fixorder", repairs_helper);// 给维修按钮设置数字
		setBadgerView(examine, "site-approveorder",examine_helper);// 给审批按钮设置数字
		f_centre = new Fragment_centre_home();// 首页的fragment的实例化
		f_examine = new Fragment_centre_examine();// 审批的fragment的实例化
		f_repairs = new Fragment_centre_repairs();// 维修的fragment的实例化
		f0 = new Fragment();
		fm = getSupportFragmentManager();// 获取fragment的管理类的对象
		xianshiFragment(f_centre);
		ima = new ImageView[3];// 界面底部的三个按钮的图标
		ima[0] = (ImageView) findViewById(R.id.imageView_centre_home_shouye);
		ima[1] = (ImageView) findViewById(R.id.imageView_centre_home_weixiu);
		ima[2] = (ImageView) findViewById(R.id.imageView_centre_home_shenpi);
		tx = new TextView[3];
		tx[0] = (TextView) findViewById(R.id.textview_centre_home_home);
		tx[1] = (TextView) findViewById(R.id.textview_centre_home_service);
		tx[2] = (TextView) findViewById(R.id.textview_centre_home_examine);
		back = (ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);

	}

	/*
	 * 实现维修数字显示赋值的接口
	 */
	private CornerUtil.OrderNumsHelper repairs_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			get_untreated_servicenumber(badgeView);

		}
	};
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
	 * 获取未完成的维修单的数量
	 */
	private void get_untreated_servicenumber(final BadgeView badgeView){
		String url = Myconstant.CENTRE_GETSERVICE_COUNTRNUMBER;
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
						Toast.makeText(Centre_HomeActivity.this, js1.getString("content"), 0).show();
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
	/*
	 * 获取未审批的审批单的数量
	 */
	private void get_untreated_examinenumber(final BadgeView badgeView){
		String url = Myconstant.CENTRE_GETAPPROVE_COUNTRNUMBER;
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
						Toast.makeText(Centre_HomeActivity.this, js1.getString("content"), 0).show();
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

	// 控件的点击事件的监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relativelayout_centre_home_shouye:// 首页

				seticon(0);
				set_title_text("站长的首页");
				xianshiFragment(f_centre);
				break;
			case R.id.relativelayout_centre_home_shenpi:// 审批
				seticon(2);
				set_title_text("站长的审批");
				//clearNums("site-approveorder");// 取消审批按钮上的数字
				xianshiFragment(f_examine);
				break;
			case R.id.relativelayout_centre_home_weixiu:// 维修
				seticon(1);
				set_title_text("站长的维修");
				//clearNums("site-fixorder");// 取消维修按钮上的数字
				xianshiFragment(f_repairs);
				break;
			case R.id.imageView_myactity_you:
				Intent in = new Intent(Centre_HomeActivity.this, User_messagectivity.class);
				startActivity(in);
				Myutil.set_activity_open(Centre_HomeActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// Fragment的替换的方法
	private void xianshiFragment(Fragment f) {
		FragmentTransaction transaction = fm.beginTransaction();// 开始一个事务
		Fragment ft = fm.findFragmentByTag(f.getClass().getCanonicalName());
		// 查找事物中的fragment并获取fragment对象
		if (f0 != null && f0 == f) {
			// 当要替换显示的fragment是当前正在显示的fragment就不发生改变，直接退出
			return;
		}
		if (f0 != null) {
			transaction.hide(f0);
			// 当要替换显示的fragment不是当前显示的fragment时，将当前的fragment进行隐藏

		}
		if (ft != null) {
			transaction.show(ft);
			// 如果要显示的fragment在事物中已经存在，那么就将它进行显示就可以了
		} else {
			transaction.add(R.id.linearlayout_centre_home_fragment, f, f.getClass().getCanonicalName());
			// 如果要显示的fragment不存在事物中，那么就在事物中添加要显示的fragment

		}
		f0 = f;// 将当前显示的fragment设置成要替换的fragment对象
		transaction.commit();// 提交事务
	}

	// 改变底部三个按钮图标的方法
	@SuppressLint("ResourceAsColor")
	private void seticon(int id) {
		switch (id) {
		case 0:
			ima[0].setImageResource(R.drawable.centre_home_check);
			ima[1].setImageResource(R.drawable.centre_service_no);
			ima[2].setImageResource(R.drawable.centre_examine_no);
			tx[0].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			tx[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 1:
			ima[0].setImageResource(R.drawable.centre_home_no);
			ima[1].setImageResource(R.drawable.centre_service_check);
			ima[2].setImageResource(R.drawable.centre_examine_no);
			tx[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[1].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			tx[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 2:
			ima[0].setImageResource(R.drawable.centre_home_no);
			ima[1].setImageResource(R.drawable.centre_service_no);
			ima[2].setImageResource(R.drawable.centre_examine_check);
			tx[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[2].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			break;

		default:
			break;
		}
	}

}
