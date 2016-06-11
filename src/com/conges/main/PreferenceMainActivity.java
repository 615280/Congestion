package com.conges.main;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.conges.user.LoginActivity;
import com.conges.user.UserInfoActivity;
import com.conges.util.HelpFunctions;

@SuppressLint({ "WorldReadableFiles", "HandlerLeak" })
public class PreferenceMainActivity extends PreferenceActivity {
	private Button logoutButton, loginButton;

	static SharedPreferences preferences;
	Editor editor;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences("conges", MODE_WORLD_READABLE);

		if (preferences.getInt("loginState", -1) == 1) {
			if (hasHeaders()) {
				logoutButton = new Button(this);
				logoutButton.setText("注销");
				// 将该按钮添加到该界面上
				setListFooter(logoutButton);
				logoutButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Builder checkAlert = new Builder(
								PreferenceMainActivity.this);
						checkAlert.setTitle("提示")
								.setIcon(R.drawable.ic_launcher)
								.setMessage("确认退出该帐号吗？");
						checkAlert.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										new Thread() {
											public void run() {
												handler.sendEmptyMessage(0x123);
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
		} else {
			if (hasHeaders()) {
				loginButton = new Button(this);
				loginButton.setText("登录");
				setListFooter(loginButton);
				loginButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToLoginActivity();
					}
				});
			}
		}
	}
	
	public void goToLoginActivity(){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		return true;
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		// 加载选项设置列表的布局文件
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	public static class Prefs1Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			System.out.println("1234");
			// addPreferencesFromResource(R.xml.preferences_user);
			// PreferenceScreen ps = getPreferenceScreen().;
			// ps.setTitle("未登录");
			if (preferences.getInt("loginState", -1) == 1) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), UserInfoActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getActivity(), "目前尚未登录", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static class Prefs2Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences_user);
		}
	}

	public static class Prefs3Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
			Toast.makeText(getActivity(), "缓存信息已清除（包括个人信息和缓存文件）",
					Toast.LENGTH_LONG).show();
		}
	}

	public static class FavorManager extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				editor = preferences.edit();
				editor.putInt("loginState", 0);
				editor.putInt("autoLogin", 0);
				editor.remove("userPass");
				editor.commit();
				HelpFunctions.useToastLong(PreferenceMainActivity.this,
						"退出登录成功！");
				logoutButton.setVisibility(View.INVISIBLE);
			}
		};
	};
}
