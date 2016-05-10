package com.conges.main;

import com.conges.util.HelpFunctions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.TextView;

public class TrafficDetailActivity extends Activity {
	private TextView timeTextView;
	private TextView infoTextView;
	private TextView commentTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficdetail);
		
		timeTextView = (TextView) findViewById(R.id.tv_trafdt_time);
		timeTextView.setText(HelpFunctions.getCurrentTime());
		infoTextView = (TextView) findViewById(R.id.tv_trafdt_info);
		infoTextView.setText("道路堵塞");
		commentTextView = (TextView) findViewById(R.id.tv_trafdt_comment);
		commentTextView.setText("评论区xxxx");
		
		Intent intent = getIntent();
		Point point = new Point(intent.getExtras().getInt("pointx"),intent.getExtras().getInt("pointx"));
	}
	
}
