package com.cnpc.zhibo.app;
import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_centre_function_introduce_select_adapter;
import com.cnpc.zhibo.app.entity.Centre_Function_introduce;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * ��ʯ�Ͷ˵Ĺ��ܽ���
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class Petrochina_function_introduceActivity extends MyActivity {
	private ImageView back;
	private ListView list;
	private List<Centre_Function_introduce> data;
	private Item_centre_function_introduce_select_adapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_function_introduce);
		setview();
	}
	/*
	 * ���ý���ķ���
	 */
	private void setview() {
		set_title_text("���ܽ���");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listView_petrochina_function_introduce_entry);
		adapter = new Item_centre_function_introduce_select_adapter(Petrochina_function_introduceActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_Function_introduce>();
		setclodingdata();
		//list.setDividerHeight(1);
		list.setSelector(new BitmapDrawable());
		list.setOnItemClickListener(listener);
	}
	/*
	 * �б���е���¼�
	 */
	private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in=new Intent(Petrochina_function_introduceActivity.this, Petrochina_See_function_introduceActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_function_introduceActivity.this);
			
		}
	};

	/*
	 * �ؼ��ļ����¼�
	 */
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
             finish();
             Myutil.set_activity_close(Petrochina_function_introduceActivity.this);
				break;

			default:
				break;
			}

		}
	};

	/*
	 * �������ݵķ���
	 */
	private void setclodingdata() {
		data.add(new Centre_Function_introduce(R.drawable.petrochina_station, 0, "����վ��Ϣ"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_suppliermessage, 1, "��Ӧ����Ϣ"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_shenpi, 2, "����"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_notice, 3, "�������"));
		data.add(new Centre_Function_introduce(R.drawable.petrochina_weixiulv, 4, "ά���ʲ�ѯ"));
		adapter.addDataBottom(data);
	}
}
