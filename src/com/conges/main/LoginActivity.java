package com.conges.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
				//+++ Make connect with Server to authentication
				boolean result_auth = true;
				if(result_auth){
//					Toast.makeText(getApplicationContext(), userName.getText(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this, FriendListActivity.class);
					Bundle data = new Bundle();
					data.putString("username", userName.getText().toString());
					intent.putExtras(data);
					startActivity(intent);
					finish();
				}
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
}
