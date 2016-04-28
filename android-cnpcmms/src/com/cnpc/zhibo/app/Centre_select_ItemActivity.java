package com.cnpc.zhibo.app;

//վ���˵ı�����Ŀ��ѡ��Ҫ������Ŀ�Ľ���
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.adapter.Item_repairs_item_adapter;
import com.cnpc.zhibo.app.adapter.Item_repairs_select_adapter;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_Repairs_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_select_ItemActivity extends MyActivity {
	private ImageView fanhui,grabble;// ���ء�������ť
	private GridView items;// ��ѡ�ı�����Ŀ����ѡ�ı�����Ŀ
	private Item_repairs_item_adapter adapter_i;// Ҫѡ�����Ŀ��adapter
	private List<Centre_Repairs_item> data_i;// Ҫѡ�����Ŀ�ļ���
	private RequestQueue mQueue;// �������
	private boolean ck = false;// �ж��û��Ƿ�ѡ����Ŀ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_item);
		setview();// ���ý����Ͽؼ��ķ���
		items.setOnItemClickListener(listener_i);// Ϊ��ѡ��gridview���õ�������¼�
		//items.setDividerHeight(0);
		items.setSelector(new BitmapDrawable());
	}

	// ��ѡ�б�ĵ���¼�
	private AdapterView.OnItemClickListener listener_i = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			Intent in = new Intent(Centre_select_ItemActivity.this, Centre_ItemDetailsActivity.class);
			
			in.putExtra("name", adapter_i.getData().get(position).name);
			in.putExtra("id",adapter_i.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_select_ItemActivity.this);
			
		}
	};

	

	private void setview() {
		mQueue = Volley.newRequestQueue(this);
		set_title_text("������Ŀ");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		grabble = (ImageView) findViewById(R.id.imageView_myactity_you);// ������ť
		grabble.setVisibility(View.VISIBLE);
		grabble.setImageResource(R.drawable.search_icon);
		grabble.setOnClickListener(l);
		items = (GridView) findViewById(R.id.gridView_centre_item_items);
		adapter_i = new Item_repairs_item_adapter(Centre_select_ItemActivity.this);// gridviewʵ����
		items.setAdapter(adapter_i);// Ϊgridview����������
		setdata();// ������Ŀ�ļ���

	}

	// �����п��Ա��޵���Ŀ���м���
	private void setdata() {
		Toast.makeText(this, "���ڼ����������Ժ�....", 0).show();// �����û����ڼ�������
		data_i = new ArrayList<Centre_Repairs_item>();
		String url = Myconstant.CENTRE_GETREPAIRSITEM;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONObject js1 = response.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONArray js2 = response.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data_i.add(new Centre_Repairs_item(js3.getString("id"), js3.getString("name")));
						}
						adapter_i.addDataBottom(data_i);
					} else {
						Toast.makeText(Centre_select_ItemActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("��ȡ���ݴ���");

			}
		});
		mQueue.add(re);

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_select_ItemActivity.this);

				break;
			
			case R.id.imageView_myactity_you:// ������ť
				startActivity(new Intent(Centre_select_ItemActivity.this, Centre_search_itemActivity.class));// ��ת����������
				Myutil.set_activity_open(Centre_select_ItemActivity.this);// ���ý����л���Ч��
				break;

			default:
				break;
			}
		}
	};

	

}
