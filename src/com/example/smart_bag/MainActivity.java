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

	/* 取得默认的蓝牙适配器 */
	private BluetoothAdapter	mbluetooth				= BluetoothAdapter.getDefaultAdapter();

	/* 请求打开蓝牙 */
	private static final int	REQUEST_ENABLE			= 0x1;
	/* 请求能够被搜索 */
	private static final int	REQUEST_DISCOVERABLE	= 0x2;
	//来自lanya
	  private final String help="help";
    BluetoothSocket btSocket = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


   // private static String address = "07:12:04:12:31:15"; // <==要连接的蓝牙设备MAC地址
   // private static String address = "20:15:07:14:20:12"; // <==要连接的蓝牙设备MAC地址
       private static String address = "FC:58:FA:45:7E:9B"; // <==要连接的蓝牙设备MAC地址

    private static Bluetooth blu;
    private static Input change;//change对象调用setstop方法对线程进行中断和开始
    
    public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;
    private static short rssi=0;
    private ToggleButton anquan,location;//anfang:help功能开启，location:距离检测功能开启
    private OutputStream outStream = null;
    private static final String TAG = "THINBTCLIENT";
    private static String[] names;
    private static String[] numbers;
    private LocationManager locationManager;
    //String[] names=blu.names.split(",");
    //使用handler接受其他线程的消息
    private Handler handler=new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		switch (msg.what){
    		case Bluetooth.READ_DATA1:{
									    			String str=(String)msg.obj;
									    			int len1=str.length();
									    			String str_need=str.substring(0, len1-1);		
									    			if(str_need.equals(help)&&(blu.bRun==true)){
									    							//			help();//执行help方法   
									    										call("10086");
									    														} 
										    		if(str_need.equals("OK1")){//OK1传过来的标识符
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
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//开启位置服务
		
		 //对拨打电话选项做出时间监听
        ImageView Call=(ImageView)findViewById(R.id.call);
        Call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent2 = new Intent();// 创建Intent对象,intent2是打电话选项的监听
                intent2.setAction(Intent.ACTION_DIAL);// 为Intent设置动作
               // intent2.setData(Uri.parse("10086"));
                startActivity(intent2);// 将Intent传递给Activity
			}
		});
        
        ImageView Shoudian=(ImageView)findViewById(R.id.shoudian);
        Shoudian.setOnClickListener(new OnClickListener() {										//手电单击事件监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent3=new Intent();
				intent3.setClass(MainActivity.this,shoudian.class);
				startActivity(intent3);
			}
		});
        ImageView Yinyue=(ImageView)findViewById(R.id.yinyue);
        Yinyue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(MainActivity.this,Gps_Mybag.class);
				startActivity(intent);
			}
		});
        ImageView Anfang=(ImageView)findViewById(R.id.anquan);
        Anfang.setOnClickListener(new OnClickListener() {											//安防功能单击事件监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent4=new Intent(MainActivity.this,anfang.class);
				startActivity(intent4);
			}
		});
        
        anquan=(ToggleButton)findViewById(R.id.anquan_button);
        anquan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
						String message;
	    				byte[] msgBuffer;  
	    				
	    				
				if(anquan.isChecked()){
					if(blu.btSocket==null){
						Toast.makeText(MainActivity.this, "请确保您的蓝牙已经连接！", Toast.LENGTH_SHORT).show();
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
	    				blu.bRun=false;//让blu中的brun标识符变化
					}
					}else{
						if(blu.btSocket==null){
							Toast.makeText(MainActivity.this, "请确保您的蓝牙已经连接！", Toast.LENGTH_SHORT).show();
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
		    				blu.bRun=true;//让blu中的brun标识符变化		    				
						}						
					}
}			
		});
        location=(ToggleButton)findViewById(R.id.suo_button);
        location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(location.isChecked()){
					 Bluetooth.gps_key=false;
				}else{
					Location loc =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
					 if(loc!=null){
						 Bluetooth.gps_key=true;//位置距离检测开启
						 Toast.makeText(MainActivity.this, "位置距离检测开启", 0).show();
					 }else{
						 Toast.makeText(MainActivity.this, "请确保您的网络和GPS已经打开,请稍后", 0).show();
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
			Intent select_people=new Intent(MainActivity.this ,Select.class);//select_people选择通讯录里的联系人
			startActivity(select_people);
		}
		if(id==R.id.Dakai){//选项菜单中蓝牙打开
			 if (mbluetooth == null) { 
	            	Toast.makeText(this, "该设备不支持蓝牙！", Toast.LENGTH_LONG).show();
	                    finish();
	            }
			Bluetooth_enabled();
		}
		
		
		else if(id==R.id.Duankai){//选项菜单中蓝牙断开
			//BluetoothDisabed();
			 try {
	    		 if(btSocket!=null)
				{
	    			 btSocket.close();
				     btSocket=null;
				     blu.btSocket=null;
				}else{
					Toast.makeText(MainActivity.this,"蓝牙未连接",Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				Toast.makeText(MainActivity.this,"关闭蓝牙遇到错误",Toast.LENGTH_SHORT).show();
			}
		}
		
		
		else if(id==R.id.Lianjie){//选项菜单中蓝牙进行对应连接
			if (!mbluetooth.isEnabled()) {
              Intent enable=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      		startActivityForResult(enable, REQUEST_ENABLE);
      }
			if(btSocket!=null){
    	  Toast.makeText(this, "正在通信，无需重复连接！", Toast.LENGTH_LONG).show();
      }else{
			Toast.makeText(this, "正在尝试连接智能背包，请稍后····", Toast.LENGTH_LONG).show();
            BluetoothDevice device = mbluetooth.getRemoteDevice(address);
           // mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
            try {
            	
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                blu.btSocket=btSocket;

             } catch (IOException e) {

                  DisplayToast("套接字创建失败！");
             }
             //DisplayToast("成功连接智能背包！可以开始操控了~~~");
             mbluetooth.cancelDiscovery();
             try {
                     btSocket.connect();                  
                     DisplayToast("连接成功建立，数据连接打开！");
                     
                     new Input(blu.btSocket, handler).start();             		//开启线程	

             		} catch (IOException e) {
                     try {
                     		btSocket.close();
                     		btSocket=null;
                     		blu.btSocket=null;
                     		DisplayToast("连接失败，请检查硬件蓝牙是否连接！");
                     		} catch (IOException e2) {
                     			DisplayToast("连接没有建立，无法关闭套接字！");
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
	
	/* 开启蓝牙 */
	public void Bluetooth_enabled()
	{
		// 用户请求打开蓝牙
		//Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		//startActivityForResult(enabler, REQUEST_ENABLE);
		//打开蓝牙
		mbluetooth.enable();
	}
	
	/* 关闭蓝牙 */
	public void BluetoothDisabed()
	{

		mbluetooth.disable();
	}
	
	/* 使设备能够被搜索 */
	public void BluetoothDiscoverable()	{

		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(enabler, REQUEST_DISCOVERABLE);
	}
	
	
	 public  void DisplayToast(String str)															//声明DisplayToast方法
	    {
	    	Toast toast=Toast.makeText(this, str, Toast.LENGTH_LONG);
	    	toast.setGravity(Gravity.TOP, 0, 220);
	    	toast.show();
	    	
	    }
	 //help方法执行拨打固定号码事件
	 public void help() {
		 try {
			blu.number=read("Mytext.txt");//警报人电话号码
			blu.numbers=read("number.txt");//常用联系人电话号码
		} catch (Exception e) {
			
			DisplayToast("help读取出现错误");
		}
		 String[] numbers=blu.numbers.split(",");
		 sendmess(blu.number);//发送短信给blu.number
		 sendmess(numbers[0]);
		 sendmess(numbers[1]);
		 Intent intent1=new Intent(Intent.ACTION_CALL);//Inten1为拨打报警电话的Activity
			intent1.setData(Uri.parse("tel:"+blu.number));
			startActivity(intent1);
}
	 
	 public void sendmess(String number) {//number为要发送信息的人
			SmsManager sManager;
			// 获取SmsManager
					sManager = SmsManager.getDefault();
					// 发送短信
					String help_content="Help!我的位置：\n119.535357,35.44366333";
					sManager.sendTextMessage(number,null, help_content, null, null);
					// 提示短信发送完成
					Toast.makeText(MainActivity.this, "短信发送完成", 0).show();
		}
	 
	 public void call_names(String str) {
	    	
	    	try {
	    		blu.names=read("name.txt");
				blu.numbers=read("number.txt");
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	    	if(blu.names==null){
	    		DisplayToast("常用联系人为空");
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
	//read读取Mytext.txt文件中的内容
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
//	    	// 这里处理逻辑代码，大家注意：该方法仅适用于2.0或更新版的sdk
//	    	moveTaskToBack(isTaskRoot());
//	    	return;
//	    	}
     protected void dialog() {

             AlertDialog.Builder builder = new Builder(MainActivity.this);
             builder.setMessage("确定退出吗？");
             builder.setTitle("退出提示");
             builder.setPositiveButton("后台运行",
                             new android.content.DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                    	 moveTaskToBack(true); //返回并不彻底
                                 //   	 dialog.dismiss();                                             
                                     }
                             });
             builder.setNegativeButton("安全退出",
                             new android.content.DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                             //dialog.dismiss();
                                            	 	finish();
                                     	}
                             });
             builder.create().show();
     }
    
     public void call(String number) {//拨打电话方法
    	 Intent intent_call=new Intent(Intent.ACTION_CALL);//Inten1为拨打报警电话的Activity
			intent_call.setData(Uri.parse("tel:"+number));
			startActivity(intent_call);
	}
		 @Override
	     protected void onDestroy(){ 
		//	 Toast.makeText(MainActivity.this,"执行了onDestroy方法",Toast.LENGTH_SHORT).show();
			 if(btSocket!=null){
				    	 try {	    		 							
				    			 btSocket.close();
							     btSocket=null;
							     blu.btSocket=null;							
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							Toast.makeText(MainActivity.this,"程序退出遇到了错误",Toast.LENGTH_SHORT).show();
						}
			 }
	    	super.onDestroy();
	    }
}

