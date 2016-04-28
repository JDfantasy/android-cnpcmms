package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.zhibo.app.adapter.Item_imageview_adapter;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class Petrochina_See_function_introduceActivity extends MyActivity {
	private ImageView back;
	private int id = 0;
	private ListView list;
	private Item_imageview_adapter adapter;
	private List<Integer> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ptrochina__see_function_introduce);
		setview();
	}

	/*
	 * ���ý���ķ���
	 */
	private void setview() {
		set_title_text("�����������");
		list = (ListView) findViewById(R.id.listview_petrochina_see_function_introduce);
		adapter = new Item_imageview_adapter(Petrochina_See_function_introduceActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Integer>();
		id = getIntent().getIntExtra("id", 0);
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		selectimageview();
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
				Myutil.set_activity_close(Petrochina_See_function_introduceActivity.this);
				break;

			default:
				break;
			}

		}
	};

	/*
	 * �ж��ϸ����洫�ݹ����Ĳ���������Ӧ��ͼƬ
	 */
	private void selectimageview() {
		switch (id) {
		case 0:// ����վ��Ϣ
			data.add(R.drawable.petrochian_jyz_1);
			break;
		case 1:// ��Ӧ����Ϣ
			data.add(R.drawable.petrochina_gys_1);
			break;
		case 2:// ����
			data.add(R.drawable.petrochina_shenpi_1);
			break;
		case 3:// �������
			data.add(R.drawable.petrochina_gonggao_1);
			break;
		case 4:// ά���ʲ�ѯ
			data.add(R.drawable.petrochina_weixiulv_1);
			break;

		default:
			break;
		}
		adapter.addDataBottom(data);
	}
}
