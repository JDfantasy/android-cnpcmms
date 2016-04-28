package com.cnpc.zhibo.app;

import java.util.ArrayList;
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

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��Ӧ�̵�ά��Ա�������
 */
public class Supplier_weixiuyuanguanliActivity extends MyActivity implements OnClickListener {
	private ImageView fanhui;
	private ListView listview;
	private List<WorkerBean> mDatas = new ArrayList<WorkerBean>();
	private TreeListViewAdapter<WorkerBean> adapter;
	private Dialog mDialog;
	private int defaultExpandLevel = 1;
	private String orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_weixiuyuanguanli);
		orderId = getIntent().getStringExtra("baoxiudan_id");
		set_title_text("���ٷ���");
		initDialog();
		setview();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setview() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(this);
		// listview
		listview = (ListView) findViewById(R.id.id_listview);
		initData();
		try {
			adapter = new TreeListViewAdapter<WorkerBean>(listview, Supplier_weixiuyuanguanliActivity.this, mDatas,
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
						convertView.setTag(holder);
					} else {
						holder = (ViewHolder) convertView.getTag();
					}

					WorkerBean workerBean = getBeanByNode(node);
					if (workerBean != null) {
						if (workerBean.getType().equals("user")) {// ��Ա��
							convertView.setBackgroundResource(R.color.baise);// ������ɫ
							holder.uIcon.setImageResource(R.drawable.icon_username);
							holder.cIcon.setVisibility(View.INVISIBLE);
							holder.mIcon.setVisibility(View.INVISIBLE);
						} else {// �ǻ���
							convertView.setBackgroundResource(R.drawable.item_bg_selector);// �ɱ仯����ɫ
							holder.uIcon.setImageResource(R.drawable.icon_password);
							holder.cIcon.setVisibility(View.INVISIBLE);
							holder.mIcon.setVisibility(View.VISIBLE);
							holder.mIcon.setImageResource(node.getIcon() == -1 ? R.drawable.tree_ec : (node.getIcon()));
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
				}

			};
			listview.setAdapter(adapter);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(Node node, int position) {
					WorkerBean workerBean = getBeanByNode(node);
					if (workerBean.getType().equals("user")) {
						dialog(workerBean);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private WorkerBean getBeanByNode(Node node) {
		WorkerBean workerBean = null;
		for (WorkerBean bean : mDatas) {
			if (node.getId() == bean.getId())
				workerBean = bean;
		}
		return workerBean;
	}

	private void initDialog() {
		mDialog = new Dialog(this, R.style.MyProgressDialogStyle);
		mDialog.setContentView(R.layout.progress_dialog);
	}

	protected void dialog(final WorkerBean worker) {
		AlertDialog.Builder builder = new Builder(Supplier_weixiuyuanguanliActivity.this);
		builder.setMessage("ȷ�Ͻ����������" + worker.getName() + "��?");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ȷ���ύ�ӿ�
				submitDatas(worker);
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				worker.setSelected(false);
				adapter.notifyDataSetChanged();
			}
		}).create().show();
	}

	/**
	 * �ύ��ѡ���ά�޹�
	 */

	private void submitDatas(final WorkerBean worker) {
		mDialog.show();
		// �ύ��ѡά�޹�
		String url = Myconstant.PROVIDER_DISTRIBUTE;
		StringRequest xx = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				mDialog.dismiss();
				try {
					System.out.println(response);
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Supplier_weixiuyuanguanliActivity.this, "�ύ�ɹ�", Toast.LENGTH_SHORT).show();
						finish();
						Myutil.set_activity_close(Supplier_weixiuyuanguanliActivity.this);
					} else {
						Toast.makeText(Supplier_weixiuyuanguanliActivity.this, "�ύ����ʧ��", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mDialog.dismiss();
				Toast.makeText(Supplier_weixiuyuanguanliActivity.this, "����ʧ�ܣ�" + error.toString(), Toast.LENGTH_SHORT)
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

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", orderId);// ���޵����
				map.put("workerId", worker.getId() + "");// ά�޹����
				return map;
			}

		};
		SysApplication.getHttpQueues().add(xx);
	}

	/**
	 * �������ݷ���
	 */
	private void initData() {
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
						Toast.makeText(Supplier_weixiuyuanguanliActivity.this, "��������ʧ��", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mDialog.dismiss();
				Toast.makeText(Supplier_weixiuyuanguanliActivity.this, "����ʧ�ܣ�" + error.getMessage(), Toast.LENGTH_LONG)
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(Supplier_weixiuyuanguanliActivity.this);
			break;
		}
	}

}
