package com.cnpc.zhibo.app.adapter;

//维修端的维修中正在维修的listview的adapter
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.ChatActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_newsverify_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;
import com.easemob.chat.core.f;
import com.easemob.easeui.EaseConstant;

public class Item_maintainhome_service_adapter extends MyBaseAdapter<Maintain_startList_item> {

	private String state;// 返回状态
	private Context context;
	private Maintain_startList_item m;
	private int finishNumber = 0;// 完成订单的方法编号
	private int closeNumber = 1;// 关闭订单方法编号
	// 新的数据排序
	public boolean ck1 = false;
	public boolean ck2 = false;
	public int tag1;
	public int tag2;

	private boolean scrollState = false; // 标识是否存在滚屏操作

	public void setScrollState(boolean scrollState) {
		this.scrollState = scrollState;
	}

	public Item_maintainhome_service_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintainhome_service, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.title.setVisibility(View.GONE);
		}
		// 添加工具方法当前时间到订单时间来显示最后一周，最后一月，半年前，或一周前
		m = (Maintain_startList_item) getItem(position);
		state = m.serviceState;

		/*
		 * if (m.iconPath!=null&&!m.iconPath.equals("")) {
		 * h.icon.setImageUrl(m.iconPath); }
		 */

		/*
		 * if(m.timeState!=null&&m.timeState.equals("weekBefore")){
		 * h.title.setVisibility(View.VISIBLE);
		 * h.displayDate.setText("一周内的维修单"); }
		 * if(m.timeState!=null&&m.timeState.equals("weekAfter")){
		 * h.title.setVisibility(View.VISIBLE);
		 * h.displayDate.setText("一周前的维修单"); }
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
		

		h.indentNumber.setText(m.indentNumber);
		h.gasstationname.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);
		// h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(m.iconPath);

		// 判断维修单的状态的方法
		h.state.setText("维修中");
		if (state != null && state.length() != 0) {

			if (state.equals("unsended")) {
				h.state.setText("未发送");
			}
			if (state.equals("unsubmited")) {
				h.state.setText("待提交");
			}
			if (state.equals("progress")) {
				h.state.setText("正在维修");
			}
			if (state.equals("wait")) {
				h.state.setText("等待审批");
			}
			if (state.equals("approved")) {
				h.state.setText("审批通过");
			}
			if (state.equals("completeconfirm")) {
				h.state.setText("完成待确认");
			}
			if (state.equals("continued")) {
				h.state.setText("继续维修");
			}
			if (state.equals("unapproved")) {
				h.state.setText("审批驳回");
			}
			if (state.equals("closed")) {
				h.state.setText("已关闭");
			}
			if (state.equals("closeconfirm")) {
				h.state.setText("关闭待确认");
			}
			if (state.equals("compelted")) {
				h.state.setText("已完成");
			}
			if (state.equals("unevaluated")) {
				h.state.setText("待评价");
			}
			if (state.equals("evaluated")) {
				h.state.setText("已评价");
			}
			if (state.equals("expenseing")) {
				h.state.setText("报销中");
			}
			if (state.equals("expensed")) {
				h.state.setText("已报销");
			}
		}

	

		/*if (!scrollState) {
			h.indentNumber.setText(m.indentNumber);
			h.gasstationname.setText(m.gasStationname);
			h.problem.setText(m.problem);
			h.time.setText(m.time);
			// h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setImageUrl(m.iconPath);

			// 判断维修单的状态的方法
			h.state.setText("维修中");
			if (state != null && state.length() != 0) {

				if (state.equals("unsended")) {
					h.state.setText("未发送");
				}
				if (state.equals("unsubmited")) {
					h.state.setText("待提交");
				}
				if (state.equals("progress")) {
					h.state.setText("正在维修");
				}
				if (state.equals("wait")) {
					h.state.setText("等待审批");
				}
				if (state.equals("approved")) {
					h.state.setText("审批通过");
				}
				if (state.equals("completeconfirm")) {
					h.state.setText("完成待确认");
				}
				if (state.equals("continued")) {
					h.state.setText("继续维修");
				}
				if (state.equals("unapproved")) {
					h.state.setText("审批驳回");
				}
				if (state.equals("closed")) {
					h.state.setText("已关闭");
				}
				if (state.equals("closeconfirm")) {
					h.state.setText("关闭待确认");
				}
				if (state.equals("compelted")) {
					h.state.setText("已完成");
				}
				if (state.equals("unevaluated")) {
					h.state.setText("待评价");
				}
				if (state.equals("evaluated")) {
					h.state.setText("已评价");
				}
				if (state.equals("expenseing")) {
					h.state.setText("报销中");
				}
				if (state.equals("expensed")) {
					h.state.setText("已报销");
				}
			}

		} else {
			h.indentNumber.setText(m.indentNumber);
			h.gasstationname.setText(m.gasStationname);
			h.problem.setText(m.problem);
			h.time.setText(m.time);
			h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.state.setText("加载中");
			h.state.setTextColor(context.getResources().getColor(R.color.huise));
		}*/

		h.chat.setOnClickListener(l);

		return v;
	}

	private class Hoderview {
		// 订单编号、状态、加油站名称、问题、时间、线、底部栏、标头、显示时间
		private TextView indentNumber, state, gasstationname, problem, time, line, chat, displayDate;
		private FrameLayout title;
		private LinearLayout footer;
		private Myroundcacheimageview icon;// 左侧显示的图片

		public Hoderview(View v) {
			super();
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.item_maintainhome_service_leftimage);
			this.indentNumber = (TextView) v.findViewById(R.id.textView_item_maintainhome_serviceing_indentNumber);
			this.gasstationname = (TextView) v.findViewById(R.id.item_maintainhome_service_gasstationname);
			this.problem = (TextView) v.findViewById(R.id.item_maintainhome_service_problem);
			this.time = (TextView) v.findViewById(R.id.item_maintainhome_service_time);
			this.state = (TextView) v.findViewById(R.id.maintainhome_service_stateTextView);
			// 开始会话
			this.chat = (TextView) v.findViewById(R.id.item_maintainhome_serviceing_chat);
			// 标头显示日期
			this.displayDate = (TextView) v.findViewById(R.id.item_maintainhome_service_dateTextView);
			// 标头
			this.title = (FrameLayout) v.findViewById(R.id.item_maintainhome_service_title);
		}
	}

	// 监听方法
	private View.OnClickListener l = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 开始会话
			case R.id.item_maintainhome_serviceing_chat:
				context.startActivity(
						new Intent(context, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, m.userName));
				break;
			}

		}
	};

}
