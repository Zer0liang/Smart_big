package com.receiver;

import com.example.smart_bag.R;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Rssi extends Activity{
	private static short rssi=0;
	@Override
	 public void onCreate(Bundle savedInstanceState) {

		
        super.onCreate(savedInstanceState);                
        setContentView(R.layout.rssi);
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);

		this.registerReceiver(mReceiver, filter);
		
		Button button1=(Button)findViewById(R.id.rssi_button1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根				
					Toast.makeText(Rssi.this, rssi+"", Toast.LENGTH_SHORT).show();				
			}
		});
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		String action =intent.getAction();

		//设备始扫描

		if (BluetoothDevice.ACTION_FOUND.equals(action)) {

		//IntentblueDevice象

		BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

		if(device.getBondState() != BluetoothDevice.BOND_BONDED) {

	    rssi =intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
		
		}

		}
		}
		}; 
}