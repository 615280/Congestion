package com.conges.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TrafficMenuDetailActivity extends Activity {
	TextView tv_tableLine;
	EditText tableLineText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmenudetail);
		int type = getIntent().getIntExtra("trafficType", -1);
		init(type);
	}

	private void init(int type) {
		tableLineText = (EditText) findViewById(R.id.traffictypedetail_et_description);
		
		setTypeDetail(type);
		
		Button waitPubButton = (Button) findViewById(R.id.traffictypedetail_bt_waitpublish);
		waitPubButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button pubButton = (Button) findViewById(R.id.traffictypedetail_bt_topublish);
		pubButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//make connection with Server
				Toast.makeText(TrafficMenuDetailActivity.this, "�����ϴ�", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setTypeDetail(int type) {
		tv_tableLine = (TextView) findViewById(R.id.traffictypedetail_tv_tableLine);
		String str_tableLine = null;
		switch (type) {
		case 1:
			str_tableLine = "��ͨ����";
			break;
		case 2:
			str_tableLine = "�¹�";
			break;
		case 3:
			str_tableLine = "Σ��";
			break;
		case 4:
			str_tableLine = "����ͷ";
			break;
		case 5:
			str_tableLine = "��ͼ����";
			break;
		case 6:
			str_tableLine = "��ͼ����";
			break;
		default:
			str_tableLine = "�����������";
			break;
		}
		tv_tableLine.setText(str_tableLine);
	}
}
