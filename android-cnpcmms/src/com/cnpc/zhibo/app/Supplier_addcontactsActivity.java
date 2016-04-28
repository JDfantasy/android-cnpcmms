package com.cnpc.zhibo.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.WorkerBean;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.util.Node;
import com.cnpc.zhibo.app.util.adapter.TreeListViewAdapter;
import com.cnpc.zhibo.app.util.adapter.TreeListViewAdapter.OnTreeNodeClickListener;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��Ӧ�̶˵Ĺ��������ϵ�˵Ľ���
 */
public class Supplier_addcontactsActivity extends MyActivity {
	private ImageView fanhui;// ����
	private TextView quanxuan, tv;// ȫѡ��ȷ��
	private ListView listview;
	private TreeListViewAdapter<WorkerBean> adapter;
	private List<WorkerBean> mDatas = new ArrayList<WorkerBean>();
	private List<WorkerBean> mChecked = new ArrayList<WorkerBean>();
	private int defaultExpandLevel = 1;// Ĭ��չ���㼶
	private Dialog mDialog;// �������ʱ��ʾ�������ڷ��ʵ�С�ջ�
	private boolean isAll = false;// ��ǰ�Ƿ��Ƕ�ѡ�ı�־
	private String title, content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_addcontacts);
		set_title_text("�����ϵ��");
		try {
			title = URLEncoder.encode(getIntent().getStringExtra("title1"), "UTF-8");
			content = URLEncoder.encode(getIntent().getStringExtra("content1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initDialog();
		setview();

	}

	private void initDialog() {
		mDialog = new Dialog(this, R.style.MyProgressDialogStyle);
		mDialog.setContentView(R.layout.progress_dialog);
	}

	/**
	 * ���������ȡ����
	 */
	private void get_data() {
		mDialog.show();
		// ��֯�ṹ�б�ӿ�
		String url = Myconstant.ORGANIZATION_TREE_USER;
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, url, null, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				mDialog.dismiss();
				try {
					String type = response.getJSONObject("response").getString("type");
					if ("success".equals(type)) {
						JSONArray array = response.getJSONArray("body");
						mDatas.clear();
						System.out.println(array.toString());
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							WorkerBean worker = new WorkerBean(object.getInt("id"), object.getInt("parent"),
									object.getString("name"));
							worker.setType(object.getString("type"));
							mDatas.add(worker);
						}
						adapter.changeDatas(mDatas, defaultExpandLevel);
					} else {
						Toast.makeText(Supplier_addcontactsActivity.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mDialog.dismiss();
				Toast.makeText(Supplier_addcontactsActivity.this, "����ʧ�ܣ�" + error.getMessage(), Toast.LENGTH_SHORT)
						.show();
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
		SysApplication.getHttpQueues().add(request);
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setview() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		quanxuan = (TextView) findViewById(R.id.textview_myacitivity_you);
		quanxuan.setVisibility(View.VISIBLE);
		quanxuan.setText("ȫѡ");
		quanxuan.setTextColor(getResources().getColor(R.color.baise));
		quanxuan.setPadding(0, 0, 50, 0);
		quanxuan.setOnClickListener(l);
		listview = (ListView) findViewById(R.id.activity_supplier_addcontacts_contactsList);
		initAdapter();
		listview.setAdapter(adapter);
		get_data();
		tv = (TextView) findViewById(R.id.activity_supplier_addcontacts_sure);
		tv.setOnClickListener(l);
	}

	private void initAdapter() {
		try {
			adapter = new TreeListViewAdapter<WorkerBean>(listview, Supplier_addcontactsActivity.this, mDatas,
					defaultExpandLevel) {
				@Override
				public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
					ViewHolder holder = null;
					if (convertView == null) {
						convertView = mInflater.inflate(R.layout.tree_listview_item, parent, false);
						holder = new ViewHolder();
						holder.mIcon = (ImageView) convertView.findViewById(R.id.id_item_icon);
						holder.uIcon = (ImageView) convertView.findViewById(R.id.id_item_usericon);
						holder.cIcon = (TextView) convertView.findViewById(R.id.id_item_chaceicon);
						holder.mText = (TextView) convertView.findViewById(R.id.id_item_text);
						holder.box = (CheckBox) convertView.findViewById(R.id.id_item_box);
						convertView.setTag(holder);
					} else {
						holder = (ViewHolder) convertView.getTag();
					}

					final WorkerBean workerBean = getBeanByNode(node);
					if (workerBean != null) {
						if (workerBean.getType().equals("user")) {// ��Ա��
							convertView.setBackgroundResource(R.color.baise);// ������ɫ
							holder.uIcon.setImageResource(R.drawable.icon_username);
							holder.cIcon.setVisibility(View.INVISIBLE);
							holder.mIcon.setVisibility(View.INVISIBLE);
							holder.box.setVisibility(View.VISIBLE);
						} else {// �ǻ���
							convertView.setBackgroundResource(R.drawable.item_bg_selector);// �ɱ仯����ɫ
							holder.uIcon.setImageResource(R.drawable.icon_password);
							holder.cIcon.setVisibility(View.INVISIBLE);
							holder.mIcon.setVisibility(View.VISIBLE);
							holder.mIcon.setImageResource(node.getIcon() == -1 ? R.drawable.tree_ec : (node.getIcon()));
							holder.box.setVisibility(View.GONE);
						}
					}
					convertView.findViewById(R.id.id_item_box).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							CheckBox b = (CheckBox) v;
							System.out.println("b=" + b.isChecked());
							if (b.isChecked()) {
								mChecked.add(workerBean);
							} else {
								mChecked.remove(workerBean);
							}
						}
					});
					((CheckBox) convertView.findViewById(R.id.id_item_box)).setChecked(false);
					for (WorkerBean m : mChecked) {
						if (workerBean.getId() == m.getId()) {
							((CheckBox) convertView.findViewById(R.id.id_item_box)).setChecked(true);
						}
					}

					holder.mText.setText(node.getName());
					return convertView;
				}

				class ViewHolder {
					ImageView mIcon;
					ImageView uIcon;
					TextView cIcon;
					TextView mText;
					CheckBox box;
				}

			};
			listview.setAdapter(adapter);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(Node node, int position) {
					WorkerBean workerBean = getBeanByNode(node);
					if (workerBean.getType().equals("user")) {
						// �����ѡ
						System.out.println("" + node.getName());
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ݽڵ��ҵ���Ӧά��Ա
	 * 
	 * @param node
	 * @return
	 */
	private WorkerBean getBeanByNode(Node node) {
		WorkerBean workerBean = null;
		for (WorkerBean bean : mDatas) {
			if (node.getId() == bean.getId())
				workerBean = bean;
		}
		return workerBean;
	}

	/**
	 * �ش����ݵķ���
	 */
	private void set2() {
		StringBuilder contacts = new StringBuilder();
		for (WorkerBean bean : mChecked) {
			contacts.append(bean.getName()).append(" ");
		}
		if (TextUtils.isEmpty(contacts)) {
			Toast.makeText(Supplier_addcontactsActivity.this, "����û��ѡ��", Toast.LENGTH_SHORT).show();
			return;
		}
		// Intent intent = new Intent();
		// intent.putExtra("result", contacts + "");
		// Supplier_addcontactsActivity.this.setResult(RESULT_OK, intent);
		String a = getData();

		send_notice(a);
		finish();
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ����
				finish();
				Myutil.set_activity_close(Supplier_addcontactsActivity.this);
				break;
			case R.id.textview_myacitivity_you:// ȫѡ
				set1();
				break;
			case R.id.activity_supplier_addcontacts_sure:// ȷ�ϰ�ť
				set2();
				break;
			default:
				break;
			}

		}
	};
	private String[] arrays;

	// ȫѡ��ť�ķ���
	private void set1() {
		isAll = !isAll;
		if (isAll) {
			for (WorkerBean bean : mDatas) {
				if (bean.getType().equals("user")) {
					mChecked.add(bean);
				}
			}
		} else {
			mChecked.clear();
		}
		quanxuan.setText(isAll ? "ȡ��" : "ȫѡ");
		adapter.notifyDataSetChanged();
	}

	/**
	 * ������ķ���
	 */
	private void send_notice(final String a) {
		String url = Myconstant.SUPPLIER_SEND_NOTICE + "?title=" + title + "&content=" + content;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						startActivity(new Intent(Supplier_addcontactsActivity.this, Supplier_gonggaoActivity.class));
						Myutil.set_activity_open(Supplier_addcontactsActivity.this);
						finish();
						Toast.makeText(Supplier_addcontactsActivity.this, obj1.getString("content"), Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(Supplier_addcontactsActivity.this, obj1.getString("content"), Toast.LENGTH_SHORT)
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
				Toast.makeText(Supplier_addcontactsActivity.this, "�����쳣�����淢��ʧ��", Toast.LENGTH_SHORT).show();

			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}

			// @Override
			// protected Map<String, String> getParams() throws AuthFailureError
			// {
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("title", getIntent().getStringExtra("title1"));
			// map.put("content", getIntent().getStringExtra("content1"));
			// return map;
			// }

			@Override
			public byte[] getBody() throws AuthFailureError {

				return a.getBytes();
			}
		};
		SysApplication.getHttpQueues().add(re);
	}

	// public String gettitle(){
	// String str=getIntent().getStringExtra("title1");
	// try {
	// str=URLEncoder.encode(str, "UTF-8");
	// Log.i("tag", "str="+str);
	// return str;
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	// public String getcontent(){
	// String str=getIntent().getStringExtra("content1");
	// try {
	// str=URLEncoder.encode(str, "UTF-8");
	// Log.i("tag", "str="+str);
	// return str;
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	private String getData() {
		arrays = new String[mChecked.size()];
		for (int i = 0; i < mChecked.size(); i++) {
			int name = mChecked.get(i).getId();
			arrays[i] = String.valueOf(name);

		}
		if (Arrays.toString(arrays) != null && Arrays.toString(arrays).length() != 0) {
			return Arrays.toString(arrays);
		} else {
			return null;
		}
	}
}
