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
// * ��Ӧ�̵ı��޵��������
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
//	 * ��ʼ���ؼ�
//	 */
//	private void setView() {
//		set_title_text("���޵�����");
//		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
//		fanhui.setVisibility(View.VISIBLE);
//		fanhui.setOnClickListener(l);
//		webView=(WebView) findViewById(R.id.webView3);
//		set_webView();
//	}
//
//	/**
//	 * ����webView
//	 */
//	private void set_webView() {
////		webView.getSettings().setJavaScriptEnabled(true);
////		webView.setWebViewClient(new WebViewClient());//������Ӳ������ⲿ�����
////		webView.getSettings().setBlockNetworkImage(true);// �ɼ���ͼƬ
////		webView.getSettings().setAppCacheEnabled(true);//Ӧ�û���
////		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//û������ʱ���ڻ���
////		webView.getSettings().setSupportZoom(true);//֧������	
////		webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
////		webView.getSettings().setBuiltInZoomControls(true); // �ɷŴ���С
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
