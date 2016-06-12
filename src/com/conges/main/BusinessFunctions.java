package com.conges.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.baidu.mapapi.model.LatLng;
import com.conges.data.ConnectUtil;
import com.conges.data.LineStep;
import com.conges.data.TrafficInfo;
import com.conges.data.UserInfo;
import com.conges.util.Constant;

@SuppressLint("WorldReadableFiles")
public class BusinessFunctions {

	@SuppressWarnings("deprecation")
	public static String login(String phoneNum, String userPass,
			SharedPreferences preferences, Context context) {
		String message = String.format(
				"{\"login\":{\"phoneNum\":\"%s\",\"userPwd\":\"%s\"}}",
				phoneNum, userPass);
		preferences = context.getSharedPreferences("conges",
				Context.MODE_WORLD_READABLE);
		Editor editor = preferences.edit();
		editor.putString("phoneNum", phoneNum);
		editor.putString("userPass", userPass);
		editor.commit();
		String result = ConnectUtil.getConnDef(message);
		if(result == null){
			result = "{\"loginResult\": 3}";
		}
		return result;
	}

	public static List<LineStep> getRoadStateResult(LatLng currentPt, double range) {
		List<LineStep> list = new ArrayList<LineStep>();
		if (currentPt == null) {
			return list;
		}
		DecimalFormat df = new DecimalFormat("#.000000");
		@SuppressWarnings("unused")
		String message = String
				.format("{\"getRoadState\":{\"latitudeMin\":\"%s\",\"latitudeMax\":\"%s\",\"longitudeMin\":\"%s\",\"longitudeMax\":\"%s\"}}",
						df.format(currentPt.latitude - range) + "",
						df.format(currentPt.latitude + range) + "",
						df.format(currentPt.longitude - range) + "",
						df.format(currentPt.longitude + range) + "");
		 String roadStateResult = ConnectUtil.getConnDef(message);
		list = phaseRoadStateResult(roadStateResult, list);
		
		return list;
	}

	private static List<LineStep> phaseRoadStateResult(String result,
			List<LineStep> list) {
		try {
			JSONObject jObject = (JSONObject) new JSONObject(result)
					.get("retRoadState");
			LineStep step;
			for (int i = 1; i <= Constant.DEGREE_MAX; i++) {
				JSONArray jsonArray = (JSONArray) jObject.get(i + "");
				for (int j = 0; j < jsonArray.length(); j++) {
					step = new LineStep();
					String a = jsonArray.getString(j);
					step.setDegree(i);
					step.setStartNode("Ыежн" + a.substring(0, a.indexOf("-")));
					step.setEndNode("Ыежн"
							+ a.substring(a.indexOf("-") + 1, a.length()));
					list.add(step);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String uploadTrafficInfo(TrafficInfo traffic) {
		String message = String
				.format("{\"uploadTrafficInfo\":"
						+ "{\"phoneNum\":\"%s\",\"latitude\":\"%s\",\"longitude\":\"%s\","
						+ "\"dateTime\":\"%s\",\"type\":\"%s\",\"level\":\"%s\",\"detail\":\"%s\"}}",
						traffic.getPubUser(), traffic.getLatitude(),
						traffic.getLongitude(), traffic.getDateTime(),
						traffic.getType(), traffic.getLevel(),
						traffic.getDetail());
		return ConnectUtil.getConnDef(message);
	}

	public static List<TrafficInfo> getTrafficInfo(double latitude,
			double longitude, double rate) {
		List<TrafficInfo> list = new ArrayList<TrafficInfo>();
		String message = String.format("{\"getTrafficInfo\":"
				+ "{\"latitudeMin\":\"%s\",\"latitudeMax\":\"%s\","
				+ "\"longitudeMin\":\"%s\",\"longitudeMax\":\"%s\"}}", latitude
				- rate + "", latitude + rate + "", longitude - rate + "",
				longitude + rate + "");
		String result = ConnectUtil.getConnDef(message);
		list = phaseGetTrafficInfoResult(result, list);
		return list;
	}

	private static List<TrafficInfo> phaseGetTrafficInfoResult(String result,
			List<TrafficInfo> list) {
		try {
			TrafficInfo trafficInfo;
			JSONArray jArray = (JSONArray) new JSONObject(result)
					.get("retTrafficInfo");

			if (jArray.length() == 0) {
				return list;
			}

			for (int i = 0; i < jArray.length(); i++) {
				trafficInfo = new TrafficInfo();
				JSONObject j_data = (JSONObject) jArray.get(i);
				trafficInfo.setLatitude(j_data.getDouble("latitude"));
				trafficInfo.setLongitude(Double.parseDouble(j_data
						.getString("longitude")));
				trafficInfo.setLevel(j_data.getInt("level"));
				trafficInfo.setDateTime(j_data.getString("dateTime"));
				trafficInfo.setDetail(j_data.getString("detail"));
				trafficInfo.setPubUser(j_data.getString("phoneNum"));
				trafficInfo.setType(j_data.getInt("type"));
				list.add(trafficInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String getTrafficTypeAndLevelStr(int type, int level) {
		String typeStr;
		switch (type) {
		case Constant.TRAFFIC_TYPE_TRAFFIC:
			typeStr = Constant.TRAFFIC_TYPE_TRAFFIC_STR + "  ";
			switch (level) {
			case Constant.TRAFFIC_INFO_LEVEL_LOW:
				return typeStr + Constant.TRAFFIC_TYPE_TRAFFIC_STR_LOW;
			case Constant.TRAFFIC_INFO_LEVEL_MIDDLE:
				return typeStr + Constant.TRAFFIC_TYPE_TRAFFIC_STR_MIDDLE;
			case Constant.TRAFFIC_INFO_LEVEL_HIGH:
				return typeStr + Constant.TRAFFIC_TYPE_TRAFFIC_STR_HIGH;
			}
			break;
		case Constant.TRAFFIC_TYPE_ACCIDENT:
			typeStr = Constant.TRAFFIC_TYPE_ACCIDENT_STR + "  ";
			switch (level) {
			case Constant.TRAFFIC_INFO_LEVEL_LOW:
				return typeStr + Constant.TRAFFIC_TYPE_ACCIDENT_STR_LOW;
			case Constant.TRAFFIC_INFO_LEVEL_HIGH:
				return typeStr + Constant.TRAFFIC_TYPE_ACCIDENT_STR_HIGH;
			}
			break;
		case Constant.TRAFFIC_TYPE_WARNING:
			typeStr = Constant.TRAFFIC_TYPE_WARNING_STR + "  ";
			switch (level) {
			case Constant.TRAFFIC_INFO_LEVEL_LOW:
				return typeStr + Constant.TRAFFIC_TYPE_WARNING_STR_LOW;
			case Constant.TRAFFIC_INFO_LEVEL_MIDDLE:
				return typeStr + Constant.TRAFFIC_TYPE_WARNING_STR_MIDDLE;
			case Constant.TRAFFIC_INFO_LEVEL_HIGH:
				return typeStr + Constant.TRAFFIC_TYPE_WARNING_STR_HIGH;
			}
			break;
		case Constant.TRAFFIC_TYPE_CAMERA:
			typeStr = Constant.TRAFFIC_TYPE_CAMERA_STR + "  ";
			switch (level) {
			case Constant.TRAFFIC_INFO_LEVEL_LOW:
				return typeStr + Constant.TRAFFIC_TYPE_CAMERA_STR_LOW;
			case Constant.TRAFFIC_INFO_LEVEL_HIGH:
				return typeStr + Constant.TRAFFIC_TYPE_CAMERA_STR_HIGH;
			}
			break;
		default:
			break;
		}
		return null;
	}
	
	public static String getUserInfoByPhoneNum(String phoneNum){
		String message = String.format("{\"getUserInfo\":{\"phoneNum\":\"%s\"}}", phoneNum);
		return ConnectUtil.getConnDef(message);
	}
}
