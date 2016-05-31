package com.conges.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.conges.util.Constant;

import android.util.Log;

public class ConnectUtil {

	public static String getConnDef(String message){
		return getConn(Constant.URL, Constant.PORT, message);
	}
	
	public static String getConn(String uri, int port, String message) {
		String result = "";
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader br = null;
		try {
			InetAddress addr = InetAddress.getByName(uri);
			socket = new Socket(addr, port);
			socket.setSoTimeout(5000);

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())));
			out.print(message);
			out.flush();

			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String line = "";
			while((line = br.readLine()) != null){
				result += line;
			}
		} catch(SocketTimeoutException e){
			result = Constant.ERROR_NETWORK;				//net error
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				br.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("result", "IOException");
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		Log.i("result",  result.toString());
		return result;
	}
	
//	public static Map<String, Object> jsonPhase(String result,Map a){
//		try {
//			JSONArray jArray = new JSONArray(result);
//			if(jArray.length() > 0){
//				for(int i=0; i<jArray.length(); i++){
//					JSONObject j_data = jArray.getJSONObject(i);
//				}
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return a;
//	}
}
