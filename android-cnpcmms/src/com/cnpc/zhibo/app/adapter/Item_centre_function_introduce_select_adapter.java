package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_Function_introduce;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 站长端查看功能介绍的列表的adapter
 */
public class Item_centre_function_introduce_select_adapter extends MyBaseAdapter<Centre_Function_introduce> {

	public Item_centre_function_introduce_select_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_centre_function_introduce_select, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
		}
		Centre_Function_introduce i=(Centre_Function_introduce) getItem(position);
		h.icon.setImageResource(i.icon);
		h.name.setText(i.name);
		return v;
	}

	private class Hoderview {
		private TextView name;
		private ImageView icon;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v.findViewById(R.id.textView_item_centre_funtion_introduce_select_name);
			this.icon = (ImageView) v.findViewById(R.id.imageview_item_centre_funtion_introduce_select_icon);
		}

	}

}
