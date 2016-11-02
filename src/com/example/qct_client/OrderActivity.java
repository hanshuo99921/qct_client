package com.example.qct_client;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct_client.util.AppConst;
import com.example.qct_client.util.DatabaseOpenHelper;

public class OrderActivity extends ActionBarActivity {

	private Context context;
	private static final String TAG = "OrderActivity";
	private String jjrXzqhString;
	private String sjrXzqhString;
	private String[] xzqhStrings;
	private RequestQueue queue;

	private ImageView addJjrdzImageView;
	private ImageView addSjrdzImageView;
	private ImageView addBzImageView;
	private ImageView jjrImageView;
	private ImageView sjrImageView;
	private ImageView bzImageView;
	private EditText jjrnameEditText;
	private EditText jjrtelEditText;
	private EditText jjraddrEditText;
	private EditText sjrnameEditText;
	private EditText sjrtelEditText;
	private EditText sjraddrEditText;
	private EditText bzEditText;
	private Button button1Button;
	private Spinner jjrSpinner;
	private Spinner sjrSpinner;

	private final static int JJRDZ_REQUEST_CODE = 1000;
	private final static int JJRDZ_RESULT_CODE = 1100;
	private final static int SJRDZ_REQUEST_CODE = 2000;
	private final static int SJRDZ_RESULT_CODE = 2100;
	private final static int BZ_REQUEST_CODE = 3000;
	private final static int BZ_RESULT_CODE = 3100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		context = this;
		xzqhStrings = ((Resources) getBaseContext().getResources()).getStringArray(R.array.xzqh);
		queue = Volley.newRequestQueue(context);

		addJjrdzImageView = (ImageView) findViewById(R.id.addJjr_imageView);
		addSjrdzImageView = (ImageView) findViewById(R.id.addSjr_imageView);
		addBzImageView = (ImageView) findViewById(R.id.addBz_imageView);
		jjrImageView = (ImageView) findViewById(R.id.Jjr_imageView);
		sjrImageView = (ImageView) findViewById(R.id.Sjr_imageView);
		bzImageView = (ImageView) findViewById(R.id.Bz_imageView);
		jjrnameEditText = (EditText) findViewById(R.id.jjrname_EditText);
		jjrtelEditText = (EditText) findViewById(R.id.jjrtel_EditText);
		jjraddrEditText = (EditText) findViewById(R.id.jjraddr_EditText);
		sjrnameEditText = (EditText) findViewById(R.id.sjrname_EditText);
		sjrtelEditText = (EditText) findViewById(R.id.sjrtel_EditText);
		sjraddrEditText = (EditText) findViewById(R.id.sjraddr_EditText);
		bzEditText = (EditText) findViewById(R.id.bz_EditText);
		button1Button = (Button) findViewById(R.id.button1);
		jjrSpinner = (Spinner) findViewById(R.id.jjrDz_spinner);
		sjrSpinner = (Spinner) findViewById(R.id.sjrDz_spinner);

		// jjraddrEditText.setKeyListener(null);
		// sjraddrEditText.setKeyListener(null);

		button1Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (jjrnameEditText.getText().toString().trim().length() == 0) {
					Toast.makeText(context, "请输入寄件人姓名", Toast.LENGTH_SHORT).show();
					jjrnameEditText.requestFocus();
				} else if (jjrtelEditText.getText().toString().trim().length() == 0) {
					Toast.makeText(context, "请输入寄件人联系电话", Toast.LENGTH_SHORT).show();
					jjrtelEditText.requestFocus();
				} else if (jjrSpinner.getSelectedItemPosition() == 0) {
					Toast.makeText(context, "请选择寄件人行政区划", Toast.LENGTH_SHORT).show();
					jjrSpinner.requestFocus();
				} else if (jjraddrEditText.getText().toString().trim().length() == 0) {
					Toast.makeText(context, "请输入寄件人地址", Toast.LENGTH_SHORT).show();
					jjraddrEditText.requestFocus();
				} else if (sjrnameEditText.getText().toString().trim().length() == 0) {
					Toast.makeText(context, "请输入收件人姓名", Toast.LENGTH_SHORT).show();
					sjrnameEditText.requestFocus();
				} else if (sjrtelEditText.getText().toString().trim().length() == 0) {
					Toast.makeText(context, "请输入收件人联系电话", Toast.LENGTH_SHORT).show();
					sjrtelEditText.requestFocus();
				} else if (sjrSpinner.getSelectedItemPosition() == 0) {
					Toast.makeText(context, "请选择收件人行政区划", Toast.LENGTH_SHORT).show();
					sjrSpinner.requestFocus();
				} else if (sjraddrEditText.getText().toString().trim().length() == 0) {
					Toast.makeText(context, "请输入收件人地址", Toast.LENGTH_SHORT).show();
					sjraddrEditText.requestFocus();
				} else {
					jjrImageView.setImageResource(android.R.drawable.star_big_on);
					sjrImageView.setImageResource(android.R.drawable.star_big_on);
					// 将收、寄件人地址保存到本机数据库
					save_to_local();
					// 将订单信息保存到远程服务器
					save_to_remote();
				}
			}

			private void save_to_remote() {
				String url = AppConst.Server_URL + "apk/submit_pdxx.php";
				JSONObject paramJsonObject = new JSONObject();
				try {
					paramJsonObject.put("jjrname", jjrnameEditText.getText().toString().trim());
					paramJsonObject.put("jjrtel", jjrtelEditText.getText().toString().trim());
					paramJsonObject.put("jjraddr", jjraddrEditText.getText().toString().trim());
					paramJsonObject.put("sjrname", sjrnameEditText.getText().toString().trim());
					paramJsonObject.put("sjrtel", sjrtelEditText.getText().toString().trim());
					paramJsonObject.put("sjraddr", sjraddrEditText.getText().toString().trim());
					if (bzEditText.getText().toString().trim().length() > 0) {
						paramJsonObject.put("memo", bzEditText.getText().toString().trim());
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Log.d(TAG, url);
				JsonRequest<JSONObject> jsonObjectRequest = new JsonObjectRequest(Method.POST, url, paramJsonObject,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
									if(Boolean.valueOf(response.getString("success"))){
										finish();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(context, "网络通信出错！" + error.getMessage(), Toast.LENGTH_SHORT).show();
							}

							protected VolleyError parseNetworkError(VolleyError volleyError) {
								if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
									VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
									volleyError = error;
								}

								return volleyError;
							}

						}) {

					@Override
					public Map<String, String> getHeaders() {
						HashMap<String, String> headers = new HashMap<String, String>();
						headers.put("Accept", "application/json");
						headers.put("Content-Type", "application/json; charset=UTF-8");

						return headers;
					}
				};
				queue.add(jsonObjectRequest);
			}

			private void save_to_local() {
				DatabaseOpenHelper dbOpenHelper = new DatabaseOpenHelper(context);
				SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("xm", jjrnameEditText.getText().toString().trim());
				values.put("lxdh", jjrtelEditText.getText().toString().trim());
				values.put("xzqh", xzqhStrings[Integer.valueOf(jjrXzqhString)]);
				values.put("dz", jjraddrEditText.getText().toString().trim());
				values.put("bz", "1");
				db.insert("dzxx", null, values);

				values.put("xm", sjrnameEditText.getText().toString().trim());
				values.put("lxdh", sjrtelEditText.getText().toString().trim());
				values.put("xzqh", xzqhStrings[Integer.valueOf(sjrXzqhString)]);
				values.put("dz", sjraddrEditText.getText().toString().trim());
				values.put("bz", "0");
				db.insert("dzxx", null, values);
				db.close();
			}
		});

		addJjrdzImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, DzxxActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("from", "jjrdz");
				intent.putExtras(bundle);
				startActivityForResult(intent, JJRDZ_REQUEST_CODE);
			}
		});

		addSjrdzImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, DzxxActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("from", "sjrdz");
				intent.putExtras(bundle);
				startActivityForResult(intent, SJRDZ_REQUEST_CODE);
			}
		});

		addBzImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, BzActivity.class);
				startActivityForResult(intent, BZ_REQUEST_CODE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order, menu);
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
		// super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == JJRDZ_REQUEST_CODE && resultCode == DzxxActivity.JJRDZ_RESULT_CODE) {
			Bundle resultBundle = data.getExtras();
			// Log.d(TAG, resultBundle.getString("jjrname"));
			jjrnameEditText.setText(resultBundle.getString("jjrname"));
			jjrtelEditText.setText(resultBundle.getString("jjrtel"));
			jjrXzqhString = resultBundle.getString("xzqh");
			jjraddrEditText.setText(resultBundle.getString("jjraddr"));
			jjrSpinner.setSelection(Integer.valueOf(jjrXzqhString));
			sjrnameEditText.requestFocus();
			jjrImageView.setImageResource(android.R.drawable.star_big_on);
		} else if (requestCode == SJRDZ_REQUEST_CODE && resultCode == DzxxActivity.SJRDZ_RESULT_CODE) {
			Bundle resultBundle = data.getExtras();
			sjrnameEditText.setText(resultBundle.getString("sjrname"));
			sjrtelEditText.setText(resultBundle.getString("sjrtel"));
			sjrXzqhString = resultBundle.getString("xzqh");
			sjrSpinner.setSelection(Integer.valueOf(sjrXzqhString));
			sjraddrEditText.setText(resultBundle.getString("sjraddr"));
			sjrImageView.setImageResource(android.R.drawable.star_big_on);
		} else if (requestCode == BZ_REQUEST_CODE && resultCode == BzActivity.BZ_RESULT_CODE) {
			Bundle resultBundle = data.getExtras();
			bzEditText.setText(resultBundle.getString("bz"));
			bzImageView.setImageResource(android.R.drawable.star_big_on);
		}
	}

}
