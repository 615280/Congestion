package com.conges.main;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class PreferenceMainActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(hasHeaders()){
			//在底部加个按钮
//			Button button = new Button(this);
//			button.setText("保存设置");
//			// 将该按钮添加到该界面上
//			setListFooter(view);
		}
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		// TODO Auto-generated method stub
		// 加载选项设置列表的布局文件
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	public static class Prefs1Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}

	public static class Prefs2Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.display_prefs);
			// 获取传入该Fragment的参数
			String website = getArguments().getString("website");
			Toast.makeText(getActivity(), "网站域名是：" + website, Toast.LENGTH_LONG)
					.show();
		}
	}
}
