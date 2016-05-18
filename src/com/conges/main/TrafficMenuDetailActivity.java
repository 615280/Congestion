package com.conges.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrafficMenuDetailActivity extends Activity {
	TextView tv_tableLine;
	EditText tableLineText;
	ImageButton lowButton,middleButton,highButton;
	
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
				boolean result = true;
				if(result){
					Toast.makeText(TrafficMenuDetailActivity.this, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TrafficMenuDetailActivity.this, "����ʧ�ܣ����Ժ����ԣ�", Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
	}

	private void setTypeDetail(int type) {
		tv_tableLine = (TextView) findViewById(R.id.traffictypedetail_tv_tableLine);
		
		LinearLayout btLinear = (LinearLayout) findViewById(R.id.traffictypedetail_imabt_linearlayout);
		lowButton = (ImageButton) findViewById(R.id.traffictypedetail_bt_low);
		middleButton = (ImageButton) findViewById(R.id.traffictypedetail_bt_middle);
		highButton = (ImageButton) findViewById(R.id.traffictypedetail_bt_high);
		
		String str_tableLine = null;
		float weightSum = 0.0f;
		switch (type) {
		case 1:
			str_tableLine = "��ͨ����";
			weightSum = 3.0f;
			btLinear.setWeightSum(weightSum);
			lowButton.setBackgroundResource(R.drawable.mark_jam_middle);
			middleButton.setBackgroundResource(R.drawable.mark_jam_serious);
			highButton.setBackgroundResource(R.drawable.mark_jam_fixed);
			break;
		case 2:
			str_tableLine = "�¹�";
			weightSum = 2.0f;
			btLinear.setWeightSum(weightSum);
			break;
		case 3:
			str_tableLine = "Σ��";
			weightSum = 3.0f;
			btLinear.setWeightSum(weightSum);
			break;
		case 4:
			str_tableLine = "����ͷ";
			weightSum = 2.0f;
			btLinear.setWeightSum(weightSum);
			break;
		case 5:
			str_tableLine = "��ͼ����";
			weightSum = 0.0f;
			btLinear.setWeightSum(weightSum);
			break;
		case 6:
			str_tableLine = "��ͼ����";
			weightSum = 0.0f;
			btLinear.setWeightSum(weightSum);
			break;
		default:
			str_tableLine = "�����������";
			weightSum = 0.0f;
			btLinear.setWeightSum(weightSum);
			break;
		}
		
		tv_tableLine.setText(str_tableLine);
	}
}
