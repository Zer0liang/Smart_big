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
	private ConnectivityManager connManager;//�������ӹ���
	private MapView mapView;
	private AMap aMap;
	private EditText latTv,lngTv,location;
	private LocationManager locationManager;
	private GeocodeSearch search;
	private RouteSearch routeSearch;
	private static final double EARTH_RADIUS = 6378137;//����뾶
	private DriveRouteResult driveRouteResult;// �ݳ�ģʽ��ѯ���
	private int drivingMode = RouteSearch.DrivingDefault;// �ݳ�Ĭ��ģʽ
	private WalkRouteResult walkRouteResult;// ����ģʽ��ѯ���
	private int walkMode = RouteSearch.WalkDefault;// ����Ĭ��ģʽ
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
		
		//ÿ�ζ����ļ�������ȡ����
//		try {
//			lng_lat = read("Lng_Lat.txt");
//		} catch (Exception e) {
//			// TODO �Զ����ɵ� catch ��
//			Toast.makeText(Gps_Mybag.this, "��ȡ��γ���ļ��������⣬�뼰ʱ���", 0).show();
//			e.printStackTrace();
//		}
//		if(lng_lat.equals("")){
//			lng_lat="119.535357"+","+"35.44366333";//Ĭ��Ϊ�����ַ
//		}
//		String[] newstr=lng_lat.split(",");
//		String str_lng=newstr[0];
//		String str_lat=newstr[1];	
//		double Lng = Double.parseDouble(str_lng);
//		double Lat= Double.parseDouble(str_lat);
//		double[] JW=new double[2];
//		GpsCorrect.transform(Lat, Lng, JW);
//		Lat_real=JW[0];													//�����׼ȷ�ľ�ƫ���ľ�γ��
//		Lng_real=JW[1];					
	//	Toast.makeText(Gps.this, str_lng+str_lat, 1).show();
		double JW_fir[]=new double[2];
		GpsCorrect.transform(Bluetooth.Lat, Bluetooth.Lng, JW_fir);
		Lat_real=JW_fir[0];
		Lng_real=JW_fir[1];
		connManager= (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);//�����������ӷ���
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//����λ�÷���
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();//��ȡ��������״̬
		mapView = (MapView) findViewById(R.id.map);
		// ����ص�MapView��onCreate()����
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
			// ���ý���������
			search.setOnGeocodeSearchListener(new OnGeocodeSearchListener()  // ��
			{
				@Override
				public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i)//�������
				{
					//Toast.makeText(Gps_Mybag.this, "ִ�����첽��ѯ", 0).show();
					RegeocodeAddress addr = regeocodeResult.getRegeocodeAddress();
				//	Toast.makeText(Gps_Mybag.this, addr.getFormatAddress(), 0).show();
					location.setText( addr.getFormatAddress());
				}
				
				@Override
				public void onGeocodeSearched(GeocodeResult arg0, int arg1) {//����
					// TODO �Զ����ɵķ������
					
				}
			});
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 8, new LocationListener()
					{

						@Override
						public void onLocationChanged(Location location) {
							// TODO �Զ����ɵķ������
							if(Bluetooth.gps_key==false){//
								  updatePosition(location);
							}else{
								double latitude=location.getLatitude();
								double longitude=location.getLongitude();			
								double[] JW=new double[2];
								GpsCorrect.transform(latitude, longitude, JW);
								double lat=JW[0];
								double lon=JW[1];//ȷ����һ��λ��
								
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
							// TODO �Զ����ɵķ������
							
						}

						@Override
						public void onProviderEnabled(String provider) {
							// TODO �Զ����ɵķ������
							
						}

						@Override
						public void onProviderDisabled(String provider) {
							// TODO �Զ����ɵķ������
							
						}
							
					});
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if(networkInfo==null){
				Toast.makeText(Gps_Mybag.this, "��ǰ���������Ӳ�����", Toast.LENGTH_SHORT).show();
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
			//	Toast.makeText(Gps.this, "����˶�λ��ť", Toast.LENGTH_SHORT).show();
				Dingwei();
			}
		});
		
		jiexi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Chaxun();				
			     Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
			     if(loc!=null){
				// ����·�߹滮����ʼ��
				double latitude=loc.getLatitude();
				double longitude=loc.getLongitude();			
				 double[] JW=new double[2];
					GpsCorrect.transform(latitude, longitude, JW);
					double lat=JW[0];
					double lon=JW[1];	
					LatLng pos=new LatLng(lat, lon);					
					// ����һ��MarkerOptions����
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(pos);
					// ����MarkerOptionsʹ���Զ���ͼ��
					markerOptions.title("Yourself");
					markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
					markerOptions.draggable(true);
					if(marker_me!=null){
						marker_me.remove();
					}
					marker_me= aMap.addMarker(markerOptions);
					marker_me.showInfoWindow(); // ����Ĭ����ʾ��Ϣ��
					//	Toast.makeText(Gps.this, lat+","+lon, 0).show();
					LatLonPoint llStart = new LatLonPoint(lat, lon);//��ʼλ��ȷ��
				//	Toast.makeText(Gps_Mybag.this, lat+lon+"", 0).show();
					
					String lat_1=latTv.getText().toString().trim();
					String lng_1=lngTv.getText().toString().trim();
					double latitude_1=Double.parseDouble(lat_1);
					double longitude_1=Double.parseDouble(lng_1);
					double[] JW_1=new double[2];
					GpsCorrect.transform(latitude_1, longitude_1, JW_1);
					latitude_1=JW_1[0];
					longitude_1=JW_1[1];
					// ��ȡĿ�굱ǰ�ľ�γ��
					LatLonPoint llEnd = new LatLonPoint(latitude_1,longitude_1);//				����Ŀ�굱ǰλ
				 ft = new RouteSearch.FromAndTo(llStart, llEnd);
				 create_dialog();
		 
			     }else{
			    	 Toast.makeText(Gps_Mybag.this, "����ش�����GPS...,���ڲ�ѯ,���Ժ�...", 0).show();
			     }
			     
			}
		});
		
		
	}
	
	public void Chaxun() {

		// TODO �Զ����ɵķ������
		String lng = lngTv.getText().toString().trim();
		String lat = latTv.getText().toString().trim();
		if (lng.equals("") || lat.equals(""))
		{
			Toast.makeText(Gps_Mybag.this, "��ȷ�����Ѿ���ȡ����ȷ�ľ�γ��!"
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
			// ���ݾ�γ��ִ���첽��ѯ
			search.getFromLocationAsyn(new RegeocodeQuery(
				new LatLonPoint(lat_double,lng_double)
				, 20 // ����뾶
				, GeocodeSearch.GPS));
		}
	
	}
	public void create_dialog() {
		final String[] items = new String[]{"����","�ݳ�"};  
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("ѡ����з�ʽ");
		builder.setItems(items,new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				switch (which) {  
		        case 0:  
		        	WalkRouteQuery query = new WalkRouteQuery(ft, walkMode);
					routeSearch.calculateWalkRouteAsyn(query);// �첽·���滮����ģʽ��ѯ
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
		// ��ȡ�û�����ľ��ȡ�γ��ֵ
		String lng = lngTv.getText().toString().trim();
		String lat = latTv.getText().toString().trim();
		if (lng.equals("") || lat.equals(""))
		{
			Toast.makeText(Gps_Mybag.this, "��������Ч�ľ��ȡ�γ��!",
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			// ���ø����û�����ĵ�ַ��λ
//				((RadioButton)findViewById(R.id.manual)).setChecked(true);
			double dLng = Double.parseDouble(lng);
			double dLat = Double.parseDouble(lat);
			double[] JW=new double[2];
			GpsCorrect.transform(dLat, dLng, JW);
			dLat=JW[0];
			dLng=JW[1];					
			// ���û�����ľ���γ�ȷ�װ��LatLng
			LatLng pos = new LatLng(dLat, dLng);  // ��
			// ����һ�����þ�γ�ȵ�CameraUpdate
			CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);  // ��
			// ���µ�ͼ����ʾ����
			if(marker!=null){
				marker.remove();
			}
			
			aMap.moveCamera(cu);  // ��
			// ����MarkerOptions����
			MarkerOptions markerOptions = new MarkerOptions();
			// ����MarkerOptions�����λ��
			markerOptions.position(pos);
			// ����MarkerOptions�ı���
			markerOptions.title("��������λ��");
			// ����MarkerOptions��ժ¼��Ϣ
	//		markerOptions.snippet("��λλ��");
			// ����MarkerOptions��ͼ��
			markerOptions.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			markerOptions.draggable(true);
			// ���MarkerOptions��ʵ���Ͼ������Marker��
//			Marker marker = aMap.addMarker(markerOptions);
			marker = aMap.addMarker(markerOptions);								//marker������ط���ֵ��
			
			marker.showInfoWindow(); // ����Ĭ����ʾ��Ϣ��
			// ʹ�ü��Ϸ�װ���ͼ�꣬������ΪMarkerOptions���ö��ͼ��
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
		// ����һ�����þ�γ�ȵ�CameraUpdate
		CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
		// ���µ�ͼ����ʾ����
		aMap.moveCamera(cu);
		// �������Marker�ȸ�����
		//aMap.clear();
		if(marker_me!=null){
			marker_me.remove();
		}
		// ����һ��MarkerOptions����
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(pos);
		markerOptions.title("Yourself");
		// ����MarkerOptionsʹ���Զ���ͼ��
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
		markerOptions.draggable(true);
		// ���MarkerOptions��ʵ���������Marker��
		marker_me= aMap.addMarker(markerOptions);
		marker_me.showInfoWindow(); // ����Ĭ����ʾ��Ϣ��
	}
	// ��ʼ��AMap����
	public void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			// ����һ�����÷Ŵ󼶱��CameraUpdate
			CameraUpdate cu = CameraUpdateFactory.zoomTo(15);
			// ���õ�ͼ��Ĭ�ϷŴ󼶱�
			aMap.moveCamera(cu);
			// ����һ�����ĵ�ͼ��б�ȵ�CameraUpdate
			CameraUpdate tiltUpdate = CameraUpdateFactory.changeTilt(30);
			// �ı��ͼ����б��
			aMap.moveCamera(tiltUpdate);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		// ����ص�MapView��onResume()����
		mapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		// ����ص�MapView��onPause()����
		mapView.onPause();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// ����ص�MapView��onSaveInstanceState()����
		mapView.onSaveInstanceState(outState);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ����ص�MapView��onDestroy()����
		mapView.onDestroy();
	}
	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		// TODO �Զ����ɵķ������
		
	}
	/*
	@Override
	public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
		// TODO �Զ����ɵķ������
		if(driveRouteResult!=null){
		// ��ȡϵͳ�滮��һ��·�ߣ�ʵ��Ӧ���п��ṩ����·�߹��û�ѡ��
		DrivePath drivePath = driveRouteResult.getPaths().get(0);
		// ��ȡ�ù滮·���������Ķ���·��
		List<DriveStep> steps = drivePath.getSteps();
		for(DriveStep step: steps)
		{
			// ��ȡ��ɸ�·�εĶ����
			List<LatLonPoint> points = step.getPolyline();
			List<LatLng> latLngs = new ArrayList<LatLng>();
			for(LatLonPoint point : points)
			{
				latLngs.add(new LatLng(point.getLatitude()
					, point.getLongitude()));
			}
			// ����һ��PolylineOptions���������ͼ��Ӷ��߶Σ�
			PolylineOptions ployOptions = new PolylineOptions()
				// ��Ӷ����
				.addAll(latLngs)
				.color(0xffff0000)
				.width(8);
			aMap.addPolyline(ployOptions);
		}
		}else{
			Toast.makeText(Gps_Mybag.this, "���ڹ滮��·�����Ժ�...", 0).show();
		}
	}*/
	
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		
	//	if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// �����ͼ�ϵ����и�����
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						this, aMap, drivePath, driveRouteResult.getStartPos(),
						driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			} else {
				Toast.makeText(Gps_Mybag.this, "�Բ������ڹ滮��·�����Ժ�...", Toast.LENGTH_SHORT)
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
		// TODO �Զ����ɵķ������
		//Toast.makeText(Gps_Mybag.this, "ִ���˲��й滮����", Toast.LENGTH_SHORT).show();
		
		if (result != null && result.getPaths() != null
				&& result.getPaths().size() > 0) {
		//if(result!=null){
			walkRouteResult = result;
			WalkPath walkPath = walkRouteResult.getPaths().get(0);
			aMap.clear();// �����ͼ�ϵ����и�����
			WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
					aMap, walkPath, walkRouteResult.getStartPos(),
					walkRouteResult.getTargetPos());
			walkRouteOverlay.removeFromMap();
			walkRouteOverlay.addToMap();
			walkRouteOverlay.zoomToSpan();
		} else {
			Toast.makeText(Gps_Mybag.this, "�Բ������ڹ滮��·�����Ժ�...", Toast.LENGTH_SHORT).show();
		}
	}
}




