package com.conges.main;

import com.conges.util.GeoCodeUtil;

public class TestActivity{
	public static void main(String[] args) {
		GeoCodeUtil gc = new GeoCodeUtil();
		gc.SearchButtonProcess(1);
		System.out.println(gc.getAddress());
	}
}
