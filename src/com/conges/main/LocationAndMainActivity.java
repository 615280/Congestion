package com.conges.main;

import java.util.ArrayList;
import java.util.List;
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
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.conges.data.LineStep;
import com.conges.user.FriendListActivity;
import com.conges.user.LoginActivity;
import com.conges.util.HelpFunctions;

@SuppressLint({ "WorldReadableFiles", "HandlerLeak" })
public class LocationAndMainActivity extends Activity implements
		OnGetRoutePlanResultListener {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private BaiduMapOptions mBaiduMapOptions;

	private LatLng currentPt = null; // �������꾭γ��
	private String touchType;
	private TextView mStateBar;

	private OnCheckedChangeListener radioButtonListener;
	private Button publishSitButton;
	private Button settingButton;
	private Button locationButton;
	private Button contactButton;

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

	ExecutorService exec = Executors.newCachedThreadPool();
	final Semaphore semaphore = new Semaphore(1);
	int degree = -1;

	SharedPreferences preferences;
	String[] nodeName = {"������ͼ���", "������", "�����㳡��", "�пƴ�", "�пƴ���" };
	AutoCompleteTextView searchText;
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
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
		
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
		mStateBar.setText(state);

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
				HelpFunctions.useToastShort(getApplicationContext(), "��������Ϊ��" + searchText.getText().toString());
			}
		});
		
		// �򿪻�ر�·����ʾ����
		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		// group.setVisibility(View.INVISIBLE);
		// group.
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.closeColor) {
					// �ر�
					// Toast.makeText(LocationAndMainActivity.this,
					// "closeColor",
					// Toast.LENGTH_SHORT).show();
					clearOverlay();
					new Thread() {
						public void run() {
							while (semaphore.getQueueLength() > 0) {
								semaphore.release();
							}
						};
					}.start();
				}

				if (checkedId == R.id.openColor) {
					// ��
					// Toast.makeText(LocationAndMainActivity.this, "openColor",
					// Toast.LENGTH_SHORT).show();
					new Thread() {
						@Override
						public void run() {
							roadStateList = BusinessFunctions
									.getRoadStateResult(currentPt);
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
			}
		};

		group.setOnCheckedChangeListener(radioButtonListener);

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
				
				if(currentPt !=null){
					Intent intent = new Intent(LocationAndMainActivity.this,
							TrafficInfoActivity.class);
					Bundle b = new Bundle();
					b.putString("latitude", currentPt.latitude+"");
					b.putString("longitude", currentPt.longitude+"");
					b.putDouble("rate", 10.0);
					intent.putExtras(b);
					startActivity(intent);
				} else {
					HelpFunctions.useToastLong(getApplicationContext(), "���Ȼ�ȡ��λ��");
					locationButton.setVisibility(View.VISIBLE);
				}
				// publishOverlay();
				// mBaiduMap.setOnMarkerClickListener(new
				// OnMarkerClickListener() {
				// public boolean onMarkerClick(final Marker marker) {
				// Intent intent = new Intent(
				// LocationAndMainActivity.this,
				// TrafficDetailActivity.class);
				// Bundle data = new Bundle();
				// Projection projection = mMapView.getMap()
				// .getProjection();
				// Point point = projection.toScreenLocation(currentPt);
				// data.putInt("pointx", point.x);
				// data.putInt("pointy", point.y);
				// intent.putExtras(data);
				// startActivity(intent);
				// /*
				// * Button button = new Button(getApplicationContext());
				// * button.setBackgroundResource(R.drawable.popup);
				// * OnInfoWindowClickListener listener = null; if (marker
				// * == marker_jam) { button.setText("������Ϣ"); listener =
				// * new OnInfoWindowClickListener() { public void
				// * onInfoWindowClick() { mBaiduMap.hideInfoWindow();
				// * //�����ʾButton֮��Ĳ��� } }; LatLng ll =
				// * marker.getPosition(); Bitmap bm =
				// * BitmapFactory.decodeResource(getResources(),
				// * R.drawable.popup); int yOffset = (int)
				// * (bm.getHeight()*(-1.1)); mInfoWindow = new
				// * InfoWindow(BitmapDescriptorFactory.fromView(button),
				// * ll, yOffset, listener);
				// * mBaiduMap.showInfoWindow(mInfoWindow); }
				// */
				// return true;
				// }
				// });
			}
		});

		settingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(LocationAndMainActivity.this, "����",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LocationAndMainActivity.this,
						PreferenceMainActivity.class);
				startActivity(intent);
			}
		});

		locationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LocationAndMainActivity.this, "��λ",
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
			// System.out.println("hello");
			final LineStep lineStep = (LineStep) msg.getData().getParcelable(
					"linestep");

			Runnable run = new Runnable() {
				@Override
				public void run() {
					lineStep.getStartNode();
					lineStep.getEndNode();
					// searchButtonProcess();
					route = null;
					PlanNode stNode = PlanNode.withCityNameAndPlaceName("����",
							lineStep.getStartNode());
					PlanNode enNode = PlanNode.withCityNameAndPlaceName("����",
							lineStep.getEndNode());

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
	};

	public void searchButtonProcess(LineStep lineStep) {
		// ��������ڵ��·������
		route = null;
		// mBaiduMap.clear();
		// PlanNode stNode = PlanNode.withCityNameAndPlaceName("����", "����");
		// PlanNode enNode = PlanNode.withCityNameAndPlaceName("����", "����");

		// if (v.getId() == R.id.bt_main_transit) {
		// HelpFunctions.useToastLong(getApplicationContext(), "����������");
		// while (roadStateList.size() > 0) {
		// Thread A = new Thread(new Runnable() {
		// @Override
		// public void run() {
		// System.out.println("hello");
		// synchronized (this) {
		// if (roadStateList.size() > 0) {
		// lineStep = roadStateList.get(0);
		// roadStateList.remove(0);
		// } else {
		// return;
		// }
		// }
		// �������յ���Ϣ
		PlanNode stNode = PlanNode.withCityNameAndPlaceName("����",
				lineStep.getStartNode());
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("����",
				lineStep.getEndNode());
		mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
				.city("����").to(enNode));
		// }
		// });
		// A.start();
		// }
		// }
	}

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
		int i = preferences.getInt("test", 0);
		Editor editor = preferences.edit();
		editor.putInt("test", i++);
		editor.commit();

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
			overlay.addToMap();
			// overlay.zoomToSpan();
		}

		semaphore.release();
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
}
