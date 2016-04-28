//package com.cnpc.zhibo.app;
//
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//
//import com.cnpc.zhibo.app.config.Myconstant;
//import com.cnpc.zhibo.app.util.Myutil;
//
///**
// * 供应商的保修单详情界面
// */
//public class Supplier_baoxiudanxiangqingActivity extends MyActivity {
//	private ImageView fanhui;
//	private WebView webView;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_supplier_baoxiudanxiangqing);
//		setView();
//	}
//
//	/**
//	 * 初始化控件
//	 */
//	private void setView() {
//		set_title_text("报修单详情");
//		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
//		fanhui.setVisibility(View.VISIBLE);
//		fanhui.setOnClickListener(l);
//		webView=(WebView) findViewById(R.id.webView3);
//		set_webView();
//	}
//
//	/**
//	 * 设置webView
//	 */
//	private void set_webView() {
////		webView.getSettings().setJavaScriptEnabled(true);
////		webView.setWebViewClient(new WebViewClient());//点击链接不调用外部浏览器
////		webView.getSettings().setBlockNetworkImage(true);// 可加载图片
////		webView.getSettings().setAppCacheEnabled(true);//应用缓存
////		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没有网络时家在缓存
////		webView.getSettings().setSupportZoom(true);//支持缩放	
////		webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
////		webView.getSettings().setBuiltInZoomControls(true); // 可放大缩小
////		webView.getSettings().setUseWideViewPort(true);
//		webView.loadUrl(Myconstant.REPAIRSMESSAGE);
//		
//	}
//
//	private View.OnClickListener l = new View.OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.imageView_myacitity_zuo:
//				finish();
//				Myutil.set_activity_close(Supplier_baoxiudanxiangqingActivity.this);
//				break;
//
//			default:
//				break;
//			}
//
//		}
//	};
//}
