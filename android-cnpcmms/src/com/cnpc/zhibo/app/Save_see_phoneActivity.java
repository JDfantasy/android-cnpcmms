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
 * 查看并且保存图片的方法
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
	private BrowseView icon;// 自定义图片
	private String imagurl = "";// 图片的网址
	private ImageView back;//返回
	private TextView save;//保存

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_see_phone);
		set_title_text("查看图片");
		icon = (BrowseView) findViewById(R.id.browseView_save_see_phone);
		// Resources res = getResources();
		// Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.banner);
		// float scale = (float) 600 / bmp.getWidth();
		// float scale2 = (float) 400 / bmp.getHeight();
		// currentMatrix.postScale(scale, scale2);
		// icon.setImageBitmap(bmp);// 设置控件图片
		imagurl = getIntent().getStringExtra("imgurl");
		setloadimageview(imagurl);// 加载图片
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
			case R.id.imageView_myacitity_zuo://返回按钮
				finish();
				break;
			case R.id.textview_myacitivity_you://保存按钮
				try {
					AlertDialog isExit = new AlertDialog.Builder(Save_see_phoneActivity.this).create();
					// 设置对话框标题
					isExit.setTitle("系统提示");
					// 设置对话框消息
					isExit.setMessage("确定要下载图片吗");
					// 添加选择按钮并注册监听
					isExit.setButton("确定", listener);
					isExit.setButton2("取消", listener);
					// 显示对话框
					isExit.show();
				} catch (Exception e) {
				}
			default:
				break;
			}
			
		}
	};


	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				Log.i("azy", "确认执行");
				new SaveImage().execute(); // Android 4.0以后要使用线程来访问网络
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	// 下载图片的方法
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
			Log.i("azy", "下载图片执行了错误机制");
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
			Log.i("azy", "开始下载");
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
					Log.i("azy", "下载成功");
				}else {
					Log.i("azy", "下载失败");
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
				Log.i("azy", "确认执行333333333333");
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
