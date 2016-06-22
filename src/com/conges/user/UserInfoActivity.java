package com.conges.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.conges.main.BusinessFunctions;
import com.conges.main.LocationAndMainActivity;
import com.conges.main.R;
import com.conges.util.HelpFunctions;

@SuppressLint({ "WorldReadableFiles", "HandlerLeak" })
public class UserInfoActivity extends Activity {
	private TextView phoneNumber, userName;
	private ImageView userIcon;
	private Button returnButton;
	private Button logoutButton;

	SharedPreferences preferences;
	Editor editor;
	String getUserResult;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
		setContentView(R.layout.activity_userinfo);
		phoneNumber = (TextView) findViewById(R.id.userinfo_tv_phonenumber);
		phoneNumber.setText("加载中...");
		getUserInfo();
		init();
	}

	private void getUserInfo() {
		new Thread() {
			public void run() {
				getUserResult = BusinessFunctions
						.getUserInfoByPhoneNum(preferences.getString(
								"phoneNum", ""));
				handler.sendEmptyMessage(0x127);
			};
		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x127) {
				try {
					JSONObject jObject = (JSONObject) new JSONObject(
							getUserResult).get("getUserInfo");
					if (!jObject.get("result").equals("0")) {
						HelpFunctions.useToastShort(getApplicationContext(),
								"信息获取失败，请重试");
						finish();
						return;
					}

					phoneNumber.setText("手机号：" + jObject.get("phoneNum"));
					userName.setText("用户名：" + jObject.get("userName"));
					// userIcon.setImageResource(R.drawable.icon_usericon);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if(msg.what == 0x135){
				editor = preferences.edit();
				editor.putInt("loginState", 0);
				editor.commit();
				finish();
			}
		}
	};

	@SuppressWarnings("deprecation")
	private void init() {
		userIcon = (ImageView) findViewById(R.id.userinfo_iv);
		userName = (TextView) findViewById(R.id.userinfo_tv_username);
		// returnButton = (Button) findViewById(R.id.userinfo_bt_returnmain);
		logoutButton = (Button) findViewById(R.id.userinfo_bt_logout);

		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);
		// phoneNumber.setText("手机号：" + preferences.getString("phoneNum",
		// "信息获取失败"));
		// userName.setText("用户名：" + preferences.getString("userName",
		// "信息获取失败"));
		userIcon.setImageResource(R.drawable.icon_usericon);

		returnButton = (Button) findViewById(R.id.userinfo_bt_returnmain);
		returnButton.setVisibility(View.GONE);
		// returnButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(getApplicationContext(),
		// LocationAndMainActivity.class);
		// startActivity(intent);
		// finish();
		// }
		// });

		logoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder checkAlert = new Builder(UserInfoActivity.this);
				checkAlert.setTitle("提示").setIcon(R.drawable.ic_launcher)
						.setMessage("确认退出该帐号吗？");
				checkAlert.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new Thread() {
									public void run() {
										handler.sendEmptyMessage(0x135);
									};
								}.start();
							}
						});

				checkAlert.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				checkAlert.show();
				
			}
		});
	}
}
