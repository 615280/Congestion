package com.conges.main;

import com.conges.util.HelpFunctions;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	private EditText phonenumber;
	private Button registerButton;
	private Button toLoginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		phonenumber = (EditText) findViewById(R.id.et_register_phonenumber);
		registerButton = (Button) findViewById(R.id.button_register);
		toLoginButton = (Button) findViewById(R.id.button_register_tologin);

		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder checkAlert = new Builder(	RegisterActivity.this);
				checkAlert.setTitle("请确认手机号").setIcon(R.drawable.ic_launcher)
						.setMessage(phonenumber.getText().toString());
				checkAlert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						HelpFunctions.useToast(getApplicationContext(), "确定");
					}
				});
				checkAlert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}	});
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

}
