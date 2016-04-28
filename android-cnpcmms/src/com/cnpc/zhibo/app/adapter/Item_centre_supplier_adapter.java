package com.cnpc.zhibo.app.adapter;
/*
 * 供应商的列表的adapter
 */
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Supplier_message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Item_centre_supplier_adapter extends
		MyBaseAdapter<Supplier_message> {

	public Item_centre_supplier_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_spinner, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		Supplier_message c = (Supplier_message) getItem(position);
		h.name.setText(c.name);
		return v;
	}

	private class Hoderview {
		private TextView name;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v
					.findViewById(R.id.textView_item_spinner_name);
		}

	}

}
