package com.cnpc.zhibo.app.adapter;

/*
 * 站长端报修单列表的adapter
 */
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

public class Item_centre_page_appove_adapter extends MyBaseAdapter<Centre_page_approve> {
	private boolean ck1 = false;
	private boolean ck2 = false;
	private int tag1 = 0;
	private int tag2 = 0;
	private Context c;

	public Item_centre_page_appove_adapter(Context context) {
		super(context);
		this.c = context;
		// TODO Auto-generated constructor stub
	}

	/*
	 * 将adapter中的记录数据变化的值进行初始化
	 */
	public void setinitializedata() {
		tag1 = 0;
		tag2 = 0;
		ck1 = false;
		ck2 = false;
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_centre_page_approve, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.item_title.setVisibility(View.GONE);
			h.item_title.setText("最近一周的报修单");

		}
		Centre_page_approve a = (Centre_page_approve) getItem(position);
		h.number.setText("编号："+a.number);// 编号
		h.state.setText(a.state);// 状态
		if (a.state.equals("wait")) {
			h.state.setText("未处理");// 状态
			h.state.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		} else if (a.state.equals("completed")) {
			h.state.setText("已处理");// 状态
			h.state.setTextColor(c.getResources().getColor(R.color.blue));
		} else {
			h.state.setText("已关闭");// 状态
			h.state.setTextColor(c.getResources().getColor(R.color.hongse));
		}
		h.statetime.setText("耗时："+a.statetime);// 状态时间
		h.time.setText("时间："+a.time);// 时间
		h.title.setText("问题："+a.title);// 简介
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(a.icon);
		h.item_title.setVisibility(View.GONE);
		h.item_title.setText("最近一周的报修单");
		if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7 && ck1 == false) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("最近一周的报修单");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7 && position == tag1) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("最近一周的报修单");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7) {
				if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && ck2 == false) {
					h.item_title.setVisibility(View.VISIBLE);
					h.item_title.setText("最后一个月的报修单");
					ck2 = true;
					tag2 = position;
				} else if (position == tag2) {
					h.item_title.setVisibility(View.VISIBLE);
					h.item_title.setText("最后一个月的报修单");
				}
			}

		}
		return v;
	}

	private class Hoderview {
		private TextView number;// 编号
		private TextView time;// 时间
		private TextView title;// 简介
		private TextView state;// 状态
		private TextView statetime;// 状态时间
		private TextView item_title;// item的标题
		private Myroundcacheimageview icon;//

		public Hoderview(View v) {
			super();
			this.number = (TextView) v.findViewById(R.id.textView_item_centre_page_approve_number);
			this.time = (TextView) v.findViewById(R.id.textView_item_centre_page_approve_time);
			this.title = (TextView) v.findViewById(R.id.textView_item_centre_page_approve_title);
			this.state = (TextView) v.findViewById(R.id.textView_item_centre_page_approve_state);
			this.statetime = (TextView) v.findViewById(R.id.textView_item_centre_page_approve_statetime);
			this.item_title = (TextView) v.findViewById(R.id.textView_item_centre_page_approve_item_title);
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.imageview_item_centre_page_approve);
		}

	}

}
