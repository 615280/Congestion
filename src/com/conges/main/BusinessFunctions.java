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
		return result;
	}

	public static List<LineStep> getRoadStateResult(LatLng currentPt) {
		List<LineStep> list = new ArrayList<LineStep>();
		if (currentPt == null) {
			return list;
		}
		DecimalFormat df = new DecimalFormat("#.000000");
		@SuppressWarnings("unused")
		String message = String
				.format("{\"getRoadState\":{\"latitudeMin\":\"%s\",\"latitudeMax\":\"%s\",\"longitudeMin\":\"%s\",\"longitudeMax\":\"%s\"}}",
						df.format(currentPt.latitude - 0.001) + "",
						df.format(currentPt.latitude + 0.001) + "",
						df.format(currentPt.longitude - 0.001) + "",
						df.format(currentPt.longitude) + 0.001 + "");
		// String roadStateResult = ConnectUtil.getConnDef(message);

		// String message =
		// String.format("{\"getRoadState\":{\"latitudeMin\":\"%s\",\"latitudeMax\":\"%s\",\"longitudeMin\":\"%s\",\"longitudeMax\":\"%s\"}}",
		// "32.282150", "32.282250" , "121.731086" , "121.731186");
		String roadStateResult = "{\"retRoadState\": {\"4\":[],\"1\": [\"\\u4e2d\\u79d1\\u5927\\u897f-\\u5357\\u5927\\u7814\\u7a76\\u751f\\u9662\", \"\\u4e2d\\u79d1\\u5927-\\u72ec\\u5885\\u6e56\\u56fe\\u4e66\\u9986\"], \"3\": [\"\\u5357\\u5927\\u7814\\u7a76\\u751f\\u9662-\\u897f\\u4ea4\\u5927\", \"\\u897f\\u4ea4\\u5927-\\u5357\\u5927\\u7814\\u7a76\\u751f\\u9662\"], \"2\": [\"\\u8363\\u57df\\u82b1\\u56ed-\\u4e2d\\u79d1\\u5927\\u897f\", \"\\u72ec\\u5885\\u6e56\\u56fe\\u4e66\\u9986-\\u8363\\u57df\\u82b1\\u56ed\"]}}";

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
			
			if(jArray.length() == 0){
				return list;
			}
			
			for(int i=0; i<jArray.length(); i++){
				trafficInfo = new TrafficInfo();
				JSONObject j_data = (JSONObject) jArray.get(i);
				trafficInfo.setLatitude(j_data.getDouble("latitude"));
				trafficInfo.setLongitude(Double.parseDouble(j_data.getString("longitude")));
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
}
