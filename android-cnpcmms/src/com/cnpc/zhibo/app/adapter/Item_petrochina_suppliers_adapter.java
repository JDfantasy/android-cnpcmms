package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Petrochina_suppliers_entry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Item_petrochina_suppliers_adapter extends MyBaseAdapter<Petrochina_suppliers_entry>{

	public Item_petrochina_suppliers_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v==null) {
			v=inflater.inflate(R.layout.item_petrochina_supplier, null);
			h=new Hoderview(v);
			v.setTag(h);
		} else {
			h=(Hoderview) v.getTag();

		}
		Petrochina_suppliers_entry en=(Petrochina_suppliers_entry) getItem(position);
		h.name.setText(en.name);
		h.number.setText(en.number);
		h.grade.setText(en.grade);
		return v;
	}
	private class Hoderview{
		private TextView name;//供应商的名称
		private TextView number;//供应商的维修次数
		private TextView grade;//供应商的服务评价
		public Hoderview(View v) {
			super();
			this.name = (TextView) v.findViewById(R.id.textView_item_petrochina_supplier_suppliernname);
			this.number = (TextView) v.findViewById(R.id.textView_item_petrochina_supplier_suppliernumber);
			this.grade = (TextView) v.findViewById(R.id.textView_item_petrochina_supplier_suppliergrade);
		}
		
		
	}

}
