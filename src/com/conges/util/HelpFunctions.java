package com.conges.util;

import android.content.Context;
import android.widget.Toast;

public class HelpFunctions {
	
	/**
	 * 使用Toast显示信息
	 * */
	public static void useToast(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	
}
