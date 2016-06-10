package com.conges.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.conges.data.TrafficInfo;
import com.conges.util.Constant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

@SuppressLint({ "WorldReadableFiles", "HandlerLeak" })
public class TrafficMenuDetailActivity extends Activity {
	TextView tv_tableLine;
	EditText detailEditText;
	RadioButton lowButton, middleButton, highButton;
	TextView lowTv, middleTv, highTv;
	TrafficInfo trafficInfo;

	SharedPreferences preferences;
	String uploadResultStr;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmenudetail);

		trafficInfo = new TrafficInfo();
		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);

		int type = getIntent().getIntExtra("trafficType", -1);
		init(type);
		trafficInfo.setType(type);
		trafficInfo.setLatitude(Double.parseDouble(getIntent().getStringExtra(
				"latitude")));
		trafficInfo.setLongitude(Double.parseDouble(getIntent().getStringExtra(
				"longitude")));
		trafficInfo.setPubUser(preferences.getString("phoneNum", "0"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());

		trafficInfo.setDateTime(sdf.format(new Date()));
	}

	private void init(int type) {
		detailEditText = (EditText) findViewById(R.id.traffictypedetail_et_description);
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
				//加个空检测！
				
				Toast.makeText(TrafficMenuDetailActivity.this, "发布上传",
						Toast.LENGTH_SHORT).show();

				trafficInfo.setDetail(detailEditText.getText().toString());

				new Thread() {
					public void run() {
						uploadResultStr = BusinessFunctions
								.uploadTrafficInfo(trafficInfo);
						handler.sendEmptyMessage(0x125);
					};
				}.start();
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x125) {
				int uploadResult = -1;
				try {
					JSONObject jObject = new JSONObject(uploadResultStr);
					uploadResult = (Integer) jObject.get("uploadResult");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (uploadResult == 0) {
					Toast.makeText(TrafficMenuDetailActivity.this, "上传成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TrafficMenuDetailActivity.this,
							"发布失败，请稍候再试！", Toast.LENGTH_LONG).show();
				}
				finish();
			}
		};
	};

	private void setTypeDetail(int type) {
		tv_tableLine = (TextView) findViewById(R.id.traffictypedetail_tv_tableLine);

		RadioGroup rgGp = (RadioGroup) findViewById(R.id.traffictypedetail_radiogp);
		rgGp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.traffictypedetail_bt_low:
					trafficInfo.setLevel(Constant.TRAFFIC_INFO_LEVEL_LOW);
					break;
				case R.id.traffictypedetail_bt_middle:
					trafficInfo.setLevel(Constant.TRAFFIC_INFO_LEVEL_MIDDLE);
					break;
				case R.id.traffictypedetail_bt_high:
					trafficInfo.setLevel(Constant.TRAFFIC_INFO_LEVEL_HIGH);
					break;
				default:
					trafficInfo.setLevel(Constant.TRAFFIC_INFO_LEVEL_LOW);
					break;
				}
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
			str_tableLine = Constant.TRAFFIC_TYPE_TRAFFIC_STR;
			lowButton.setBackgroundResource(R.drawable.mark_jam_middle);
			middleButton.setBackgroundResource(R.drawable.mark_jam_serious);
			highButton.setBackgroundResource(R.drawable.mark_jam_fixed);
			lowTv.setText(Constant.TRAFFIC_TYPE_TRAFFIC_STR_LOW);
			middleTv.setText(Constant.TRAFFIC_TYPE_TRAFFIC_STR_MIDDLE);
			highTv.setText(Constant.TRAFFIC_TYPE_TRAFFIC_STR_HIGH);
			break;
		case 2:
			str_tableLine = Constant.TRAFFIC_TYPE_ACCIDENT_STR;
			lowButton.setBackgroundResource(R.drawable.mark_accident_low);
			middleButton.setVisibility(View.GONE);
			highButton.setBackgroundResource(R.drawable.mark_accident_high);
			lowTv.setText(Constant.TRAFFIC_TYPE_ACCIDENT_STR_LOW);
			middleTv.setVisibility(View.GONE);
			highTv.setText(Constant.TRAFFIC_TYPE_ACCIDENT_STR_HIGH);
			break;
		case 3:
			str_tableLine = Constant.TRAFFIC_TYPE_WARNING_STR;
			lowButton.setBackgroundResource(R.drawable.mark_warning_road);
			middleButton
					.setBackgroundResource(R.drawable.mark_warning_roadside);
			highButton.setBackgroundResource(R.drawable.mark_warning_weather);
			lowTv.setText(Constant.TRAFFIC_TYPE_WARNING_STR_LOW);
			middleTv.setText(Constant.TRAFFIC_TYPE_WARNING_STR_MIDDLE);
			highTv.setText(Constant.TRAFFIC_TYPE_WARNING_STR_HIGH);
			break;
		case 4:
			str_tableLine = Constant.TRAFFIC_TYPE_CAMERA_STR;
			lowButton.setBackgroundResource(R.drawable.mark_camera_speed);
			middleButton.setVisibility(View.GONE);
			highButton.setBackgroundResource(R.drawable.mark_camera_redlight);
			lowTv.setText(Constant.TRAFFIC_TYPE_CAMERA_STR_LOW);
			middleTv.setVisibility(View.GONE);
			highTv.setText(Constant.TRAFFIC_TYPE_CAMERA_STR_HIGH);
			break;
		case 5:
			str_tableLine = Constant.TRAFFIC_TYPE_MAPHI_STR;
			lowButton.setVisibility(View.GONE);
			middleButton.setVisibility(View.GONE);
			highButton.setVisibility(View.GONE);
			lowTv.setVisibility(View.GONE);
			middleTv.setVisibility(View.GONE);
			highTv.setVisibility(View.GONE);
			break;
		case 6:
			str_tableLine = Constant.TRAFFIC_TYPE_MAPPROBLEM_STR;
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
			Intent intent = new Intent(TrafficMenuDetailActivity.this,
					TrafficMenuActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
}
