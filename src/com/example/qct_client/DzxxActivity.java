package com.example.qct_client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.qct_client.util.DatabaseOpenHelper;

public class DzxxActivity extends Activity {

	private Context context;
	private static final String TAG = "DzxxActivity";
	private final static int CONTACTS_REQUEST_CODE = 1010;
	public final static int CONTACTS_RESULT_CODE = 1011;
	public final static int JJRDZ_RESULT_CODE = 1100;
	public final static int SJRDZ_RESULT_CODE = 2100;
	private String[] xzqhStrings;
	private Bundle bundle;
	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	private String[] fromStrings;
	private int[] toInts;
	List<Map<String, String>> list;

	private ImageView contactsImageView;
	private EditText jjrnameEditText;
	private EditText jjrLxdhEditText;
	private EditText jjrDzEditText;
	private Spinner xzqhSpinner;
	private TextView saveTextView;
	private TextView cancelTextView;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dzxx);
		context = this;
		bundle = this.getIntent().getExtras();
		list = new ArrayList<Map<String, String>>();
		xzqhStrings = ((Resources) getBaseContext().getResources()).getStringArray(R.array.xzqh);

		jjrnameEditText = (EditText) findViewById(R.id.jjrname_EditText);
		jjrLxdhEditText = (EditText) findViewById(R.id.jjrLxdh_EditText);
		xzqhSpinner = (Spinner) findViewById(R.id.jjrDz_spinner);
		jjrDzEditText = (EditText) findViewById(R.id.jjrDz_EditText);
		saveTextView = (TextView) findViewById(R.id.textView3);
		cancelTextView = (TextView) findViewById(R.id.textView2);
		listView = (ListView) findViewById(R.id.listView);

		// if (xmString.length() > 0) {
		// fromStrings = new String[] { xmString, lxdhString, xzqhString,
		// dzString };
		fromStrings = new String[] { "xm", "lxdh", "xzqh", "dz" };
		toInts = new int[] { R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4 };
		SimpleAdapter listAdapter = new SimpleAdapter(context, getData(), R.layout.dzxx_item, fromStrings, toInts);
		// listView.setAdapter(listAdapter);
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				// Log.d(TAG,
				// "arg2:"+arg2+";content:"+list.get(arg2).toString());

				jjrnameEditText.setText(list.get(arg2).get("xm"));
				jjrLxdhEditText.setText(list.get(arg2).get("lxdh"));
				int position = 0;
				for (;; position++) {
					String string = list.get(arg2).get("xzqh");
					if (xzqhStrings[position].equals(string)) {
						break;
					}
				}
				xzqhSpinner.setSelection(position);
				jjrDzEditText.setText(list.get(arg2).get("dz"));
			}
		});
		// }

		contactsImageView = (ImageView) findViewById(R.id.imageView1);
		contactsImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ContactsActivity.class);
				startActivityForResult(intent, CONTACTS_REQUEST_CODE);
			}
		});

		xzqhSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Spinner1: position=" + arg2);
				if (arg2 > 0) {
					int i = jjrDzEditText.getText().toString().trim().length();
					if (i > 0) {
						Resources resource = (Resources) getBaseContext().getResources();
						ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.red);
						saveTextView.setTextColor(csl);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		jjrDzEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int i = xzqhSpinner.getSelectedItemPosition();
				Log.d(TAG, i + "");
				if (i > 0) {
					Resources resource = (Resources) getBaseContext().getResources();
					ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.red);
					saveTextView.setTextColor(csl);
				}
			}
		});

		cancelTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		saveTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle mBundle = new Bundle();
				String fromString = bundle.getString("from");
				if (fromString.equals("jjrdz")) {
					mBundle.putString("jjrname", jjrnameEditText.getText().toString());
					mBundle.putString("jjrtel", jjrLxdhEditText.getText().toString());
					String jjraddr = "";

					// Log.d(TAG, xzqhSpinner.getSelectedItemPosition()+"");
					mBundle.putString("xzqh", xzqhSpinner.getSelectedItemPosition() + "");
					mBundle.putString("jjraddr", jjrDzEditText.getText().toString());

					Intent intent = new Intent();
					intent.putExtras(mBundle);
					setResult(JJRDZ_RESULT_CODE, intent);
				} else {
					mBundle.putString("sjrname", jjrnameEditText.getText().toString());
					mBundle.putString("sjrtel", jjrLxdhEditText.getText().toString());
					String jjraddr = "";

					// Log.d(TAG,
					// xzqhStrings[xzqhSpinner.getSelectedItemPosition()]);
					mBundle.putString("xzqh", xzqhSpinner.getSelectedItemPosition() + "");
					mBundle.putString("sjraddr", jjrDzEditText.getText().toString());

					Intent intent = new Intent();
					intent.putExtras(mBundle);
					setResult(SJRDZ_RESULT_CODE, intent);
				}
				finish();
			}
		});
	}

	private List<Map<String, String>> getData() {
		dbOpenHelper = new DatabaseOpenHelper(context);
		db = dbOpenHelper.getReadableDatabase();
		// cursor = db.rawQuery("select * from yjxx where tm = '" + tm + "'",
		// null);
		cursor = db.query("dzxx", null, null, null, null, null, "id desc");

		while (cursor.moveToNext()) {
			String xmString = cursor.getString(cursor.getColumnIndex("xm"));
			String lxdhString = cursor.getString(cursor.getColumnIndex("lxdh"));
			String xzqhString = cursor.getString(cursor.getColumnIndex("xzqh"));
			String dzString = cursor.getString(cursor.getColumnIndex("dz"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("xm", xmString);
			map.put("lxdh", lxdhString);
			map.put("xzqh", xzqhString);
			map.put("dz", dzString);
			list.add(map);
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dzxx, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CONTACTS_REQUEST_CODE && resultCode == ContactsActivity.CONTACTS_RESULT_CODE) {
			Bundle resultBundle = data.getExtras();
			Log.d(TAG, resultBundle.getString("name"));
			jjrnameEditText.setText(resultBundle.getString("name"));
			jjrLxdhEditText.setText(resultBundle.getString("tel"));
			jjrDzEditText.requestFocus();
			jjrDzEditText.setInputType(InputType.TYPE_CLASS_TEXT);
		}
	}
}
