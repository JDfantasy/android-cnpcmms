package com.cnpc.zhibo.app.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cnpc.zhibo.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.format.Time;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class Myutil {
	private static int version = Integer.valueOf(android.os.Build.VERSION.SDK); // 设置跳转动画的第一步

	// 设置打开activity界面的跳转效果
	public static void set_activity_open(Activity c) {
		if (version > 5) {
			// 设置跳转动画的第二步
			c.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	}

	// 设置关闭activity界面的跳转效果
	public static void set_activity_close(Activity c) {
		if (version > 5) {
			// 设置跳转动画的第二步
			c.overridePendingTransition(R.anim.tuimout, R.anim.tuimin);
		}
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles.length() == 11) {
			return true;
		} else {
			return false;
		}
	}

	// 获取当前时间的方法——格式——格式——yyyy-MM-dd HH:mm:ss
	public static String get_current_time() {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Time time = new Time();
		time.setToNow();
		int year = time.year;// 年
		int month = time.month;// 月
		int day = time.monthDay;// 天
		int hour = time.hour;// 小时
		int minute = time.minute;// 分钟
		int sec = time.second;// 秒
		String str1 = year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute + ":" + sec;
		return str1;

	}

	// 获取当前时间的前一周的时间
	public static String get_aweek_time() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;

		if (day > 8) {
			return year + "-" + (month + 1) + "-" + (day - 7);
		} else {
			int a = 7 - day;
			if (month == 2) {
				return year + "-" + month + "-" + (29 - a);
			} else {
				if (month == 0) {
					return (year - 1) + "-" + 12 + "-" + (31 - a);
				}
				return year + "-" + month + "-" + (30 - a);
			}
		}

	}

	// 获取当前时间的前一个月的时间
	public static String get_month_time() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		if (month == 0) {
			return (year - 1) + "-" + 12 + "-" + day;
		} else {

			return year + "-" + (month - 1) + "-" + day;

		}

	}

	/*
	 * 获取当前时间的前三个月的时间
	 */
	public static String get_threemonth_time() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		if (month <= 2) {
			return (year - 1) + "-" + (12 + month - 3) + "-" + day;
		} else {

			return year + "-" + (month - 3) + "-" + day;

		}

	}
	/*
	 * 获取当前时间的前六个月的时间
	 */
	public static String get_sixmonth_time() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		if (month <= 5) {
			return (year - 1) + "-" + (12 + month -6) + "-" + day;
		} else {

			return year + "-" + (month - 6) + "-" + day;

		}

	}

	/*
	 * 获取当前时间的前一年的时间
	 */
	public static String get_oneyear_time() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;

		return (year - 1) + "-" + (month - 1) + "-" + day;

	}

	/*
	 * 判断并且显示维修单的状态
	 */
	public static String getjudgestatus(String name) {
		String str = name;

		if (name.equals("progress")) {
			str = "正在维修";
		}
		if (name.equals("unsubmited")) {
			str = "待提交";
		}
		if (name.equals("completeconfirm")) {
			str = "完成待确认";
		}
		if (name.equals("wait")) {
			str = "等待审批";
		}
		if (name.equals("approved")) {
			str = "审批通过";
		}
		if (name.equals("continued")) {
			str = "继续维修";
		}
		if (name.equals("unapproved")) {
			str = "审批驳回";
		}
		if (name.equals("evaluated")) {
			str = "已审批";
		}
		if (name.equals("unevaluated")) {
			str = "待评价";
		}
		if (name.equals("compelted")) {
			str = "已完成";
		}
		if (name.equals("closed")) {
			str = "已关闭";
		}
		if (name.equals("evaluated")) {
			str = "已评价";
		}
		if (name.equals("closeconfirm")) {
			str = "关闭待确认";
		}
		return str;
	}

	/*
	 * 判断并且显示维修单的状态
	 */
	public static String getjudgestatuscolour(String name, TextView t, Context c) {
		String str = name;
		if (name.equals("unsended")) {
			str = "未发送";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("progress")) {
			str = "正在维修";
			t.setTextColor(c.getResources().getColor(R.color.hongse));
		}
		if (name.equals("unsubmited")) {
			str = "待提交";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("completeconfirm")) {
			str = "完成待确认";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("wait")) {
			str = "等待审批";
			t.setTextColor(c.getResources().getColor(R.color.hongse));
		}
		if (name.equals("approved")) {
			str = "审批通过";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("continued")) {
			str = "继续维修";
			t.setTextColor(c.getResources().getColor(R.color.hongse));
		}
		if (name.equals("unapproved")) {
			str = "审批驳回";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("evaluated")) {
			str = "已审批";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("unevaluated")) {
			str = "待评价";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("compelted")) {
			str = "已完成";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("closed")) {
			str = "已关闭";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("evaluated") || name.equals("expenseing") || name.equals("expensed")) {
			str = "已完成";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("closeconfirm")) {
			str = "关闭待确认";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		return str;
	}

	// 获取当前的时间——格式——yyyy-MM-dd
	public static String get_time_ymd() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		return year + "-" + (month + 1) + "-" + day;
	}

	// 获取时间差的方法
	@SuppressLint("SimpleDateFormat")
	public static String get_delta_t(String max, String min) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date d1 = df.parse(max);
			Date d2 = df.parse(min);
			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别

			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = 1;
			if ((diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)>= (1000 * 60)*1)) {
				minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			} else {

			}
			return "" + days + "天" + hours + "小时" + minutes + "分";
			// System.out.println("" + days + "天" + hours + "小时" + minutes +
			// "分");

		} catch (Exception e) {
		}
		return null;

	}

	// 获取当前的时差，时差只有天的方法
	public static int get_delta_t_data(String max, String min) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(max);

			Date d2 = df.parse(min);
			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			return (int) days;
			// System.out.println("" + days + "天" + hours + "小时" + minutes +
			// "分");

		} catch (Exception e) {
		}
		return -1;

	}

	// 获取时间判断一周
	public static int get_delta_t_data1(String max, String min) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(max);
			Date d2 = df.parse(min);
			Calendar now = Calendar.getInstance();// 取得日历对象默认格式为当前时间
			Calendar other = Calendar.getInstance();
			other.setTimeInMillis(Long.parseLong(min));// 设置传过来的时间为日期格式
			// 判断今天
			if (now.get(Calendar.YEAR) == other.get(Calendar.YEAR)
					&& now.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)) {
				// SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
				// return sdf.format(other.getTime());
				return 1;
			} else {
				return 2;
			}

		} catch (Exception e) {
		}
		return -1;

	}

	// 截取字符串的方法
	public static String set_cutString(String str) {
		if (str.length() > 12) {
			return str.substring(0, 12) + "...";

		} else {
			return str;
		}

	}
	// /**
	// * 设置返回键
	// */
	// public static void set_back(ImageView back){
	// back.setImageResource(R.drawable.back);
	// LayoutParams ps = back.getLayoutParams();
	// ps.height=100;
	// ps.width=100;
	// back.setLayoutParams(ps);
	// back.setPadding(50, 50, 0, 30);
	// }
}
