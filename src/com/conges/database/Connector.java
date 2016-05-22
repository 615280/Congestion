package com.conges.database;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class Connector {
	
	private String url = "http://159.115.216:50001";
	
	public String sendPost(String url){
		String result = "";
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			URL realUrl = new URL(url);
			URLConnection uc = realUrl.openConnection();
			
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			return result;
		}
	}
}
