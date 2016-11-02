package com.example.qct_client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct_client.util.AppConst;
import com.example.qct_client.util.StringUtils;
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class QueryActivity extends ActionBarActivity {

	private Context context;
	private static final String TAG = "QueryActivity";
	private RequestQueue queue;
	private String[] fromStrings;
	private int[] toInts;
	private final static int REQUEST_CODE = 100;
	private Bundle bundle;

	private ImageButton queryImageButton;
	private ImageButton scanImageButton;
	private EditText tmEditText;
	private ListView listView;
	private TextView textView;
	// 查询应用的对话框
	private Dialog dialog;
	// 查询应用的进度条
	private ProgressBar progressBar;
	// 进度条的当前刻度值
	private int curProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		context = this;
		queue = Volley.newRequestQueue(context);
		fromStrings = new String[] { "rq", "desc" };
		toInts = new int[] { R.id.textView1, R.id.textView2 };

		queryImageButton = (ImageButton) findViewById(R.id.imageButton2);
		scanImageButton = (ImageButton) findViewById(R.id.imageButton1);
		tmEditText = (EditText) findViewById(R.id.editText1);
		listView = (ListView) findViewById(R.id.listView1);
		textView = (TextView) findViewById(R.id.textView2);
		
		scanImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(context, CaptureActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		queryImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = AppConst.Server_URL + "apk/yjcx.php";
				JSONObject paramJsonObject = new JSONObject();
				try {
					paramJsonObject.put("tm", StringUtils.replaceBlank(tmEditText.getText().toString()));
					tmEditText.setText(StringUtils.replaceBlank(tmEditText.getText().toString()));
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
									// Toast.makeText(context, "total:" +
									// response.getString("total"),
									// Toast.LENGTH_LONG).show();
									if (response.getInt("total") > 0) {
										dialog.dismiss();
										textView.setVisibility(View.VISIBLE);
										JSONArray rows = response.getJSONArray("rows");
										ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
										for (int i = 0; i < rows.length(); i++) {
											// Log.d(TAG,
											// rows.getJSONObject(i).getString("tm"));
											Map<String, String> map = new HashMap<String, String>();
											map.put("rq", rows.getJSONObject(i).getString("rq") + "  "
													+ rows.getJSONObject(i).getString("sj"));
											map.put("desc", rows.getJSONObject(i).getString("msg"));
											data.add(map);
										}

										SimpleAdapter listAdapter = new SimpleAdapter(context, data,
												R.layout.query_item, fromStrings, toInts);
										// listView.setAdapter(listAdapter);
										listView.setAdapter(listAdapter);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(context, "网络通信出错！" + error.getMessage(), Toast.LENGTH_SHORT).show();
								dialog.dismiss();
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

					@Override
					protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

						try {
							JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
							return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
						} catch (UnsupportedEncodingException e) {
							return Response.error(new ParseError(e));
						} catch (Exception je) {
							return Response.error(new ParseError(je));
						}
					}
				};
				queue.add(jsonObjectRequest);
				showQueryDialog();
			}
		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
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

	/**
	 * 显示查询进度对话框
	 */
	private void showQueryDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.queryprogressbar, null);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("正在查询，请稍候......");
		builder.setView(view);

		dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(arg0, arg1, arg2);
		if (requestCode == REQUEST_CODE && resultCode == CaptureActivity.RESULT_CODE) {
			bundle = data.getExtras();
			tmEditText.setText( bundle.getString("barcode"));
		}
	}
	
	
}
