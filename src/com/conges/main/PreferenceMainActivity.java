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
			//�ڵײ��Ӹ���ť
//			Button button = new Button(this);
//			button.setText("��������");
//			// ���ð�ť��ӵ��ý�����
//			setListFooter(view);
		}
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		// TODO Auto-generated method stub
		// ����ѡ�������б�Ĳ����ļ�
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
			// ��ȡ�����Fragment�Ĳ���
			String website = getArguments().getString("website");
			Toast.makeText(getActivity(), "��վ�����ǣ�" + website, Toast.LENGTH_LONG)
					.show();
		}
	}
}
