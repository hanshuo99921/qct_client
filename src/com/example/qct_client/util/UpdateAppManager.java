package com.example.qct_client.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct_client.R;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateAppManager {
	// 文件分隔符
	private static final String FILE_SEPARATOR = "/";
	// 外存sdcard存放路径
	private static final String FILE_PATH = Environment.getExternalStorageDirectory() + FILE_SEPARATOR + "autoupdate"
			+ FILE_SEPARATOR;
	// 下载应用存放全路径
	private static final String FILE_NAME = FILE_PATH + "qct_client.apk";
	// 更新应用版本标记
	private static final int UPDARE_TOKEN = 0x29;
	// 准备安装新版本应用标记
	private static final int INSTALL_TOKEN = 0x31;

	private Context context;
	private String message = "检测到本程序有新版本发布，建议您更新！";
	private RequestQueue queue;
	private static final String TAG = "UpdateAppManager";

	private String spec = AppConst.Server_URL;
	// 下载应用的对话框
	private Dialog dialog;
	// 下载应用的进度条
	private ProgressBar progressBar;
	// 进度条的当前刻度值
	private int curProgress;
	// 用户是否取消下载
	private boolean isCancel;

	public UpdateAppManager(Context context) {
		this.context = context;
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDARE_TOKEN:
				progressBar.setProgress(curProgress);
				break;

			case INSTALL_TOKEN:
				installApp();
				break;
			}
		}
	};

	/**
	 * 检测应用更新信息
	 */
	public void checkUpdateInfo() {
		queue = Volley.newRequestQueue(context);
		String urlString = AppConst.Server_URL + "apk/check_version.php";
		JSONObject paramJsonObject = new JSONObject();
		try {
			paramJsonObject.put("name", "qct_client");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, urlString, paramJsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							// Toast.makeText(context,
							// response.getString("versionCode"),
							// Toast.LENGTH_LONG).show();

							try {
								int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(),
										0).versionCode;
								// Log.d(TAG, "versionCode:" + versionCode);
								int apkCode = response.getInt("versionCode");
								// Log.d(TAG, "apkCode:" + apkCode);
								if (versionCode < apkCode) {
									message += "\n" + response.getString("updateMessage");
									// Log.d(TAG, "message:" + message);
									spec += response.getString("url");
									// Log.d(TAG, "url:" + spec);
									// showNoticeDialog();
									new AlertDialog.Builder(context).setTitle("软件版本更新").setMessage(message)
											.setPositiveButton("下载", new OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
													showDownloadDialog();
												}
											}).setNegativeButton("以后再说", new OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
												}
											}).create().show();
								}
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							showNoticeDialog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(context, "网络通信出错！" + error.getMessage(), Toast.LENGTH_SHORT).show();
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

	/**
	 * 显示提示更新对话框
	 */
	private void showNoticeDialog() {

	}

	/**
	 * 显示下载进度对话框
	 */
	private void showDownloadDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.progressbar, null);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("软件版本更新");
		builder.setView(view);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				isCancel = true;
			}
		});
		dialog = builder.create();
		dialog.show();
		downloadApp();
	}

	/**
	 * 下载新版本应用
	 */
	private void downloadApp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				URL url = null;
				InputStream in = null;
				FileOutputStream out = null;
				HttpURLConnection conn = null;
				try {
					url = new URL(spec);
					conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					long fileLength = conn.getContentLength();
					in = conn.getInputStream();
					File filePath = new File(FILE_PATH);
					if (!filePath.exists()) {
						filePath.mkdir();
					}
					out = new FileOutputStream(new File(FILE_NAME));
					byte[] buffer = new byte[1024];
					int len = 0;
					long readedLength = 0l;
					while ((len = in.read(buffer)) != -1) {
						// 用户点击“取消”按钮，下载中断
						if (isCancel) {
							break;
						}
						out.write(buffer, 0, len);
						readedLength += len;
						curProgress = (int) (((float) readedLength / fileLength) * 100);
						handler.sendEmptyMessage(UPDARE_TOKEN);
						if (readedLength >= fileLength) {
							dialog.dismiss();
							// 下载完毕，通知安装
							handler.sendEmptyMessage(INSTALL_TOKEN);
							break;
						}
					}
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 安装新版本应用
	 */
	private void installApp() {
		File appFile = new File(FILE_NAME);
		if (!appFile.exists()) {
			return;
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
