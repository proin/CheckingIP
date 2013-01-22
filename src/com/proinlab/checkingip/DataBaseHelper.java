package com.proinlab.checkingip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_GROUP = "TABLE_GROUP";
	public static final String ROW_GROUP_01_NAME = "GROUP";

	public static final String TABLE_LIST = "TABLE_LIST";

	public static final String ROW_LIST_01_ADDRESS = "ADDRESS";
	public static final String ROW_LIST_02_GROUP = "GROUP";

	public DataBaseHelper(Context context) {
		super(context, "DATABASE", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_GROUP
				+ " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ROW_GROUP_01_NAME + " TEXT);");

		db.execSQL("CREATE TABLE " + TABLE_LIST
				+ " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ROW_LIST_01_ADDRESS + " TEXT, " + ROW_LIST_02_GROUP
				+ " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
		onCreate(db);
	}

}
