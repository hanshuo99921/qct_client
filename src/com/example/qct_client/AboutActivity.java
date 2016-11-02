package com.example.qct_client;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.duguang.baseanimation.ui.base.BaseActivity;
import com.duguang.baseanimation.utils.ApkInfoTool;

public class AboutActivity extends BaseActivity implements OnClickListener {

	private TextView textView_VersionName;
	private Button tv_address;

	@Override
	public void setView() {
		setContentView(R.layout.activity_about);

	}

	@Override
	public void initView() {
		textView_VersionName = (TextView) findViewById(R.id.textView_VersionName);
		tv_address = (Button) findViewById(R.id.tv_address);

		textView_VersionName.setText("版本号:" + ApkInfoTool.getVersionName(this));

		tv_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone_number = "4001511185";

				phone_number = phone_number.trim();// 删除字符串首部和尾部的空格

				if (phone_number != null && !phone_number.equals("")) {

					// 调用系统的拨号服务实现电话拨打功能
					// 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_number));
					startActivity(intent);// 内部类
				}
			}

		});

	}

	@Override
	public void setListener() {
		tv_address.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_address:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://blog.csdn.net/duguang77"));
			startActivity(intent);
			break;

		default:
			break;
		}

	}
}
