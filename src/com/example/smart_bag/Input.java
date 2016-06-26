package com.example.smart_bag;

import java.io.IOException;
import java.io.InputStream;

//import com.example.BluetoothChat.BluetoothChatService.ConnectedThread;


import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class Input extends Thread {
	private Handler handler;
	private InputStream inStream;
	public static boolean bRun = true;
	public  String s;
	private String smsg = "";    //显示用数据缓存
    static boolean isInterrupt=false;
	
	//读取数据
	public Input  (BluetoothSocket btSocket,Handler handler) { 
		
		this.handler=handler;
		try {
		      inStream =  btSocket.getInputStream();
			 
		} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		} 	
	}
	
	
	public void run(){
		int num = 0;
		byte[] buffer = new byte[1024];
		byte[] buffer_new = new byte[1024];
		int i = 0;
		int n = 0;
		while(true){
			try{
				while(inStream.available()==0){
					while(bRun == false){}
				}
				while(true){
					num = inStream.read(buffer);         //读入数据
					n=0;
					
					String s0 = new String(buffer,0,num);
					for(i=0;i<num;i++){
						if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
							buffer_new[n] = 0x0a;
							i++;
						}else{
							buffer_new[n] = buffer[i];
						}
						n++;
					}
					String s = new String(buffer_new,0,n);
					smsg=s;   //写入接收缓存
					if(inStream.available()==0)
						break;  //短时间没有数据才跳出进行显示
				}
				//发送显示消息，进行显示刷新
					//handler.sendMessage(handler.obtainMessage());
					 Message msg=handler.obtainMessage();
	  	               msg.what=Bluetooth.READ_DATA1;//发送给target的消息的标志位
	  	               msg.obj=smsg; //发送给target的消息的内
	  	               msg.sendToTarget();
	    		}catch(IOException e){

		             try {
		  inStream.close();
		 } catch (IOException e1) {
		  // TODO Auto-generated catch block
		  e1.printStackTrace();
		 }
		                break;		            
	    		}
		}
	    
		}
		
	}




