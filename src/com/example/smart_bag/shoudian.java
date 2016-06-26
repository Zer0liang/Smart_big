package com.example.smart_bag;

import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class shoudian extends Activity {

    private OutputStream outStream = null;
    private static final String TAG = "THINBTCLIENT";
    private static Bluetooth blu1;
   static int len2;
    //Button Led_close=(Button)findViewById(R.id.led_close);//获取Led关闭按钮
   /*
    //使用handler接受其他线程的消息
    private Handler handler=new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		switch (msg.what){
    		case Bluetooth.READ_DATA:{
    		//	byte[] readBuf = (byte[]) msg.obj;
    			String str=(String)msg.obj;
    			int len1=str.length();
    			String str_need=str.substring(0, len1-1);
//    			if(str_need.equals("help")){
//    				Toast toast= Toast.makeText(shoudian.this,str,Toast.LENGTH_SHORT ); 
//    				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); 
//    				toast.show();
//    			}
    			
   			if(str_need.equals(help)){
    			help();//执行help方法    				
    			}   			
    		}
		      	  break;
    		}
    		super.handleMessage(msg);
    	}
    };*/
	@Override
    public void onCreate(Bundle savedInstanceState) {

		
            super.onCreate(savedInstanceState);                
            setContentView(R.layout.shoudian_main);
            if(blu1.btSocket==null){
				Toast.makeText(shoudian.this, "请确保您的蓝牙已经连接！", Toast.LENGTH_LONG).show();
			}else{
            
//           new Input(blu1.btSocket,handler).start();
   
          //led开启
            Button Led_open=(Button)findViewById(R.id.led_open);//获取Led打开按钮
            Led_open.setOnClickListener(new OnClickListener() {//设置事件监听
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					String message;
    				byte[] msgBuffer;
    				
    				try{
    					// blu.socketconnect();
    					
    					outStream=blu1.btSocket.getOutputStream();}
    				catch(IOException e1){
    					Log.e(TAG, "ON RESUME:  Output stream creation failed.", e1);
    				}
    				message="LED_ONx";
    				msgBuffer=message.getBytes();
    				try{
    					outStream.write(msgBuffer);
    				}catch(IOException e1){
    					Log.e(TAG, "ON RESUME:  Exception during write.", e1);
    				}
				}
    				}
			);
            
            //led关闭
            Button Led_close=(Button)findViewById(R.id.led_close);//获取Led关闭按钮
            Led_close.setOnClickListener(new OnClickListener() {//为Led按钮设置事件监听
    			
    			
				@Override
    			public void onClick(View v) {
    				// TODO 自动生成的方法存根
    				String message;
    				byte[] msgBuffer;
    				try{
    					outStream=blu1.btSocket.getOutputStream();
    				}catch(IOException e){
    					Log.e(TAG, "ON RESUME:  Output stream creation failed.", e);
    				}
    				message="LED_OFFx";
    				msgBuffer=message.getBytes();
    				try{
    					outStream.write(msgBuffer);
    					
    				}catch(IOException e){
    					Log.e(TAG, "ON RESUME:  Exception during write.", e);
    				}
    				
    			}
    		}); 
            /*while(true){
            Toast toast= Toast.makeText(this, "成功了",Toast.LENGTH_LONG );
      	  toast.setGravity(Gravity.CENTER, 0, 0);
      	  toast.show();}*/
    		
}	
}
	 /*public void help() {
		 Intent intent1=new Intent(Intent.ACTION_CALL);//Inten1为拨打报警电话的Activity
			intent1.setData(Uri.parse("tel:10086"));
			try {
		         startActivity(intent1);
		         finish();
		         Log.i("Finished making a call...", "");
		      } catch (android.content.ActivityNotFoundException ex) {
		         Toast.makeText(this, 
		         "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
	}
}*/
}

