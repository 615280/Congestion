package com.conges.user;

import com.conges.main.LocationAndMainActivity;
import com.conges.main.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("WorldReadableFiles")
public class UserInfoActivity extends Activity {
	private TextView phoneNumber,userName;
	private ImageView userIcon; 
	private Button returnButton;
	private Button logoutButton;
	
	SharedPreferences preferences;
	Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		init();
		
	}

	@SuppressWarnings("deprecation")
	private void init(){
		userIcon = (ImageView) findViewById(R.id.userinfo_iv);
		phoneNumber = (TextView) findViewById(R.id.userinfo_tv_phonenumber);
		userName = (TextView) findViewById(R.id.userinfo_tv_username);
		returnButton = (Button) findViewById(R.id.userinfo_bt_returnmain);
		logoutButton = (Button) findViewById(R.id.userinfo_bt_logout);
		
		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
		phoneNumber.setText("手机号：" + preferences.getString("phoneNum", "信息获取失败"));
		userName.setText("用户名：" + preferences.getString("userName", "信息获取失败"));
		userIcon.setImageResource(R.drawable.icon_usericon);
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LocationAndMainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		logoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
				editor = preferences.edit();
				editor.putInt("loginState", 0);
				editor.commit();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LocationAndMainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
