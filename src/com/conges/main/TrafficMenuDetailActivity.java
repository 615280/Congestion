package com.conges.main;

import com.conges.util.HelpFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TrafficMenuDetailActivity extends Activity {
	TextView tv_tableLine;
	EditText tableLineText;
	RadioButton lowButton, middleButton, highButton;
	TextView lowTv, middleTv, highTv;

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
				// make connection with Server
				Toast.makeText(TrafficMenuDetailActivity.this, "发布上传",
						Toast.LENGTH_SHORT).show();
				boolean result = true;
				if (result) {
					Toast.makeText(TrafficMenuDetailActivity.this, "上传成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TrafficMenuDetailActivity.this,
							"发布失败，请稍候再试！", Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
	}

	private void setTypeDetail(int type) {
		tv_tableLine = (TextView) findViewById(R.id.traffictypedetail_tv_tableLine);

		RadioGroup rgGp = (RadioGroup) findViewById(R.id.traffictypedetail_radiogp);
		rgGp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				HelpFunctions.useToastShort(getApplicationContext(), checkedId+"");
			}
		});
		
		lowButton = (RadioButton) findViewById(R.id.traffictypedetail_bt_low);
		middleButton = (RadioButton) findViewById(R.id.traffictypedetail_bt_middle);
		highButton = (RadioButton) findViewById(R.id.traffictypedetail_bt_high);
		
		lowTv = (TextView) findViewById(R.id.traffictypedetail_tv_low);
		middleTv = (TextView) findViewById(R.id.traffictypedetail_tv_middle);
		highTv = (TextView) findViewById(R.id.traffictypedetail_tv_high);

		String str_tableLine = null;
		switch (type) {
		case 1:
			str_tableLine = "交通堵塞";
			lowButton.setBackgroundResource(R.drawable.mark_jam_middle);
			middleButton.setBackgroundResource(R.drawable.mark_jam_serious);
			highButton.setBackgroundResource(R.drawable.mark_jam_fixed);
			lowTv.setText("中等");
			middleTv.setText("严重");
			highTv.setText("不动");
			break;
		case 2:
			str_tableLine = "事故";
			lowButton.setBackgroundResource(R.drawable.mark_accident_low);
			middleButton.setVisibility(View.GONE);
			highButton.setBackgroundResource(R.drawable.mark_accident_high);
			lowTv.setText("轻微");
			middleTv.setVisibility(View.GONE);
			highTv.setText("严重");
			break;
		case 3:
			str_tableLine = "危险";
			lowButton.setBackgroundResource(R.drawable.mark_warning_road);
			middleButton
					.setBackgroundResource(R.drawable.mark_warning_roadside);
			highButton.setBackgroundResource(R.drawable.mark_warning_weather);
			lowTv.setText("路上");
			middleTv.setText("路边");
			highTv.setText("天气");
			break;
		case 4:
			str_tableLine = "摄像头";
			lowButton.setBackgroundResource(R.drawable.mark_camera_speed);
			middleButton.setVisibility(View.GONE);
			highButton.setBackgroundResource(R.drawable.mark_camera_redlight);
			lowTv.setText("速度");
			middleTv.setVisibility(View.GONE);
			highTv.setText("红灯");
			break;
		case 5:
			str_tableLine = "地图聊天";
			lowButton.setVisibility(View.GONE);
			middleButton.setVisibility(View.GONE);
			highButton.setVisibility(View.GONE);
			lowTv.setVisibility(View.GONE);
			middleTv.setVisibility(View.GONE);
			highTv.setVisibility(View.GONE);
			break;
		case 6:
			str_tableLine = "地图问题";
			lowButton.setVisibility(View.GONE);
			middleButton.setVisibility(View.GONE);
			highButton.setVisibility(View.GONE);
			lowTv.setVisibility(View.GONE);
			middleTv.setVisibility(View.GONE);
			highTv.setVisibility(View.GONE);
			break;
		default:
			str_tableLine = "程序出现问题，请重试";
			finish();
			break;
		}

		tv_tableLine.setText(str_tableLine);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(TrafficMenuDetailActivity.this, TrafficMenuActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
}
