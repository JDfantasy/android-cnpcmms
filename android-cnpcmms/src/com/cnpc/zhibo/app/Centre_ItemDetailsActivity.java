package com.cnpc.zhibo.app;

//站长端填写选中的要保修的项目详情的界面
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.adapter.Item_centre_supplier_adapter;
import com.cnpc.zhibo.app.adapter.Item_tupian_adapter;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_Repairs_item;
import com.cnpc.zhibo.app.entity.Supplier_message;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.util.Phoneutil;
import com.cnpc.zhibo.app.util.Photograph_util;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_ItemDetailsActivity extends MyActivity {
	private ImageView fanhui;// 返回按钮

	private TextView finishi;// 完成

	private TextView name;// 项目名称

	private Spinner supplier;// 供应商

	private EditText title, remarks;// 项目的描述

	private GridView gri;// 存放图片的集合对象

	private Item_centre_supplier_adapter adapter_supplier;// 供应商的adapter

	private List<Supplier_message> data_supplier;// 供应商的数据集合

	private PopupWindow pop, pop_delete;// 添加图片的、删除图片
	private Button bu1, bu2, bu3, delete, abrogate;// pop上的三个按钮：相机、相册、取消 删除图片、取消

	private Item_tupian_adapter adapter_ph;// 存放图片的adapter对象

	private File[] f;// 暂时存储图片的文件

	private static int number = 0;// 记录图片上传的个数

	private RequestQueue mqueQueue;// 请求对象
	private int timemy = 0;

	private String st_name = "", st_projects = "", st_providerId = "", st_description = "", st_remarks = "无备注信息";
	private long time;
	private int record_delete_number = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_item_details);
		setview();// 加载界面上半部分的控件的方法

		setviewitem();// 加载界面下半部分的控件的方法
		name.setText("报修项目："+getIntent().getStringExtra("name"));
		st_projects = getIntent().getStringExtra("id");
		setdata_spinner();// 加载供应商的数据
		supplier.setOnItemSelectedListener(sp_supplier);// 给供应商设置监听

		set_initializebitmap();// 加载添加图片的按钮

		gri.setOnItemClickListener(listener);// 图片列表的点击事件
		// gri.setOnItemLongClickListener(listener2);//图片列表的长按事件

	}

	/*
	 * 图片的长按事件
	 */
	private AdapterView.OnItemLongClickListener listener2 = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (position != number) {
				View v1 = LayoutInflater.from(Centre_ItemDetailsActivity.this).inflate(R.layout.popup_delete_photograph,
						null);// 创建view
				record_delete_number = position;// 记录下当前点击的图片的ID
				pop_delete = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法

				pop_delete.setBackgroundDrawable(new ColorDrawable());
				// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法

				pop_delete.showAtLocation(gri, Gravity.BOTTOM, 0, 0);
				delete = (Button) v1.findViewById(R.id.button_popup_delete_photograph_delete);// 删除图片的方法
				abrogate = (Button) v1.findViewById(R.id.button_popup_delete_photograph_abrogate);// 取消按钮
				delete.setOnClickListener(l);
				abrogate.setOnClickListener(l);
			}

			return true;
		}
	};
	/*
	 * 图片的列表的行监听事件
	 */
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (position == adapter_ph.getData().size() - 1) {
				if (number < 4) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							Centre_ItemDetailsActivity.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏软键盘的方法
					View v1 = LayoutInflater.from(Centre_ItemDetailsActivity.this).inflate(R.layout.popup_phone_select,
							null);// 创建view

					pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法

					pop.setBackgroundDrawable(new ColorDrawable());
					// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法

					pop.showAtLocation(gri, Gravity.BOTTOM, 0, 0);
					bu1 = (Button) v1.findViewById(R.id.btn_popup_picture);// 相册按钮

					bu2 = (Button) v1.findViewById(R.id.btn_popup_photo);// 相机按钮

					bu3 = (Button) v1.findViewById(R.id.btn_popup_enter);// 取消按钮

					bu1.setOnClickListener(l);
					bu2.setOnClickListener(l);
					bu3.setOnClickListener(l);

				} else {
					Toast.makeText(Centre_ItemDetailsActivity.this, "图片上传数量已经最大了", 0).show();
				}

			}

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			number = 0;
			timemy=0;
			adapter_ph.getData().clear();
			finish();
			Myutil.set_activity_close(Centre_ItemDetailsActivity.this);
		}

		return false;

	}

	// 为gridview先放置一张初始化的图片

	private void set_initializebitmap() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.addphonebutton);
		adapter_ph.addDataBottom(bitmap);
	}

	// 选中供应商的监听事件

	private AdapterView.OnItemSelectedListener sp_supplier = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			st_providerId = adapter_supplier.getData().get(position).id;// 供应商的id

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	// 设置界面下半部分的方法

	private void setviewitem() {
		name = (TextView) findViewById(R.id.textview_centre_item_details_name);// 项目的名称

		supplier = (Spinner) findViewById(R.id.spinner_centre_item_details_gongyingsahng);// 供应商

		title = (EditText) findViewById(R.id.editText_centre_item_details_miaoshu);// 项目的描述

		remarks = (EditText) findViewById(R.id.editText_centre_item_details_remarks);// 项目的描述

		gri = (GridView) findViewById(R.id.gridView_centre_item_details_tupian);// 存放图片的集合

		adapter_ph = new Item_tupian_adapter(Centre_ItemDetailsActivity.this);// 存放图片的adapter

		gri.setAdapter(adapter_ph);
		gri.setSelector(new BitmapDrawable());
		// 实例化存放图片的集合和适配器，并且给集合设置适配器
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				Centre_ItemDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏软键盘的方法
		adapter_supplier = new Item_centre_supplier_adapter(Centre_ItemDetailsActivity.this);
		data_supplier = new ArrayList<Supplier_message>();
		supplier.setAdapter(adapter_supplier);

	}

	// 点击确认提交的方法

	@SuppressLint("ShowToast")
	private void set_submit() {

		if (getdescription()) {// 获取要上传的数据的方法

			Toast.makeText(Centre_ItemDetailsActivity.this, "正在提交报修单请稍后", 0).show();
			Mytask ta = new Mytask();
			ta.execute();// 执行进行上传数据的方法

		}

	}

	// 获取要上传的数据

	private boolean getdescription() {

		st_description = title.getText().toString();
		if (!remarks.getText().toString().equals("")) {
			st_remarks = remarks.getText().toString();
		}

		if (st_description.length() > 10) {
			st_name = st_description.substring(0, 10);
		} else {
			st_name = st_description;
		}
		if (st_description.equals("")) {
			Toast.makeText(this, "请填写问题描述", 0).show();
			return false;
		}
		if (st_providerId.equals("")) {
			Toast.makeText(this, "请选择供应商", 0).show();
			return false;
		}
		if (st_projects.equals("")) {
			Toast.makeText(this, "请选择要保修的项目", 0).show();
			return false;
		}
		return true;

	}

	// 设置界面上半部分的方法

	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);// 请求对象

		f = new File[4];
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		set_title_text("报修项目");// 设置界面的标题

		finishi = (TextView) findViewById(R.id.textview_myacitivity_you);
		finishi.setText("完成");
		finishi.setVisibility(View.VISIBLE);
		finishi.setOnClickListener(l);

	}

	// 界面上控件的点击事件

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮

				number = 0;
				adapter_ph.getData().clear();
				finish();

				Myutil.set_activity_close(Centre_ItemDetailsActivity.this);// 设置切换界面的效果

				break;
			case R.id.textview_myacitivity_you:// 完成按钮

				// 判断用户是否已经将所选的项目是否填写完整，当用户填写完成并且全部提交时，返回站长的首页

				set_submit();

				break;

			case R.id.btn_popup_photo:
				// 照相机

				time = System.currentTimeMillis();
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					try {
						number++;
						f[number - 1] = new File(Environment.getExternalStorageDirectory().getPath(),
								"image" + time + ".jpg");
						f[number - 1].delete();
						if (!f[number - 1].exists()) {
							try {
								f[number - 1].createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
								Toast.makeText(Centre_ItemDetailsActivity.this, "对不起APP出现错误，请退出APP重新登录",
										Toast.LENGTH_LONG).show();
								return;
							}
						}
						Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
						intent1.addCategory("android.intent.category.DEFAULT");
						Uri imageUri = Uri.fromFile(f[number - 1]);
						intent1.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent1, 100);//

					} catch (Exception e) {

					}
				} else {
					Toast.makeText(Centre_ItemDetailsActivity.this, "对不清请插入SD卡", Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.btn_popup_picture:
				// 相册
				try {
					number++;
					Intent intent;
					if (Build.VERSION.SDK_INT >= 19) {
						intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);

					} else {
						intent = new Intent(Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库}

						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						// intent.setAction(Intent.ACTION_GET_CONTENT);
					}

					startActivityForResult(intent, 200);
				} catch (Exception e) {
					// TODO: handle exception
				}

				break;
			case R.id.btn_popup_enter:// 取消按钮

				pop.dismiss();// 将显示的popupwindow进行隐藏

				break;
			case R.id.button_popup_delete_photograph_delete:// 删除图片的按钮
				String[] str = new String[number];
				for (int i = 0; i < number; i++) {
					str[i] = f[i].getAbsolutePath();
				}
				adapter_ph.getData().remove(record_delete_number);
				adapter_ph.notifyDataSetChanged();

				Log.i("azy", "选中的图片的ID是：" + record_delete_number);
				if (record_delete_number != 0) {
					if (number == 4) {
						for (int j = (adapter_ph.getData().size()
								- record_delete_number); j < (adapter_ph.getData().size() - record_delete_number)
										+ record_delete_number; j++) {
							f[j] = new File(str[j + 1]);
						}
					} else {
						for (int j = (adapter_ph.getData().size() - 1
								- record_delete_number); j < (adapter_ph.getData().size() - 1 - record_delete_number)
										+ record_delete_number; j++) {
							f[j] = new File(str[j + 1]);
						}
					}

				}
				if (number == 4) {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.addphonebutton);
					adapter_ph.addDataBottom(bitmap);
				}
				number--;
				pop_delete.dismiss();
				break;
			case R.id.button_popup_delete_photograph_abrogate:// 删除图片的取消按钮
				pop_delete.dismiss();
				break;
			default:
				break;
			}

		}
	};

	// 利用意图 返回值时调用的方法

	@SuppressLint("ShowToast")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {

			number--;
			pop.dismiss();// popup消失的方法

			if (requestCode == 200 && resultCode == RESULT_OK) {
				number++;
				// 相册返回

				Uri uri = data.getData();
				String path = Phoneutil.getPath(Centre_ItemDetailsActivity.this, uri);
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory.decodeFile(path, option);
				if (bitmap != null) {
					Bitmap newBitmap = Photograph_util.comp(bitmap);// 将原图片进行压缩

					// 将原图片进行压缩

					bitmap.recycle();// 释放原图片占用的内存
					timemy++;
					f[number - 1] = Photograph_util.yasuoimage(newBitmap,
							Environment.getExternalStorageDirectory() + "/image" + timemy + ".jpg");
					adapter_ph.addDataTop(newBitmap);
					if (adapter_ph.getData().size() > 4) {
						adapter_ph.getData().remove(4);
						adapter_ph.notifyDataSetChanged();
					}
				} else {
					number--;
					Toast.makeText(Centre_ItemDetailsActivity.this, "对不起，没有获取到相册中的图片", 0).show();
				}

			} else if (requestCode == 100 && resultCode == RESULT_OK) {
				number++;

				// 从照相机获取的图片

				if (f[number - 1] != null && f[number - 1].exists()) {
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = 2;
					Bitmap bitmap = BitmapFactory.decodeFile(f[number - 1].getPath(), option);
					if (bitmap != null) {
						Bitmap newBitmap = Photograph_util.comp(bitmap);// 将原图片进行压缩

						// 将原图片进行压缩

						bitmap.recycle();// 释放原图片占用的内存

						f[number - 1] = Photograph_util.yasuoimage(newBitmap,
								Environment.getExternalStorageDirectory() + "/image" + time + ".jpg");
						adapter_ph.addDataTop(newBitmap);
						if (adapter_ph.getData().size() > 4) {
							adapter_ph.getData().remove(4);
							adapter_ph.notifyDataSetChanged();
						}
					}

				}
				// Bitmap bitmap = BitmapFactory

				// .decodeFile(Environment.getExternalStorageDirectory() +

				// "/image" + number + ".jpg");// 拿到图片bitmap

			}

		} catch (Exception e) {
			finish();
			Toast.makeText(Centre_ItemDetailsActivity.this, "对不起，因为手机问题，请重新进入界面", 0).show();
		}

	};

	// 给供应商下拉列表添加数据的方法

	private void setdata_spinner() {

		String url = Myconstant.CENTRE_GETREPAIRSITEMENTY + st_projects;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject js1 = response.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_supplier.add(new Supplier_message(js3.getString("id"), js3.getString("name")));
						}
						if (data_supplier.size() == 0) {
							Toast.makeText(Centre_ItemDetailsActivity.this, "对不起你选择项目，暂时无法提供服务。", 0).show();
							finish();
							Myutil.set_activity_close(Centre_ItemDetailsActivity.this);
						}
						adapter_supplier.addDataBottom(data_supplier);
					} else {

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Centre_ItemDetailsActivity.this, "供应商加载数据失败，请重新登录", 0).show();

			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				map.put("accept", "application/json");
				map.put("api_key", Myconstant.token);
				return map;

			}
		};

		mqueQueue.add(re);

	}

	// 上传维修单的方法

	private class Mytask extends AsyncTask<Void, Void, String> {

		String str = Myconstant.CENTRE_SUBMITREPAIRSITEM;

		@Override
		protected String doInBackground(Void... params) {
			finishi.setClickable(false);
			Map<String, Object> map = new HashMap<String, Object>();
			switch (number) {// 根据选择图片的个数传递相应的图片

			case 1:
				map.put("file1", f[0]);
				break;
			case 2:
				map.put("file1", f[0]);
				map.put("file2", f[1]);
				break;
			case 3:
				map.put("file1", f[0]);
				map.put("file2", f[1]);
				map.put("file3", f[2]);
				break;
			case 4:
				map.put("file1", f[0]);
				map.put("file2", f[1]);
				map.put("file3", f[2]);
				map.put("file4", f[3]);
				break;

			default:
				break;
			}

			map.put("name", st_name);// 标题

			map.put("projects", st_projects);// 保修项目

			map.put("providerId", st_providerId);// 供应商的id

			map.put("description", st_description);// 问题描述

			map.put("remark", st_remarks);// 备注信息

			return postFileToString(str, map);
		}

		@Override
		protected void onPostExecute(String result) {
			finishi.setClickable(true);
			if (result.equals("")) {
				Toast.makeText(Centre_ItemDetailsActivity.this, "对不起，因为网络等原因，报修单上传失败。", 0).show();
				finish();
				Myutil.set_activity_close(Centre_ItemDetailsActivity.this);
			} else {
				try {
					JSONObject js = new JSONObject(result);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Centre_ItemDetailsActivity.this, js1.getString("content"), 0).show();
						startActivity(new Intent(Centre_ItemDetailsActivity.this, Centre_HomeActivity.class));
						Myutil.set_activity_open(Centre_ItemDetailsActivity.this);
						number = 0;
						finish();
					} else {
						Toast.makeText(Centre_ItemDetailsActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			}

		}

	}

	// post传递文件返回string数据

	@SuppressWarnings("unused")
	@SuppressLint("ShowToast")
	public static String postFileToString(String urlStr, Map<String, Object> map) {
		try {
			HttpClient client = new DefaultHttpClient();

			HttpParams params = client.getParams();
			// params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,

			// "UTF-8");// API 识别 charset

			// HttpConnectionParams.setConnectionTimeout(params, 2000);

			// HttpConnectionParams.setSoTimeout(params, 2000);

			HttpPost request = new HttpPost(urlStr);
			request.addHeader("accept", "application/json");// 改变头消息的

			request.addHeader("api_key", Myconstant.token);// 改变头消息的

			MultipartEntity entity = new MultipartEntity();
			Set<String> keys = map.keySet();
			for (String key : keys) {
				Object value = map.get(key);
				if (value instanceof File) {
					if (value != null) {
						entity.addPart("files", new FileBody((File) value));
					} else {
						Log.i("azy", "文件是空的");
					}

				} else {
					entity.addPart(key, new StringBody(value.toString(), Charset.forName("UTF-8")));
				}
			}

			request.setEntity(entity);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 201) {
				HttpEntity entity2 = response.getEntity();
				return EntityUtils.toString(entity2);
			} else {
				HttpEntity entity2 = response.getEntity();
				System.out.println("上传失败：" + response.getStatusLine().getStatusCode() + EntityUtils.toString(entity2));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}