package com.conges.util;

import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;

public interface OnGetRouteResultMyListener extends OnGetRoutePlanResultListener{

	public void onGetMyTransitRouteResult(TransitRouteResult result);
	
}
