package com.cnpc.zhibo.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
/*
 * վ���˲鿴���޵�����Ľ���
 */
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
/*
 * վ���˲鿴���޵��������
 */
import com.cnpc.zhibo.app.config.Myconstant;
//վ���˲鿴���޵�����Ľ���
import com.cnpc.zhibo.app.util.Myutil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.JavascriptInterface;
public class Centre_See_repairs_messageActivity extends MyActivity {
	private ImageView fanhui;// ����
	private WebView web;// ���ؾ�̬��ҳ�Ŀؼ�
	private TextView shut;// �رն���
	@SuppressLint("JavascriptInterface")
	@JavascriptInterface
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_see_item);
		setview();
//		web.addJavascriptInterface(new JavascriptInterfaces(Centre_See_repairs_messageActivity.this), "imagelistner");
//		web.setWebViewClient(new MyWebViewClient());
	}

	// ���ý���ķ���
	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	private void setview() {
		set_title_text("���޵�");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		web = (WebView) findViewById(R.id.webView_centre_seerepairsitem);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// ������Ӳ������ⲿ�����
		web.getSettings().setBlockNetworkImage(false);// �ɼ���ͼƬ
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// û������ʱ���ڻ���
		web.loadUrl(Myconstant.REPAIRSMESSAGE + getIntent().getStringExtra("id"));
		shut = (TextView) findViewById(R.id.textview_myacitivity_you);// �رն���
		if (getIntent().getStringExtra("state").equals("wait")) {

			shut.setVisibility(View.VISIBLE);
		} else {
			shut.setVisibility(View.GONE);
		}
		shut.setText("�رն���");
		shut.setOnClickListener(l);

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_See_repairs_messageActivity.this);

				break;
			case R.id.textview_myacitivity_you:// �رն���
				set_shutmonad();
			default:
				break;
			}

		}
	};

	// �رն����ķ���
	private void set_shutmonad() {
		String url = Myconstant.CENTRE_CANCEL_REPAIRSMONAD;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Centre_See_repairs_messageActivity.this, "�ɹ��رն���", 0).show();
						startActivity(new Intent(Centre_See_repairs_messageActivity.this, Centre_HomeActivity.class));
						Myutil.set_activity_close(Centre_See_repairs_messageActivity.this);
					} else {
						Toast.makeText(Centre_See_repairs_messageActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Centre_See_repairs_messageActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", getIntent().getStringExtra("id"));
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	/*
	 * ����ͼƬʵ�ֵ���¼�
	 *
	 */
	// ע��js��������
	@JavascriptInterface
	private void addImageClickListner() {
		Log.i("azy", "ִ������Ӽ�������");
		// ���js�����Ĺ��ܾ��ǣ��������е�img���㣬�����onclick�����������Ĺ�������ͼƬ�����ʱ����ñ���java�ӿڲ�����url��ȥ
		web.loadUrl("javascript:(function(){" + "var objs=document.getElementsByTagName(\"img\");"
				+ "for(var i=0;i<objs.length;i++)" + "{" + "objs[i].onclick=function()" + "{"
				+ "window.imagelistner.openImage(this.src);" + "}" + "}" + "})()");
	}

	// jsͨ�Žӿ�
	
	public class JavascriptInterfaces {
		private Context context;

		public JavascriptInterfaces(Context context) {
			this.context = context;
		}
		@JavascriptInterface
		public void openImage(String imgurl) {
			Log.i("azy", "img"+imgurl);
			Intent intent = new Intent();
			intent.putExtra("imgurl", imgurl);
			intent.setClass(context, Save_see_phoneActivity.class);
			context.startActivity(intent);
			
		}
	}

	// ����
	private class MyWebViewClient extends WebViewClient {
		@JavascriptInterface
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}
		@JavascriptInterface
		@Override
		public void onPageFinished(WebView view, String url) {

			view.getSettings().setJavaScriptEnabled(true);

			super.onPageFinished(view, url);
			// html�������֮����Ӽ���ͼƬ�ĵ��js����
			addImageClickListner();

		}
		@JavascriptInterface
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);

			super.onPageStarted(view, url, favicon);
		}
		@JavascriptInterface
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}

}
