package com.conges.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.conges.main.LocationAndMainActivity;
import com.conges.main.R;
import com.conges.util.HelpFunctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint("WorldReadableFiles")
public class FriendListActivity extends Activity {

	private TextView userNameTextView;
	private Button addFriendButton;
	private Button okButton;

	ListView listView;
	List<Map<String, Object>> friendList;
	String[] friendsName = { "Congestion小助手", "苏州发布", "苏州公交", "holly" };

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendlist);

		listView = (ListView) findViewById(R.id.listview_fl_friendlist);
		userNameTextView = (TextView) findViewById(R.id.tv_fl_username);
		SharedPreferences preferences = getSharedPreferences("conges",
				MODE_WORLD_READABLE);
		String userName = preferences.getString("userName", "信息获取失败");

		userNameTextView.setText("用户: " + userName);
		userNameTextView.setTextColor(getResources().getColor(
				R.color.whitesmoke));

		friendList = new ArrayList<Map<String, Object>>();
		initFriendList();

		addFriendButton = (Button) findViewById(R.id.bt_fl_addfriend);
		addFriendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HelpFunctions.useToastShort(getApplicationContext(),
						getResources().getString(R.string.waittocode));
			}
		});

		okButton = (Button) findViewById(R.id.button_fl_ok);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FriendListActivity.this,
						LocationAndMainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initFriendList() {
		Map<String, Object> map;
		for (int i = 0; i < friendsName.length; i++) {
			map = new HashMap<String, Object>();
			map.put("userName", friendsName[i]);
			friendList.add(map);
		}

		SimpleAdapter sa = new SimpleAdapter(getApplicationContext(),
				friendList,
				R.layout.acitivity_friendlist_fragment_listview_item_layout,
				new String[] { "userName" },
				new int[] { R.id.friendlist_fragment_username });
		listView.setAdapter(sa);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HelpFunctions.useToastShort(getApplicationContext(), "开始会话: " + friendsName[position]);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				HelpFunctions.useToastShort(getApplicationContext(), "长按删除好友: " + friendsName[position]);
				return true;		//true则不会调用onitemclick了
			}
		});
	}
}
