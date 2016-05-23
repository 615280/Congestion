package com.conges.main;

import com.conges.database.Connector;
import com.conges.util.Constant;

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
	private EditText userName;
	private EditText userPass;
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
	
	private void init(){
		userName = (EditText)findViewById(R.id.et_login_username);
		userPass = (EditText)findViewById(R.id.et_login_userpass);
		loginButton = (Button)findViewById(R.id.button_login);
		toRegisterButton = (Button) findViewById(R.id.button_login_toregister);
		
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(){
					public void run() {
						result = Connector.sendPost(Constant.URL, 
								"{'login':{'phoneNum':'13718528992','userPwd':'123456'}}");
						handler.sendEmptyMessage(0x123);
					};
				}.start();
				
				
				Log.i("result", result);
				
//				if(Integer.parseInt(result) == 0){
////					Toast.makeText(getApplicationContext(), userName.getText(), Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(LoginActivity.this, FriendListActivity.class);
//					Bundle data = new Bundle();
//					data.putString("username", userName.getText().toString());
//					intent.putExtras(data);
//					startActivity(intent);
//					finish();
//				} else {
//					Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//				}
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
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x123){
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			}
		}
	};
}
