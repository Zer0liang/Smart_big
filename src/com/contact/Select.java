package com.contact;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.example.smart_bag.Bluetooth;
import com.example.smart_bag.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Select extends Activity {
	
	private static  Bluetooth blu;
	
	private EditText File_name;
	private EditText Call_name;
	private EditText Call_number;
	private Button Insert;
	private Button Save;
	private Button Notify;
	private Button Send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);
	
		
		 Insert=(Button)findViewById(R.id.daoru);
		 Save=(Button)findViewById(R.id.save);	
	     Notify=(Button)findViewById(R.id.notify);
		 Send=(Button)findViewById(R.id.send);
		
		 Call_name=(EditText)findViewById(R.id.EditView1);//报警人姓名
		 Call_number=(EditText)findViewById(R.id.EditView2);//报警人电话号码
		 Call_name.setText("默认");
		 Call_number.setText("10086");
		File_name =(EditText)findViewById(R.id.EditView3);//保存数据的文件名
		File_name.setText("Mytext.txt");
		chakan_help();
		Send.setOnClickListener(new OnClickListener() {//发送按钮的时间监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//String Danger_number=Call_number.getText().toString();
				String Danger_number="10086";
				try {
					Danger_number = read("Mytext.txt");
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				blu.number=Danger_number;//传递数据并结束当前Activity
				finish();
			}
		});
		
		Insert.setOnClickListener(new OnClickListener() {//导入按钮的事件监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intnet1=new Intent(Select.this,Listview.class);
				startActivityForResult(intnet1,0x11);
			}
		});
		
		
		Save.setOnClickListener(new OnClickListener() {//save按钮的时间监听			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//获取编辑框中的内容
				String str_Call_name=Call_name.getText().toString();
				String str_Call_number=Call_number.getText().toString();
				try {
					save("Mytext.txt",str_Call_number);//调用save方法
					save("Myname.txt",str_Call_name);
					Toast.makeText(Select.this, "保存成功", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(Select.this, "保存失败", Toast.LENGTH_SHORT).show();
				}				
				finish();
			}
		});		
		Notify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				chakan_help();
			}
		});
		
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if(requestCode==0x11&&resultCode==0x11){
			Bundle bundle=data.getExtras();
			String str_peopleName=bundle.getString("peopleName");
			String str_phoneNum=bundle.getString("phoneNum");
			Call_name.setText(str_peopleName);
			Call_name.setSelection(str_peopleName.length());
			Call_number.setText(str_phoneNum);
			Call_number.setSelection(str_phoneNum.length());
		}
	
	}
	
	//save保存数据到Mytext.txt文件的方法
	public void save(String file,String number)throws Exception {
		FileOutputStream fileOutputStream=openFileOutput(file,Context.MODE_PRIVATE);
		//fileOutputStream.write(Name.getBytes());
		fileOutputStream.write(number.getBytes());
		fileOutputStream.close();
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
	public void chakan_help() {
		try {
			String str_file_name=read("Myname.txt");
			String str_file_number=read("Mytext.txt");
			//Toast.makeText(Select.this, str_file_content, Toast.LENGTH_SHORT).show();
			Call_name.setText(str_file_name);
			Call_number.setText(str_file_number);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(Select.this, "文件内容读取失败", Toast.LENGTH_SHORT).show();
		}
	
	}
}
