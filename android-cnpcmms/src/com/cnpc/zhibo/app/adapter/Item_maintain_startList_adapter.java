package com.cnpc.zhibo.app.adapter;
//维修端主页开始界面、维修单、维修中等待维修、历史维修listview的adapter
//这是改过的
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_newsverify_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.GlobalConsts;

public class Item_maintain_startList_adapter extends
		MyBaseAdapter<Maintain_startList_item> {

	private String state;
	private Maintain_startList_item m;
	
	public Item_maintain_startList_adapter(Context context) {
		super(context);
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintain_startlist, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		m = (Maintain_startList_item) getItem(position);
		h.indentNumber.setText(m.indentNumber);
		//判断四个状态，显示不同内容
		/*switch (serviceState) {
		case GlobalConsts.MAINTAIN_HOME_NOTFINISH://未完成
			h.days.setText("已经报修5天");
			break;
		case GlobalConsts.MAINTAIN_HOME_ALREADYTFINISH://已完成
			h.days.setText("已完成");
			break;
		case GlobalConsts.MAINTAIN_SERVICEING://正在维修
			h.days.setText("正在维修");
			break;
		case GlobalConsts.MAINTAIN_WAITSERVICE://等待维修
			h.days.setText("等待维修");
			break;
		case GlobalConsts.MAINTAIN_HISTORYSERVICE://历史维修
			h.days.setText("已完成");
			break;
		default :
			h.days.setText("不知道");
			break;
		}*/
		h.name.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);
		/*if(m.timeState!=null&&m.timeState.equals("weekBefore")){
			h.title.setVisibility(View.VISIBLE);
			h.titledate.setText("一周内的维修单");
		}
		if(m.timeState!=null&&m.timeState.equals("weekAfter")){
			h.title.setVisibility(View.VISIBLE);
			h.titledate.setText("一周前的维修单");
		}*/
		state=m.serviceState;;
		// 判断维修单的状态的方法
				h.state.setText("无法状态");
				if (state.equals("progress")) {
					h.state.setText("正在维修");
				}
				if (state.equals("unsubmited")) {
					h.state.setText("待审批");
				}
				if (state.equals("completeconfirm")) {
					h.state.setText("完成待确认");
				}
				if (state.equals("wait")) {
					h.state.setText("等待审批");
				}
				if (state.equals("approved")) {
					h.state.setText("审批通过");
				}
				if (state.equals("continued")) {
					h.state.setText("继续维修");
				}
				if (state.equals("unapproved")) {
					h.state.setText("审批驳回");
				}
				if (state.equals("evaluated")) {
					h.state.setText("已审批");
				}
				if (state.equals("unevaluated")) {
					h.state.setText("待评价");
				}
				if (state.equals("compelted")) {
					h.state.setText("已完成");
				}
				if (state.equals("unsended")) {
					h.state.setText("未发送");
				}
				if (state.equals("closed")) {
					h.state.setText("已关闭");
				}
				if (state.equals("closeconfirm")) {
					h.state.setText("关闭待确认");
				}
		return v;
	}

	private class Hoderview {
		private TextView indentNumber,state,name,problem,time;

		public Hoderview(View v) {
			super();
			this.indentNumber = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_indentNumber);
			this.name = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_name);
			this.state = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_days);
			this.problem = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_problem);
			this.time = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_time);
			
		}

	}

}
