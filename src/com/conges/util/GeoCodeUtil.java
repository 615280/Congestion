package com.conges.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class GeoCodeUtil implements OnGetGeoCoderResultListener {
	GeoCoder mSearch = null;
	LatLng point = null;
	String address = "";

	public LatLng getPoint() {
		return point;
	}

	public String getAddress() {
		return address;
	}

	public void initial() {
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
	}

	public void SearchButtonProcess(int type) {
		// if (v.getId() == R.id.reversegeocode) {
		// EditText lat = (EditText) findViewById(R.id.lat);
		// EditText lon = (EditText) findViewById(R.id.lon);
		if (type == 1) {
			String lat = "39.904965";
			String lon = "116.327764";
			// LatLng ptCenter = new LatLng((Float.valueOf(lat.getText()
			// .toString())), (Float.valueOf(lon.getText().toString())));
			LatLng ptCenter = new LatLng((Float.valueOf(lat)),
					(Float.valueOf(lon)));
			// 反Geo搜索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(ptCenter));
		} else if (type == 2) {
			// } else if (v.getId() == R.id.geocode) {
			// EditText editCity = (EditText) findViewById(R.id.city);
			// EditText editGeoCodeKey = (EditText)
			// findViewById(R.id.geocodekey);
			String editCity = "北京";
			String editGeoCodeKey = "南开大学";
			// Geo搜索
			// mSearch.geocode(new GeoCodeOption().city(
			// editCity.getText().toString()).address(
			// editGeoCodeKey.getText().toString()));
			mSearch.geocode(new GeoCodeOption().city(editCity).address(
					editGeoCodeKey));
		}
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			// Toast.makeText(TestActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
			// .show();
			return;
		}
		// mBaiduMap.clear();
		// mBaiduMap.addOverlay(new
		// MarkerOptions().position(result.getLocation())
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_marka)));
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
		// .getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		// Toast.makeText(TestActivity.this, strInfo, Toast.LENGTH_LONG).show();
		point = new LatLng(result.getLocation().latitude,
				result.getLocation().longitude);
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			// Toast.makeText(TestActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
			// .show();
			return;
		}
		// mBaiduMap.clear();
		// mBaiduMap.addOverlay(new
		// MarkerOptions().position(result.getLocation())
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_marka)));
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
		// .getLocation()));
		// Toast.makeText(TestActivity.this, result.getAddress(),
		// Toast.LENGTH_LONG).show();
		address = result.getAddress();
	}
}
