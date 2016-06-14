package com.conges.data;

import android.graphics.Bitmap;

public class UserInfo {
	private String phoneNum;
	private String userName;
	private String sex;
	private Bitmap userIcon;

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Bitmap getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(Bitmap userIcon) {
		this.userIcon = userIcon;
	}

}
