package com.hwyncho.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
	protected static final String DATABASE_NAME;
	protected static final int DATABASE_VERSION;
	protected static final String TABLE_NAME;
	protected static final String COL_ID;
	protected static final String COL_ACCOUNT;
	protected static final String COL_PASSWORD;
	protected static final String COL_DATE;

	private static DBManager dbManager;

	static {
		DATABASE_NAME = "PASSWORD.DB";
		DATABASE_VERSION = 1;
		TABLE_NAME = "Password";
		COL_ID = "_id";
		COL_ACCOUNT = "account";
		COL_PASSWORD = "password";
		COL_DATE = "date";

		dbManager = null;
	}

	private Context context;

	{
		context = null;
	}

	public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);

		this.context = context;
	}

	public static DBManager getInstance(Context context) {
		if (dbManager == null)
			dbManager = new DBManager(context, DATABASE_NAME, null, DATABASE_VERSION);

		return dbManager;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_ACCOUNT + " TEXT, "
				+ COL_PASSWORD + " TEXT, "
				+ COL_DATE + " TEXT);";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public Cursor query(String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder) {
		return getReadableDatabase().query(TABLE_NAME, projection, selection, selectionArgs, groupBy, having, sortOrder);
	}

	public long insert(ContentValues values) {
		return getWritableDatabase().insert(TABLE_NAME, null, values);
	}

	public int insertAll(ContentValues[] values) {
		SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();
		for (ContentValues value : values) {
			db.insert(TABLE_NAME, null, value);
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		return values.length;
	}

	public int delete(String selection, String[] selectionArgs) {
		return getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
	}

	public int update(ContentValues values, String selection, String[] selectionArgs) {
		return getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
	}
}