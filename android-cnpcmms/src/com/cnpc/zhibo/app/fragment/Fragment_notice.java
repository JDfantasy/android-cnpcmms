package com.cnpc.zhibo.app.fragment;
/*
 * 公告的fragment
 */
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Commonality_notice;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_notice extends Fragment{
	private Commonality_notice notice;
	private TextView title,centent,time;
	public Fragment_notice(Commonality_notice notice) {
		super();
		this.notice = notice;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//View v = inflater.inflate(R.layout.fragment_notice, container, false);
		View v = inflater.inflate(R.layout.fragment_notice, null);
		title=(TextView) v.findViewById(R.id.textView_fragment_notice_title);
		centent=(TextView) v.findViewById(R.id.textView_fragment_notice_content);
		time=(TextView) v.findViewById(R.id.textView_fragment_notice_time);
		title.setText("标题："+notice.title);
		centent.setText("内容："+notice.content);
		time.setText("时间："+notice.time);
		return v;
	}
}
