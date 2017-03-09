package com.hwyncho.passwordmanager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DBCursorAdapter extends CursorAdapter {
	protected static final Uri URI;

	protected static final String COL_ID;
	protected static final String COL_ACCOUNT;
	protected static final String COL_PASSWORD;
	protected static final String COL_DATE;

	static {
		URI = Uri.parse("content://com.hwyncho.passwordmanager.DBProvider");

		COL_ID = "_id";
		COL_ACCOUNT = "account";
		COL_PASSWORD = "password";
		COL_DATE = "date";
	}

	private Context context;
	private LayoutInflater layoutInflater;

	{
		context = null;
		layoutInflater = null;
	}

	public DBCursorAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);

		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = this.layoutInflater.inflate(R.layout.content_main_item, null);

		ViewHolder viewHolder = new ViewHolder();
		viewHolder._id = "";
		viewHolder.textView_account = (TextView) view.findViewById(R.id.textView_account);
		viewHolder.textView_password = (TextView) view.findViewById(R.id.textView_password);
		viewHolder.textView_date = (TextView) view.findViewById(R.id.textView_date);

		view.setTag(viewHolder);

		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String _id = cursor.getString(cursor.getColumnIndex(COL_ID));
		String account = cursor.getString(cursor.getColumnIndex(COL_ACCOUNT));
		String password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));
		String date = cursor.getString(cursor.getColumnIndex(COL_DATE));

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder._id = _id;
		viewHolder.textView_account.setText(account);
		viewHolder.textView_password.setText(password);
		viewHolder.textView_date.setText(view.getResources().getString(R.string.textview_date) + " " + date);
	}

	@Override
	protected void onContentChanged() {
		super.onContentChanged();
	}

	public int delete(String selection, String[] selectionArgs) {
		return context.getContentResolver().delete(URI, selection, selectionArgs);
	}

	private class ViewHolder {
		public String _id;
		public TextView textView_account;
		public TextView textView_password;
		public TextView textView_date;

		{
			textView_account = null;
			textView_password = null;
			textView_date = null;
		}
	}
}