package com.conges.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class TrafficMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmenu);
		initButton();
	}

	private void initButton() {
		Button cancelPubButton = (Button) findViewById(R.id.trafficmenu_bt_cancel);
		cancelPubButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ImageButton buttJam = (ImageButton) findViewById(R.id.trafficmenu_bt_traffic);
		ImageButton buttAccident = (ImageButton) findViewById(R.id.trafficmenu_bt_accident);
		ImageButton buttWarning = (ImageButton) findViewById(R.id.trafficmenu_bt_warning);
		ImageButton buttCamera = (ImageButton) findViewById(R.id.trafficmenu_bt_camera);
		ImageButton buttMapHi = (ImageButton) findViewById(R.id.trafficmenu_bt_maphi);
		ImageButton buttMapProblem = (ImageButton) findViewById(R.id.trafficmenu_bt_mapproblem);

		buttJam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTypeDataAndSA(1);
			}
		});

		buttAccident.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTypeDataAndSA(2);
			}
		});

		buttWarning.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTypeDataAndSA(3);
			}
		});

		buttCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTypeDataAndSA(4);
			}
		});

		buttMapHi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTypeDataAndSA(5);
			}
		});

		buttMapProblem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTypeDataAndSA(6);
			}
		});
	}

	private void setTypeDataAndSA(int type) {
		Intent intent = new Intent(TrafficMenuActivity.this, TrafficMenuDetailActivity.class);
		Bundle data = new Bundle();
		data.putInt("trafficType", type);
		data.putString("latitude", getIntent().getExtras().getString("latitude"));
		data.putString("longitude", getIntent().getExtras().getString("longitude"));
		intent.putExtras(data);
		startActivity(intent);
		finish();
	}
}
