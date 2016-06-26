package com.example.smart_bag;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


public class Bluetooth {
	 public static BluetoothSocket btSocket = null;
	 public static final int READ_DATA1=1;
	 public static boolean sucess=false;//Changyong.java中的标志位
	 public static char a='6';//Changyong.java中的顺序
	 public static char b='0';//Kouling.java中的顺序
	 public static String number="10086";
	 static boolean bRun = true;//用来识别是否开启安防功能的标识符
	 public static boolean gps_key=false;
	 public static String names=null;
	 public static String numbers=null;
	 public static double Lat=35.44366333;
	 public static double Lng=119.535357;
	 
	 public static void setNetworkMethod(final Context context){
	        //提示对话框
	        AlertDialog.Builder builder=new Builder(context);
	        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                Intent intent=null;
	                //判断手机系统的版本  即API大于10 就是3.0或以上版本 
	                if(android.os.Build.VERSION.SDK_INT>10){
	                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	                }else{
	                    intent = new Intent();
	                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
	                    intent.setComponent(component);
	                    intent.setAction("android.intent.action.VIEW");
	                }
	                context.startActivity(intent);
	            }
	        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                dialog.dismiss();
	            }
	        }).show();
	    }
}
	
	