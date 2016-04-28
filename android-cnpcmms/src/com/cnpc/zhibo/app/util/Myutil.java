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
	private static int version = Integer.valueOf(android.os.Build.VERSION.SDK); // ������ת�����ĵ�һ��

	// ���ô�activity�������תЧ��
	public static void set_activity_open(Activity c) {
		if (version > 5) {
			// ������ת�����ĵڶ���
			c.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	}

	// ���ùر�activity�������תЧ��
	public static void set_activity_close(Activity c) {
		if (version > 5) {
			// ������ת�����ĵڶ���
			c.overridePendingTransition(R.anim.tuimout, R.anim.tuimin);
		}
	}

	/**
	 * ��֤�ֻ���ʽ
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles.length() == 11) {
			return true;
		} else {
			return false;
		}
	}

	// ��ȡ��ǰʱ��ķ���������ʽ������ʽ����yyyy-MM-dd HH:mm:ss
	public static String get_current_time() {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Time time = new Time();
		time.setToNow();
		int year = time.year;// ��
		int month = time.month;// ��
		int day = time.monthDay;// ��
		int hour = time.hour;// Сʱ
		int minute = time.minute;// ����
		int sec = time.second;// ��
		String str1 = year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute + ":" + sec;
		return str1;

	}

	// ��ȡ��ǰʱ���ǰһ�ܵ�ʱ��
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

	// ��ȡ��ǰʱ���ǰһ���µ�ʱ��
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
	 * ��ȡ��ǰʱ���ǰ�����µ�ʱ��
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
	 * ��ȡ��ǰʱ���ǰ�����µ�ʱ��
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
	 * ��ȡ��ǰʱ���ǰһ���ʱ��
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
	 * �жϲ�����ʾά�޵���״̬
	 */
	public static String getjudgestatus(String name) {
		String str = name;

		if (name.equals("progress")) {
			str = "����ά��";
		}
		if (name.equals("unsubmited")) {
			str = "���ύ";
		}
		if (name.equals("completeconfirm")) {
			str = "��ɴ�ȷ��";
		}
		if (name.equals("wait")) {
			str = "�ȴ�����";
		}
		if (name.equals("approved")) {
			str = "����ͨ��";
		}
		if (name.equals("continued")) {
			str = "����ά��";
		}
		if (name.equals("unapproved")) {
			str = "��������";
		}
		if (name.equals("evaluated")) {
			str = "������";
		}
		if (name.equals("unevaluated")) {
			str = "������";
		}
		if (name.equals("compelted")) {
			str = "�����";
		}
		if (name.equals("closed")) {
			str = "�ѹر�";
		}
		if (name.equals("evaluated")) {
			str = "������";
		}
		if (name.equals("closeconfirm")) {
			str = "�رմ�ȷ��";
		}
		return str;
	}

	/*
	 * �жϲ�����ʾά�޵���״̬
	 */
	public static String getjudgestatuscolour(String name, TextView t, Context c) {
		String str = name;
		if (name.equals("unsended")) {
			str = "δ����";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("progress")) {
			str = "����ά��";
			t.setTextColor(c.getResources().getColor(R.color.hongse));
		}
		if (name.equals("unsubmited")) {
			str = "���ύ";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("completeconfirm")) {
			str = "��ɴ�ȷ��";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("wait")) {
			str = "�ȴ�����";
			t.setTextColor(c.getResources().getColor(R.color.hongse));
		}
		if (name.equals("approved")) {
			str = "����ͨ��";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("continued")) {
			str = "����ά��";
			t.setTextColor(c.getResources().getColor(R.color.hongse));
		}
		if (name.equals("unapproved")) {
			str = "��������";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("evaluated")) {
			str = "������";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("unevaluated")) {
			str = "������";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		if (name.equals("compelted")) {
			str = "�����";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("closed")) {
			str = "�ѹر�";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("evaluated") || name.equals("expenseing") || name.equals("expensed")) {
			str = "�����";
			t.setTextColor(c.getResources().getColor(R.color.blue));
		}
		if (name.equals("closeconfirm")) {
			str = "�رմ�ȷ��";
			t.setTextColor(c.getResources().getColor(R.color.color_of_progress));
		}
		return str;
	}

	// ��ȡ��ǰ��ʱ�䡪����ʽ����yyyy-MM-dd
	public static String get_time_ymd() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		return year + "-" + (month + 1) + "-" + day;
	}

	// ��ȡʱ���ķ���
	@SuppressLint("SimpleDateFormat")
	public static String get_delta_t(String max, String min) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date d1 = df.parse(max);
			Date d2 = df.parse(min);
			long diff = d1.getTime() - d2.getTime();// �����õ��Ĳ�ֵ��΢�뼶��

			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = 1;
			if ((diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)>= (1000 * 60)*1)) {
				minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			} else {

			}
			return "" + days + "��" + hours + "Сʱ" + minutes + "��";
			// System.out.println("" + days + "��" + hours + "Сʱ" + minutes +
			// "��");

		} catch (Exception e) {
		}
		return null;

	}

	// ��ȡ��ǰ��ʱ�ʱ��ֻ����ķ���
	public static int get_delta_t_data(String max, String min) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(max);

			Date d2 = df.parse(min);
			long diff = d1.getTime() - d2.getTime();// �����õ��Ĳ�ֵ��΢�뼶��
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			return (int) days;
			// System.out.println("" + days + "��" + hours + "Сʱ" + minutes +
			// "��");

		} catch (Exception e) {
		}
		return -1;

	}

	// ��ȡʱ���ж�һ��
	public static int get_delta_t_data1(String max, String min) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(max);
			Date d2 = df.parse(min);
			Calendar now = Calendar.getInstance();// ȡ����������Ĭ�ϸ�ʽΪ��ǰʱ��
			Calendar other = Calendar.getInstance();
			other.setTimeInMillis(Long.parseLong(min));// ���ô�������ʱ��Ϊ���ڸ�ʽ
			// �жϽ���
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

	// ��ȡ�ַ����ķ���
	public static String set_cutString(String str) {
		if (str.length() > 12) {
			return str.substring(0, 12) + "...";

		} else {
			return str;
		}

	}
	// /**
	// * ���÷��ؼ�
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
