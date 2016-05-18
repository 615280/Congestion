package com.conges.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrafficMenuDetailActivity extends Activity {
	TextView tv_tableLine;
	EditText tableLineText;
	ImageButton lowButton,middleButton,highButton;
	TextView lowTv,middleTv,highTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmenudetail);
		int type = getIntent().getIntExtra("trafficType", -1);
		init(type);
	}

	private void init(int type) {
		tableLineText = (EditText) findViewById(R.id.traffictypedetail_et_description);
		
		setTypeDetail(type);
		
		Button waitPubButton = (Button) findViewById(R.id.traffictypedetail_bt_waitpublish);
		waitPubButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button pubButton = (Button) findViewById(R.id.traffictypedetail_bt_topublish);
		pubButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//make connection with Server
				Toast.makeText(TrafficMenuDetailActivity.this, "发布上传", Toast.LENGTH_SHORT).show();
				boolean result = true;
				if(result){
					Toast.makeText(TrafficMenuDetailActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TrafficMenuDetailActivity.this, "发布失败，请稍候再试！", Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
	}

	private void setTypeDetail(int type) {
		tv_tableLine = (TextView) findViewById(R.id.traffictypedetail_tv_tableLine);
		
		LinearLayout btLinear = (LinearLayout) findViewById(R.id.traffictypedetail_linearlayout_imabts);
		lowButton = (ImageButton) findViewById(R.id.traffictypedetail_bt_low);
		middleButton = (ImageButton) findViewById(R.id.traffictypedetail_bt_middle);
		highButton = (ImageButton) findViewById(R.id.traffictypedetail_bt_high);
		
		LinearLayout tvLinear = (LinearLayout) findViewById(R.id.traffictypedetail_linearlayout_tvs);
		lowTv = (TextView) findViewById(R.id.traffictypedetail_tv_low);
		middleTv = (TextView) findViewById(R.id.traffictypedetail_tv_middle);
		highTv = (TextView) findViewById(R.id.traffictypedetail_tv_high);
		
		String str_tableLine = null;
		float weightSum = 0.0f;
		switch (type) {
		case 1:
			str_tableLine = "交通堵塞";
			weightSum = 3.0f;
			btLinear.setWeightSum(weightSum);
			lowButton.setBackgroundResource(R.drawable.mark_jam_middle);
			lowButton.setScaleType(ScaleType.CENTER_INSIDE);
			middleButton.setBackgroundResource(R.drawable.mark_jam_serious);
			highButton.setBackgroundResource(R.drawable.mark_jam_fixed);
			lowTv.setText("中等");
			middleTv.setText("严重");
			highTv.setText("不动");
			break;
		case 2:
			str_tableLine = "事故";
			weightSum = 2.0f;
			btLinear.setWeightSum(weightSum);
			lowButton.setBackgroundResource(R.drawable.mark_accident_low);
			highButton.setBackgroundResource(R.drawable.mark_accident_high);
			lowTv.setText("轻微");
			highTv.setText("严重");
			break;
		case 3:
			str_tableLine = "危险";
			weightSum = 3.0f;
			btLinear.setWeightSum(weightSum);
			lowButton.setBackgroundResource(R.drawable.mark_warning_road);
			middleButton.setBackgroundResource(R.drawable.mark_warning_roadside);
			highButton.setBackgroundResource(R.drawable.mark_warning_weather);
			lowTv.setText("路上");
			middleTv.setText("路边");
			highTv.setText("天气");
			break;
		case 4:
			str_tableLine = "摄像头";
			weightSum = 2.0f;
			btLinear.setWeightSum(weightSum);
			lowButton.setBackgroundResource(R.drawable.mark_camera_speed);
			highButton.setBackgroundResource(R.drawable.mark_camera_redlight);
			lowTv.setText("速度");
			highTv.setText("红灯");
			break;
		case 5:
			str_tableLine = "地图聊天";
			weightSum = 0.0f;
			btLinear.setWeightSum(weightSum);
			break;
		case 6:
			str_tableLine = "地图问题";
			weightSum = 0.0f;
			btLinear.setWeightSum(weightSum);
			break;
		default:
			str_tableLine = "程序出现问题";
			weightSum = 0.0f;
			btLinear.setWeightSum(weightSum);
			break;
		}
		
		tv_tableLine.setText(str_tableLine);
	}
}
