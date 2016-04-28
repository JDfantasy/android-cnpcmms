package com.cnpc.zhibo.app;

//վ������дѡ�е�Ҫ���޵���Ŀ����Ľ���
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
	private ImageView fanhui;// ���ذ�ť

	private TextView finishi;// ���

	private TextView name;// ��Ŀ����

	private Spinner supplier;// ��Ӧ��

	private EditText title, remarks;// ��Ŀ������

	private GridView gri;// ���ͼƬ�ļ��϶���

	private Item_centre_supplier_adapter adapter_supplier;// ��Ӧ�̵�adapter

	private List<Supplier_message> data_supplier;// ��Ӧ�̵����ݼ���

	private PopupWindow pop, pop_delete;// ���ͼƬ�ġ�ɾ��ͼƬ
	private Button bu1, bu2, bu3, delete, abrogate;// pop�ϵ�������ť���������ᡢȡ�� ɾ��ͼƬ��ȡ��

	private Item_tupian_adapter adapter_ph;// ���ͼƬ��adapter����

	private File[] f;// ��ʱ�洢ͼƬ���ļ�

	private static int number = 0;// ��¼ͼƬ�ϴ��ĸ���

	private RequestQueue mqueQueue;// �������
	private int timemy = 0;

	private String st_name = "", st_projects = "", st_providerId = "", st_description = "", st_remarks = "�ޱ�ע��Ϣ";
	private long time;
	private int record_delete_number = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_item_details);
		setview();// ���ؽ����ϰ벿�ֵĿؼ��ķ���

		setviewitem();// ���ؽ����°벿�ֵĿؼ��ķ���
		name.setText("������Ŀ��"+getIntent().getStringExtra("name"));
		st_projects = getIntent().getStringExtra("id");
		setdata_spinner();// ���ع�Ӧ�̵�����
		supplier.setOnItemSelectedListener(sp_supplier);// ����Ӧ�����ü���

		set_initializebitmap();// �������ͼƬ�İ�ť

		gri.setOnItemClickListener(listener);// ͼƬ�б�ĵ���¼�
		// gri.setOnItemLongClickListener(listener2);//ͼƬ�б�ĳ����¼�

	}

	/*
	 * ͼƬ�ĳ����¼�
	 */
	private AdapterView.OnItemLongClickListener listener2 = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (position != number) {
				View v1 = LayoutInflater.from(Centre_ItemDetailsActivity.this).inflate(R.layout.popup_delete_photograph,
						null);// ����view
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
	/*
	 * ͼƬ���б���м����¼�
	 */
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (position == adapter_ph.getData().size() - 1) {
				if (number < 4) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							Centre_ItemDetailsActivity.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);// ��������̵ķ���
					View v1 = LayoutInflater.from(Centre_ItemDetailsActivity.this).inflate(R.layout.popup_phone_select,
							null);// ����view

					pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���

					pop.setBackgroundDrawable(new ColorDrawable());
					// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���

					pop.showAtLocation(gri, Gravity.BOTTOM, 0, 0);
					bu1 = (Button) v1.findViewById(R.id.btn_popup_picture);// ��ᰴť

					bu2 = (Button) v1.findViewById(R.id.btn_popup_photo);// �����ť

					bu3 = (Button) v1.findViewById(R.id.btn_popup_enter);// ȡ����ť

					bu1.setOnClickListener(l);
					bu2.setOnClickListener(l);
					bu3.setOnClickListener(l);

				} else {
					Toast.makeText(Centre_ItemDetailsActivity.this, "ͼƬ�ϴ������Ѿ������", 0).show();
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

	// Ϊgridview�ȷ���һ�ų�ʼ����ͼƬ

	private void set_initializebitmap() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.addphonebutton);
		adapter_ph.addDataBottom(bitmap);
	}

	// ѡ�й�Ӧ�̵ļ����¼�

	private AdapterView.OnItemSelectedListener sp_supplier = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			st_providerId = adapter_supplier.getData().get(position).id;// ��Ӧ�̵�id

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	// ���ý����°벿�ֵķ���

	private void setviewitem() {
		name = (TextView) findViewById(R.id.textview_centre_item_details_name);// ��Ŀ������

		supplier = (Spinner) findViewById(R.id.spinner_centre_item_details_gongyingsahng);// ��Ӧ��

		title = (EditText) findViewById(R.id.editText_centre_item_details_miaoshu);// ��Ŀ������

		remarks = (EditText) findViewById(R.id.editText_centre_item_details_remarks);// ��Ŀ������

		gri = (GridView) findViewById(R.id.gridView_centre_item_details_tupian);// ���ͼƬ�ļ���

		adapter_ph = new Item_tupian_adapter(Centre_ItemDetailsActivity.this);// ���ͼƬ��adapter

		gri.setAdapter(adapter_ph);
		gri.setSelector(new BitmapDrawable());
		// ʵ�������ͼƬ�ļ��Ϻ������������Ҹ���������������
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				Centre_ItemDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);// ��������̵ķ���
		adapter_supplier = new Item_centre_supplier_adapter(Centre_ItemDetailsActivity.this);
		data_supplier = new ArrayList<Supplier_message>();
		supplier.setAdapter(adapter_supplier);

	}

	// ���ȷ���ύ�ķ���

	@SuppressLint("ShowToast")
	private void set_submit() {

		if (getdescription()) {// ��ȡҪ�ϴ������ݵķ���

			Toast.makeText(Centre_ItemDetailsActivity.this, "�����ύ���޵����Ժ�", 0).show();
			Mytask ta = new Mytask();
			ta.execute();// ִ�н����ϴ����ݵķ���

		}

	}

	// ��ȡҪ�ϴ�������

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
			Toast.makeText(this, "����д��������", 0).show();
			return false;
		}
		if (st_providerId.equals("")) {
			Toast.makeText(this, "��ѡ��Ӧ��", 0).show();
			return false;
		}
		if (st_projects.equals("")) {
			Toast.makeText(this, "��ѡ��Ҫ���޵���Ŀ", 0).show();
			return false;
		}
		return true;

	}

	// ���ý����ϰ벿�ֵķ���

	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);// �������

		f = new File[4];
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		set_title_text("������Ŀ");// ���ý���ı���

		finishi = (TextView) findViewById(R.id.textview_myacitivity_you);
		finishi.setText("���");
		finishi.setVisibility(View.VISIBLE);
		finishi.setOnClickListener(l);

	}

	// �����Ͽؼ��ĵ���¼�

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť

				number = 0;
				adapter_ph.getData().clear();
				finish();

				Myutil.set_activity_close(Centre_ItemDetailsActivity.this);// �����л������Ч��

				break;
			case R.id.textview_myacitivity_you:// ��ɰ�ť

				// �ж��û��Ƿ��Ѿ�����ѡ����Ŀ�Ƿ���д���������û���д��ɲ���ȫ���ύʱ������վ������ҳ

				set_submit();

				break;

			case R.id.btn_popup_photo:
				// �����

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
								Toast.makeText(Centre_ItemDetailsActivity.this, "�Բ���APP���ִ������˳�APP���µ�¼",
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
					Toast.makeText(Centre_ItemDetailsActivity.this, "�Բ��������SD��", Toast.LENGTH_SHORT).show();
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

				pop.dismiss();// ����ʾ��popupwindow��������

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

	// ������ͼ ����ֵʱ���õķ���

	@SuppressLint("ShowToast")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {

			number--;
			pop.dismiss();// popup��ʧ�ķ���

			if (requestCode == 200 && resultCode == RESULT_OK) {
				number++;
				// ��᷵��

				Uri uri = data.getData();
				String path = Phoneutil.getPath(Centre_ItemDetailsActivity.this, uri);
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
					Toast.makeText(Centre_ItemDetailsActivity.this, "�Բ���û�л�ȡ������е�ͼƬ", 0).show();
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

				// "/image" + number + ".jpg");// �õ�ͼƬbitmap

			}

		} catch (Exception e) {
			finish();
			Toast.makeText(Centre_ItemDetailsActivity.this, "�Բ�����Ϊ�ֻ����⣬�����½������", 0).show();
		}

	};

	// ����Ӧ�������б�������ݵķ���

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
							Toast.makeText(Centre_ItemDetailsActivity.this, "�Բ�����ѡ����Ŀ����ʱ�޷��ṩ����", 0).show();
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
				Toast.makeText(Centre_ItemDetailsActivity.this, "��Ӧ�̼�������ʧ�ܣ������µ�¼", 0).show();

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

	// �ϴ�ά�޵��ķ���

	private class Mytask extends AsyncTask<Void, Void, String> {

		String str = Myconstant.CENTRE_SUBMITREPAIRSITEM;

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

			map.put("name", st_name);// ����

			map.put("projects", st_projects);// ������Ŀ

			map.put("providerId", st_providerId);// ��Ӧ�̵�id

			map.put("description", st_description);// ��������

			map.put("remark", st_remarks);// ��ע��Ϣ

			return postFileToString(str, map);
		}

		@Override
		protected void onPostExecute(String result) {
			finishi.setClickable(true);
			if (result.equals("")) {
				Toast.makeText(Centre_ItemDetailsActivity.this, "�Բ�����Ϊ�����ԭ�򣬱��޵��ϴ�ʧ�ܡ�", 0).show();
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

	// post�����ļ�����string����

	@SuppressWarnings("unused")
	@SuppressLint("ShowToast")
	public static String postFileToString(String urlStr, Map<String, Object> map) {
		try {
			HttpClient client = new DefaultHttpClient();

			HttpParams params = client.getParams();
			// params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,

			// "UTF-8");// API ʶ�� charset

			// HttpConnectionParams.setConnectionTimeout(params, 2000);

			// HttpConnectionParams.setSoTimeout(params, 2000);

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