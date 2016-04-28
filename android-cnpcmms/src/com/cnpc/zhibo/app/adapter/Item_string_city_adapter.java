package com.cnpc.zhibo.app.adapter;

//站长端的选择地址界面中的市的信息的adapter
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_Addres_item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Item_string_city_adapter extends MyBaseAdapter<Centre_Addres_item> {
	private Context c;
	private int id = -1;

	public Item_string_city_adapter(Context context) {
		super(context);
		this.c = context;
		// TODO Auto-generated constructor stub
	}

	public void setcolor(int id) {
		this.id = id;
		notifyDataSetChanged();
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_string, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {

			h = (Hoderview) v.getTag();
		}
		Centre_Addres_item str = (Centre_Addres_item) getItem(position);
		h.name.setText(str.name);
		if (id == position) {
			v.setBackgroundColor(c.getResources().getColor(R.color.qianhuise));
		} else {
			v.setBackgroundColor(c.getResources().getColor(R.color.baise));
		}

		return v;
	}

	public void set_textcolor(int id) {
	}

	private class Hoderview {

		private TextView name;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v.findViewById(R.id.textView_item_string);
		}

	}
}
