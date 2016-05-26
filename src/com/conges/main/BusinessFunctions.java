package com.conges.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.conges.database.ConnectUtil;

@SuppressLint("WorldReadableFiles")
public class BusinessFunctions {
	@SuppressWarnings("deprecation")
	public static String login(String phoneNum, String userPass,SharedPreferences preferences,Context context){
		String message = String
				.format("{\"login\":{\"phoneNum\":\"%s\",\"userPwd\":\"%s\"}}",
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
	
	
}
