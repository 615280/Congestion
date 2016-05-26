package com.conges.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.conges.database.ConnectUtil;

import android.content.Context;
import android.widget.Toast;

public class HelpFunctions {
	
	/**
	 * 使用Toast显示信息
	 * */
	public static void useToastShort(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void useToastLong(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());       
		return format.format(date);
	}
	
	public static String getConnResult(String message){
		return ConnectUtil.getConnDef(message);
	}
	
	public static void main(String[] args) {
		String message = String
				.format("{\"login\":{\"phoneNum\":\"%s\",\"userPwd\":\"%s\"}}",
						"18522569895", "123456");
		System.out.println(getConnResult(message));
	}
}
