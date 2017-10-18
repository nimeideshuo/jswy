package com.ahjswy.cn.dao;

import com.ahjswy.cn.model.DocContainerEntity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Sv_docitem {
	private DBOpenHelper helper = new DBOpenHelper();
	private SQLiteDatabase db;

	public DocContainerEntity queryDoc() {
		this.db = this.helper.getWritableDatabase();
		String sql = "select id,deleteinitem,deleteitem,doc,item,doctype,paytype from sv_docitem";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			DocContainerEntity docEntity = new DocContainerEntity();
			docEntity.setDeleteinitem(cursor.getString(1));
			docEntity.setDeleteitem(cursor.getString(2));
			docEntity.setDoc(cursor.getString(3));
			docEntity.setItem(cursor.getString(4));
			docEntity.setDoctype(cursor.getString(5));
			docEntity.setPaytype(cursor.getString(6));
			return docEntity;
		}
		return null;
	}

	public void insetDocItem(DocContainerEntity docEntity) {
		this.db = this.helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("deleteinitem", docEntity.getDeleteinitem());
		values.put("deleteitem", docEntity.getDeleteitem());
		values.put("doc", docEntity.getDoc());
		values.put("item", docEntity.getItem());
		values.put("doctype", docEntity.getDoctype());
		values.put("paytype", docEntity.getPaytype());
		values.put("paytype", docEntity.getItem());
		db.insert("sv_docitem", null, values);
	}

	public void updataDocItem(DocContainerEntity docEntity) {
		this.db = this.helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("deleteinitem", docEntity.getDeleteinitem());
		values.put("deleteitem", docEntity.getDeleteitem());
		values.put("doc", docEntity.getDoc());
		values.put("item", docEntity.getItem());
		values.put("doctype", docEntity.getDoctype());
		values.put("paytype", docEntity.getPaytype());
		db.update("sv_docitem", values, null, null);
	}

	public void deleteAllDoc() {
		this.db = this.helper.getWritableDatabase();
		db.delete("sv_docitem", null, null);
	}
}
