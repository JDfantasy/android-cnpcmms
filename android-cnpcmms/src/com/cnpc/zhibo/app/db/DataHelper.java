package com.cnpc.zhibo.app.db;

import java.util.ArrayList;

import java.util.List;

import com.easemob.easeui.domain.EaseUser;

import android.content.ContentValues;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * ���ݸ�����
 * 
 * @author xicunyou
 *
 */
public class DataHelper {

	// ���ݿ�����

	private static String DB_NAME = "mycache.db";

	// ���ݿ�汾

	private static int DB_VERSION = 2;

	private SQLiteDatabase db;

	private SqliteHelper dbHelper;

	public DataHelper(Context context) {

		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);

		db = dbHelper.getWritableDatabase();

	}

	public void Close() {

		db.close();

		dbHelper.close();

	}
	// ��ȡusers���е�UserID��Access Token��Access Secret�ļ�¼

	public List<EaseUser> GetUserList() {

		List<EaseUser> userList = new ArrayList<EaseUser>();

		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null,

				null, "username DESC");

		cursor.moveToFirst();

		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {

			EaseUser user = new EaseUser(cursor.getString(0));// username
			user.setNick(cursor.getString(1));// nick
			user.setAvatar(cursor.getString(2));// avater
			userList.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return userList;
	}
	/**
	 * ��ȡָ�����û���Ϣ
	 * @param username
	 * @return
	 */
	public EaseUser getUserInfo(String username){
		EaseUser user = null;
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, "username" + "=?", new String[] { username }, null, null,null);
		if(cursor.moveToFirst()){
			user = new EaseUser(cursor.getString(0));// username
			user.setNick(cursor.getString(1));// nick
			user.setAvatar(cursor.getString(2));// avater
		}
		cursor.close();
		return user;
	}
	
	// �ж�users���е��Ƿ����ĳ��UserID�ļ�¼
	public Boolean HaveUserInfo(String UserId) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, "username" + "=?", new String[] { UserId }, null, null,
				null);
		b = cursor.moveToFirst();
		cursor.close();
		return b;
	}

	// ����users��ļ�¼������UserId�����û��ǳƺ��û�ͼ��
	public int UpdateUserInfo(String nick, String avatar, String username) {

		ContentValues values = new ContentValues();
		values.put("nick", nick);
		values.put("avatar", avatar);
		int id = db.update(SqliteHelper.TB_NAME, values, " username =?", new String[] { username });
		Log.e("UpdateUserInfo2", id + "");
		return id;
	}

	// ����users��ļ�¼
	public int UpdateUserInfo(EaseUser user) {

		ContentValues values = new ContentValues();

		values.put("username", user.getUsername());

		values.put("nick", user.getNick());

		values.put("avatar", user.getAvatar());

		int id = db.update(SqliteHelper.TB_NAME, values, " username ="

				+ user.getUsername(), null);

		Log.e("UpdateUserInfo", id + "");

		return id;

	}

	// ���users��ļ�¼

	public Long SaveUserInfo(EaseUser user) {

		ContentValues values = new ContentValues();
		values.put("username", user.getUsername());
		values.put("nick", user.getNick());
		values.put("avatar", user.getAvatar());
		Long uid = db.insert(SqliteHelper.TB_NAME, "username", values);
		Log.e("SaveUserInfo", uid + "");
		return uid;
	}

	// ɾ��users��ļ�¼

	public int DelUserInfo(String username) {

		int id = db.delete(SqliteHelper.TB_NAME,

				"username =?", new String[] { username });

		Log.e("DelUserInfo", id + "");

		return id;

	}

}
