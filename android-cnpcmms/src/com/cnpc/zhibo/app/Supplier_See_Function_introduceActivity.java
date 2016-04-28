package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_imageview_adapter;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class Supplier_See_Function_introduceActivity extends MyActivity {

	private ImageView back;
	private int id;
	private ListView list;
	private Item_imageview_adapter adapter;
	private List<Integer> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier__see__function_introduce);
		setview();
	}

	private void setview() {
		set_title_text("�����������");
		id=getIntent().getIntExtra("id", 0);
		list = (ListView) findViewById(R.id.listview_supplier_see_function_introduce);
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		adapter=new Item_imageview_adapter(this);
		data=new ArrayList<Integer>();
		list.setAdapter(adapter);
		selectimageview();
	}
	private void selectimageview() {
		switch (id) {
		case 0:// ��������
			data.add(R.drawable.supplier_introduce0);
			break;
		case 1:// �������
			data.add(R.drawable.supplier_introduce1);
			break;
		case 2:// �������
			data.add(R.drawable.supplier_introduce2);
			break;
		case 3:// ά��
			data.add(R.drawable.supplier_introduce3);
			break;
		case 4:// ����
			data.add(R.drawable.supplier_introduce4);
			break;

		default:
			break;

		}
		adapter.addDataBottom(data);
	}
		
	/*
	 * �ؼ��ĵ���¼��ļ���
	 */
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Supplier_See_Function_introduceActivity.this);
				break;

			default:
				break;
			}

		}
	};
}
