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
		if(currentPt == null){
			return list;
		}
		DecimalFormat df = new DecimalFormat("#.000000");
		String message = String.format("{\"getRoadState\":{\"latitudeMin\":\"%s\",\"latitudeMax\":\"%s\",\"longitudeMin\":\"%s\",\"longitudeMax\":\"%s\"}}",
				df.format(currentPt.latitude - 0.001) + "",
				df.format(currentPt.latitude + 0.001) + "" ,
				df.format(currentPt.longitude - 0.001) + "" ,
				df.format(currentPt.longitude) + 0.001 + "");
//		String roadStateResult = ConnectUtil.getConnDef(message);
		
//		String message = String.format("{\"getRoadState\":{\"latitudeMin\":\"%s\",\"latitudeMax\":\"%s\",\"longitudeMin\":\"%s\",\"longitudeMax\":\"%s\"}}",
//		"32.282150", "32.282250" , "121.731086" , "121.731186");
		String roadStateResult = "{\"retRoadState\": {\"4\":[],\"1\": [\"\\u4e2d\\u79d1\\u5927\\u897f-\\u5357\\u5927\\u7814\\u7a76\\u751f\\u9662\", \"\\u4e2d\\u79d1\\u5927-\\u72ec\\u5885\\u6e56\\u56fe\\u4e66\\u9986\"], \"3\": [\"\\u5357\\u5927\\u7814\\u7a76\\u751f\\u9662-\\u897f\\u4ea4\\u5927\", \"\\u897f\\u4ea4\\u5927-\\u5357\\u5927\\u7814\\u7a76\\u751f\\u9662\"], \"2\": [\"\\u8363\\u57df\\u82b1\\u56ed-\\u4e2d\\u79d1\\u5927\\u897f\", \"\\u72ec\\u5885\\u6e56\\u56fe\\u4e66\\u9986-\\u8363\\u57df\\u82b1\\u56ed\"]}}";

		list = phaseRoadStateResult(roadStateResult, list);
		return list;
	}

	private static List<LineStep> phaseRoadStateResult(String result,
			List<LineStep> list) {
		try {
			JSONObject jObject = (JSONObject) new JSONObject(result).get("retRoadState");
			LineStep step;
			for (int i = 1; i <= Constant.DEGREE_MAX; i++) {
				JSONArray jsonArray = (JSONArray) jObject.get(i+"");
				for(int j=0; j<jsonArray.length(); j++){
					step = new LineStep();
					String a = jsonArray.getString(j);
					step.setDegree(i);
					step.setStartNode("Ыежн" + a.substring(0, a.indexOf("-")));
					step.setEndNode("Ыежн" + a.substring(a.indexOf("-")+1, a.length()));
					list.add(step);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static int uploadTrafficInfo(){
		int result = -1;
		
		return result;
	}
	
	public static void getTrafficInfo(){
		
	}

}
