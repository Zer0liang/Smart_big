package com.receiver;

import java.io.IOException;
import java.io.OutputStream;

import com.example.smart_bag.Bluetooth;
import com.example.smart_bag.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {

	private static Bluetooth blu;
	private OutputStream outStream = null;
	private static String message;
	private static byte[] msgBuffer;
	public final static String TAG = "MyPhone";
	@Override
	public void onReceive(Context context, Intent intent) {
	String action = intent.getAction();
	//Log.i(TAG, "[Broadcast]"+action);

	//呼入电话
	if(action.equals(MainActivity.B_PHONE_STATE)){
	doReceivePhone(context,intent);
	}
	}
	
	public void doReceivePhone(Context context, Intent intent) {
		TelephonyManager telephony = (TelephonyManager)context.getSystemService(
		Context.TELEPHONY_SERVICE);
		int state = telephony.getCallState();
		if(state==TelephonyManager.CALL_STATE_IDLE){
			
			try{
				outStream=blu.btSocket.getOutputStream();
				}
				catch(IOException e1){
				Log.e(TAG, "ON RESUME:  Output stream creation failed.", e1);
				}						
				message="call_end";
				msgBuffer=message.getBytes();
				try{
				outStream.write(msgBuffer);
				}catch(IOException e1){
				Log.e(TAG, "ON RESUME:  Exception during write.", e1);
		}
	//		Toast.makeText(context, "电话挂断", Toast.LENGTH_SHORT).show();
		}
		
	}
}
