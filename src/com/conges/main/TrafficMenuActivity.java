package com.conges.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.conges.util.Constant;

public class TrafficMenuActivity extends Activity implements
		OnGetGeoCoderResultListener {

	GeoCoder mSearch = null;
	String geoAddress = "";
	String latitude = "";
	String longitude = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmenu);
		initButton();
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		new Thread() {
			public void run() {
				latitude = getIntent().getExtras().getString("latitude");
				longitude = getIntent().getExtras().getString("longitude");
				LatLng ptCenter = new LatLng(Float.valueOf(latitude),
						Float.valueOf(longitude));
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
			};
		}.start();
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
				setTypeDataAndSA(Constant.TRAFFIC_TYPE_TRAFFIC);
			}
		});

		buttAccident.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTypeDataAndSA(Constant.TRAFFIC_TYPE_ACCIDENT);
			}
		});

		buttWarning.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTypeDataAndSA(Constant.TRAFFIC_TYPE_WARNING);
			}
		});

		buttCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTypeDataAndSA(Constant.TRAFFIC_TYPE_CAMERA);
			}
		});

		buttMapHi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTypeDataAndSA(Constant.TRAFFIC_TYPE_MAPHI);
			}
		});

		buttMapProblem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTypeDataAndSA(Constant.TRAFFIC_TYPE_MAPPROBLEM);
			}
		});
	}

	private void setTypeDataAndSA(int type) {
		Bundle data = new Bundle();
		while (geoAddress.equals("")) { // 自旋等待
		}

		if (geoAddress.equals("-1")) {
			return;
		}

		Intent intent = new Intent(TrafficMenuActivity.this,
				TrafficMenuDetailActivity.class);
		data.putInt("trafficType", type);
		data.putString("latitude", latitude);
		data.putString("longitude", longitude);
		data.putString("address", geoAddress);
		intent.putExtras(data);
		startActivity(intent);
		finish();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		return;
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(TrafficMenuActivity.this, "抱歉，当前位置获取失败，请移动后重试！",
					Toast.LENGTH_LONG).show();
			geoAddress = "-1";
			return;
		}
//		geoAddress = result.getAddress();
		geoAddress = result.getAddressDetail().district;
		geoAddress += result.getAddressDetail().street;
		geoAddress += result.getAddressDetail().streetNumber;
	}
}
