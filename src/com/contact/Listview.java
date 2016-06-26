package com.contact;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
public class Listview extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
	 final ListView mlistView = new ListView(this);
	 
	  ArrayList<HashMap<String, String>> list = getPeopleInPhone2();
	  SimpleAdapter adapter = new SimpleAdapter(
		         this,
		         list,
		         android.R.layout.simple_list_item_2,
		         new String[] {"peopleName", "phoneNum"},
		         new int[]{android.R.id.text1, android.R.id.text2}
		        );
	  mlistView.setAdapter(adapter);
	  setContentView(mlistView);
	  mlistView.setOnItemClickListener(new OnItemClickListener() {

			

			 @Override   
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,   
	                    long arg3) {
				 HashMap<String,String> map=(HashMap<String,String>)mlistView.getItemAtPosition(arg2); 
				 String peopleName=map.get("peopleName");
				 String phoneNum=map.get("phoneNum");
	//			 Toast.makeText(Listview.this, phoneNum, Toast.LENGTH_SHORT).show();
				
				 Intent intent=getIntent();
				 Bundle bundle=new Bundle();
				 bundle.putString("peopleName", peopleName);
				 bundle.putString("phoneNum", phoneNum);
				 intent.putExtras(bundle);
				 setResult(0x11,intent);				 
				 finish();
			 }
			 
		});
	 
}
	
	private ArrayList<HashMap<String, String>> getPeopleInPhone2(){
		  ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);  // 获取手机联系人
		  while (cursor.moveToNext()) {
		   HashMap<String, String> map = new HashMap<String, String>();

		   int indexPeopleName = cursor.getColumnIndex(Phone.DISPLAY_NAME);  // people name
		   int indexPhoneNum = cursor.getColumnIndex(Phone.NUMBER);    // phone number

		   String strPeopleName = cursor.getString(indexPeopleName);
		   String strPhoneNum = cursor.getString(indexPhoneNum);
		   
		   map.put("peopleName", strPeopleName);
		   map.put("phoneNum", strPhoneNum);
		   list.add(map);
		  }
		        if(!cursor.isClosed()){
		         cursor.close();
		         cursor = null;
		        }

		        return list;
		 }
}
