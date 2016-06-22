package com.conges.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.conges.data.LineStep;
import com.conges.user.FriendListActivity;
import com.conges.user.LoginActivity;
import com.conges.util.HelpFunctions;

@SuppressLint({ "WorldReadableFiles", "HandlerLeak" })
public class LocationAndMainActivity extends Activity implements
		OnGetRoutePlanResultListener, OnGetPoiSearchResultListener,
		OnGetSuggestionResultListener {

	private static final double RANGE = 0.5;
	private static int Count = 0;
	private static int RefreshTime = 10;
	private static Timer timer;

	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private BaiduMapOptions mBaiduMapOptions;

	private LatLng currentPt = null; // �������꾭γ��
	private String touchType;
	private TextView mStateBar;

	private TextView loadingText;
	private OnCheckedChangeListener radioButtonListener;
	private Button publishSitButton;
	private Button settingButton;
	private Button locationButton;
	private Button contactButton;
	private RadioGroup radioGroup;

	// ��λ���
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	volatile boolean isFirstLoc = true; // �Ƿ��״ζ�λ

	// �ı䶨λ����ʽ
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	private static final int accuracyCircleFillColor = 0x2200CCCC; // ��ΧȦ����ɫ
	private static final int accuracyCircleStrokeColor = 0xAA00FF00; // ��Ե��

	private List<LineStep> roadStateList = new ArrayList<LineStep>();
	@SuppressWarnings("rawtypes")
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	RoutePlanSearch mSearch = null;
	private PoiSearch mPoiSearch = null;

	ExecutorService exec = Executors.newCachedThreadPool();
	final Semaphore semaphore = new Semaphore(1);
	int degree = -1;
	List<TransitRouteOverlay> roadStateOverlayList = new ArrayList<TransitRouteOverlay>();

	SharedPreferences preferences;
	String[] nodeName = { "������ͼ���", "������", "�����㳡��", "�пƴ�", "�пƴ���" };
	AutoCompleteTextView searchText;
	// private int load_Index = 0;
	ImageButton searchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		init();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, nodeName);
		searchText.setAdapter(adapter);

		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);

		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);

		getRoadStateList();

		// timer = new Timer();
		// timer.schedule(new TimerTask() {
		// @Override
		// public void run() {
		// handler.sendEmptyMessage(0x150);
		// }
		// }, 30000, RefreshTime * 60 * 100);
	}

	private void getRoadStateList() {
		loadingText.setVisibility(View.VISIBLE); // ��ʾ����������
		radioGroup.setVisibility(View.GONE); // ����ʾ�򿪹رհ�ť

		new Thread() {
			@Override
			public void run() {
				while (currentPt == null) {
				}
				roadStateList = BusinessFunctions.getRoadStateResult(currentPt,
						RANGE);
				// handler.sendEmptyMessage(0x123);
				for (int i = 0; i < roadStateList.size(); i++) {
					Message message = Message.obtain();

					Bundle b = new Bundle();
					// b.putParcelable("linestep",
					// (Parcelable) roadStateList.get(0));

					b.putParcelable("linestep",
							(Parcelable) roadStateList.get(i));

					message.setData(b);
					handler.sendMessage(message);
				}
			}
		}.start();
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
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;

		mSearch.destroy();
		mPoiSearch.destroy();

		timer.cancel();
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
		RefreshTime = preferences.getInt("refreshtime", 10);

		// ��ͼ��ʼ��
		mBaiduMapOptions = new BaiduMapOptions();
		mBaiduMapOptions.zoomControlsEnabled(false);
		// mBaiduMapOptions.scaleControlPosition();
		mMapView = new MapView(this, mBaiduMapOptions);
		setContentView(mMapView);

		// ��ť��ʼ��
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View view = LayoutInflater.from(this).inflate(R.layout.activity_main,
				null);
		this.addContentView(view, params);
		initButton();

		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationEnabled(true); // ������λͼ��
		initLocPosition(); // ��ʼ����λ
		initChangeIconGeo(); // �ı䶨λ����ʽ

		initListener(); // ��ʼ������
	}

	private void initListener() {
		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {
			@Override
			public void onTouch(MotionEvent event) {

			}
		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				touchType = "����";
				currentPt = point;
				updateMapState();
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				touchType = "����";
				currentPt = point;
				updateMapState();
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {
				touchType = "˫��";
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
		if (mStateBar == null) {
			return;
		}
		String state = "";
		if (currentPt == null) {
			state = "�����������˫����ͼ�Ի�ȡ��γ�Ⱥ͵�ͼ״̬";
		} else {
			state = String.format(touchType + ",��ǰ���ȣ� %f ��ǰγ�ȣ�%f",
					currentPt.longitude, currentPt.latitude);
		}
		state += "\n";
		MapStatus ms = mBaiduMap.getMapStatus();
		state += String.format("zoom=%.1f rotate=%d overlook=%d", ms.zoom,
				(int) ms.rotate, (int) ms.overlook);
		mStateBar.setText(state); // ����ʾ���û���ֻ��debugʱʹ��
		mStateBar.setVisibility(View.GONE);

		// changeLocationButtonVisible(); //��ʾ��λ��ť
	}

	private void initLocPosition() {
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
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

		searchText = (AutoCompleteTextView) findViewById(R.id.main_autotv);
		searchButton = (ImageButton) findViewById(R.id.main_bt_search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// HelpFunctions.useToastShort(getApplicationContext(), "��������Ϊ��"
				// + searchText.getText().toString());
				mPoiSearch.searchInCity((new PoiCitySearchOption()).city("����")
						.keyword(searchText.getText().toString())
						.pageCapacity(1));
				// .pageNum(load_Index));
			}
		});

		loadingText = (TextView) findViewById(R.id.tv_main_loading);
		// �򿪻�ر�·����ʾ����
		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.closeColor) {
					// �ر�
					clearOverlay();
					// new Thread() {
					// public void run() {
					// while (semaphore.getQueueLength() > 0) {
					// semaphore.release();
					// }
					// };
					// }.start();
				}

				if (checkedId == R.id.openColor) {
					// ��
					// Toast.makeText(LocationAndMainActivity.this, "openColor",
					// Toast.LENGTH_SHORT).show();

					for (int i = 0; i < roadStateOverlayList.size(); i++) {
						roadStateOverlayList.get(i).addToMap();
					}

					// new Thread() {
					// @Override
					// public void run() {
					// roadStateList = BusinessFunctions
					// .getRoadStateResult(currentPt, RANGE);
					// // handler.sendEmptyMessage(0x123);
					// for (int i = 0; i < roadStateList.size(); i++) {
					// Message message = Message.obtain();
					//
					// Bundle b = new Bundle();
					// // b.putParcelable("linestep",
					// // (Parcelable) roadStateList.get(0));
					//
					// b.putParcelable("linestep",
					// (Parcelable) roadStateList.get(i));
					//
					// message.setData(b);
					// handler.sendMessage(message);
					// }
					// }
					// }.start();
				}
			}
		};

		radioGroup.setOnCheckedChangeListener(radioButtonListener);

		publishSitButton = (Button) findViewById(R.id.button_main_publish);
		settingButton = (Button) findViewById(R.id.button_main_setting);
		locationButton = (Button) findViewById(R.id.button_main_location);
		contactButton = (Button) findViewById(R.id.button_main_contact);

		publishSitButton.setBackgroundResource(R.drawable.button_message60);
		settingButton.setBackgroundResource(R.drawable.button_setting60);
		locationButton.setText("��λ");
		contactButton.setBackgroundResource(R.drawable.button_friend60);

		publishSitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (currentPt != null) {
					Intent intent = new Intent(LocationAndMainActivity.this,
							TrafficInfoActivity.class);
					Bundle b = new Bundle();
					// b.putString("latitude", currentPt.latitude + "");
					// b.putString("longitude", currentPt.longitude + "");
					b.putDouble("rate", 10.0); // ���û�ȡ·���ľ�γ�Ȳ�ֵ 10��Ӧ-10 +10
					intent.putExtras(b);
					startActivity(intent);
				} else {
					HelpFunctions.useToastLong(getApplicationContext(),
							"���Ȼ�ȡ��λ��");
					locationButton.setVisibility(View.VISIBLE);
				}
			}
		});

		settingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationAndMainActivity.this,
						PreferenceMainActivity.class);
				startActivity(intent);
			}
		});

		locationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LocationAndMainActivity.this, "��λ�С���",
						Toast.LENGTH_SHORT).show();
				isFirstLoc = true;
				initLocPosition();
			}
		});

		contactButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings({ "deprecation" })
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int loginState = preferences.getInt("loginState", -1);
				Intent intent = new Intent();
				switch (loginState) {
				case 0:
					intent.setClass(getApplicationContext(),
							LoginActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent.setClass(getApplicationContext(),
							FriendListActivity.class);
					startActivity(intent);
					break;
				default:
					intent.setClass(getApplicationContext(),
							LoginActivity.class);
					startActivity(intent);
					preferences = getSharedPreferences("conges",
							MODE_WORLD_READABLE);
					Editor editor = preferences.edit();
					editor.putInt("loginState", 0);
					editor.commit();
					break;
				}
			}
		});

		mStateBar = (TextView) findViewById(R.id.state);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x150) { // ·������ȫ�����
				Log.i("test", "test");
				getRoadStateList();
			} else {
				final LineStep lineStep = (LineStep) msg.getData()
						.getParcelable("linestep");

				Runnable run = new Runnable() {
					@Override
					public void run() {
						lineStep.getStartNode();
						lineStep.getEndNode();
						// searchButtonProcess();
						route = null;
						PlanNode stNode = PlanNode.withCityNameAndPlaceName(
								"����", lineStep.getStartNode());
						PlanNode enNode = PlanNode.withCityNameAndPlaceName(
								"����", lineStep.getEndNode());

						try {
							semaphore.acquire();
							degree = lineStep.getDegree();
							mSearch.transitSearch((new TransitRoutePlanOption())
									.from(stNode).city("����").to(enNode));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};

				exec.execute(run);
			}
		}
	};

	@SuppressWarnings("unused")
	private void changeLocationButtonVisible() {
		locationButton.setVisibility(View.VISIBLE);
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				locationButton.setVisibility(View.INVISIBLE);

				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				currentPt = ll;

				Editor editor = preferences.edit();
				editor.putString("latitude", currentPt.latitude + "");
				editor.putString("longitude", currentPt.longitude + "");
				editor.commit();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * ���Overlay
	 */
	public void publishOverlay() {
		Marker marker_jam;
		// private InfoWindow mInfoWindow;
		ArrayList<Marker> markerArr = new ArrayList<Marker>();

		BitmapDescriptor bd_jam = BitmapDescriptorFactory
				.fromResource(R.drawable.mark_jam);
		// add marker overlay
		MarkerOptions ooA = new MarkerOptions().position(currentPt)
				.icon(bd_jam).zIndex(9).draggable(true);
		marker_jam = (Marker) (mBaiduMap.addOverlay(ooA));

		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(bd_jam);
		markerArr.add(marker_jam);
	}

	/**
	 * �������Overlay
	 */
	public void clearOverlay() {
		mBaiduMap.clear();
		// for(int i=0; i<markerArr.size(); i++){
		// markerArr.get(i).setVisible(false);;
		// }
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		Count++;

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			// Toast.makeText(LocationAndMainActivity.this, "��Ǹ��δ�ҵ����",
			// Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
			// result.getSuggestAddrInfo()
			// return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap,
					degree);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			roadStateOverlayList.add(overlay);
			loadingText.setText("·����Ϣ������" + Count * 100 / roadStateList.size()
					+ "%");
			// Log.i("test", Count + " " + roadStateList.size() + " " +
			// Count*100/roadStateList.size());
			// overlay.zoomToSpan();
		}

		semaphore.release();
		if (Count == roadStateList.size()) {
			loadingText.setVisibility(View.GONE);
			radioGroup.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetSuggestionResult(SuggestionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
		} else {
			Toast.makeText(LocationAndMainActivity.this,
					result.getName() + ": " + result.getAddress(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(LocationAndMainActivity.this, "δ�ҵ����",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			String strInfo = "�ݲ��ṩ�Ƕ�λ���ڳ��е�������";

			// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
			// String strInfo = "��";
			// for (CityInfo cityInfo : result.getSuggestCityList()) {
			// strInfo += cityInfo.city;
			// strInfo += ",";
			// }
			// strInfo += "�ҵ����";
			Toast.makeText(LocationAndMainActivity.this, strInfo,
					Toast.LENGTH_LONG).show();
		}
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));
			// }
			return true;
		}
	}
}
