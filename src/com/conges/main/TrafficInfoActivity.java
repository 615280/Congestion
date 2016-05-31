package com.conges.main;

import java.util.ArrayList;
import java.util.List;
import com.conges.data.TrafficInfo;
import com.conges.util.HelpFunctions;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("HandlerLeak")
public class TrafficInfoActivity extends Activity {
	Button publishButton;
	List<TrafficInfo> trafficInfoList;
	double latitude,longitude = 0.0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficinfo);
		init();
		
		latitude = Double.parseDouble(getIntent().getExtras().getString("latitude"));
		longitude = Double.parseDouble(getIntent().getExtras().getString("longitude"));
		trafficInfoList = new ArrayList<TrafficInfo>();
		new Thread(){
			public void run() {
				trafficInfoList = BusinessFunctions.getTrafficInfo(latitude,longitude, getIntent().getExtras().getDouble("rate"));
				handler.sendEmptyMessage(0x126);
			};
		}.start();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0x126){
				System.out.println("hello");
				if(trafficInfoList.size() == 0){
					HelpFunctions.useToastShort(getApplicationContext(), "当前无信息");
				} else {
					
				}
			}
		};
	};
	
	private void init(){
		publishButton = (Button) findViewById(R.id.trafficinfo_bt_publish);
		publishButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), TrafficMenuActivity.class);
				Bundle data = new Bundle();
				data.putString("latitude", latitude+"");
				data.putString("longitude", longitude+"");
				intent.putExtras(data);
				startActivity(intent);
			}
		});
		
	}
}
