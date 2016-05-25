package com.conges.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends Activity {
	private TextView phoneNumber,userName;
	private ImageView userIcon; 
	private Button returnButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		init();
		
	}

	private void init(){
		userIcon = (ImageView) findViewById(R.id.userinfo_iv);
		phoneNumber = (TextView) findViewById(R.id.userinfo_tv_phonenumber);
		userName = (TextView) findViewById(R.id.userinfo_tv_username);
		returnButton = (Button) findViewById(R.id.userinfo_bt_returnmain);
		
		phoneNumber.setText("手机号：" + getIntent().getStringExtra("phoneNumber"));
		userName.setText("用户名：" + getIntent().getStringExtra("userName"));
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
	}
}
