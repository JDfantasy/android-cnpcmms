package com.cnpc.zhibo.app.adapter;

//维修端主页分配通知界面中listview的adapter
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

public class Item_maintain_alloction_adapter extends MyBaseAdapter<Maintain_home_alloction_item> {

	// 新的数据排序
	public boolean ck1 = false;
	public boolean ck2 = false;
	public int tag1;
	public int tag2;

	public Item_maintain_alloction_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintain_home_alloction, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.title.setVisibility(View.GONE);
		}

		Maintain_home_alloction_item m = (Maintain_home_alloction_item) getItem(position);
		h.personName.setText(m.personName);
		h.gasStationname.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);

		// h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(m.iconPath[0]);
		// 不用加下面的判断方法了
		// if
		// (m.iconPath!=null&&m.iconPath.length!=0&&!m.iconPath[0].equals("")) {
		// h.icon.setImageUrl(m.iconPath[0]);
		// }

		/*
		 * if(m.timeState!=null&&m.timeState.equals("weekBefore")){
		 * h.title.setVisibility(View.VISIBLE); h.titledate.setText("一周内的维修单");
		 * } if(m.timeState!=null&&m.timeState.equals("weekAfter")){
		 * h.title.setVisibility(View.VISIBLE); h.titledate.setText("一周前的维修单");
		 * }
		 */

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && ck1 == false) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("最后一周的维修单");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && position == tag1) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("最后一周的维修单");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && ck2 == false) {
				h.title.setVisibility(View.VISIBLE);
				h.displayDate.setText("最后一个月的维修单");
				ck2 = true;
				tag2 = position;
			} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && position == tag2) {
				h.title.setVisibility(View.VISIBLE);
				h.displayDate.setText("最后一个月的维修单");
			}

		}

		// 开始会话按钮暂时删除
		/*
		 * h.conversation.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO 点击跳转进入到会话列表
		 * Log.i("tag", "h.btn.setOnClickListener"); } });
		 */
		return v;
	}

	private class Hoderview {
		// 站名名称，加油站名称，问题，时间，标题文字
		private TextView personName, gasStationname, problem, time,  displayDate;
		private FrameLayout title;
		private LinearLayout footer;
		private Myroundcacheimageview icon;// 左侧显示的图片

		// private Button btn;

		public Hoderview(View v) {
			super();
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.item_maintainhome_alloction_leftimage);
			this.personName = (TextView) v.findViewById(R.id.textView_item_maintain_homealloction_personName);
			this.gasStationname = (TextView) v.findViewById(R.id.textView_item_maintainhome_alloction_gasStationName);
			this.problem = (TextView) v.findViewById(R.id.textView_item_maintainhome_alloction_problem);
			this.time = (TextView) v.findViewById(R.id.textView_item_maintainhome_alloction_time);
			this.displayDate = (TextView) v.findViewById(R.id.item_maintainhome_alloction_titledateTextView);
			this.title = (FrameLayout) v.findViewById(R.id.item_maintain_home_alloction_titleFrameLayout);
			this.footer = (LinearLayout) v.findViewById(R.id.item_maintainhome_alloction_footerLinearlayout);

		}

	}

}
