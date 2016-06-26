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
				Toast.makeText(anfang.this, "��ȷ�����������Ѿ����ӣ�", Toast.LENGTH_LONG).show();
			}else{
            Button Anfang_open=(Button)findViewById(R.id.anfang_ON);//��ȡLed�򿪰�ť
            Anfang_open.setOnClickListener(new OnClickListener() {//�����¼�����
				
				@Override
				public void onClick(View v) {
					// TODO �Զ����ɵķ������
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
            
            //led�ر�
            Button Anfang_close=(Button)findViewById(R.id.anfang_OFF);//��ȡLed�رհ�ť
            Anfang_close.setOnClickListener(new OnClickListener() {//ΪLed��ť�����¼�����
    			
    			
				@Override
    			public void onClick(View v) {
    				// TODO �Զ����ɵķ������
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
            Toast toast= Toast.makeText(this, "�ɹ���",Toast.LENGTH_LONG );
      	  toast.setGravity(Gravity.CENTER, 0, 0);
      	  toast.show();}*/
    		
}	
	}
}
