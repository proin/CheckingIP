package com.proinlab.checkingip;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseEdit {

	public boolean INSERT(SQLiteOpenHelper mHelper, String IP, String Group,
			String Count) {

		SQLiteDatabase db;
		ContentValues row;

		db = mHelper.getWritableDatabase();

		row = new ContentValues();
		row.put(DataBaseHelper.ROW_LIST_01_ADDRESS, IP);
		row.put(DataBaseHelper.ROW_LIST_02_GROUP, Group);
		row.put(DataBaseHelper.ROW_LIST_03_COUNT, Count);

		db.insert(DataBaseHelper.GROUP_TABLE, null, row);

		mHelper.close();
		return true;
	}

	public ArrayList<String[]> READ(SQLiteOpenHelper mHelper) {
		SQLiteDatabase db;
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] columns = { "_id", DataBaseHelper.ROW_LIST_01_ADDRESS,
				DataBaseHelper.ROW_LIST_02_GROUP,
				DataBaseHelper.ROW_LIST_03_COUNT };
		db = mHelper.getReadableDatabase();
		Cursor cursor;
		cursor = db.query(DataBaseHelper.GROUP_TABLE, columns, null, null,
				null, null, null);
		if (cursor.getCount() == 0)
			;
		else {
			while (cursor.moveToNext()) {
				String[] result_data = new String[4];
				for (int i = 0; i < 4; i++)
					result_data[i] = cursor.getString(i);
				for (int i = 0; i < 4; i++)
					Log.i("TAG", result_data[i]);
				result.add(result_data);
			}
		}

		cursor.close();
		mHelper.close();
		return result;
	}

	public boolean DELETE(SQLiteOpenHelper mHelper, String id) {

		SQLiteDatabase db;
		db = mHelper.getWritableDatabase();
		db.delete(DataBaseHelper.GROUP_TABLE, "_id" + " = '" + id + "'", null);
		mHelper.close();
		return true;
	}

	public boolean CHANGE(SQLiteOpenHelper mHelper, int _id, String group,
			String ip, String count) {

		SQLiteDatabase db;
		ContentValues row;
		db = mHelper.getWritableDatabase();

		row = new ContentValues();
		row.put(DataBaseHelper.ROW_LIST_01_ADDRESS, ip);
		row.put(DataBaseHelper.ROW_LIST_02_GROUP, group);
		row.put(DataBaseHelper.ROW_LIST_03_COUNT, count);

		db.update(DataBaseHelper.GROUP_TABLE, row, "_id" + " = '" + _id + "'",
				null);

		mHelper.close();
		return true;
	}

}
