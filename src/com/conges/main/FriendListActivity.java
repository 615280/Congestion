package com.conges.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FriendListActivity extends Activity {

	private TextView userNameTextView;
	private Button okButton;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendlist);
		
		userNameTextView = (TextView) findViewById(R.id.tv_fl_userName);
		userNameTextView.setText(getIntent().getStringExtra("userName"));
		userNameTextView.setTextColor(getResources().getColor(R.color.whitesmoke));
		
		okButton = (Button) findViewById(R.id.button_fl_ok);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =  new Intent();
				intent.setClass(FriendListActivity.this, LocationAndMainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
