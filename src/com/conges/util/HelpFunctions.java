package com.conges.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

public class HelpFunctions {
	
	/**
	 * ʹ��Toast��ʾ��Ϣ
	 * */
	public static void useToastShort(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void useToastLong(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());       
		return format.format(date);
	}
}
