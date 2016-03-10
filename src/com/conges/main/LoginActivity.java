package com.conges.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	EditText userName;
	EditText userPass;
	Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
	}
	
	private void init(){
		userName = (EditText)findViewById(R.id.et_login_username);
		userPass = (EditText)findViewById(R.id.et_login_userpass);
		loginButton = (Button)findViewById(R.id.button_login);
	}
}
