package com.cnpc.zhibo.app.adapter;
//վ����Ҫ������Ŀ��adapter
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_Repairs_item;

public class Item_repairs_item_adapter extends MyBaseAdapter<Centre_Repairs_item> {
	private int setid = -1, upid = -1;
	private boolean b;//�����ڲ��ж�ִ���Ǹ�����
	private Context c;

	public Item_repairs_item_adapter(Context context) {
		super(context);
		this.c = context;
	}

	public void setcolor(int id) {
		this.setid = id;
		this.b = true;// �����ж���ִ��setcolor����ʱ���Ͳ�ִ��upcolor
		notifyDataSetChanged();
	}

	public void upcolor(int id) {
		this.upid = id;
		this.b = false;// �����ж���ִ��upcolor����ʱ���Ͳ�ִ��setcolor
		notifyDataSetChanged();
	};

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_repairs_name, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		Centre_Repairs_item re = (Centre_Repairs_item) getItem(position);
		h.name.setText(re.name);
		if (setid == position && b == true) {
			//v.setBackgroundColor(c.getResources().getColor(R.color.qianlvse));
           h.name.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (upid == position && b == false) {
			//v.setBackgroundColor(c.getResources().getColor(R.color.baise));
			  h.name.setTextColor(c.getResources().getColor(R.color.heise));
		}
		return v;
	}

	private class Hoderview {
		private TextView name;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v
					.findViewById(R.id.textView_item_repais_name);
		}

	}

}
