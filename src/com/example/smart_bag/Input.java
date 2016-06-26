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
	private String smsg = "";    //��ʾ�����ݻ���
    static boolean isInterrupt=false;
	
	//��ȡ����
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
					num = inStream.read(buffer);         //��������
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
					smsg=s;   //д����ջ���
					if(inStream.available()==0)
						break;  //��ʱ��û�����ݲ�����������ʾ
				}
				//������ʾ��Ϣ��������ʾˢ��
					//handler.sendMessage(handler.obtainMessage());
					 Message msg=handler.obtainMessage();
	  	               msg.what=Bluetooth.READ_DATA1;//���͸�target����Ϣ�ı�־λ
	  	               msg.obj=smsg; //���͸�target����Ϣ����
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




