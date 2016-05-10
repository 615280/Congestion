package com.conges.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

public class HelpFunctions {
	
	/**
	 * ʹ��Toast��ʾ��Ϣ
	 * */
	public static void useToast(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());       
		return format.format(date);
	}
}
