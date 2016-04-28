package com.cnpc.zhibo.app.adapter;

/*
 * ֤����������adapter
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_credentials_intro;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

public class Item_centre_credenttials_intro_adapter extends MyBaseAdapter<Centre_credentials_intro> {

	public Item_centre_credenttials_intro_adapter(Context context) {
		super(context);

	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_centre_certificate, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		Centre_credentials_intro in = (Centre_credentials_intro) getItem(position);
		h.certificatetype.setText(in.certificatetype);
		h.hzslrname.setText("��֤�����ˣ�"+in.hzslrname);
		h.validtime.setText("��Чʱ�䣺"+in.validtime);
		h.alarmDate.setText("����ʱ�䣺"+in.alarmDate);
		h.keeper.setText("�����ˣ�"+in.keeper);

		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);

		h.icon.setImageUrl(in.icon);

		return v;
	}

	private class Hoderview {
		private TextView certificatetype;// ֤������
		private TextView validtime;// ��Чʱ��
		private TextView hzslrname;// ��֤������
		private TextView alarmDate;// ����ʱ��
		private TextView keeper;// ������
		private Myroundcacheimageview icon;// ͼƬ

		public Hoderview(View v) {
			super();
			this.certificatetype = (TextView) v.findViewById(R.id.textView_item_centre_certificate_certificatetype);
			this.validtime = (TextView) v.findViewById(R.id.textView_item_centre_certificate_validtime);
			this.hzslrname = (TextView) v.findViewById(R.id.textView_item_centre_certificate_hzslrname);
			this.alarmDate = (TextView) v.findViewById(R.id.textView_item_centre_certificate_alarmDate);
			this.keeper = (TextView) v.findViewById(R.id.textView_item_centre_certificate_keeper);
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.imageView_item_centre_certificate_icon);
		}

	}
}
