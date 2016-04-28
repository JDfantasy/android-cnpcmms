package com.cnpc.zhibo.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商的分配订单详情界面
 */
public class Supplier_fenpeixiangqingActivity extends MyActivity {

	private ImageView fanhui;// 返回
	private TextView kuaisufenpei;// 快速分配
	private WebView webView;
	private String baoxiudan_id;
	private int tag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_fenpeixiangqing);

		Intent intent = getIntent();
		baoxiudan_id = intent.getStringExtra("baoxiudan_id");
		tag = intent.getIntExtra("tag", 0);
		setView();
//		registerForContextMenu(webView);//注册上下文菜单
	}

	/**
	 * 初始化控件
	 */
	private void setView() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		kuaisufenpei = (TextView) findViewById(R.id.textview_myacitivity_you);
		if (tag == 2) {
			set_title_text("订单分配");
			kuaisufenpei.setVisibility(View.VISIBLE);
			kuaisufenpei.setText("快速分配");
			kuaisufenpei.setTextColor(getResources().getColor(R.color.baise));
			kuaisufenpei.setPadding(0, 0, 50, 0);
			kuaisufenpei.setOnClickListener(l);
		} else if (tag == 1) {
			set_title_text("报修单详情");
		}
		// fujinweixiuyuan=(TextView)
		// findViewById(R.id.textview_supplier_fujinweixiuyuan);
		fanhui.setOnClickListener(l);
		kuaisufenpei.setOnClickListener(l);
		// fujinweixiuyuan.setOnClickListener(l);
		webView = (WebView) findViewById(R.id.webview_supplier_see_sp_news1);
		set_webView();
	}

	/**
	 * 设置webView
	 */
	private void set_webView() {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		webView.getSettings().setBlockNetworkImage(false);// 可加载图片
		// webView.getSettings().setAppCacheEnabled(true);// 应用缓存
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//
		// 没有网络时家在缓存
		// webView.getSettings().setSupportZoom(true);// 支持缩放
		// webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// webView.getSettings().setBuiltInZoomControls(true); // 可放大缩小
		// webView.getSettings().setUseWideViewPort(true);
		webView.loadUrl(Myconstant.REPAIRSMESSAGE + baoxiudan_id);

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Supplier_fenpeixiangqingActivity.this);

				break;
			case R.id.textview_myacitivity_you:// 查看维修员列表
				Intent intent = new Intent(Supplier_fenpeixiangqingActivity.this,
						Supplier_weixiuyuanguanliActivity.class);
				intent.putExtra("baoxiudan_id", baoxiudan_id);
				startActivity(intent);
				finish();
				Myutil.set_activity_open(Supplier_fenpeixiangqingActivity.this);
				break;
			// case R.id.textview_supplier_fujinweixiuyuan:// 查看附近维修员
			// finish();
			// Myutil.set_activity_open(Supplier_fenpeixiangqingActivity.this);
			// break;
			default:
				break;
			}

		}
	};

	private String imgurl = "";

	/***
	 * 功能：长按图片保存到手机
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getTitle() == "保存到手机") {
					new SaveImage().execute(); // Android 4.0以后要使用线程来访问网络
				} else {
					return false;
				}
				return true;
			}
		};
		if (v instanceof WebView) {
			WebView.HitTestResult result = ((WebView) v).getHitTestResult();
			if (result != null) {
				int type = result.getType();
				if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
					imgurl = result.getExtra();
					menu.setHeaderTitle("提示");
					menu.add(0, v.getId(), 0, "保存到手机").setOnMenuItemClickListener(handler);
				}
			}
		}
	}

	/***
	 * 功能：用线程保存图片
	 * 
	 * @author wangyp
	 * 
	 */
	private class SaveImage extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			try {
				String sdcard = Environment.getExternalStorageDirectory().toString();
				File file = new File(sdcard + "/Download");
				if (!file.exists()) {
					file.mkdirs();
				}
				int idx = imgurl.lastIndexOf(".");
				String ext = imgurl.substring(idx);
				file = new File(sdcard + "/Download/" + new Date().getTime() + ext);
				InputStream inputStream = null;
				URL url = new URL(imgurl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(20000);
				if (conn.getResponseCode() == 200) {
					inputStream = conn.getInputStream();
				}
				byte[] buffer = new byte[4096];
				int len = 0;
				FileOutputStream outStream = new FileOutputStream(file);
				while ((len = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				result = "图片已保存至：" + file.getAbsolutePath();
			} catch (Exception e) {
				result = "保存失败！" + e.getLocalizedMessage();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(Supplier_fenpeixiangqingActivity.this, result, Toast.LENGTH_SHORT).show();
			;
		}
	}

}
