package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.fragment.Fragment_supplier_home;
import com.cnpc.zhibo.app.fragment.Fragment_supplier_reimbursement;
import com.cnpc.zhibo.app.fragment.Fragment_supplier_repairs;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.Myutil;

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
import android.widget.TextView;

/**
 * 供应商的主页
 */
public class Supplier_HomeActivity extends MyActivity {
	private Fragment_supplier_home f_supplier; // 首页
	private Fragment_supplier_repairs f_repairs; // 维修
	private Fragment_supplier_reimbursement f_reimbursement; // 报销
	private Fragment f0; // 最初的fragment对象
	private FragmentManager fm; // fragment的管理类对象
	private LinearLayout centre, reimbursement, repairs; // 首页、报销、维修
	private ImageView img[];
	private TextView text[];
	private ImageView back;//返回按键
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_home);
		set_title_text("供应商的主页");
		setView();
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

	/**
	 * 初始化控件
	 */
	private void setView() {
		centre = (LinearLayout) findViewById(R.id.linearlayout_supplier_home_shouye);
		repairs = (LinearLayout) findViewById(R.id.linearlayout_supplier_home_weixiu);
		reimbursement = (LinearLayout) findViewById(R.id.linearlayout_supplier_home_baoxiao);
//		setBadgerView(CornerUtil.addCorner(this, repairs, "provider-fixorder"), "provider-fixorder");// 给维修按钮设置数字
//		setBadgerView(CornerUtil.addCorner(this, reimbursement, "provider-expenseorder"), "provider-expenseorder");// 给报销按钮设置数字
		f_supplier = new Fragment_supplier_home();
		f_repairs = new Fragment_supplier_repairs();
		f_reimbursement = new Fragment_supplier_reimbursement();

		centre.setOnClickListener(l);
		repairs.setOnClickListener(l);
		reimbursement.setOnClickListener(l);

		f0 = new Fragment();
		fm = getSupportFragmentManager();
		xianshiFragment(f_supplier);
		img = new ImageView[3];
		img[0] = (ImageView) findViewById(R.id.imageView_supplier_home_shouye);
		img[1] = (ImageView) findViewById(R.id.imageView_supplier_home_weixiu);
		img[2] = (ImageView) findViewById(R.id.imageView_supplier_home_baoxiao);
		img[0].setImageResource(R.drawable.centre_home_check);
		text = new TextView[3];
		text[0] = (TextView) findViewById(R.id.supplier_dibu_home);
		text[1] = (TextView) findViewById(R.id.supplier_dibu_repairs);
		text[2] = (TextView) findViewById(R.id.supplier_dibu_baoxiao);
		text[0].setTextColor(getResources().getColor(R.color.dingbubeijingse));
		back=(ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);
	}

	// 控件的点击事件的监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.linearlayout_supplier_home_shouye:// 首页

				seticon(0);
				set_title_text("运营商的首页");
				xianshiFragment(f_supplier);
				break;
			case R.id.linearlayout_supplier_home_weixiu:// 维修

				seticon(1);
				set_title_text("运营商的维修");
				//clearNums("provider-fixorder");// 取消维修按钮上的数字
				xianshiFragment(f_repairs);
				break;
			case R.id.linearlayout_supplier_home_baoxiao:// 报销

				seticon(2);
				//clearNums("provider-expenseorder");// 取报销修按钮上的数字
				set_title_text("运营商的报销");
				xianshiFragment(f_reimbursement);
				break;
			case R.id.imageView_myactity_you:
				Intent in=new Intent(Supplier_HomeActivity.this, User_messagectivity.class);
				startActivity(in);
				Myutil.set_activity_open(Supplier_HomeActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// 改变底部三个按钮图标的方法
	protected void seticon(int id) {
		switch (id) {
		case 0:
			img[0].setImageResource(R.drawable.centre_home_check);
			img[1].setImageResource(R.drawable.centre_service_no);
			img[2].setImageResource(R.drawable.supplier_baoxiao2);
			text[0].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			text[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 1:
			img[0].setImageResource(R.drawable.centre_home_no);
			img[1].setImageResource(R.drawable.centre_service_check);
			img[2].setImageResource(R.drawable.supplier_baoxiao2);
			text[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[1].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			text[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 2:
			img[0].setImageResource(R.drawable.centre_home_no);
			img[1].setImageResource(R.drawable.centre_service_no);
			img[2].setImageResource(R.drawable.supplier_baoxiao1);
			text[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[2].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			break;
		default:
			break;
		}

	}

	// Fragment的替换的方法
	protected void xianshiFragment(Fragment f) {
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
			transaction.add(R.id.linearlayout_supplier_home_fragment, f, f.getClass().getCanonicalName());
			// 如果要显示的fragment不存在事物中，那么就在事物中添加要显示的fragment

		}
		f0 = f;// 将当前显示的fragment设置成要替换的fragment对象
		transaction.commit();// 提交事务

	}

}
