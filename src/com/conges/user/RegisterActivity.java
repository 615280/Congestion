package com.conges.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.conges.database.ConnectUtil;
import com.conges.main.R;
import com.conges.main.R.drawable;
import com.conges.main.R.id;
import com.conges.main.R.layout;
import com.conges.util.HelpFunctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

				Pattern p = Pattern
						.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,1,6,7,8]))/d{8}$");
				Matcher m = p.matcher(phoneNumStr);
				if (phoneNumStr.equals("") || m.matches()) {
					// �����ϵ绰����ģʽ����ʾ��������
					HelpFunctions.useToastShort(getApplicationContext(), "��ȷ�������ֻ��ţ�");
					return;
				}
				
				if (userNameStr.equals("")) {
					HelpFunctions.useToastShort(getApplicationContext(), "��ȷ���û�����");
					return;
				}

				if (userPassStr.equals("") || userPassCheckStr.equals("")) {
					HelpFunctions.useToastShort(getApplicationContext(), "��ȷ�����룡");
					return;
				}

				if (!userPassCheckStr.equals(userPassStr)) {
					HelpFunctions.useToastShort(getApplicationContext(),
							"��ȷ����������һ�£�");
					return;
				}

				Builder checkAlert = new Builder(RegisterActivity.this);
				checkAlert.setTitle("��ȷ���ֻ���").setIcon(R.drawable.ic_launcher)
						.setMessage(phoneNumStr);
				checkAlert.setPositiveButton("ȷ��",
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

				checkAlert.setNegativeButton("ȡ��",
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
				if (reg_result == 0) { // ��֤�ɹ�������0
					Intent intent = new Intent(RegisterActivity.this,
							UserInfoActivity.class);
					Bundle data = new Bundle();
					data.putString("phoneNumber", phoneNumStr);
					data.putString("userName", userNameStr);
					intent.putExtras(data);
					startActivity(intent);
					finish();
				} else if(reg_result == 2) {
					//��ע�ᣬ��ֱ�ӵ�¼
					
				} else { // ������󣬷��� !0
					HelpFunctions.useToastLong(getApplicationContext(), "ע��ʧ�ܣ����Ժ�����");
				}
			}
		};
	};
}
