package com.hwyncho.passwordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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

	public DBManager dbManager;
	private ListView listView;
	private DBCursorAdapter cursorAdapter;

	{
		dbManager = null;

		listView = null;
		cursorAdapter = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle(R.string.activity_main);

		dbManager = DBManager.getInstance(this);

		this.myFindView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (this.cursorAdapter != null) {
			String[] projection = new String[]{COL_ID, COL_ACCOUNT, COL_PASSWORD, COL_DATE};
			Cursor cursor = getContentResolver().query(URI, projection, null, null, null);

			this.cursorAdapter.changeCursor(cursor);
		}
	}

	@Override
	protected void onDestroy() {
		this.cursorAdapter.changeCursor(null);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Snackbar.make(findViewById(R.id.activity_main), "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void myFindView() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
				myStartActivity();
			}
		});

		String[] projection = new String[]{COL_ID, COL_ACCOUNT, COL_PASSWORD, COL_DATE};
		Cursor cursor = getContentResolver().query(URI, projection, null, null, null);

		this.cursorAdapter = new DBCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		this.listView = (ListView) findViewById(R.id.listview_account);
		this.listView.setEmptyView(findViewById(R.id.listview_empty));
		this.listView.setAdapter(this.cursorAdapter);
		this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String password = ((TextView) view.findViewById(R.id.textView_password)).getText().toString();
				myCopy(password);
			}
		});
		this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				String[] selectionArgs = new String[1];
				selectionArgs[0] = ((TextView) view.findViewById(R.id.textView_account)).getText().toString();
				myDelete(null, selectionArgs);

				return true;
			}
		});
	}

	private void myStartActivity() {
		startActivity(new Intent(getApplicationContext(), GeneratorActivity.class));
	}

	private void myCopy(String password) {
		ClipData clipData = ClipData.newPlainText("PASSWORD", password);
		ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setPrimaryClip(clipData);

		this.myShowMessage(R.string.message_copy);
	}

	private void myDelete(String selection, String[] selectionArgs) {
		final String s = selection;
		final String[] sa = selectionArgs;

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(R.string.dialog_title_delete);
		alertDialog.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cursorAdapter.delete("account=?", sa);

				myShowMessage(R.string.message_delete);

				String[] projection = new String[]{COL_ID, COL_ACCOUNT, COL_PASSWORD, COL_DATE};
				Cursor cursor = getContentResolver().query(URI, projection, null, null, null);

				cursorAdapter.changeCursor(cursor);
			}
		});
		alertDialog.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	private void myShowMessage(int resid) {
		View view = findViewById(R.id.activity_main);
		Snackbar.make(view, resid, Snackbar.LENGTH_LONG).show();
	}

	private void myShowMessage(String message) {
		View view = findViewById(R.id.activity_main);
		Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
	}
}