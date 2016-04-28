package com.cnpc.zhibo.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.cnpc.zhibo.app.application.SysApplication;
/*
 * �鿴���ұ���ͼƬ�ķ���
 */
import com.cnpc.zhibo.app.view.BrowseView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Save_see_phoneActivity extends MyActivity {

	private Matrix currentMatrix = new Matrix();
	private BrowseView icon;// �Զ���ͼƬ
	private String imagurl = "";// ͼƬ����ַ
	private ImageView back;//����
	private TextView save;//����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_see_phone);
		set_title_text("�鿴ͼƬ");
		icon = (BrowseView) findViewById(R.id.browseView_save_see_phone);
		// Resources res = getResources();
		// Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.banner);
		// float scale = (float) 600 / bmp.getWidth();
		// float scale2 = (float) 400 / bmp.getHeight();
		// currentMatrix.postScale(scale, scale2);
		// icon.setImageBitmap(bmp);// ���ÿؼ�ͼƬ
		imagurl = getIntent().getStringExtra("imgurl");
		setloadimageview(imagurl);// ����ͼƬ
		icon.setis_Editable(true);
		icon.setImageMatrix(currentMatrix);
		back=(ImageView) findViewById(R.id.imageView_myacitity_zuo);
		save=(TextView) findViewById(R.id.textview_myacitivity_you);
		back.setVisibility(View.VISIBLE);
		save.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		save.setOnClickListener(l);
	}
	private View.OnClickListener l=new View.OnClickListener() {
		
		@SuppressWarnings({ "deprecation", "deprecation" })
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo://���ذ�ť
				finish();
				break;
			case R.id.textview_myacitivity_you://���水ť
				try {
					AlertDialog isExit = new AlertDialog.Builder(Save_see_phoneActivity.this).create();
					// ���öԻ������
					isExit.setTitle("ϵͳ��ʾ");
					// ���öԻ�����Ϣ
					isExit.setMessage("ȷ��Ҫ����ͼƬ��");
					// ���ѡ��ť��ע�����
					isExit.setButton("ȷ��", listener);
					isExit.setButton2("ȡ��", listener);
					// ��ʾ�Ի���
					isExit.show();
				} catch (Exception e) {
				}
			default:
				break;
			}
			
		}
	};


	/** �����Ի��������button����¼� */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				Log.i("azy", "ȷ��ִ��");
				new SaveImage().execute(); // Android 4.0�Ժ�Ҫʹ���߳�����������
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};

	// ����ͼƬ�ķ���
	private void setloadimageview(String url) {
		try {
			ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
				@Override
				public void onResponse(Bitmap response) {
					icon.setImageBitmap(response);
				}
			}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					icon.setImageResource(R.drawable.ic_delete);
				}
			});
			SysApplication.getHttpQueues().add(imageRequest);
		} catch (Exception e) {
			Log.i("azy", "����ͼƬִ���˴������");
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
			Log.i("azy", "��ʼ����");
			String result = "";
			try {
				String sdcard = Environment.getExternalStorageDirectory().toString();
				File file = new File(sdcard + "/Download");
				if (!file.exists()) {
					file.mkdirs();
				}
				int idx = imagurl.lastIndexOf(".");
				String ext = imagurl.substring(idx);
				file = new File(sdcard + "/Download/" + new Date().getTime() + ext);
				InputStream inputStream = null;
				URL url = new URL(imagurl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(20000);
				if (conn.getResponseCode() == 200) {
					inputStream = conn.getInputStream();
					Log.i("azy", "���سɹ�");
				}else {
					Log.i("azy", "����ʧ��");
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
				Log.i("azy", "ȷ��ִ��333333333333");
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(Save_see_phoneActivity.this, result, Toast.LENGTH_SHORT).show();
			;
		}
	}

}
