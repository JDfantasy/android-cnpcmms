package com.cnpc.zhibo.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
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
//վ���˲鿴֤������Ľ���
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_credential_itemmessageActivity extends MyActivity {
	private ImageView back;
	private WebView web;
	private TextView abolish;// ȡ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_credential_itemmessage);
		setview();
//		registerForContextMenu(web);//ע�������Ĳ˵�
	}

	// ���ý���ķ���
	private void setview() {
		set_title_text("֤������");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		web = (WebView) findViewById(R.id.webView_centre_credential_itemmessage);
		web.loadUrl(Myconstant.CREDENTIALSMESSAGE + getIntent().getStringExtra("id"));
		abolish = (TextView) findViewById(R.id.textview_myacitivity_you);
		abolish.setText("ȡ������");
		if (getIntent().getStringExtra("status").equals("yes")) {
			abolish.setVisibility(View.VISIBLE);
		} else {
			abolish.setVisibility(View.GONE);
		}
		abolish.setOnClickListener(l);
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_credential_itemmessageActivity.this);
				break;
			case R.id.textview_myacitivity_you:// ȡ������
				setnumberabolish();
				break;
			default:
				break;
			}

		}
	};

	// ȡ������ִ�еķ���
	private void setnumberabolish() {
		String url = Myconstant.CENTRE_SETNUMBERABOLISH;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Centre_credential_itemmessageActivity.this, "�Ѿ��ɹ�ȡ������", 0).show();
						startActivity(new Intent(Centre_credential_itemmessageActivity.this, Centre_HomeActivity.class));
						Myutil.set_activity_close(Centre_credential_itemmessageActivity.this);
					} else {
						Toast.makeText(Centre_credential_itemmessageActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_credential_itemmessageActivity.this, "��������ʧ��", 0).show();
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
	private String imgurl = "";

	/***
	 * ���ܣ�����ͼƬ���浽�ֻ�
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getTitle() == "���浽�ֻ�") {
					new SaveImage().execute(); // Android 4.0�Ժ�Ҫʹ���߳�����������
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
					menu.setHeaderTitle("��ʾ");
					menu.add(0, v.getId(), 0, "���浽�ֻ�").setOnMenuItemClickListener(handler);
				}
			}
		}
	}

	/***
	 * ���ܣ����̱߳���ͼƬ
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
				result = "ͼƬ�ѱ�������" + file.getAbsolutePath();
			} catch (Exception e) {
				result = "����ʧ�ܣ�" + e.getLocalizedMessage();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(Centre_credential_itemmessageActivity.this, result, Toast.LENGTH_SHORT).show();
			;
		}
	}
}
