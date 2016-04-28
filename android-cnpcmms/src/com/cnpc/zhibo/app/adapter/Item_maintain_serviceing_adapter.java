package com.cnpc.zhibo.app.adapter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.Maintain_apply_newapplylistActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.app.ActionBar.LayoutParams;
//维修端的维修中正在维修的listview的adapter
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Item_maintain_serviceing_adapter extends MyBaseAdapter<Maintain_startList_item> {

	private Context context;
	private PopupWindow pop;
	private Maintain_startList_item m;
	private int finishNumber = 0;// 完成订单的方法编号
	private int closeNumber = 1;// 关闭订单方法编号
	private Hoderview h;
	// 新的数据排序
	public boolean ck1 = false;
	public boolean ck2 = false;
	public int tag1;
	public int tag2;

	private  boolean scrollState=false; //标识是否存在滚屏操作
	
	public void setScrollState(boolean scrollState) {  
        this.scrollState = scrollState;  
    } 
	
	public Item_maintain_serviceing_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(final int position, View v, ViewGroup parent) {
		
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintain_serviceing, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.title.setVisibility(View.GONE);
		}
		// 添加工具方法当前时间到订单时间来显示最后一周，最后一月，半年前，或一周前
		//m = (Maintain_startList_item) getItem(position);
		m = (Maintain_startList_item) data.get(position);

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && ck1 == false) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("最后一周的维修单");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && position == tag1) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("最后一周的维修单");
		} else {
			if(Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7){
				if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && ck2 == false) {
					h.title.setVisibility(View.VISIBLE);
					h.displayDate.setText("最后一个月的维修单");
					ck2 = true;
					tag2 = position;
				} else if (position == tag2) {
					h.title.setVisibility(View.VISIBLE);
					h.displayDate.setText("最后一个月的维修单");
				}
			}
		}
		
//		Log.i("tag", "adapter.tag1="+tag1);
//		Log.i("tag", "adapter.tag2="+tag2);
		
		/*if (!scrollState){  
			h.indentNumber.setText(m.indentNumber);
			h.name.setText(m.gasStationname);
			h.problem.setText(m.problem);
			h.time.setText(m.time);
			
			//h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setImageUrl(m.iconPath);
			
			h.state.setText("维修中");
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.GONE);
			h.starrating.setVisibility(View.GONE);
			h.line.setVisibility(View.GONE);
			if (m.serviceState.equals("unsended")) {
				h.state.setText("未发送");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("unsubmited")) {
				h.state.setText("待提交");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("progress")) {
				h.state.setText("正在维修");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.VISIBLE);
				h.closeIndent.setVisibility(View.VISIBLE);
				h.newApplylist.setVisibility(View.GONE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("wait")) {
				h.state.setText("等待审批");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("approved")) {
				h.state.setText("审批通过");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("completeconfirm")) {
				h.state.setText("完成待确认");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("continued")) {
				h.state.setText("继续维修");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.VISIBLE);
				h.closeIndent.setVisibility(View.VISIBLE);
				h.newApplylist.setVisibility(View.GONE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("unapproved")) {
				h.state.setText("审批驳回");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("closed")) {
				h.state.setText("已关闭");
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			} else if (m.serviceState.equals("closeconfirm")) {
				h.state.setText("关闭待确认");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("compelted")) {
				h.state.setText("已完成");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.GONE);
				h.closeIndent.setVisibility(View.GONE);
				h.newApplylist.setVisibility(View.VISIBLE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			} else if (m.serviceState.equals("unevaluated")) {
				h.state.setText("待评价");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.GONE);
				h.closeIndent.setVisibility(View.GONE);
				h.newApplylist.setVisibility(View.VISIBLE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			} else if (m.serviceState.equals("evaluated")) {
				h.state.setText("已评价");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.GONE);
				h.closeIndent.setVisibility(View.GONE);
				h.newApplylist.setVisibility(View.VISIBLE);
				h.starrating.setVisibility(View.VISIBLE);
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
				//h.starrating.setProgressTintList(Color.RED);
				if (m.starNumber == null||m.starNumber.equals("null")) {
					h.starrating.setRating(1);
				} else {
					h.starrating.setRating(Float.parseFloat(m.starNumber));
				}
			}else if (m.serviceState.equals("expenseing")) {
				h.state.setText("报销中");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
			}else if (m.serviceState.equals("expensed")) {
				h.state.setText("已报销");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
			}
  
        }else{  
        	h.indentNumber.setText(m.indentNumber);
    		h.name.setText(m.gasStationname);
    		h.problem.setText(m.problem);
    		h.time.setText(m.time);
			h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.state.setText("加载中"); 
			h.state.setTextColor(context.getResources().getColor(R.color.huise));
        }*/  
		  
		h.indentNumber.setText(m.indentNumber);
		h.name.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);
		
		//h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(m.iconPath);
		
		h.state.setText("维修中");
		h.finishservice.setVisibility(View.GONE);
		h.closeIndent.setVisibility(View.GONE);
		h.newApplylist.setVisibility(View.GONE);
		h.starrating.setVisibility(View.GONE);
		if (m.serviceState.equals("unsended")) {
			h.state.setText("未发送");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("unsubmited")) {
			h.state.setText("待提交");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("progress")) {
			h.state.setText("正在维修");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.VISIBLE);
			h.closeIndent.setVisibility(View.VISIBLE);
			h.newApplylist.setVisibility(View.GONE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("wait")) {
			h.state.setText("等待审批");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("approved")) {
			h.state.setText("审批通过");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("completeconfirm")) {
			h.state.setText("完成待确认");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("continued")) {
			h.state.setText("继续维修");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.VISIBLE);
			h.closeIndent.setVisibility(View.VISIBLE);
			h.newApplylist.setVisibility(View.GONE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("unapproved")) {
			h.state.setText("审批驳回");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("closed")) {
			h.state.setText("已关闭");
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else if (m.serviceState.equals("closeconfirm")) {
			h.state.setText("关闭待确认");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("compelted")) {
			h.state.setText("已完成");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.VISIBLE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else if (m.serviceState.equals("unevaluated")) {
			h.state.setText("待评价");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.VISIBLE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else if (m.serviceState.equals("evaluated")) {
			h.state.setText("已评价");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.VISIBLE);
			h.starrating.setVisibility(View.VISIBLE);
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			//h.starrating.setProgressTintList(Color.RED);
			if (m.starNumber == null||m.starNumber.equals("null")) {
				h.starrating.setRating(1);
			} else {
				h.starrating.setRating(Float.parseFloat(m.starNumber));
			}
		}else if (m.serviceState.equals("expenseing")) {
			h.state.setText("报销中");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
		}else if (m.serviceState.equals("expensed")) {
			h.state.setText("已报销");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
		}

    
		// 完成维修
		h.finishservice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterCanle(v, finishNumber, data.get(position).id, position);
			}
		});
		// 关闭订单
		h.closeIndent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterCanle(v, closeNumber, data.get(position).id, position);
			}
		});
		// 新建报销单
		// h.newApplylist.setOnClickListener(l);
		h.newApplylist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(context, Maintain_apply_newapplylistActivity.class);
					intent.putExtra("id", data.get(position).id);
					intent.putExtra("gasStationname", data.get(position).gasStationname);
					context.startActivity(intent);
					h.newApplylist.setVisibility(View.GONE);
//				getData().remove(position);
//				getData().get(position).serviceState="expenseing";
//				notifyDataSetChanged();
//				applylist();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return v;
	}
	
	private void applylist(){
		this.notifyDataSetChanged();
		h.newApplylist.setVisibility(View.GONE);
	}

	private class Hoderview {
		// 订单编号、状态、名称、问题、时间、底部栏、标头、显示时间,新建报销单
		private TextView indentNumber, state, name, problem, time, finishservice, closeIndent, displayDate,
				newApplylist;
		private FrameLayout title;
		private LinearLayout footer;
		private Myroundcacheimageview icon;//左侧显示的图片
		private RatingBar starrating;//星星

		public Hoderview(View v) {
			super();
			this.starrating=(RatingBar) v.findViewById(R.id.item_maintain_serviceing_ratingBar);//星星
			this.icon=(Myroundcacheimageview) v.findViewById(R.id.item_maintainhome_serivce_leftimage);
			this.indentNumber = (TextView) v.findViewById(R.id.textView_item_maintain_serviceing_indentNumber);
			this.name = (TextView) v.findViewById(R.id.item_maintain_service_name);
			this.problem = (TextView) v.findViewById(R.id.item_maintain_service_problem);
			this.time = (TextView) v.findViewById(R.id.item_maintain_service_time);
			this.state = (TextView) v.findViewById(R.id.maintain_service_stateTextView);
			// 底部
			this.footer = (LinearLayout) v.findViewById(R.id.item_maintain_page_Linearlayout_footer);
			// 完成维修
			this.finishservice = (TextView) v.findViewById(R.id.item_maintain_serviceing_finishservice);
			// 关闭订单
			this.closeIndent = (TextView) v.findViewById(R.id.item_maintain_serviceing_closeIndent);
			// 整个标头
			this.title = (FrameLayout) v.findViewById(R.id.item_maintain_service_title);
			// 标头显示日期
			this.displayDate = (TextView) v.findViewById(R.id.item_maintain_service_dateTextView);
			// 新建报销单
			this.newApplylist = (TextView) v.findViewById(R.id.item_maintain_serviceing_newapplylist);
		}
	}

	// 监听方法
	/*
	 * private View.OnClickListener l = new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { switch (v.getId()) { // 完成维修
	 * 
	 * case R.id.item_maintain_serviceing_finishservice: Toast.makeText(context,
	 * "请等待站长端确认", 0).show(); closeAndFinishIndent(finishNumber); break; // 关闭订单
	 * case R.id.item_maintain_serviceing_closeIndent: Toast.makeText(context,
	 * "关闭订单，将订单从当前界面删除", 0).show(); closeAndFinishIndent(closeNumber);
	 * notifyDataSetChanged(); break;
	 * 
	 * // 新建维修单 case R.id.item_maintain_serviceing_newapplylist:
	 * Toast.makeText(context, "新建报销单", 0).show(); Intent intent=new
	 * Intent(context, Maintain_apply_newapplylistActivity.class);
	 * intent.putExtra("id", data.get(location)); context.startActivity(intent);
	 * break; }
	 * 
	 * } };
	 */

	// 弹出确认和取消框
	private void enterCanle(View v, final int number, final String id, final int positon) {
		System.out.println("postion:" + positon);
		System.out.println("number:" + number);
		View v1 = LayoutInflater.from(context).inflate(R.layout.poppup_mainhome_closefinish_entercanle, null);// 创建view
		pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
		// pop.setBackgroundDrawable(new ColorDrawable());
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
		pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		Button enter = (Button) v1.findViewById(R.id.button_popup_closefinish_enter);// 确认按钮
		Button canle = (Button) v1.findViewById(R.id.button_popup_closefinish_canle);// 取消按钮
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeAndFinishIndent(number, id, positon);

				pop.dismiss();

			}
		});
		canle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
	}

	// 关闭订单和完成订单
	public void closeAndFinishIndent(final int number, final String id, final int postion) {
		String url = null;
		if (number == finishNumber) {// 维修结束,完成订单
			url = Myconstant.MAINTAIN_SERVICE_ITEM_FINISH;
		} else if (number == closeNumber) {// 无法维修关闭订单
			url = Myconstant.MAINTAIN_SERVICE_ITEM_CLOSED;
		}
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
						if (number == finishNumber) {// 维修结束,完成订单
							getData().get(postion).serviceState = "completeconfirm";
							notifyDataSetChanged();
						} else if (number == closeNumber) {// 无法维修关闭订单
							getData().get(postion).serviceState = "closeconfirm";
							notifyDataSetChanged();

						} else {
							notifyDataSetChanged();
						}
					} else {
						Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, "访问失败：" + error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);// 报修单编号
				return map;
			}
		};
		SysApplication.getHttpQueues().add(request);
	}

}
