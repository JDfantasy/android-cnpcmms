package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Supplier_notice_contacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 *供应商的添加部门界面的Adapter
 */
public class Item_supplier_notice_contacts_adapter extends MyBaseAdapter<Supplier_notice_contacts> {

	public Item_supplier_notice_contacts_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_supplier_notice_contacts, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		final Supplier_notice_contacts s = (Supplier_notice_contacts) getItem(position);
		h.name.setText(s.department_name);
		h.ck.setChecked(s.ched);
		return v;
	}

	private class Hoderview {
		private TextView name;
		private CheckBox ck;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v.findViewById(R.id.item_supplier_notice_contacts_department_name);
			this.ck = (CheckBox) v.findViewById(R.id.checkbox_item_supplier_notice_contacts);
		}

	}
}
