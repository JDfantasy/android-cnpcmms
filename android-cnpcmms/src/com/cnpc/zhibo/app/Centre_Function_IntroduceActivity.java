package com.cnpc.zhibo.app;
/*
 * վ���˵Ĺ����б���ܵĽ���
 */
import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_centre_function_introduce_select_adapter;
import com.cnpc.zhibo.app.entity.Centre_Function_introduce;
import com.cnpc.zhibo.app.util.Myutil;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class Centre_Function_IntroduceActivity extends MyActivity {
	private ImageView back;
	private ListView list;
	private List<Centre_Function_introduce> data;
	private Item_centre_function_introduce_select_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitycentre_function_introduce);
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
		list = (ListView) findViewById(R.id.listView_centre_function_introduce_entry);
		adapter = new Item_centre_function_introduce_select_adapter(Centre_Function_IntroduceActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_Function_introduce>();
		setclodingdata();
		//list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		list.setOnItemClickListener(listener);
	}
	/*
	 * �б���е���¼�
	 */
	private AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in=new Intent(Centre_Function_IntroduceActivity.this, Centre_See_Function_introduceActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_Function_IntroduceActivity.this);
			
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
             Myutil.set_activity_close(Centre_Function_IntroduceActivity.this);
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
		data.add(new Centre_Function_introduce(R.drawable.woyaobaoxiu, 0, "��Ҫ����"));	
		data.add(new Centre_Function_introduce(R.drawable.copy_file, 1, "���޵�"));
		data.add(new Centre_Function_introduce(R.drawable.shenpiguanli, 2, "��������"));
		data.add(new Centre_Function_introduce(R.drawable.gonggaoguanli, 3, "�������"));
		data.add(new Centre_Function_introduce(R.drawable.zhengjianguanli, 4, "֤������"));
		data.add(new Centre_Function_introduce(R.drawable.centre_service_check, 5, "ά��"));
		data.add(new Centre_Function_introduce(R.drawable.centre_examine_check, 6, "����"));
		adapter.addDataBottom(data);
	}
}
