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
		
		 Call_name=(EditText)findViewById(R.id.EditView1);//����������
		 Call_number=(EditText)findViewById(R.id.EditView2);//�����˵绰����
		 Call_name.setText("Ĭ��");
		 Call_number.setText("10086");
		File_name =(EditText)findViewById(R.id.EditView3);//�������ݵ��ļ���
		File_name.setText("Mytext.txt");
		chakan_help();
		Send.setOnClickListener(new OnClickListener() {//���Ͱ�ť��ʱ�����
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				//String Danger_number=Call_number.getText().toString();
				String Danger_number="10086";
				try {
					Danger_number = read("Mytext.txt");
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				blu.number=Danger_number;//�������ݲ�������ǰActivity
				finish();
			}
		});
		
		Insert.setOnClickListener(new OnClickListener() {//���밴ť���¼�����
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intnet1=new Intent(Select.this,Listview.class);
				startActivityForResult(intnet1,0x11);
			}
		});
		
		
		Save.setOnClickListener(new OnClickListener() {//save��ť��ʱ�����			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				//��ȡ�༭���е�����
				String str_Call_name=Call_name.getText().toString();
				String str_Call_number=Call_number.getText().toString();
				try {
					save("Mytext.txt",str_Call_number);//����save����
					save("Myname.txt",str_Call_name);
					Toast.makeText(Select.this, "����ɹ�", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(Select.this, "����ʧ��", Toast.LENGTH_SHORT).show();
				}				
				finish();
			}
		});		
		Notify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
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
	
	//save�������ݵ�Mytext.txt�ļ��ķ���
	public void save(String file,String number)throws Exception {
		FileOutputStream fileOutputStream=openFileOutput(file,Context.MODE_PRIVATE);
		//fileOutputStream.write(Name.getBytes());
		fileOutputStream.write(number.getBytes());
		fileOutputStream.close();
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
	public void chakan_help() {
		try {
			String str_file_name=read("Myname.txt");
			String str_file_number=read("Mytext.txt");
			//Toast.makeText(Select.this, str_file_content, Toast.LENGTH_SHORT).show();
			Call_name.setText(str_file_name);
			Call_number.setText(str_file_number);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(Select.this, "�ļ����ݶ�ȡʧ��", Toast.LENGTH_SHORT).show();
		}
	
	}
}
