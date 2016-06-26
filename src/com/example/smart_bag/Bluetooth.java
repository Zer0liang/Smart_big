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
	 public static boolean sucess=false;//Changyong.java�еı�־λ
	 public static char a='6';//Changyong.java�е�˳��
	 public static char b='0';//Kouling.java�е�˳��
	 public static String number="10086";
	 static boolean bRun = true;//����ʶ���Ƿ����������ܵı�ʶ��
	 public static boolean gps_key=false;
	 public static String names=null;
	 public static String numbers=null;
	 public static double Lat=35.44366333;
	 public static double Lng=119.535357;
	 
	 public static void setNetworkMethod(final Context context){
	        //��ʾ�Ի���
	        AlertDialog.Builder builder=new Builder(context);
	        builder.setTitle("����������ʾ").setMessage("�������Ӳ�����,�Ƿ��������?").setPositiveButton("����", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                Intent intent=null;
	                //�ж��ֻ�ϵͳ�İ汾  ��API����10 ����3.0�����ϰ汾 
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
	        }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	            
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                dialog.dismiss();
	            }
	        }).show();
	    }
}
	
	