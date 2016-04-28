package com.cnpc.zhibo.app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.adapter.Item_credential_genre_adapter;
import com.cnpc.zhibo.app.adapter.Item_tupian_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.entity.Credential_genre;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.util.Phoneutil;
import com.cnpc.zhibo.app.util.Photograph_util;

import android.annotation.SuppressLint;
/*
 * 站长端添加录入新的证件的界面
 */
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;

public class Centre_credential_addcredentialActivity extends MyActivity {
	private PopupWindow pop_time, pop_selecttype;// 日期选择器、选择证件类型
	private ImageView back;// 返回按钮
	private TextView finishi;// 完成
	private GridView gri;// 存放图片的集合对象
	private PopupWindow pop_phone, pop_delete;// 添加图片的、删除图片
	private Button bu1, bu2, bu3, delete, abrogate;// pop上的三个按钮：相机、相册、取消 删除图片、取消

	private Item_tupian_adapter adapter_ph;// 存放图片的adapter对象
	// 编号、名称、保管人、换证负责人、换证受理人、联系电话、受理信息、备注、
	private EditText sn, name, keeper, replacer, accepter, telphone, acceptinfo, remark;
	// 类型、开始时间、结束时间、提醒时间、
	private TextView typeId, beginDate, endDate, alarmDate;
	private File[] f;// 暂时存储图片的文件

	private static int number = 0;// 记录图片上传的个数
	private int timemy = 0;//记录删除图片的ID
	private long phone_time;// 图片拍照时间
	private int record_delete_number = 0;// 图片删除的ID
	private String stationId = "";//加油站的ID
	private Item_credential_genre_adapter adapter;//证件列表的adapter
	private List<Credential_genre>data;//证件的集合
	private String st_typeid="";//选中的证件的ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置这句话强布局上的标题去掉
		setContentView(R.layout.activity_centre_credential_addcredential);
		setview();// 设置界面的方法
		set_initializebitmap();// 初始化图片集合的方法
		gri.setOnItemClickListener(listener);// 图片列表的点击事件
		// gri.setOnItemLongClickListener(listener2);//图片列表的长按事件
		getuserstationmessage();
	}

	/*
	 * 获取当前用户所属加油站信息的方法
	 */
	private void getuserstationmessage() {
		String url = Myconstant.COMMONALITY_GETSTATIONMESSAGE;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONObject js2 = js.getJSONObject("body");
						stationId = js2.getString("id");
					} else {
						Toast.makeText(Centre_credential_addcredentialActivity.this, js1.getString("content"), 0)
								.show();
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
	 * 设置界面的方法
	 */
	private void setview() {
		set_title_text("添加证件");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		f = new File[4];

		finishi = (TextView) findViewById(R.id.textview_myacitivity_you);
		finishi.setText("完成");
		finishi.setVisibility(View.VISIBLE);
		finishi.setOnClickListener(l);
		gri = (GridView) findViewById(R.id.gridView_centre_credential_addcredential_phone);// 存放图片的集合

		adapter_ph = new Item_tupian_adapter(Centre_credential_addcredentialActivity.this);// 存放图片的adapter

		gri.setAdapter(adapter_ph);
		gri.setSelector(new BitmapDrawable());
		
		// 实例化存放图片的集合和适配器，并且给集合设置适配器
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				Centre_credential_addcredentialActivity.this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏软键盘的方法
		sn = (EditText) findViewById(R.id.editText_centre_credential_addcredential_number);// 编号
		name = (EditText) findViewById(R.id.editText_centre_credential_addcredential_name);// 名称
		typeId = (TextView) findViewById(R.id.textview_centre_credential_addcredential_type);// 类型
		beginDate = (TextView) findViewById(R.id.textview_centre_credential_addcredential_starttime);// 开始时间
		endDate = (TextView) findViewById(R.id.textview_centre_credential_addcredential_endtime);// 结束时间
		alarmDate = (TextView) findViewById(R.id.textview_centre_credential_addcredential_remindtime);// 提醒时间
		keeper = (EditText) findViewById(R.id.editText_centre_credential_addcredential_custodyname);// 保管人
		replacer = (EditText) findViewById(R.id.editText_centre_credential_addcredential_responsiblename);// 换证负责人
		accepter = (EditText) findViewById(R.id.editText_centre_credential_addcredential_acceptname);// 受理人
		telphone = (EditText) findViewById(R.id.editText_centre_credential_addcredential_contacttype);// 联系方式
		acceptinfo = (EditText) findViewById(R.id.editText_centre_credential_addcredential_acceptmessage);// 受理信息
		remark = (EditText) findViewById(R.id.editText_centre_credential_addcredential_remarksmessage);// 备注
		typeId.setOnClickListener(l);
		beginDate.setOnClickListener(l);
		endDate.setOnClickListener(l);
		alarmDate.setOnClickListener(l);
	}

	/*
	 * 空间的点击事件的监听
	 */
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_credential_addcredentialActivity.this);
				break;
			case R.id.textview_myacitivity_you:// 完成按钮

				if (setexamine()) {
					Mytask ta = new Mytask();
					ta.execute();// 执行进行上传数据的方法
				}
				break;
			case R.id.textView_popup_date_select_accomplish:// 关闭时间选择器
				pop_time.dismiss();
				break;
			case R.id.textview_centre_credential_addcredential_type:// 证件类型
				setshowtypepopup();// 显示选择证件类型的popup
				break;
			case R.id.textview_centre_credential_addcredential_starttime:// 开始时间
				setdateselect(beginDate);// 显示日期选择器
				break;
			case R.id.textview_centre_credential_addcredential_endtime:// 结束时间
				setdateselect(endDate);// 显示日期选择器
				break;
			case R.id.textview_centre_credential_addcredential_remindtime:// 提醒时间
				setdateselect(alarmDate);// 显示日期选择器
				break;
			case R.id.btn_popup_photo:
				// 照相机

				phone_time = System.currentTimeMillis();
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					try {
						number++;
						f[number - 1] = new File(Environment.getExternalStorageDirectory().getPath(),
								"image" + phone_time + ".jpg");
						f[number - 1].delete();
						if (!f[number - 1].exists()) {
							try {
								f[number - 1].createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
								Toast.makeText(Centre_credential_addcredentialActivity.this, "对不起APP出现错误，请退出APP重新登录",
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
					Toast.makeText(Centre_credential_addcredentialActivity.this, "对不清请插入SD卡", Toast.LENGTH_SHORT)
							.show();
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

				pop_phone.dismiss();// 将显示的添加图片的popupwindow进行隐藏

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

	/*
	 * 显示选择证件类型的popup
	 */
	private void setshowtypepopup(){
		View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this).inflate(R.layout.popup_listview,
				null);// 创建view
		pop_selecttype = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法

		pop_selecttype.setBackgroundDrawable(new ColorDrawable());
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法

		pop_selecttype.showAtLocation(back, Gravity.BOTTOM, 0, 0);
		ListView list=(ListView) v1.findViewById(R.id.listView_poppup_listview_entry);
		adapter=new Item_credential_genre_adapter(Centre_credential_addcredentialActivity.this);
		list.setAdapter(adapter);
		settypedataloading();
		list.setOnItemClickListener(listener3);
	}
	/*
	 * 加载证件类型列表的数据的方法
	 */
	private void settypedataloading(){
		data=new ArrayList<Credential_genre>();
		String url = Myconstant.COMMONALITY_GETCERTIFICATETYPE;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONArray js2 = js.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3=js2.getJSONObject(i);
							data.add(new Credential_genre(js3.getString("id"), js3.getString("name")));
						}
						adapter.addDataBottom(data);
					} else {
						Toast.makeText(Centre_credential_addcredentialActivity.this, js1.getString("content"), 0)
								.show();
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
	 * 调用事件选择器的方法
	 */
	private void setdateselect(final TextView tx) {
		View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this).inflate(R.layout.popup_date_select,
				null);// 创建view
		pop_time = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法

		pop_time.setBackgroundDrawable(new ColorDrawable());
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法

		pop_time.showAtLocation(back, Gravity.BOTTOM, 0, 0);
		final TextView name, accomplish;
		DatePicker date;
		name = (TextView) v1.findViewById(R.id.textView_popup_date_select_time);
		date = (DatePicker) v1.findViewById(R.id.datePicker_popup_date_select);
		accomplish = (TextView) v1.findViewById(R.id.textView_popup_date_select_accomplish);
		accomplish.setOnClickListener(l);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		date.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener() {

			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				name.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
				tx.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}

		});
	}

	/*
	 * 证件列表的点击事件
	 */
	private AdapterView.OnItemClickListener listener3 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			typeId.setText(adapter.getData().get(position).name);
			st_typeid=adapter.getData().get(position).id;
			pop_selecttype.dismiss();
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
							Centre_credential_addcredentialActivity.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏软键盘的方法
					View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this)
							.inflate(R.layout.popup_phone_select, null);// 创建view

					pop_phone = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法

					pop_phone.setBackgroundDrawable(new ColorDrawable());
					// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法

					pop_phone.showAtLocation(gri, Gravity.BOTTOM, 0, 0);
					bu1 = (Button) v1.findViewById(R.id.btn_popup_picture);// 相册按钮

					bu2 = (Button) v1.findViewById(R.id.btn_popup_photo);// 相机按钮

					bu3 = (Button) v1.findViewById(R.id.btn_popup_enter);// 取消按钮

					bu1.setOnClickListener(l);
					bu2.setOnClickListener(l);
					bu3.setOnClickListener(l);

				} else {
					Toast.makeText(Centre_credential_addcredentialActivity.this, "图片上传数量已经最大了", 0).show();
				}

			}

		}
	};
	/*
	 * 图片的长按事件
	 */
	private AdapterView.OnItemLongClickListener listener2 = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (position != number) {
				View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this)
						.inflate(R.layout.popup_delete_photograph, null);// 创建view
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
	// 利用意图 返回值时调用的方法

	@SuppressLint("ShowToast")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {

			number--;
			pop_phone.dismiss();// popup消失的方法

			if (requestCode == 200 && resultCode == RESULT_OK) {
				number++;
				// 相册返回

				Uri uri = data.getData();
				String path = Phoneutil.getPath(Centre_credential_addcredentialActivity.this, uri);
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
					Toast.makeText(Centre_credential_addcredentialActivity.this, "对不起，没有获取到相册中的图片", 0).show();
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
								Environment.getExternalStorageDirectory() + "/image" + phone_time + ".jpg");
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
			Toast.makeText(Centre_credential_addcredentialActivity.this, "对不起，因为手机问题，请重新进入界面", 0).show();
		}

	};

	/*
	 * 为gridview先放置一张初始化的图片
	 */
	private void set_initializebitmap() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.addphonebutton);
		adapter_ph.addDataBottom(bitmap);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			number = 0;
			timemy = 0;
			adapter_ph.getData().clear();
			finish();
			Myutil.set_activity_close(Centre_credential_addcredentialActivity.this);
		}

		return false;

	}

	/*
	 * 检查要上传的内容是否填写完毕 // 编号、名称、保管人、换证负责人、换证受理人、联系电话、受理信息、备注、 private EditText
	 * sn, name, keeper, replacer, accepter, telphone, acceptinfo, remark; //
	 * 类型、开始时间、结束时间、提醒时间、 private TextView typeId, beginDate, endDate,
	 * alarmDate;
	 */
	private boolean setexamine() {
		if (sn.getText().toString().equals("") || name.getText().toString().equals("")
				|| keeper.getText().toString().equals("") || replacer.getText().toString().equals("")
				|| accepter.getText().toString().equals("") || telphone.getText().toString().equals("")
				|| acceptinfo.getText().toString().equals("") || remark.getText().toString().equals("")
				|| typeId.getText().toString().equals("请选择") || beginDate.getText().toString().equals("请选择")
				|| endDate.getText().toString().equals("请选择") || alarmDate.getText().toString().equals("请选择")) {
			Toast.makeText(Centre_credential_addcredentialActivity.this, "请先填好信息，在确认提交。", 0).show();
			return false;

		}
		return true;

	}

	/*
	 * 上传维修单的方法
	 */
	private class Mytask extends AsyncTask<Void, Void, String> {

		String str = Myconstant.CENTRE_SUBMITCERTIFICATE;

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

			map.put("stationId", stationId);
			map.put("sn", sn.getText().toString());// 标题
			map.put("name", name.getText().toString());// 标题
			map.put("typeId", st_typeid);// 标题
			map.put("beginDate", beginDate.getText().toString());// 标题
			map.put("endDate", endDate.getText().toString());// 标题
			map.put("alarmDate", alarmDate.getText().toString());// 标题
			map.put("keeper", keeper.getText().toString());// 标题
			map.put("replacer", replacer.getText().toString());// 标题
			map.put("accepter", accepter.getText().toString());// 标题
			map.put("telphone", telphone.getText().toString());// 标题
			map.put("acceptinfo", acceptinfo.getText().toString());// 标题
			map.put("remark", remark.getText().toString());// 标题

			return postFileToString(str, map);
		}

		@Override
		protected void onPostExecute(String result) {
			finishi.setClickable(true);
			if (result.equals("")) {
				Toast.makeText(Centre_credential_addcredentialActivity.this, "对不起，因为网络等原因，报修单上传失败。", 0).show();
				finish();
				Myutil.set_activity_close(Centre_credential_addcredentialActivity.this);
			} else {
				try {
					JSONObject js = new JSONObject(result);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Centre_credential_addcredentialActivity.this, js1.getString("content"), 0)
								.show();
						startActivity(
								new Intent(Centre_credential_addcredentialActivity.this, Centre_HomeActivity.class));
						Myutil.set_activity_open(Centre_credential_addcredentialActivity.this);
						number = 0;
						finish();
					} else {
						Toast.makeText(Centre_credential_addcredentialActivity.this, js1.getString("content"), 0)
								.show();
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
