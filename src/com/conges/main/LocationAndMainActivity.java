package com.conges.main;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class LocationAndMainActivity extends Activity {
	MapView mMapView = null;
	BaiduMap mBaiduMap;
	BaiduMapOptions mBaiduMapOptions;
	
	LatLng currentPt = null;		//地理坐标经纬度
	String touchType;

	OnCheckedChangeListener radioButtonListener;
	Button publishSitButton;
	Button settingButton;
	Button locationButton;
	Button contactButton;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	volatile boolean isFirstLoc = true; // 是否首次定位

	// 改变定位点样式
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private static final int accuracyCircleFillColor = 0xCC00CCCC; // 包围圈背景色
	private static final int accuracyCircleStrokeColor = 0xAA00FF00; // 边缘线

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		// 地图初始化
		mBaiduMapOptions = new BaiduMapOptions();
		mBaiduMapOptions.zoomControlsEnabled(false);
		// mBaiduMapOptions.scaleControlPosition();
		mMapView = new MapView(this, mBaiduMapOptions);
		setContentView(mMapView);

		// 按钮初始化
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View view = LayoutInflater.from(this).inflate(R.layout.activity_main,
				null);
		this.addContentView(view, params);
		initButton();

		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationEnabled(true);	// 开启定位图层
		initLocPosition();		// 初始化定位
		initChangeIconGeo();		//改变定位点样式
		
//		System.out.println(currentPt.latitude);
//		System.out.println(currentPt.longitude);
//		initListener();		//初始化监听	
	}

	private void initListener() {
		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {
			
			@Override
			public void onTouch(MotionEvent event) {
				
			}
		});
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				touchType = "单击";
				currentPt = point;
				updateMapState();
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				touchType = "长按";
				currentPt = point;
				updateMapState();
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {
				touchType = "双击";
				currentPt = point;
				updateMapState();
			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			public void onMapStatusChangeStart(MapStatus status) {
				updateMapState();
			}

			public void onMapStatusChangeFinish(MapStatus status) {
				updateMapState();
			}

			public void onMapStatusChange(MapStatus status) {
				updateMapState();
			}
		});
	}
	
	private void updateMapState() {
		// TODO Auto-generated method stub
		String state = "";
		if (currentPt == null) {
			state = "点击、长按、双击地图以获取经纬度和地图状态";
		} else {
			state = String.format(touchType + ",当前经度： %f 当前纬度：%f",
					currentPt.longitude, currentPt.latitude);
		}
		System.out.println(state);
	}
	
	private void initLocPosition() {
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		locationButton.setVisibility(View.INVISIBLE);
	}

	private void initChangeIconGeo() {
		mCurrentMode = LocationMode.NORMAL;
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo_blue);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker, accuracyCircleFillColor,
				accuracyCircleStrokeColor));
	}

	private void initButton() {
		// 打开或关闭路况显示功能
		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.closeColor) {
					// 关闭
					Toast.makeText(LocationAndMainActivity.this, "closeColor",
							Toast.LENGTH_SHORT).show();
				}
				if (checkedId == R.id.openColor) {
					// 打开
					Toast.makeText(LocationAndMainActivity.this, "openColor",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		group.setOnCheckedChangeListener(radioButtonListener);

		publishSitButton = (Button) findViewById(R.id.button_main_publish);
		settingButton = (Button) findViewById(R.id.button_main_setting);
		locationButton = (Button) findViewById(R.id.button_main_location);
		contactButton = (Button) findViewById(R.id.button_main_contact);

		publishSitButton.setText("路况");
		settingButton.setText("设置");
		locationButton.setText("定位");
		contactButton.setText("好友");

		publishSitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LocationAndMainActivity.this, "路况",
						Toast.LENGTH_SHORT).show();
			}
		});

		settingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LocationAndMainActivity.this, "设置",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LocationAndMainActivity.this, PreferenceMainActivity.class);
				startActivity(intent);
			}
		});

		locationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LocationAndMainActivity.this, "定位",
						Toast.LENGTH_SHORT).show();
				isFirstLoc = true;
				initLocPosition();
			}
		});

		contactButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LocationAndMainActivity.this, "好友",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				currentPt = ll;
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
}
