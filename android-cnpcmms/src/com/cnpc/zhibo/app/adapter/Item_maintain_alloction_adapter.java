package com.cnpc.zhibo.app.adapter;

//ά�޶���ҳ����֪ͨ������listview��adapter
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

	// �µ���������
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
		// ���ü�������жϷ�����
		// if
		// (m.iconPath!=null&&m.iconPath.length!=0&&!m.iconPath[0].equals("")) {
		// h.icon.setImageUrl(m.iconPath[0]);
		// }

		/*
		 * if(m.timeState!=null&&m.timeState.equals("weekBefore")){
		 * h.title.setVisibility(View.VISIBLE); h.titledate.setText("һ���ڵ�ά�޵�");
		 * } if(m.timeState!=null&&m.timeState.equals("weekAfter")){
		 * h.title.setVisibility(View.VISIBLE); h.titledate.setText("һ��ǰ��ά�޵�");
		 * }
		 */

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && ck1 == false) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵ�ά�޵�");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && position == tag1) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵ�ά�޵�");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && ck2 == false) {
				h.title.setVisibility(View.VISIBLE);
				h.displayDate.setText("���һ���µ�ά�޵�");
				ck2 = true;
				tag2 = position;
			} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && position == tag2) {
				h.title.setVisibility(View.VISIBLE);
				h.displayDate.setText("���һ���µ�ά�޵�");
			}

		}

		// ��ʼ�Ự��ť��ʱɾ��
		/*
		 * h.conversation.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO �����ת���뵽�Ự�б�
		 * Log.i("tag", "h.btn.setOnClickListener"); } });
		 */
		return v;
	}

	private class Hoderview {
		// վ�����ƣ�����վ���ƣ����⣬ʱ�䣬��������
		private TextView personName, gasStationname, problem, time,  displayDate;
		private FrameLayout title;
		private LinearLayout footer;
		private Myroundcacheimageview icon;// �����ʾ��ͼƬ

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
