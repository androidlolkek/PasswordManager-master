package com.hwyncho.passwordmanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class DBProvider extends ContentProvider {
	public static DBManager dbManager;

	{
		dbManager = null;
	}

	@Override
	public boolean onCreate() {
		dbManager = DBManager.getInstance(getContext());

		return true;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return dbManager.query(projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		dbManager.insert(values);

		return null;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		return dbManager.insertAll(values);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return dbManager.delete(selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return dbManager.update(values, selection, selectionArgs);
	}
}
