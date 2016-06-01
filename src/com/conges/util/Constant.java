package com.conges.util;

public class Constant {
	public static final String URL = "115.159.115.216";
	public static final int PORT = 50001;
	
	public static final String ERROR_NETWORK = "ERROR_NET";
	
	public static final int DEGREE_MAX = 4; 
	
	public static final int TRAFFIC_INFO_LEVEL_LOW = 1;
	public static final int TRAFFIC_INFO_LEVEL_MIDDLE = 2;
	public static final int TRAFFIC_INFO_LEVEL_HIGH = 3;
	
	public static final int TRAFFIC_TYPE_TRAFFIC = 1;
	public static final int TRAFFIC_TYPE_ACCIDENT = 2;
	public static final int TRAFFIC_TYPE_WARNING = 3;
	public static final int TRAFFIC_TYPE_CAMERA = 4;
	public static final int TRAFFIC_TYPE_MAPHI = 5;
	public static final int TRAFFIC_TYPE_MAPPROBLEM =6;
	
	public static final String TRAFFIC_TYPE_TRAFFIC_STR = "交通阻塞";
	public static final String TRAFFIC_TYPE_TRAFFIC_STR_LOW = "中等";
	public static final String TRAFFIC_TYPE_TRAFFIC_STR_MIDDLE = "严重"; 
	public static final String TRAFFIC_TYPE_TRAFFIC_STR_HIGH = "不动";
	
	public static final String TRAFFIC_TYPE_ACCIDENT_STR = "事故";
	public static final String TRAFFIC_TYPE_ACCIDENT_STR_LOW = "轻微";
	public static final String TRAFFIC_TYPE_ACCIDENT_STR_HIGH = "严重";

	public static final String TRAFFIC_TYPE_WARNING_STR = "危险";
	public static final String TRAFFIC_TYPE_WARNING_STR_LOW = "路上";
	public static final String TRAFFIC_TYPE_WARNING_STR_MIDDLE = "路边";
	public static final String TRAFFIC_TYPE_WARNING_STR_HIGH = "天气";

	public static final String TRAFFIC_TYPE_CAMERA_STR = "摄像头";
	public static final String TRAFFIC_TYPE_CAMERA_STR_LOW = "速度";
	public static final String TRAFFIC_TYPE_CAMERA_STR_HIGH = "红灯";

	public static final String TRAFFIC_TYPE_MAPHI_STR = "地图聊天";
	public static final String TRAFFIC_TYPE_MAPPROBLEM_STR ="地图问题";
}
