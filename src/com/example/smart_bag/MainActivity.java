package com.example.smart_bag;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import com.contact.Changyong;
import com.contact.Kouling;
import com.contact.Select;
import com.gps.Gps_Mybag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	/* ȡ��Ĭ�ϵ����������� */
	private BluetoothAdapter	mbluetooth				= BluetoothAdapter.getDefaultAdapter();

	/* ��������� */
	private static final int	REQUEST_ENABLE			= 0x1;
	/* �����ܹ������� */
	private static final int	REQUEST_DISCOVERABLE	= 0x2;
	//����lanya
	  private final String help="help";
    BluetoothSocket btSocket = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


   // private static String address = "07:12:04:12:31:15"; // <==Ҫ���ӵ������豸MAC��ַ
   // private static String address = "20:15:07:14:20:12"; // <==Ҫ���ӵ������豸MAC��ַ
       private static String address = "FC:58:FA:45:7E:9B"; // <==Ҫ���ӵ������豸MAC��ַ

    private static Bluetooth blu;
    private static Input change;//change�������setstop�������߳̽����жϺͿ�ʼ
    
    public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;
    private static short rssi=0;
    private ToggleButton anquan,location;//anfang:help���ܿ�����location:�����⹦�ܿ���
    private OutputStream outStream = null;
    private static final String TAG = "THINBTCLIENT";
    private static String[] names;
    private static String[] numbers;
    private LocationManager locationManager;
    //String[] names=blu.names.split(",");
    //ʹ��handler���������̵߳���Ϣ
    private Handler handler=new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		switch (msg.what){
    		case Bluetooth.READ_DATA1:{
									    			String str=(String)msg.obj;
									    			int len1=str.length();
									    			String str_need=str.substring(0, len1-1);		
									    			if(str_need.equals(help)&&(blu.bRun==true)){
									    							//			help();//ִ��help����   
									    										call("10086");
									    														} 
										    		if(str_need.equals("OK1")){//OK1�������ı�ʶ��
										    			blu.sucess=true;
										    		}					
										    		else if((str_need.equals("CALL_1"))||(str_need.equals("CALL_2"))||(str_need.equals("CALL_3"))||(str_need.equals("CALL_4"))||(str_need.equals("CALL_5"))){
										    		//	Toast.makeText(MainActivity.this, str_need, Toast.LENGTH_SHORT).show();
										    			call_names(str_need);
										    		}
									    							}
											      	  break;
    											}
    		super.handleMessage(msg);
    	}
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//����λ�÷���
		
		 //�Բ���绰ѡ������ʱ�����
        ImageView Call=(ImageView)findViewById(R.id.call);
        Call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent2 = new Intent();// ����Intent����,intent2�Ǵ�绰ѡ��ļ���
                intent2.setAction(Intent.ACTION_DIAL);// ΪIntent���ö���
               // intent2.setData(Uri.parse("10086"));
                startActivity(intent2);// ��Intent���ݸ�Activity
			}
		});
        
        ImageView Shoudian=(ImageView)findViewById(R.id.shoudian);
        Shoudian.setOnClickListener(new OnClickListener() {										//�ֵ絥���¼�����
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent3=new Intent();
				intent3.setClass(MainActivity.this,shoudian.class);
				startActivity(intent3);
			}
		});
        ImageView Yinyue=(ImageView)findViewById(R.id.yinyue);
        Yinyue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent=new Intent(MainActivity.this,Gps_Mybag.class);
				startActivity(intent);
			}
		});
        ImageView Anfang=(ImageView)findViewById(R.id.anquan);
        Anfang.setOnClickListener(new OnClickListener() {											//�������ܵ����¼�����
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent4=new Intent(MainActivity.this,anfang.class);
				startActivity(intent4);
			}
		});
        
        anquan=(ToggleButton)findViewById(R.id.anquan_button);
        anquan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
						String message;
	    				byte[] msgBuffer;  
	    				
	    				
				if(anquan.isChecked()){
					if(blu.btSocket==null){
						Toast.makeText(MainActivity.this, "��ȷ�����������Ѿ����ӣ�", Toast.LENGTH_SHORT).show();
					}else{
						try{
	    					// blu.socketconnect();
	    					
	    					outStream=blu.btSocket.getOutputStream();}
	    				catch(IOException e1){
	    					Log.e(TAG, "ON RESUME:  Output stream creation failed.", e1);
	    				}						
	    				message="help_off";
	    				msgBuffer=message.getBytes();
	    				try{
	    					outStream.write(msgBuffer);
	    				}catch(IOException e1){
	    					Log.e(TAG, "ON RESUME:  Exception during write.", e1);
	    				}
	    				blu.bRun=false;//��blu�е�brun��ʶ���仯
					}
					}else{
						if(blu.btSocket==null){
							Toast.makeText(MainActivity.this, "��ȷ�����������Ѿ����ӣ�", Toast.LENGTH_SHORT).show();
						}else{
		    				
		    				try{
		    					// blu.socketconnect();
		    					
		    					outStream=blu.btSocket.getOutputStream();}
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
		    				blu.bRun=true;//��blu�е�brun��ʶ���仯		    				
						}						
					}
}			
		});
        location=(ToggleButton)findViewById(R.id.suo_button);
        location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				if(location.isChecked()){
					 Bluetooth.gps_key=false;
				}else{
					Location loc =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
					 if(loc!=null){
						 Bluetooth.gps_key=true;//λ�þ����⿪��
						 Toast.makeText(MainActivity.this, "λ�þ����⿪��", 0).show();
					 }else{
						 Toast.makeText(MainActivity.this, "��ȷ�����������GPS�Ѿ���,���Ժ�", 0).show();
					 }	
					}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		MenuInflater inflater=new MenuInflater(this);
		inflater.inflate(R.menu.main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent select_people=new Intent(MainActivity.this ,Select.class);//select_peopleѡ��ͨѶ¼�����ϵ��
			startActivity(select_people);
		}
		if(id==R.id.Dakai){//ѡ��˵���������
			 if (mbluetooth == null) { 
	            	Toast.makeText(this, "���豸��֧��������", Toast.LENGTH_LONG).show();
	                    finish();
	            }
			Bluetooth_enabled();
		}
		
		
		else if(id==R.id.Duankai){//ѡ��˵��������Ͽ�
			//BluetoothDisabed();
			 try {
	    		 if(btSocket!=null)
				{
	    			 btSocket.close();
				     btSocket=null;
				     blu.btSocket=null;
				}else{
					Toast.makeText(MainActivity.this,"����δ����",Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				Toast.makeText(MainActivity.this,"�ر�������������",Toast.LENGTH_SHORT).show();
			}
		}
		
		
		else if(id==R.id.Lianjie){//ѡ��˵����������ж�Ӧ����
			if (!mbluetooth.isEnabled()) {
              Intent enable=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      		startActivityForResult(enable, REQUEST_ENABLE);
      }
			if(btSocket!=null){
    	  Toast.makeText(this, "����ͨ�ţ������ظ����ӣ�", Toast.LENGTH_LONG).show();
      }else{
			Toast.makeText(this, "���ڳ����������ܱ��������Ժ󡤡�����", Toast.LENGTH_LONG).show();
            BluetoothDevice device = mbluetooth.getRemoteDevice(address);
           // mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
            try {
            	
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                blu.btSocket=btSocket;

             } catch (IOException e) {

                  DisplayToast("�׽��ִ���ʧ�ܣ�");
             }
             //DisplayToast("�ɹ��������ܱ��������Կ�ʼ�ٿ���~~~");
             mbluetooth.cancelDiscovery();
             try {
                     btSocket.connect();                  
                     DisplayToast("���ӳɹ��������������Ӵ򿪣�");
                     
                     new Input(blu.btSocket, handler).start();             		//�����߳�	

             		} catch (IOException e) {
                     try {
                     		btSocket.close();
                     		btSocket=null;
                     		blu.btSocket=null;
                     		DisplayToast("����ʧ�ܣ�����Ӳ�������Ƿ����ӣ�");
                     		} catch (IOException e2) {
                     			DisplayToast("����û�н������޷��ر��׽��֣�");
                     		}

             }

		}
	}
		else if (id==R.id.shebeikejian) {
			BluetoothDiscoverable();
		}
		
		else if (id==R.id.lianxiren) {
			Intent intent=new Intent(MainActivity.this,Changyong.class);
			startActivity(intent);
		}
		else if(id==R.id.kouling){
			Intent intent=new Intent(MainActivity.this,Kouling.class);
			startActivity(intent);
		}
		else if(id==R.id.chushihua){
//			Intent intent=new Intent();
//            rssi =intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
//            Toast.makeText(MainActivity.this, rssi+"", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* �������� */
	public void Bluetooth_enabled()
	{
		// �û����������
		//Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		//startActivityForResult(enabler, REQUEST_ENABLE);
		//������
		mbluetooth.enable();
	}
	
	/* �ر����� */
	public void BluetoothDisabed()
	{

		mbluetooth.disable();
	}
	
	/* ʹ�豸�ܹ������� */
	public void BluetoothDiscoverable()	{

		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(enabler, REQUEST_DISCOVERABLE);
	}
	
	
	 public  void DisplayToast(String str)															//����DisplayToast����
	    {
	    	Toast toast=Toast.makeText(this, str, Toast.LENGTH_LONG);
	    	toast.setGravity(Gravity.TOP, 0, 220);
	    	toast.show();
	    	
	    }
	 //help����ִ�в���̶������¼�
	 public void help() {
		 try {
			blu.number=read("Mytext.txt");//�����˵绰����
			blu.numbers=read("number.txt");//������ϵ�˵绰����
		} catch (Exception e) {
			
			DisplayToast("help��ȡ���ִ���");
		}
		 String[] numbers=blu.numbers.split(",");
		 sendmess(blu.number);//���Ͷ��Ÿ�blu.number
		 sendmess(numbers[0]);
		 sendmess(numbers[1]);
		 Intent intent1=new Intent(Intent.ACTION_CALL);//Inten1Ϊ���򱨾��绰��Activity
			intent1.setData(Uri.parse("tel:"+blu.number));
			startActivity(intent1);
}
	 
	 public void sendmess(String number) {//numberΪҪ������Ϣ����
			SmsManager sManager;
			// ��ȡSmsManager
					sManager = SmsManager.getDefault();
					// ���Ͷ���
					String help_content="Help!�ҵ�λ�ã�\n119.535357,35.44366333";
					sManager.sendTextMessage(number,null, help_content, null, null);
					// ��ʾ���ŷ������
					Toast.makeText(MainActivity.this, "���ŷ������", 0).show();
		}
	 
	 public void call_names(String str) {
	    	
	    	try {
	    		blu.names=read("name.txt");
				blu.numbers=read("number.txt");
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
	    	if(blu.names==null){
	    		DisplayToast("������ϵ��Ϊ��");
	    	}else{
	    		String[] names=blu.names.split(",");
	    		String[] numbers=blu.numbers.split(",");
	    		if(str.equals("CALL_1")){
	    			call(numbers[0]);
	    		}
	    		if(str.equals("CALL_2")){
	    			call(numbers[1]);
	    		}
	    		if(str.equals("CALL_3")){
	    			call(numbers[2]);
	    		}
	    		if(str.equals("CALL_4")){
	    			call(numbers[3]);
	    		}
	    		if(str.equals("CALL_5")){
	    			call(numbers[4]);
	    		}
	    	}
			
			
		}
	//read��ȡMytext.txt�ļ��е�����
		public String read(String file)throws Exception {
			FileInputStream fileInputStream=openFileInput(file);
			int len1=fileInputStream.available();
			byte[] buffer=new byte[len1];
			fileInputStream.read(buffer);
			fileInputStream.close();		
			return new String(buffer);		
		}
	 
	 @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
             // TODO Auto-generated method stub
             if (keyCode == KeyEvent.KEYCODE_BACK ) {
                     dialog();                  
                return true;                                         
             }
             return super.onKeyDown(keyCode, event);
     }
//		@Override
//	    public void onBackPressed() {
//	    	// ���ﴦ���߼����룬���ע�⣺�÷�����������2.0����°��sdk
//	    	moveTaskToBack(isTaskRoot());
//	    	return;
//	    	}
     protected void dialog() {

             AlertDialog.Builder builder = new Builder(MainActivity.this);
             builder.setMessage("ȷ���˳���");
             builder.setTitle("�˳���ʾ");
             builder.setPositiveButton("��̨����",
                             new android.content.DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                    	 moveTaskToBack(true); //���ز�������
                                 //   	 dialog.dismiss();                                             
                                     }
                             });
             builder.setNegativeButton("��ȫ�˳�",
                             new android.content.DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                             //dialog.dismiss();
                                            	 	finish();
                                     	}
                             });
             builder.create().show();
     }
    
     public void call(String number) {//����绰����
    	 Intent intent_call=new Intent(Intent.ACTION_CALL);//Inten1Ϊ���򱨾��绰��Activity
			intent_call.setData(Uri.parse("tel:"+number));
			startActivity(intent_call);
	}
		 @Override
	     protected void onDestroy(){ 
		//	 Toast.makeText(MainActivity.this,"ִ����onDestroy����",Toast.LENGTH_SHORT).show();
			 if(btSocket!=null){
				    	 try {	    		 							
				    			 btSocket.close();
							     btSocket=null;
							     blu.btSocket=null;							
						} catch (IOException e) {
							// TODO �Զ����ɵ� catch ��
							Toast.makeText(MainActivity.this,"�����˳������˴���",Toast.LENGTH_SHORT).show();
						}
			 }
	    	super.onDestroy();
	    }
}

