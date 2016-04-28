package com.cnpc.zhibo.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Supplier_notice;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

/**
 * ��Ӧ�̹��������б��adapter
 */
public class Item_supplier_notice_adapter extends MyBaseAdapter<Supplier_notice> {
	private boolean ck1 = false;
	private boolean ck2 = false;
	private int tag1;
	private int tag2;
	private boolean viewstate=false;

	public Item_supplier_notice_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/*
	 * ��ʯ�Ͷ˵��ã������������ֵ�
	 */
	public void setviewstate(){
		this.viewstate=true;
		notifyDataSetChanged();
	}

	@SuppressLint("InflateParams")
	@Override
	public View setView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_supplier_notice, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.item_title.setVisibility(View.GONE);
		}
		Supplier_notice a = (Supplier_notice) getItem(position);
		holder.noticeTitle.setText(a.noticeTitle);
		holder.content.setText(a.content);
		holder.time.setText(a.time.subSequence(0, 6));
		if(Integer.valueOf(a.unreadcount) > 0){
			holder.unreadcount.setText(a.unreadcount);
		}else{
			holder.unreadcount.setVisibility(View.GONE);
		}
		if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) < 7 && ck1 == false) {
			holder.item_title.setVisibility(View.VISIBLE);
			holder.item_title.setText("���һ�ܵĹ���");
			ck1 = true;
			tag1 = position;
		} else if (position == tag1) {
			holder.item_title.setVisibility(View.VISIBLE);
			holder.item_title.setText("���һ�ܵĹ���");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && ck2 == false) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µĹ���");
				ck2 = true;
				tag2 = position;
			} else if (position == tag2) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µĹ���");
			}

		}
		if (viewstate) {
			holder.unreadcount.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView noticeTitle;
		private TextView content;
		private BadgeView unreadcount;
		private TextView time;
		// private ImageView notice_img;
		private TextView item_title; // item����

		public ViewHolder(View v) {
			super();
			this.noticeTitle = (TextView) v.findViewById(R.id.notice_title);
			this.content = (TextView) v.findViewById(R.id.notice_content);
			this.time = (TextView) v.findViewById(R.id.notice_time);
			this.unreadcount = (BadgeView) v.findViewById(R.id.textview_supplier_unreadcount);
			// this.notice_img=(ImageView)
			// v.findViewById(R.id.imageView_supplier_notice);
			this.item_title = (TextView) v.findViewById(R.id.textView_item_supplier_page_approve_item_title1);
		}

	}
}
