package com.conges.database;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Toast;

public class Connector {

	public static String sendPost(String url, String params) {
		String result = "";
		PrintWriter out = null;
		DataInputStream in = null;

		try {
			URL uri = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			out = new PrintWriter(conn.getOutputStream());
			out.print(params);
			out.flush();

			conn.getInputStream();
			
			in = new DataInputStream(conn.getInputStream());
//					new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
//			while ((line = in.read(buffer)) != null) {
//				result += "\n" + line;
//			}

			System.out.println(result);

		} catch(SocketTimeoutException e){
			//socket time out, please retry
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return result;
	}
}
