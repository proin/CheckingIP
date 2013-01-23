package com.proinlab.checkingip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String GROUP_TABLE = "GROUP_TABLE";

	public static final String ROW_LIST_01_ADDRESS = "ADDRESS_VALUE";
	public static final String ROW_LIST_02_GROUP = "GROUP_VALUE";
	public static final String ROW_LIST_03_COUNT = "COUNT_VALUE";

	public DataBaseHelper(Context context) {
		super(context, "database.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + GROUP_TABLE
				+ " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ROW_LIST_01_ADDRESS + " TEXT, " + ROW_LIST_02_GROUP
				+ " TEXT, " + ROW_LIST_03_COUNT + " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
		onCreate(db);
	}

}
