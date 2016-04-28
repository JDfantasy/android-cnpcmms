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
 * 数据辅助类
 * 
 * @author xicunyou
 *
 */
public class DataHelper {

	// 数据库名称

	private static String DB_NAME = "mycache.db";

	// 数据库版本

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
	// 获取users表中的UserID、Access Token、Access Secret的记录

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
	 * 获取指定的用户信息
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
	
	// 判断users表中的是否包含某个UserID的记录
	public Boolean HaveUserInfo(String UserId) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, "username" + "=?", new String[] { UserId }, null, null,
				null);
		b = cursor.moveToFirst();
		cursor.close();
		return b;
	}

	// 更新users表的记录，根据UserId更新用户昵称和用户图标
	public int UpdateUserInfo(String nick, String avatar, String username) {

		ContentValues values = new ContentValues();
		values.put("nick", nick);
		values.put("avatar", avatar);
		int id = db.update(SqliteHelper.TB_NAME, values, " username =?", new String[] { username });
		Log.e("UpdateUserInfo2", id + "");
		return id;
	}

	// 更新users表的记录
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

	// 添加users表的记录

	public Long SaveUserInfo(EaseUser user) {

		ContentValues values = new ContentValues();
		values.put("username", user.getUsername());
		values.put("nick", user.getNick());
		values.put("avatar", user.getAvatar());
		Long uid = db.insert(SqliteHelper.TB_NAME, "username", values);
		Log.e("SaveUserInfo", uid + "");
		return uid;
	}

	// 删除users表的记录

	public int DelUserInfo(String username) {

		int id = db.delete(SqliteHelper.TB_NAME,

				"username =?", new String[] { username });

		Log.e("DelUserInfo", id + "");

		return id;

	}

}
