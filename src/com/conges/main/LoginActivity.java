package com.conges.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.conges.database.ConnectUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText et_userName;
	private EditText et_userPass;
	private Button loginButton;
	private Button toRegisterButton;
	String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();

	}

	private void init() {
		et_userName = (EditText) findViewById(R.id.et_login_username);
		et_userPass = (EditText) findViewById(R.id.et_login_userpass);
		loginButton = (Button) findViewById(R.id.button_login);
		toRegisterButton = (Button) findViewById(R.id.button_login_toregister);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final String userName = et_userName.getText().toString();
				final String userPass = et_userPass.getText().toString();
				if(userName.equals("") || userPass.equals("")){
					Toast.makeText(getApplicationContext(), "请输入正确的手机号和密码！", Toast.LENGTH_LONG).show();
					return;
				}
				
				new Thread() {
					public void run() {
						String message = String.format("{\"login\":{\"phoneNum\":\"%s\",\"userPwd\":\"%s\"}}", userName,userPass);
						result = ConnectUtil.getConnDef(message);
						handler.sendEmptyMessage(0x123);
					};
				}.start();
			}
		});
		toRegisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				int auth_result = -1;
				String userName = "";
				try {
					JSONArray jArray = new JSONArray(result);
					JSONObject j_data = jArray.getJSONObject(0);
					auth_result = (Integer) j_data.get("loginResult");
					if(j_data.getString("userName") != null){
						userName = (String) j_data.getString("userName");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.i("result", "loginResult:" + auth_result);
				if (auth_result == 0) {		//验证成功，返回0
					Intent intent = new Intent(LoginActivity.this,
							FriendListActivity.class);
					Bundle data = new Bundle();
					data.putString("userName", userName);
					intent.putExtras(data);
					startActivity(intent);
					finish();
				} else if(auth_result == 1){		//输入错误，返回1
					Toast.makeText(getApplicationContext(), "登录失败，\n请检查用户名密码是否正确",
							Toast.LENGTH_LONG).show();
				} else {		//程序错误，返回-1
					Toast.makeText(getApplicationContext(), "登录失败，请稍后重试",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	};
}
