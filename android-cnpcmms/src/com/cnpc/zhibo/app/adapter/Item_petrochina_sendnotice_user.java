package com.cnpc.zhibo.app.adapter;
/*
 * 中石油端发送公告选择联系人列表的接口
 */
import com.cnpc.zhibo.app.R;

import com.cnpc.zhibo.app.entity.Petrochina_sendnoticeuser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class Item_petrochina_sendnotice_user extends MyBaseAdapter<Petrochina_sendnoticeuser> {

	public Item_petrochina_sendnotice_user(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_cnpcnsend_oticeentry, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
		}
		Petrochina_sendnoticeuser p = (Petrochina_sendnoticeuser) getItem(position);
		h.ch.setText(p.name);

		h.ch.setChecked(p.check);
		return v;
	}

	private class Hoderview {
		private CheckBox ch;

		public Hoderview(View v) {
			super();
			this.ch = (CheckBox) v.findViewById(R.id.checkBox_item_cnpcsend_noticeentry);
		}

	}

}
