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
 * վ�������¼���µ�֤���Ľ���
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
	private PopupWindow pop_time, pop_selecttype;// ����ѡ������ѡ��֤������
	private ImageView back;// ���ذ�ť
	private TextView finishi;// ���
	private GridView gri;// ���ͼƬ�ļ��϶���
	private PopupWindow pop_phone, pop_delete;// ���ͼƬ�ġ�ɾ��ͼƬ
	private Button bu1, bu2, bu3, delete, abrogate;// pop�ϵ�������ť���������ᡢȡ�� ɾ��ͼƬ��ȡ��

	private Item_tupian_adapter adapter_ph;// ���ͼƬ��adapter����
	// ��š����ơ������ˡ���֤�����ˡ���֤�����ˡ���ϵ�绰��������Ϣ����ע��
	private EditText sn, name, keeper, replacer, accepter, telphone, acceptinfo, remark;
	// ���͡���ʼʱ�䡢����ʱ�䡢����ʱ�䡢
	private TextView typeId, beginDate, endDate, alarmDate;
	private File[] f;// ��ʱ�洢ͼƬ���ļ�

	private static int number = 0;// ��¼ͼƬ�ϴ��ĸ���
	private int timemy = 0;//��¼ɾ��ͼƬ��ID
	private long phone_time;// ͼƬ����ʱ��
	private int record_delete_number = 0;// ͼƬɾ����ID
	private String stationId = "";//����վ��ID
	private Item_credential_genre_adapter adapter;//֤���б��adapter
	private List<Credential_genre>data;//֤���ļ���
	private String st_typeid="";//ѡ�е�֤����ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ������仰ǿ�����ϵı���ȥ��
		setContentView(R.layout.activity_centre_credential_addcredential);
		setview();// ���ý���ķ���
		set_initializebitmap();// ��ʼ��ͼƬ���ϵķ���
		gri.setOnItemClickListener(listener);// ͼƬ�б�ĵ���¼�
		// gri.setOnItemLongClickListener(listener2);//ͼƬ�б�ĳ����¼�
		getuserstationmessage();
	}

	/*
	 * ��ȡ��ǰ�û���������վ��Ϣ�ķ���
	 */
	private void getuserstationmessage() {
		String url = Myconstant.COMMONALITY_GETSTATIONMESSAGE;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
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
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	/*
	 * ���ý���ķ���
	 */
	private void setview() {
		set_title_text("���֤��");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		f = new File[4];

		finishi = (TextView) findViewById(R.id.textview_myacitivity_you);
		finishi.setText("���");
		finishi.setVisibility(View.VISIBLE);
		finishi.setOnClickListener(l);
		gri = (GridView) findViewById(R.id.gridView_centre_credential_addcredential_phone);// ���ͼƬ�ļ���

		adapter_ph = new Item_tupian_adapter(Centre_credential_addcredentialActivity.this);// ���ͼƬ��adapter

		gri.setAdapter(adapter_ph);
		gri.setSelector(new BitmapDrawable());
		
		// ʵ�������ͼƬ�ļ��Ϻ������������Ҹ���������������
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				Centre_credential_addcredentialActivity.this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);// ��������̵ķ���
		sn = (EditText) findViewById(R.id.editText_centre_credential_addcredential_number);// ���
		name = (EditText) findViewById(R.id.editText_centre_credential_addcredential_name);// ����
		typeId = (TextView) findViewById(R.id.textview_centre_credential_addcredential_type);// ����
		beginDate = (TextView) findViewById(R.id.textview_centre_credential_addcredential_starttime);// ��ʼʱ��
		endDate = (TextView) findViewById(R.id.textview_centre_credential_addcredential_endtime);// ����ʱ��
		alarmDate = (TextView) findViewById(R.id.textview_centre_credential_addcredential_remindtime);// ����ʱ��
		keeper = (EditText) findViewById(R.id.editText_centre_credential_addcredential_custodyname);// ������
		replacer = (EditText) findViewById(R.id.editText_centre_credential_addcredential_responsiblename);// ��֤������
		accepter = (EditText) findViewById(R.id.editText_centre_credential_addcredential_acceptname);// ������
		telphone = (EditText) findViewById(R.id.editText_centre_credential_addcredential_contacttype);// ��ϵ��ʽ
		acceptinfo = (EditText) findViewById(R.id.editText_centre_credential_addcredential_acceptmessage);// ������Ϣ
		remark = (EditText) findViewById(R.id.editText_centre_credential_addcredential_remarksmessage);// ��ע
		typeId.setOnClickListener(l);
		beginDate.setOnClickListener(l);
		endDate.setOnClickListener(l);
		alarmDate.setOnClickListener(l);
	}

	/*
	 * �ռ�ĵ���¼��ļ���
	 */
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_credential_addcredentialActivity.this);
				break;
			case R.id.textview_myacitivity_you:// ��ɰ�ť

				if (setexamine()) {
					Mytask ta = new Mytask();
					ta.execute();// ִ�н����ϴ����ݵķ���
				}
				break;
			case R.id.textView_popup_date_select_accomplish:// �ر�ʱ��ѡ����
				pop_time.dismiss();
				break;
			case R.id.textview_centre_credential_addcredential_type:// ֤������
				setshowtypepopup();// ��ʾѡ��֤�����͵�popup
				break;
			case R.id.textview_centre_credential_addcredential_starttime:// ��ʼʱ��
				setdateselect(beginDate);// ��ʾ����ѡ����
				break;
			case R.id.textview_centre_credential_addcredential_endtime:// ����ʱ��
				setdateselect(endDate);// ��ʾ����ѡ����
				break;
			case R.id.textview_centre_credential_addcredential_remindtime:// ����ʱ��
				setdateselect(alarmDate);// ��ʾ����ѡ����
				break;
			case R.id.btn_popup_photo:
				// �����

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
								Toast.makeText(Centre_credential_addcredentialActivity.this, "�Բ���APP���ִ������˳�APP���µ�¼",
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
					Toast.makeText(Centre_credential_addcredentialActivity.this, "�Բ��������SD��", Toast.LENGTH_SHORT)
							.show();
				}

				break;
			case R.id.btn_popup_picture:
				// ���
				try {
					number++;
					Intent intent;
					if (Build.VERSION.SDK_INT >= 19) {
						intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);

					} else {
						intent = new Intent(Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// ����android��ͼ��}

						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						// intent.setAction(Intent.ACTION_GET_CONTENT);
					}

					startActivityForResult(intent, 200);
				} catch (Exception e) {
					// TODO: handle exception
				}

				break;
			case R.id.btn_popup_enter:// ȡ����ť

				pop_phone.dismiss();// ����ʾ�����ͼƬ��popupwindow��������

				break;
			case R.id.button_popup_delete_photograph_delete:// ɾ��ͼƬ�İ�ť
				String[] str = new String[number];
				for (int i = 0; i < number; i++) {
					str[i] = f[i].getAbsolutePath();
				}
				adapter_ph.getData().remove(record_delete_number);
				adapter_ph.notifyDataSetChanged();

				Log.i("azy", "ѡ�е�ͼƬ��ID�ǣ�" + record_delete_number);
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
			case R.id.button_popup_delete_photograph_abrogate:// ɾ��ͼƬ��ȡ����ť
				pop_delete.dismiss();
				break;

			default:
				break;
			}

		}
	};

	/*
	 * ��ʾѡ��֤�����͵�popup
	 */
	private void setshowtypepopup(){
		View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this).inflate(R.layout.popup_listview,
				null);// ����view
		pop_selecttype = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���

		pop_selecttype.setBackgroundDrawable(new ColorDrawable());
		// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���

		pop_selecttype.showAtLocation(back, Gravity.BOTTOM, 0, 0);
		ListView list=(ListView) v1.findViewById(R.id.listView_poppup_listview_entry);
		adapter=new Item_credential_genre_adapter(Centre_credential_addcredentialActivity.this);
		list.setAdapter(adapter);
		settypedataloading();
		list.setOnItemClickListener(listener3);
	}
	/*
	 * ����֤�������б�����ݵķ���
	 */
	private void settypedataloading(){
		data=new ArrayList<Credential_genre>();
		String url = Myconstant.COMMONALITY_GETCERTIFICATETYPE;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
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
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	/*
	 * �����¼�ѡ�����ķ���
	 */
	private void setdateselect(final TextView tx) {
		View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this).inflate(R.layout.popup_date_select,
				null);// ����view
		pop_time = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���

		pop_time.setBackgroundDrawable(new ColorDrawable());
		// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���

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
				name.setText(year + "��" + (monthOfYear + 1) + "��" + dayOfMonth + "��");
				tx.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}

		});
	}

	/*
	 * ֤���б�ĵ���¼�
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
	 * ͼƬ���б���м����¼�
	 */
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (position == adapter_ph.getData().size() - 1) {
				if (number < 4) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							Centre_credential_addcredentialActivity.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);// ��������̵ķ���
					View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this)
							.inflate(R.layout.popup_phone_select, null);// ����view

					pop_phone = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���

					pop_phone.setBackgroundDrawable(new ColorDrawable());
					// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���

					pop_phone.showAtLocation(gri, Gravity.BOTTOM, 0, 0);
					bu1 = (Button) v1.findViewById(R.id.btn_popup_picture);// ��ᰴť

					bu2 = (Button) v1.findViewById(R.id.btn_popup_photo);// �����ť

					bu3 = (Button) v1.findViewById(R.id.btn_popup_enter);// ȡ����ť

					bu1.setOnClickListener(l);
					bu2.setOnClickListener(l);
					bu3.setOnClickListener(l);

				} else {
					Toast.makeText(Centre_credential_addcredentialActivity.this, "ͼƬ�ϴ������Ѿ������", 0).show();
				}

			}

		}
	};
	/*
	 * ͼƬ�ĳ����¼�
	 */
	private AdapterView.OnItemLongClickListener listener2 = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (position != number) {
				View v1 = LayoutInflater.from(Centre_credential_addcredentialActivity.this)
						.inflate(R.layout.popup_delete_photograph, null);// ����view
				record_delete_number = position;// ��¼�µ�ǰ�����ͼƬ��ID
				pop_delete = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���

				pop_delete.setBackgroundDrawable(new ColorDrawable());
				// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���

				pop_delete.showAtLocation(gri, Gravity.BOTTOM, 0, 0);
				delete = (Button) v1.findViewById(R.id.button_popup_delete_photograph_delete);// ɾ��ͼƬ�ķ���
				abrogate = (Button) v1.findViewById(R.id.button_popup_delete_photograph_abrogate);// ȡ����ť
				delete.setOnClickListener(l);
				abrogate.setOnClickListener(l);
			}

			return true;
		}
	};
	// ������ͼ ����ֵʱ���õķ���

	@SuppressLint("ShowToast")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {

			number--;
			pop_phone.dismiss();// popup��ʧ�ķ���

			if (requestCode == 200 && resultCode == RESULT_OK) {
				number++;
				// ��᷵��

				Uri uri = data.getData();
				String path = Phoneutil.getPath(Centre_credential_addcredentialActivity.this, uri);
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory.decodeFile(path, option);
				if (bitmap != null) {
					Bitmap newBitmap = Photograph_util.comp(bitmap);// ��ԭͼƬ����ѹ��

					// ��ԭͼƬ����ѹ��

					bitmap.recycle();// �ͷ�ԭͼƬռ�õ��ڴ�
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
					Toast.makeText(Centre_credential_addcredentialActivity.this, "�Բ���û�л�ȡ������е�ͼƬ", 0).show();
				}

			} else if (requestCode == 100 && resultCode == RESULT_OK) {
				number++;

				// ���������ȡ��ͼƬ

				if (f[number - 1] != null && f[number - 1].exists()) {
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = 2;
					Bitmap bitmap = BitmapFactory.decodeFile(f[number - 1].getPath(), option);
					if (bitmap != null) {
						Bitmap newBitmap = Photograph_util.comp(bitmap);// ��ԭͼƬ����ѹ��

						// ��ԭͼƬ����ѹ��

						bitmap.recycle();// �ͷ�ԭͼƬռ�õ��ڴ�

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

				// "/image" + number + ".jpg");// �õ�ͼƬbitmap

			}

		} catch (Exception e) {
			finish();
			Toast.makeText(Centre_credential_addcredentialActivity.this, "�Բ�����Ϊ�ֻ����⣬�����½������", 0).show();
		}

	};

	/*
	 * Ϊgridview�ȷ���һ�ų�ʼ����ͼƬ
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
	 * ���Ҫ�ϴ��������Ƿ���д��� // ��š����ơ������ˡ���֤�����ˡ���֤�����ˡ���ϵ�绰��������Ϣ����ע�� private EditText
	 * sn, name, keeper, replacer, accepter, telphone, acceptinfo, remark; //
	 * ���͡���ʼʱ�䡢����ʱ�䡢����ʱ�䡢 private TextView typeId, beginDate, endDate,
	 * alarmDate;
	 */
	private boolean setexamine() {
		if (sn.getText().toString().equals("") || name.getText().toString().equals("")
				|| keeper.getText().toString().equals("") || replacer.getText().toString().equals("")
				|| accepter.getText().toString().equals("") || telphone.getText().toString().equals("")
				|| acceptinfo.getText().toString().equals("") || remark.getText().toString().equals("")
				|| typeId.getText().toString().equals("��ѡ��") || beginDate.getText().toString().equals("��ѡ��")
				|| endDate.getText().toString().equals("��ѡ��") || alarmDate.getText().toString().equals("��ѡ��")) {
			Toast.makeText(Centre_credential_addcredentialActivity.this, "���������Ϣ����ȷ���ύ��", 0).show();
			return false;

		}
		return true;

	}

	/*
	 * �ϴ�ά�޵��ķ���
	 */
	private class Mytask extends AsyncTask<Void, Void, String> {

		String str = Myconstant.CENTRE_SUBMITCERTIFICATE;

		@Override
		protected String doInBackground(Void... params) {
			finishi.setClickable(false);
			Map<String, Object> map = new HashMap<String, Object>();
			switch (number) {// ����ѡ��ͼƬ�ĸ���������Ӧ��ͼƬ

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
			map.put("sn", sn.getText().toString());// ����
			map.put("name", name.getText().toString());// ����
			map.put("typeId", st_typeid);// ����
			map.put("beginDate", beginDate.getText().toString());// ����
			map.put("endDate", endDate.getText().toString());// ����
			map.put("alarmDate", alarmDate.getText().toString());// ����
			map.put("keeper", keeper.getText().toString());// ����
			map.put("replacer", replacer.getText().toString());// ����
			map.put("accepter", accepter.getText().toString());// ����
			map.put("telphone", telphone.getText().toString());// ����
			map.put("acceptinfo", acceptinfo.getText().toString());// ����
			map.put("remark", remark.getText().toString());// ����

			return postFileToString(str, map);
		}

		@Override
		protected void onPostExecute(String result) {
			finishi.setClickable(true);
			if (result.equals("")) {
				Toast.makeText(Centre_credential_addcredentialActivity.this, "�Բ�����Ϊ�����ԭ�򣬱��޵��ϴ�ʧ�ܡ�", 0).show();
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
	// post�����ļ�����string����

	@SuppressWarnings("unused")
	@SuppressLint("ShowToast")
	public static String postFileToString(String urlStr, Map<String, Object> map) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpParams params = client.getParams();
			HttpPost request = new HttpPost(urlStr);
			request.addHeader("accept", "application/json");// �ı�ͷ��Ϣ��
			request.addHeader("api_key", Myconstant.token);// �ı�ͷ��Ϣ��
			MultipartEntity entity = new MultipartEntity();
			Set<String> keys = map.keySet();
			for (String key : keys) {
				Object value = map.get(key);
				if (value instanceof File) {
					if (value != null) {
						entity.addPart("files", new FileBody((File) value));
					} else {
						Log.i("azy", "�ļ��ǿյ�");
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
				System.out.println("�ϴ�ʧ�ܣ�" + response.getStatusLine().getStatusCode() + EntityUtils.toString(entity2));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
