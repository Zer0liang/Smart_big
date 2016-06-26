package com.example.smart_bag;

import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class anfang extends Activity {
	private OutputStream outStream = null;
	private Bluetooth blu2;
	  private static final String TAG = "THINBTCLIENT";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

		
            super.onCreate(savedInstanceState);                
            setContentView(R.layout.anfang_main);
            
            if(blu2.btSocket==null){
				Toast.makeText(anfang.this, "请确保您的蓝牙已经连接！", Toast.LENGTH_LONG).show();
			}else{
            Button Anfang_open=(Button)findViewById(R.id.anfang_ON);//获取Led打开按钮
            Anfang_open.setOnClickListener(new OnClickListener() {//设置事件监听
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					String message;
    				byte[] msgBuffer;
    				
    				try{
    					// blu.socketconnect();
    					
    					outStream=blu2.btSocket.getOutputStream();}
    				catch(IOException e1){
    					Log.e(TAG, "ON RESUME:  Output stream creation failed.", e1);
    				}
    				message="help_on";
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
            Button Anfang_close=(Button)findViewById(R.id.anfang_OFF);//获取Led关闭按钮
            Anfang_close.setOnClickListener(new OnClickListener() {//为Led按钮设置事件监听
    			
    			
				@Override
    			public void onClick(View v) {
    				// TODO 自动生成的方法存根
    				String message;
    				byte[] msgBuffer;
    				try{
    					outStream=blu2.btSocket.getOutputStream();
    				}catch(IOException e){
    					Log.e(TAG, "ON RESUME:  Output stream creation failed.", e);
    				}
    				message="help_off";
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
}
