package com.hwyncho.passwordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GeneratorActivity extends AppCompatActivity {
	protected static final String URI;

	protected static final String COL_ID;
	protected static final String COL_ACCOUNT;
	protected static final String COL_PASSWORD;
	protected static final String COL_DATE;

	protected static final String UPPER;
	protected static final String LOWER;
	protected static final String NUMBER;
	protected static final String SYMBOL;

	static {
		URI = "content://com.hwyncho.passwordmanager.DBProvider";

		COL_ID = "_id";
		COL_ACCOUNT = "account";
		COL_PASSWORD = "password";
		COL_DATE = "date";

		UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		LOWER = "abcdefghijklmnopqrstuvwxyz";
		NUMBER = "0123456789";
		SYMBOL = "~!@#$%^&*()+";
	}

	private Boolean hasUpper;
	private Boolean hasLower;
	private Boolean hasNumber;
	private Boolean hasSymbol;
	private int length;
	private String password;
	private Random random;

	{
		hasUpper = false;
		hasLower = false;
		hasNumber = true;
		hasSymbol = false;

		length = 10;
		password = "";

		random = new Random();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generator);

		this.myFindView();
	}

	private void myFindView() {
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_item, android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final Spinner spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(arrayAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = spinner.getSelectedItem().toString();
				length = Integer.parseInt(item);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		Button button_generate = (Button) findViewById(R.id.button_generate);
		button_generate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myGenerate();
			}
		});

		Button button_copy = (Button) findViewById(R.id.button_copy);
		button_copy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myCopy();
			}
		});

		Button button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mySave();
			}
		});

		Button button_cancel = (Button) findViewById(R.id.button_cancel);
		button_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myCancel();
			}
		});

		Switch switch_upper = (Switch) findViewById(R.id.switch_upper);
		switch_upper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hasUpper = isChecked;
			}
		});

		Switch switch_lower = (Switch) findViewById(R.id.switch_lower);
		switch_lower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hasLower = isChecked;
			}
		});

		Switch switch_number = (Switch) findViewById(R.id.switch_number);
		switch_number.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hasNumber = isChecked;
			}
		});

		Switch switch_symbol = (Switch) findViewById(R.id.switch_symbol);
		switch_symbol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hasSymbol = isChecked;
			}
		});
	}

	private void myGenerate() {
		String candidate = "";

		if (this.hasUpper)
			candidate += UPPER;

		if (this.hasLower)
			candidate += LOWER;

		if (this.hasNumber)
			candidate += NUMBER;

		if (this.hasSymbol)
			candidate += SYMBOL;

		StringBuilder temp = new StringBuilder();

		for (int i = 0; i < this.length; i++) {
			temp.append(candidate.charAt(this.random.nextInt(candidate.length())));
		}

		this.password = temp.toString();

		((EditText) findViewById(R.id.editText_result)).setText(this.password);
	}

	private void myCopy() {
		ClipData clipData = ClipData.newPlainText("PASSWORD", this.password);
		ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setPrimaryClip(clipData);

		this.myShowMessage(R.string.message_copy);
	}

	private void mySave() {
		final EditText editText = new EditText(this);
		editText.setHint("Google");
		editText.setText("");

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(R.string.dialog_title_save);
		alertDialog.setMessage(R.string.dialog_message);
		alertDialog.setView(editText);
		alertDialog.setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (editText.getText().toString().equals(""))
					Toast.makeText(getApplicationContext(), "입력값이 없습니다", Toast.LENGTH_SHORT).show();
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

					ContentValues values = new ContentValues();
					values.put(COL_ACCOUNT, editText.getText().toString());
					values.put(COL_PASSWORD, password);
					values.put(COL_DATE, simpleDateFormat.format(new Date()).toString());

					getContentResolver().insert(Uri.parse(URI), values);

					myShowMessage(R.string.message_save);

					finish();
				}
			}
		});
		alertDialog.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	private void myCancel() {
		finish();
	}

	private void myShowMessage(int resid) {
		Toast.makeText(this, resid, Toast.LENGTH_LONG).show();
	}

	private void myShowMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}