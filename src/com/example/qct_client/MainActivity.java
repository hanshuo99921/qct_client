package com.example.qct_client;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct_client.util.AppConst;
import com.example.qct_client.util.DatabaseOpenHelper;
import com.example.qct_client.util.UpdateAppManager;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	private boolean areButtonsShowing;
	
	private UpdateAppManager updateManager;

	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsShowHideButton;
	private ImageView aboutImageView;
	private ImageView orderImageView;
	private ImageView queryImageView;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;

		MyAnimations.initOffset(MainActivity.this);
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);

		aboutImageView = (ImageView) findViewById(R.id.composer_button_about);
		orderImageView = (ImageView) findViewById(R.id.composer_button_order);
		queryImageView = (ImageView) findViewById(R.id.composer_button_query);

		updateManager = new UpdateAppManager(context);  
        updateManager.checkUpdateInfo(); 
		
		

		DatabaseOpenHelper dbOpenHelper = new DatabaseOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areButtonsShowing) {
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(0, -270, 300));
				} else {
					MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-270, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			composerButtonsWrapper.getChildAt(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
				}
			});
		}

		composerButtonsShowHideButton.startAnimation(MyAnimations.getRotateAnimation(0, 360, 200));

		aboutImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, AboutActivity.class);
				startActivity(intent);
			}
		});

		orderImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, OrderActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_translate_top, R.anim.out_translate_top);
			}
		});
		
		queryImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, QueryActivity.class);
				startActivity(intent);
			}
		});
	}

}
