package com.cnpc.zhibo.app.adapter;
//站长端要保修项目的adapter
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_Repairs_item;

public class Item_repairs_item_adapter extends MyBaseAdapter<Centre_Repairs_item> {
	private int setid = -1, upid = -1;
	private boolean b;//用来内部判断执行那个方法
	private Context c;

	public Item_repairs_item_adapter(Context context) {
		super(context);
		this.c = context;
	}

	public void setcolor(int id) {
		this.setid = id;
		this.b = true;// 用来判断在执行setcolor方法时，就不执行upcolor
		notifyDataSetChanged();
	}

	public void upcolor(int id) {
		this.upid = id;
		this.b = false;// 用来判断在执行upcolor方法时，就不执行setcolor
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
