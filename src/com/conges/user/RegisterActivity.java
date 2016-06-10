package com.conges.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.conges.data.ConnectUtil;
import com.conges.main.R;
import com.conges.util.HelpFunctions;

@SuppressLint("WorldReadableFiles")
public class RegisterActivity extends Activity {
	private EditText phonenumber;
	private EditText userName;
	private EditText userPass;
	private EditText userPassCheck;

	private String phoneNumStr, userNameStr, userPassStr, userPassCheckStr;

	private Button registerButton;
	private Button toLoginButton;
	private String registerResult;
	
	SharedPreferences preferences;
	Editor editor;

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

				String pattern = "(1[358][0-9]\\d{8})|(14[57]\\d{8})|(17[01678]\\d{8})";
				if (phoneNumStr.equals("") || !phoneNumStr.matches(pattern)) {
					// 不符合电话号码模式，提示重新输入
					HelpFunctions.useToastShort(getApplicationContext(), "请确认输入手机号！");
					return;
				}
				
				if (userNameStr.equals("")) {
					HelpFunctions.useToastShort(getApplicationContext(), "请输入用户名！");
					return;
				}
				
				if (userNameStr.length() > 20) {
					HelpFunctions.useToastShort(getApplicationContext(), "请确认用户名，不超过20个字符！");
					return;
				}

				if (userPassStr.equals("")) {
					HelpFunctions.useToastShort(getApplicationContext(), "请输入密码！");
					return;
				}
				
				if (userPassCheckStr.equals("")) {
					HelpFunctions.useToastShort(getApplicationContext(), "请再次输入密码！");
					return;
				}
				
				if (userPassStr.length() < 6 || userPassStr.length() > 12) {
					HelpFunctions.useToastShort(getApplicationContext(), "请确认密码长度为6-12位！");
					return;
				}

				if (!userPassCheckStr.equals(userPassStr)) {
					HelpFunctions.useToastShort(getApplicationContext(),
							"请确认两次密码一致！");
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
										String message = String
												.format("{\"register\":{\"phoneNum\":\"%s\",\"userName\":\"%s\",\"userPwd\":\"%s\"}}",
														phoneNumStr,
														userNameStr,
														userPassStr);
										registerResult = ConnectUtil
												.getConnDef(message);
										handler.sendEmptyMessage(0x123);
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

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				int reg_result = -1;
				try {
					JSONObject j_data = new JSONObject(registerResult);
					if(j_data.get("registerResult") != null){
						reg_result = (Integer) j_data.get("registerResult");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.i("result", "registerResult:" + reg_result);
				if (reg_result == 0) { // 验证成功，返回0
					Intent intent = new Intent(RegisterActivity.this,
							UserInfoActivity.class);
					preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
					editor = preferences.edit();
					editor.putString("phoneNum", phoneNumStr);
					editor.putString("userName", userNameStr);
					editor.putInt("loginState", 1);
					editor.commit();
					
					startActivity(intent);
					finish();
				} else if(reg_result == 2) {
					//已注册，可直接登录
					
				} else { // 程序错误，返回 !0
					HelpFunctions.useToastLong(getApplicationContext(), "注册失败，请稍后重试");
				}
			}
		};
	};
}
