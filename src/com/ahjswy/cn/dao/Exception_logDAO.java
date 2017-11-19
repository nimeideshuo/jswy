package com.ahjswy.cn.dao;

import java.util.ArrayList;
import java.util.List;

import com.ahjswy.cn.bean.bmob.ExceptionLog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.bmob.v3.BmobObject;

public class Exception_logDAO {
	private DBOpenHelper helper = new DBOpenHelper();
	private SQLiteDatabase db;

	/**
	 * 查询所有的 没有上传的 日志
	 * 
	 * @return
	 */
	public List<ExceptionLog> queryAll() {
		db = helper.getReadableDatabase();
		String sql = "select id,userid,username,deviceid,model,message,log,datetime from exception_log where isupdata=1";
		ArrayList<ExceptionLog> listReqLog = new ArrayList<ExceptionLog>();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				ExceptionLog reqLog = new ExceptionLog();
				reqLog.id = cursor.getString(0);
				reqLog.userid = cursor.getString(1);
				reqLog.username = cursor.getString(2);
				reqLog.deviceid = cursor.getString(3);
				reqLog.model = cursor.getString(4);
				reqLog.message = cursor.getString(5);
				reqLog.log = cursor.getString(6);
				reqLog.datetime = cursor.getString(7);
				listReqLog.add(reqLog);
			}
			return listReqLog;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listReqLog;
	}

	public List<BmobObject> queryBmobAll() {
		db = helper.getReadableDatabase();
		String sql = "select id,userid,username,deviceid,model,message,log,datetime from exception_log where isupdata=1";
		ArrayList<BmobObject> listReqLog = new ArrayList<BmobObject>();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				ExceptionLog reqLog = new ExceptionLog();
				reqLog.id = cursor.getString(0);
				reqLog.userid = cursor.getString(1);
				reqLog.username = cursor.getString(2);
				reqLog.deviceid = cursor.getString(3);
				reqLog.model = cursor.getString(4);
				reqLog.message = cursor.getString(5);
				reqLog.log = cursor.getString(6);
				reqLog.datetime = cursor.getString(7);
				listReqLog.add(reqLog);
			}
			return listReqLog;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listReqLog;
	}

	/**
	 * 写入错误日志
	 * 
	 * @return
	 */
	public boolean insertLog(ExceptionLog log) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userid", log.userid);
		values.put("username", log.username);
		values.put("deviceid", log.deviceid);
		values.put("model", log.model);
		values.put("message", log.message);
		values.put("log", log.log);
		values.put("datetime", log.datetime);
		values.put("isupdata", log.isupdata == false ? 0 : 1);
		try {
			long insert = db.insert("exception_log", null, values);
			return insert == -1 ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 删除本地 是否上传的 日志 true 上传后的删除 false 没有上传的删除
	 * 
	 * @param isupdata
	 * @return
	 */
	public boolean deleteIsupdata(boolean isupdata) {
		db = helper.getWritableDatabase();
		String sql = "delete from exception_log where isupdata=" + (isupdata == false ? 0 : 1);
		try {
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteRow(int row) {
		db = helper.getWritableDatabase();
		String sql = "delete from exception_log where id=" + row;
		try {
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
