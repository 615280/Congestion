package com.conges.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.conges.database.ConnectUtil;
import com.conges.util.HelpFunctions;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PatternMatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText phonenumber;
	private EditText userName;
	private EditText userPass;
	private EditText userPassCheck;
	
	private String phoneNumStr, userNameStr, userPassStr, userPassCheckStr;
	
	private Button registerButton;
	private Button toLoginButton;
	private String registerResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		phonenumber = (EditText) findViewById(R.id.et_register_phonenumber);
		userName = (EditText) findViewById(R.id.et_register_username);
		userPass = (EditText) findViewById(R.id.et_register_userpass);
		userPassCheck = (EditText) findViewById(R.id.et_register_userpasscheck);
		
		registerButton = (Button) findViewById(R.id.button_register);
		toLoginButton = (Button) findViewById(R.id.button_register_tologin);

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumStr = phonenumber.getText().toString();
				userNameStr = userName.getText().toString();
				userPassStr = userPass.getText().toString();
				userPassCheckStr = userPassCheck.getText().toString();
				
				if(userNameStr.equals("")){
					HelpFunctions.useToast(getApplicationContext(), "请确认用户名！");
					return;
				}
				
				if(userPassStr.equals("") || userPassCheckStr.equals("")){
					HelpFunctions.useToast(getApplicationContext(), "请确认密码！");
					return;
				}
				
				if(userPassCheckStr.equals(userPassStr)){
					HelpFunctions.useToast(getApplicationContext(), "请确认两次密码一致！");
					return;
				}
				
				Pattern p = Pattern
						.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,1,6,7,8]))\\d{8}$");
				Matcher m = p.matcher(phoneNumStr);
				if (phoneNumStr.equals("") || m.matches()) {
					// 不符合电话号码模式，提示重新输入
					Toast.makeText(RegisterActivity.this, "请确认输入手机号！",
							Toast.LENGTH_LONG).show();
					return;
				}

				Builder checkAlert = new Builder(RegisterActivity.this);
				checkAlert.setTitle("请确认手机号").setIcon(R.drawable.ic_launcher)
						.setMessage(phoneNumStr);
				checkAlert.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new Thread() {
									public void run() {
										String message = String.format(
												"{\"register\":{\"phoneNum\":\"%s\",\"userName\":\"%s\",\"userPwd\":\"%s\"}}",
												phoneNumStr, userNameStr, userPassStr);
										registerResult = ConnectUtil.getConnDef(message);
									};
								}.start();
							}
						});

				checkAlert.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				checkAlert.show();
			}
		});

		toLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	Handler handler = new Handler(){
		
	};
}
