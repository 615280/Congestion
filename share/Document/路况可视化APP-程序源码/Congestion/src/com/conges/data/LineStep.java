package com.conges.data;

import android.os.Parcel;
import android.os.Parcelable;

public class LineStep implements Parcelable {
	private String startNode;
	private String endNode;
	private int degree;

	public String getStartNode() {
		return startNode;
	}

	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	public String getEndNode() {
		return endNode;
	}

	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(startNode);
		dest.writeString(endNode);
		dest.writeInt(degree);
	}
}
