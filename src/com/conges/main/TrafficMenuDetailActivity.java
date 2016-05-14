package com.conges.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class TrafficMenuDetailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmenudetail);
		init();
	}
	
	private void init(){
		TextView tableLineTv = (TextView)findViewById(R.id.traffictypedetail_tv_tableLine);
//		tableLineTv.setText(R.string.);
		int type = getIntent().getIntExtra("trafficType", -1);
		EditText tableLineText = (EditText) findViewById(R.id.traffictypedetail_et_description);
		tableLineText.setText(Integer.toString(type));
	}
}
