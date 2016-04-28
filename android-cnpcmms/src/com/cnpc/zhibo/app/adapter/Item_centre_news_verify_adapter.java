package com.cnpc.zhibo.app.adapter;
//站长端的信息确认界面中listview的adapter
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_newsverify_item;

public class Item_centre_news_verify_adapter extends
		MyBaseAdapter<Centre_newsverify_item> {

	public Item_centre_news_verify_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_centre_newsverify, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		Centre_newsverify_item i = (Centre_newsverify_item) getItem(position);
		h.name.setText(i.name);
		h.key.setText(i.key);
		return v;
	}

	private class Hoderview {
		private TextView name, key;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v
					.findViewById(R.id.textView_item_centre_newsverify_name);
			this.key = (TextView) v
					.findViewById(R.id.textView_item_centre_newsverify_key);
		}

	}

}
