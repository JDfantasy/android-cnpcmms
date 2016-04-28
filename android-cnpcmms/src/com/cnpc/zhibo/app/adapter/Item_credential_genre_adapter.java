package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Credential_genre;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Item_credential_genre_adapter extends MyBaseAdapter<Credential_genre> {

	public Item_credential_genre_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v==null) {
			v=inflater.inflate(R.layout.item_textview, null);
			h=new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
		}
		Credential_genre g=(Credential_genre) getItem(position);
		h.name.setText(g.name);
		return v;
	}
	private class Hoderview{
		private TextView name;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v.findViewById(R.id.textView_item_textview_name);
		}
		
	}

}
