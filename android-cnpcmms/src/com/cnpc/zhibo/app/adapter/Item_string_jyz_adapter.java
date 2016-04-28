package com.cnpc.zhibo.app.adapter;

//站长端选择地址界面中的加油站的adapter
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_Addres_item;
import com.cnpc.zhibo.app.entity.Centre_gasstation_item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Item_string_jyz_adapter extends MyBaseAdapter<Centre_gasstation_item> {
	private Context c;
	private int id = -1;

	public Item_string_jyz_adapter(Context context) {
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
		Centre_gasstation_item str = (Centre_gasstation_item) getItem(position);
		h.name.setText(str.name);
		if (id == position) {
			h.name.setTextColor(c.getResources().getColor(R.color.zitiyanse1));
			// v.setBackgroundColor(c.getResources().getColor(R.color.baise));
		} else {
			h.name.setTextColor(c.getResources().getColor(R.color.heise));
			// v.setBackgroundColor(c.getResources().getColor(R.color.qianhuise));
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
