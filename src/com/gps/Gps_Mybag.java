package com.gps;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkRouteResult;
import com.example.smart_bag.Bluetooth;
import com.example.smart_bag.R;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Gps_Mybag extends Activity implements OnRouteSearchListener{
	private ConnectivityManager connManager;//网络连接管理
	private MapView mapView;
	private AMap aMap;
	private EditText latTv,lngTv,location;
	private LocationManager locationManager;
	private GeocodeSearch search;
	private RouteSearch routeSearch;
	private static final double EARTH_RADIUS = 6378137;//地球半径
	private DriveRouteResult driveRouteResult;// 驾车模式查询结果
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	private double Lng_real,Lat_real;
	private LocationManager locMgr;
	private String lng_lat;
	private Marker marker;
	private Marker marker_me;
	private RouteSearch.FromAndTo ft;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_mabag);
		
		//每次都从文件里来读取数据
//		try {
//			lng_lat = read("Lng_Lat.txt");
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			Toast.makeText(Gps_Mybag.this, "读取经纬度文件遇到问题，请及时解决", 0).show();
//			e.printStackTrace();
//		}
//		if(lng_lat.equals("")){
//			lng_lat="119.535357"+","+"35.44366333";//默认为这个地址
//		}
//		String[] newstr=lng_lat.split(",");
//		String str_lng=newstr[0];
//		String str_lat=newstr[1];	
//		double Lng = Double.parseDouble(str_lng);
//		double Lat= Double.parseDouble(str_lat);
//		double[] JW=new double[2];
//		GpsCorrect.transform(Lat, Lng, JW);
//		Lat_real=JW[0];													//这才是准确的纠偏过的经纬度
//		Lng_real=JW[1];					
	//	Toast.makeText(Gps.this, str_lng+str_lat, 1).show();
		double JW_fir[]=new double[2];
		GpsCorrect.transform(Bluetooth.Lat, Bluetooth.Lng, JW_fir);
		Lat_real=JW_fir[0];
		Lng_real=JW_fir[1];
		connManager= (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);//开启网络连接服务
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//开启位置服务
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();//获取网络连接状态
		mapView = (MapView) findViewById(R.id.map);
		// 必须回调MapView的onCreate()方法
		mapView.onCreate(savedInstanceState);
		init();
		Button dingwei = (Button) findViewById(R.id.dingwei);
		Button jiexi= (Button) findViewById(R.id.jiexi);
		 latTv = (EditText) findViewById(R.id.lat);
		 lngTv = (EditText) findViewById(R.id.lng);
		 location = (EditText) findViewById(R.id.location);		 
		 lngTv.setText(Bluetooth.Lng+"");
		 latTv.setText(Bluetooth.Lat+"");
		 routeSearch=new RouteSearch(this);
		 routeSearch.setRouteSearchListener(this);
		 search = new GeocodeSearch(this);
			// 设置解析监听器
			search.setOnGeocodeSearchListener(new OnGeocodeSearchListener()  // ①
			{
				@Override
				public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i)//反向解析
				{
					//Toast.makeText(Gps_Mybag.this, "执行了异步查询", 0).show();
					RegeocodeAddress addr = regeocodeResult.getRegeocodeAddress();
				//	Toast.makeText(Gps_Mybag.this, addr.getFormatAddress(), 0).show();
					location.setText( addr.getFormatAddress());
				}
				
				@Override
				public void onGeocodeSearched(GeocodeResult arg0, int arg1) {//解析
					// TODO 自动生成的方法存根
					
				}
			});
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 8, new LocationListener()
					{

						@Override
						public void onLocationChanged(Location location) {
							// TODO 自动生成的方法存根
							if(Bluetooth.gps_key==false){//
								  updatePosition(location);
							}else{
								double latitude=location.getLatitude();
								double longitude=location.getLongitude();			
								double[] JW=new double[2];
								GpsCorrect.transform(latitude, longitude, JW);
								double lat=JW[0];
								double lon=JW[1];//确定第一个位置
								
								String str_lat=latTv.getText().toString();
								String str_lng=lngTv.getText().toString();
								double latitude_1=Double.parseDouble(str_lat);
								double longitude_1=Double.parseDouble(str_lng);
								double []JW1=new double[2];
								GpsCorrect.transform(latitude_1, longitude_1, JW1);
								latitude_1=JW1[0];
								longitude_1=JW1[1];
								double distance=DistanceOfTwoPoints(lat,lon,latitude_1,longitude_1);
								String s1=String.valueOf(distance);
								Toast.makeText(Gps_Mybag.this, s1, 0).show();								
							}
						}

						@Override
						public void onStatusChanged(String provider, int status, Bundle extras) {
							// TODO 自动生成的方法存根
							
						}

						@Override
						public void onProviderEnabled(String provider) {
							// TODO 自动生成的方法存根
							
						}

						@Override
						public void onProviderDisabled(String provider) {
							// TODO 自动生成的方法存根
							
						}
							
					});
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if(networkInfo==null){
				Toast.makeText(Gps_Mybag.this, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
				Bluetooth.setNetworkMethod(Gps_Mybag.this);
			}else{
				Dingwei();
				Chaxun();
			}
			
		dingwei.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			//	Toast.makeText(Gps.this, "点击了定位按钮", Toast.LENGTH_SHORT).show();
				Dingwei();
			}
		});
		
		jiexi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Chaxun();				
			     Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
			     if(loc!=null){
				// 创建路线规划的起始点
				double latitude=loc.getLatitude();
				double longitude=loc.getLongitude();			
				 double[] JW=new double[2];
					GpsCorrect.transform(latitude, longitude, JW);
					double lat=JW[0];
					double lon=JW[1];	
					LatLng pos=new LatLng(lat, lon);					
					// 创建一个MarkerOptions对象
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(pos);
					// 设置MarkerOptions使用自定义图标
					markerOptions.title("Yourself");
					markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
					markerOptions.draggable(true);
					if(marker_me!=null){
						marker_me.remove();
					}
					marker_me= aMap.addMarker(markerOptions);
					marker_me.showInfoWindow(); // 设置默认显示信息窗
					//	Toast.makeText(Gps.this, lat+","+lon, 0).show();
					LatLonPoint llStart = new LatLonPoint(lat, lon);//起始位置确定
				//	Toast.makeText(Gps_Mybag.this, lat+lon+"", 0).show();
					
					String lat_1=latTv.getText().toString().trim();
					String lng_1=lngTv.getText().toString().trim();
					double latitude_1=Double.parseDouble(lat_1);
					double longitude_1=Double.parseDouble(lng_1);
					double[] JW_1=new double[2];
					GpsCorrect.transform(latitude_1, longitude_1, JW_1);
					latitude_1=JW_1[0];
					longitude_1=JW_1[1];
					// 获取目标当前的经纬度
					LatLonPoint llEnd = new LatLonPoint(latitude_1,longitude_1);//				这是目标当前位
				 ft = new RouteSearch.FromAndTo(llStart, llEnd);
				 create_dialog();
		 
			     }else{
			    	 Toast.makeText(Gps_Mybag.this, "请务必打开您的GPS...,正在查询,请稍后...", 0).show();
			     }
			     
			}
		});
		
		
	}
	
	public void Chaxun() {

		// TODO 自动生成的方法存根
		String lng = lngTv.getText().toString().trim();
		String lat = latTv.getText().toString().trim();
		if (lng.equals("") || lat.equals(""))
		{
			Toast.makeText(Gps_Mybag.this, "请确保您已经获取到正确的经纬度!"
				, Toast.LENGTH_LONG)
				.show();
		}
		else
		{
			double lat_double=Double.parseDouble(lat);
			double lng_double=Double.parseDouble(lng);
			double[] JW=new double[2];
			GpsCorrect.transform(lat_double, lng_double, JW);
			lat_double=JW[0];
			lng_double=JW[1];					
			// 根据经纬度执行异步查询
			search.getFromLocationAsyn(new RegeocodeQuery(
				new LatLonPoint(lat_double,lng_double)
				, 20 // 区域半径
				, GeocodeSearch.GPS));
		}
	
	}
	public void create_dialog() {
		final String[] items = new String[]{"步行","驾车"};  
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("选择出行方式");
		builder.setItems(items,new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				switch (which) {  
		        case 0:  
		        	WalkRouteQuery query = new WalkRouteQuery(ft, walkMode);
					routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		            break;  
		        case 1:  
		        	RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(ft, RouteSearch.DrivingDefault, null, null,null);
					routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
		            break;  
		        }  
			}
		});
		builder.create().show();
	}
	public void Dingwei() {
		// 获取用户输入的经度、纬度值
		String lng = lngTv.getText().toString().trim();
		String lat = latTv.getText().toString().trim();
		if (lng.equals("") || lat.equals(""))
		{
			Toast.makeText(Gps_Mybag.this, "请输入有效的经度、纬度!",
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			// 设置根据用户输入的地址定位
//				((RadioButton)findViewById(R.id.manual)).setChecked(true);
			double dLng = Double.parseDouble(lng);
			double dLat = Double.parseDouble(lat);
			double[] JW=new double[2];
			GpsCorrect.transform(dLat, dLng, JW);
			dLat=JW[0];
			dLng=JW[1];					
			// 将用户输入的经、纬度封装成LatLng
			LatLng pos = new LatLng(dLat, dLng);  // ①
			// 创建一个设置经纬度的CameraUpdate
			CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);  // ②
			// 更新地图的显示区域
			if(marker!=null){
				marker.remove();
			}
			
			aMap.moveCamera(cu);  // ③
			// 创建MarkerOptions对象
			MarkerOptions markerOptions = new MarkerOptions();
			// 设置MarkerOptions的添加位置
			markerOptions.position(pos);
			// 设置MarkerOptions的标题
			markerOptions.title("背包所在位置");
			// 设置MarkerOptions的摘录信息
	//		markerOptions.snippet("定位位置");
			// 设置MarkerOptions的图标
			markerOptions.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			markerOptions.draggable(true);
			// 添加MarkerOptions（实际上就是添加Marker）
//			Marker marker = aMap.addMarker(markerOptions);
			marker = aMap.addMarker(markerOptions);								//marker在这个地方赋值。
			
			marker.showInfoWindow(); // 设置默认显示信息窗
			// 使用集合封装多个图标，这样可为MarkerOptions设置多个图标
			ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
			giflist.add(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			giflist.add(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			giflist.add(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		}
	
	}
	private static double rad(double d) {   
        return d * Math.PI / 180.0;   
    } 
	 public static double DistanceOfTwoPoints(double lat1,double lng1,    
             double lat2,double lng2) {   
		 double s=0;
        double radLat1 = rad(lat1);   
        double radLat2 = rad(lat2);   
        double a = radLat1 - radLat2;   
        double b = rad(lng1) - rad(lng2);   
         s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)   
                + Math.cos(radLat1) * Math.cos(radLat2)   
                * Math.pow(Math.sin(b / 2), 2)));   
        s = s * EARTH_RADIUS;   
        s = Math.round(s * 10000) / 10000;   
        return s;   
    }  
	private void updatePosition(Location location)
	{
		double latitude=location.getLatitude();
		double longitude=location.getLongitude();			
		 double[] JW=new double[2];
			GpsCorrect.transform(latitude, longitude, JW);
			double lat=JW[0];
			double lon=JW[1];	
		LatLng pos = new LatLng(lat,lon);
		// 创建一个设置经纬度的CameraUpdate
		CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
		// 更新地图的显示区域
		aMap.moveCamera(cu);
		// 清除所有Marker等覆盖物
		//aMap.clear();
		if(marker_me!=null){
			marker_me.remove();
		}
		// 创建一个MarkerOptions对象
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(pos);
		markerOptions.title("Yourself");
		// 设置MarkerOptions使用自定义图标
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
		markerOptions.draggable(true);
		// 添加MarkerOptions（实际上是添加Marker）
		marker_me= aMap.addMarker(markerOptions);
		marker_me.showInfoWindow(); // 设置默认显示信息窗
	}
	// 初始化AMap对象
	public void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			// 创建一个设置放大级别的CameraUpdate
			CameraUpdate cu = CameraUpdateFactory.zoomTo(15);
			// 设置地图的默认放大级别
			aMap.moveCamera(cu);
			// 创建一个更改地图倾斜度的CameraUpdate
			CameraUpdate tiltUpdate = CameraUpdateFactory.changeTilt(30);
			// 改变地图的倾斜度
			aMap.moveCamera(tiltUpdate);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		// 必须回调MapView的onResume()方法
		mapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		// 必须回调MapView的onPause()方法
		mapView.onPause();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 必须回调MapView的onSaveInstanceState()方法
		mapView.onSaveInstanceState(outState);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 必须回调MapView的onDestroy()方法
		mapView.onDestroy();
	}
	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		// TODO 自动生成的方法存根
		
	}
	/*
	@Override
	public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
		// TODO 自动生成的方法存根
		if(driveRouteResult!=null){
		// 获取系统规划第一条路线（实际应用中可提供多条路线供用户选择）
		DrivePath drivePath = driveRouteResult.getPaths().get(0);
		// 获取该规划路线所包含的多条路段
		List<DriveStep> steps = drivePath.getSteps();
		for(DriveStep step: steps)
		{
			// 获取组成该路段的多个点
			List<LatLonPoint> points = step.getPolyline();
			List<LatLng> latLngs = new ArrayList<LatLng>();
			for(LatLonPoint point : points)
			{
				latLngs.add(new LatLng(point.getLatitude()
					, point.getLongitude()));
			}
			// 创建一个PolylineOptions（用于向地图添加多线段）
			PolylineOptions ployOptions = new PolylineOptions()
				// 添加多个点
				.addAll(latLngs)
				.color(0xffff0000)
				.width(8);
			aMap.addPolyline(ployOptions);
		}
		}else{
			Toast.makeText(Gps_Mybag.this, "正在规划线路，请稍后...", 0).show();
		}
	}*/
	
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		
	//	if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						this, aMap, drivePath, driveRouteResult.getStartPos(),
						driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			} else {
				Toast.makeText(Gps_Mybag.this, "对不起，正在规划线路，请稍后...", Toast.LENGTH_SHORT)
						.show();
			}
//		} 
//	else if (rCode == 27) {
//			Toast.makeText(Gps.this, "net error", Toast.LENGTH_SHORT).show();
//		} else if (rCode == 32) {
//			Toast.makeText(Gps.this, "key error", Toast.LENGTH_SHORT).show();
//		} else {
//			Toast.makeText(Gps.this, "other error", Toast.LENGTH_SHORT).show();
//		}
//		System.out.println("========onDriveRouteSearched()=========");
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		// TODO 自动生成的方法存根
		//Toast.makeText(Gps_Mybag.this, "执行了步行规划操作", Toast.LENGTH_SHORT).show();
		
		if (result != null && result.getPaths() != null
				&& result.getPaths().size() > 0) {
		//if(result!=null){
			walkRouteResult = result;
			WalkPath walkPath = walkRouteResult.getPaths().get(0);
			aMap.clear();// 清理地图上的所有覆盖物
			WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
					aMap, walkPath, walkRouteResult.getStartPos(),
					walkRouteResult.getTargetPos());
			walkRouteOverlay.removeFromMap();
			walkRouteOverlay.addToMap();
			walkRouteOverlay.zoomToSpan();
		} else {
			Toast.makeText(Gps_Mybag.this, "对不起，正在规划线路，请稍后...", Toast.LENGTH_SHORT).show();
		}
	}
}




