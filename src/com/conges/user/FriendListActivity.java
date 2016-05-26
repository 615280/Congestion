package com.conges.user;

import com.conges.main.LocationAndMainActivity;
import com.conges.main.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("WorldReadableFiles")
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
		SharedPreferences preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
		String userName = preferences.getString("userName","信息获取失败");
		
		userNameTextView.setText(userName + preferences.getInt("loginState", -1));
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
