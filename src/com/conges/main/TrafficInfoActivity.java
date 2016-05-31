package com.conges.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TrafficInfoActivity extends Activity {
	Button publishButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficinfo);
		init();
	}
	
	private void init(){
		publishButton = (Button) findViewById(R.id.trafficinfo_bt_publish);
		publishButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), TrafficMenuActivity.class);
				startActivity(intent);
			}
		});
		
	}
}
