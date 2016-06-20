package com.conges.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.conges.data.TrafficInfo;
import com.conges.util.HelpFunctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class TrafficInfoActivity extends Activity {
	Button publishButton;
	ImageButton refreshButton;
	List<TrafficInfo> trafficInfoList;
	List<Map<String, Object>> trafficInfoMapList;
	ListView listView;
	double latitude, longitude = 0.0;

	SharedPreferences preferences;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficinfo);
		init();

		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);

//		latitude = Double.parseDouble(getIntent().getExtras().getString(
//				"latitude"));
//		longitude = Double.parseDouble(getIntent().getExtras().getString(
//				"longitude"));
		latitude = Double.parseDouble(preferences.getString("latitude", ""));
		longitude = Double.parseDouble(preferences.getString("longitude", ""));
		
		if(latitude == 0 || longitude == 0){
			HelpFunctions.useToastShort(getApplicationContext(), "请先定位！");
			finish();
			return;
		}
		
		trafficInfoList = new ArrayList<TrafficInfo>();
		trafficInfoMapList = new ArrayList<Map<String, Object>>();

		getTrafficInfoList();
	}

	public void getTrafficInfoList() {
		new Thread() {
			public void run() {
				trafficInfoList = BusinessFunctions.getTrafficInfo(latitude,
						longitude, getIntent().getExtras().getDouble("rate"));
				handler.sendEmptyMessage(0x126);
			};
		}.start();
		refreshButton
				.setBackgroundResource(R.drawable.icon_refresh_50n_reverse);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x126) {
				trafficInfoMapList = new ArrayList<Map<String, Object>>();
				if (trafficInfoList.size() == 0) {
					HelpFunctions.useToastShort(getApplicationContext(),
							"当前无信息");
				} else {
					for (int i = 0; i < trafficInfoList.size(); i++) {
						Map<String, Object> map = getMapFromTrafficInfo(trafficInfoList
								.get(i));
						trafficInfoMapList.add(map);
					}

					SimpleAdapter sa = new SimpleAdapter(
							getApplicationContext(),
							trafficInfoMapList,
							R.layout.acitivity_fragment_listview_item_layout,
							new String[] { "nodeName", "dateTimeAndPubuser",
									"typeAndLevel", "detail" },
							new int[] {
									R.id.fragment_listView_item_tv_nodeName,
									R.id.fragment_listView_item_tv_datetimeandpubuser,
									R.id.fragment_listView_item_tv_typeandlevel,
									R.id.fragment_listView_item_tv_detail });

					listView.setAdapter(sa);
					refreshButton
							.setBackgroundResource(R.drawable.icon_refresh_50n);
				}
			}
		};
	};

	private void init() {
		listView = (ListView) findViewById(R.id.fragment_listView);

		publishButton = (Button) findViewById(R.id.trafficinfo_bt_publish);
		publishButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (preferences.getInt("loginState", 0) != 1) {
					HelpFunctions.useToastLong(getApplicationContext(),
							"发布路况信息，需要进行登录！");
					return;
				}

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						TrafficMenuActivity.class);
				Bundle data = new Bundle();
				data.putString("latitude", latitude + "");
				data.putString("longitude", longitude + "");
				intent.putExtras(data);
				startActivity(intent);
			}
		});

		refreshButton = (ImageButton) findViewById(R.id.trafficinfo_bt_refresh);
		refreshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getTrafficInfoList();
			}
		});
	}

	private Map<String, Object> getMapFromTrafficInfo(TrafficInfo traInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeName", traInfo.getAddress());
		String pubUser = traInfo.getPubUser();
		if (pubUser.equals("0")) {
			pubUser = "未知";
		} else {
			pubUser = pubUser.substring(pubUser.length() - 4, pubUser.length());
		}
		map.put("dateTimeAndPubuser", traInfo.getDateTime() + " by " + pubUser);
		map.put("typeAndLevel", BusinessFunctions.getTrafficTypeAndLevelStr(
				traInfo.getType(), traInfo.getLevel()));
		map.put("detail", traInfo.getDetail());
		return map;
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshButton.performClick();
	}
}
