package com.example.qct_client;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class BzActivity extends ActionBarActivity {

	private Context context;
	private static final String TAG = "OrderActivity";
	private String bzString;
	public final static int BZ_RESULT_CODE = 3100;

	private ImageView cancelImageView;
	private Button button1Button;
	private Button button2Button;
	private Button button3Button;
	private Button button4Button;
	private Button button5Button;
	private EditText bzEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bz);
		context = this;
		bzString = "";

		cancelImageView = (ImageView) findViewById(R.id.imageView1);
		button1Button = (Button) findViewById(R.id.button1);
		button2Button = (Button) findViewById(R.id.button2);
		button3Button = (Button) findViewById(R.id.button3);
		button4Button = (Button) findViewById(R.id.button4);
		button5Button = (Button) findViewById(R.id.button5);
		bzEditText = (EditText) findViewById(R.id.editText1);

		bzEditText.addTextChangedListener(new TextWatcher() {

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
				button1Button.setEnabled(true);
				Resources resource = (Resources) getBaseContext().getResources();
				ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.blue);
				button1Button.setTextColor(csl);
			}
		});

		button1Button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bzString = bzEditText.getText().toString();
				Bundle mBundle = new Bundle();
				mBundle.putString("bz", bzString);
				Intent intent = new Intent();
				intent.putExtras(mBundle);
				setResult(BZ_RESULT_CODE, intent);
				finish();
			}
		});

		button2Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bzString = bzEditText.getText().toString().trim();
				if (bzString.equals("")) {
					bzEditText.setText(button2Button.getText());
				} else {
					bzEditText.setText(bzString + "," + button2Button.getText());
				}
				button2Button.setBackgroundResource(R.color.dodgerblue);
			}
		});

		button3Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bzString = bzEditText.getText().toString().trim();
				if (bzString.equals("")) {
					bzEditText.setText(button3Button.getText());
				} else {
					bzEditText.setText(bzString + "," + button3Button.getText());
				}
				button3Button.setBackgroundResource(R.color.dodgerblue);
			}
		});

		button4Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bzString = bzEditText.getText().toString().trim();
				if (bzString.equals("")) {
					bzEditText.setText(button4Button.getText());
				} else {
					bzEditText.setText(bzString + "," + button4Button.getText());
				}
				button4Button.setBackgroundResource(R.color.dodgerblue);
			}
		});

		button5Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bzString = bzEditText.getText().toString().trim();
				if (bzString.equals("")) {
					bzEditText.setText(button5Button.getText());
				} else {
					bzEditText.setText(bzString + "," + button5Button.getText());
				}
				button5Button.setBackgroundResource(R.color.dodgerblue);
			}
		});

		cancelImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bz, menu);
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
}
