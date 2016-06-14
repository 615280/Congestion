//package com.conges.main;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.asynctasks.ievent.Get_event_with_id_task;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//
//public class Fragment_with_Id {
//
//	private boolean wasSelected = true;
//
//	private final static int ITEM_INDEX = 5;
//
//	private int fragmentId;
//
//	private int eventMaxCount;
//
//	private ListView lv;
//
//	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//
//	private String strs[] = { "title", "starttime", "endtime", "place" };
//	private int ids[] = { R.id.fragment_listView_item_textView_title,
//			R.id.fragment_listView_item_textView_starttime,
//			R.id.fragment_listView_item_textView_endtime,
//			R.id.fragment_listView_item_textView_place };
//
//	private SimpleAdapter sa;
//
//	public boolean getWasSelected() {
//		return wasSelected;
//	}
//
//	public void setWasSelected(boolean is) {
//		wasSelected = is;
//	}
//
//	public void setId(int fragmentid) {
//		fragmentId = fragmentid;
//	}
//
//	public void setEventMaxCount(int count) {
//		eventMaxCount = count;
//	}
//
//	public List<Map<String, Object>> getData() {
//		return data;
//	}
//
//	public void setData() {
//		Map<String, Object> map;
//		// openWebInMainThread();
//		if (wasSelected) {
//			for (int i = 0; i < ITEM_INDEX; i++) {
//				map = new HashMap<String, Object>();
//				map.put(strs[0], "数据加载中...");
//				map.put(strs[1], "数据加载中...");
//				data.add(map);
//			}
//		}
//	}
//
//	public SimpleAdapter getSimpleAdapter() {
//		return sa;
//	}
//
//	public void setSimpleAdapter() {
//		sa = new SimpleAdapter(getActivity(), data,
//				R.layout.fragment_listview_item_layout, strs, ids);
//
//		sa.setViewBinder(new SimpleAdapter.ViewBinder() {
//
//			@Override
//			public boolean setViewValue(View view, Object data,
//					String textRepresentation) {
//				// TODO Auto-generated method stub
//				if (view instanceof ImageView && data instanceof Bitmap) {
//					ImageView iv = (ImageView) view;
//					iv.setImageBitmap((Bitmap) data);
//					return true;
//				} else
//					return false;
//			}
//		});
//	}
//
//	public void setSaToListView() {
//		lv.setAdapter(sa);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		View v = inflater.inflate(R.layout.fragment, container, false);
//
//		setupGoodsCount();
//
//		setupListView(v);
//
//		return v;
//	}
//
//	// public void setupGoodsCount(){
//	// getEventwithId=new Get_event_with_id_task();
//	// getEventwithId.execute(fragmentId,this);
//	// // Log.i("看count值:", "eventMaxCount:"+eventMaxCount);
//	// }
//
//	public void setupListView(View v) {
//
//		lv = (ListView) v.findViewById(R.id.fragment_listView);
//		lv.setOnItemClickListener(new OnItemClickListener());
//		lv.setOnScrollListener(new OnscrollListener());
//
//		setData();
//
//		setSimpleAdapter();
//
//		lv.setAdapter(sa);
//
//		if (wasSelected) {
//			for (int i = 0; i < ITEM_INDEX; i++) {
//				lits.add(new LoadImageTask());
//				lits.get(i).execute(i, fragmentId, sa, data.get(i));
//			}
//		}
//	}
//
//	public class OnItemClickListener implements AdapterView.OnItemClickListener {
//		@SuppressWarnings("unchecked")
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			// TODO Auto-generated method stub
//			ListView list = (ListView) arg0;
//			HashMap<String, Object> hmap = (HashMap<String, Object>) list
//					.getItemAtPosition(arg2);
//
//			Intent intent = new Intent(getActivity(),
//					Infosdetail_Activity.class);
//
//			Bundle b = new Bundle();
//			b.putSerializable("item_info_map", hmap);
//			intent.putExtra("item_info_bundle", b);
//			startActivity(intent);
//		}
//
//	}
//
//	public class OnscrollListener implements AbsListView.OnScrollListener {
//
//		private int lastItem;
//
//		boolean isLastItem = false;
//
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem,
//				int visibleItemCount, int totalItemCount) {
//
//			// TODO Auto-generated method stub
//
//			if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//				isLastItem = true;
//				lastItem = totalItemCount;
//
//			} else
//				isLastItem = false;
//		}
//
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			// TODO Auto-generated method stub
//			if (scrollState == SCROLL_STATE_TOUCH_SCROLL && isLastItem) {
//				Map<String, Object> map;
//
//				if (lastItem + 3 < eventMaxCount) {
//					for (int i = lastItem; i < lastItem + 3; i++) {
//						map = new HashMap<String, Object>();
//						map.put(strs[0], "数据加载中");
//						map.put(strs[1], "数据加载中");
//						data.add(map);
//						sa.notifyDataSetChanged();
//						lits.add(new LoadImageTask());
//						lits.get(lits.size() - 1).execute(i, fragmentId, sa,
//								map);
//					}
//
//				} else {
//					for (int i = lastItem; i < eventMaxCount; i++) {
//						map = new HashMap<String, Object>();
//						map.put(strs[0], "数据加载中");
//						map.put(strs[1], "数据加载中");
//						data.add(map);
//						sa.notifyDataSetChanged();
//						lits.add(new LoadImageTask());
//						lits.get(lits.size() - 1).execute(i, fragmentId, sa,
//								map);
//					}
//
//				}
//
//			}
//
//		}
//	}
//	/*
//	 * public void openWebInMainThread(){ StrictMode.setThreadPolicy(new
//	 * StrictMode.ThreadPolicy.Builder() .detectDiskReads() .detectDiskWrites()
//	 * .detectNetwork() // or .detectAll() for all detectable problems
//	 * .penaltyLog() .build()); StrictMode.setVmPolicy(new
//	 * StrictMode.VmPolicy.Builder() .detectLeakedSqlLiteObjects()
//	 * .detectLeakedClosableObjects() .penaltyLog() .penaltyDeath() .build()); }
//	 */
//
//}
