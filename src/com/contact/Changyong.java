package com.contact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.example.smart_bag.Bluetooth;
import com.example.smart_bag.MainActivity;
import com.example.smart_bag.R;
import com.zhuanhua.HanziToPinyin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Changyong extends Activity {
	
	private EditText name1,name2,name3,name4,name5;
	private  EditText number1,number2,number3,number4,number5;
	private Button daoru,qingkong;
	private Button save1,chakan1,send1;
	File file1=new File("name.txt");
	File file2=new File("number.txt");
	int num;
	private static Bluetooth blu;
	public OutputStream outStream = null;
	public static boolean isInterrupt=false;//线程中断的标志性变量
	private static final String TAG = "THINBTCLIENT";
	private  static String str_newname;
	private static String str_newnumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_people);
	//	isInterrupt=false;
		name1=(EditText)findViewById(R.id.name1);
		number1=(EditText)findViewById(R.id.number1);
		name1.setText("默认");
		number1.setText("10086");
		name2=(EditText)findViewById(R.id.name2);
		number2=(EditText)findViewById(R.id.number2);
		name2.setText("默认");
		number2.setText("10086");
		name3=(EditText)findViewById(R.id.name3);
		number3=(EditText)findViewById(R.id.number3);
		name3.setText("默认");
		number3.setText("10086");
		name4=(EditText)findViewById(R.id.name4);
		number4=(EditText)findViewById(R.id.number4);
		name4.setText("默认");
		number4.setText("10086");
		name5=(EditText)findViewById(R.id.name5);
		number5=(EditText)findViewById(R.id.number5);
		name5.setText("默认");
		number5.setText("10086");
		save1=(Button)findViewById(R.id.save1);
		chakan1=(Button)findViewById(R.id.chankan1);
		daoru=(Button)findViewById(R.id.daoru);
		qingkong=(Button)findViewById(R.id.qingkong);
		send1=(Button)findViewById(R.id.send1);
		
		Chakan();
	//	savename_number();
		daoru.setOnClickListener(new OnClickListener() {//导入按钮的时间监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent1=new Intent(Changyong.this,Listview.class);
				startActivityForResult(intent1, 0x11);//Inten1为开启Listview的intent，发送结果码为0x11;
			}
		});
		
		qingkong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				name1.setText("");
				number1.setText("");
				
				name2.setText("");
				number2.setText("");
				
				name3.setText("");
				number3.setText("");
				
				name4.setText("");
				number4.setText("");
				
				name5.setText("");
				number5.setText("");			
			}
		});
		
		save1.setOnClickListener(new OnClickListener() {//save1按钮的时间监听,保存并结束当前
			
			@Override
			public void onClick(View v) {
				savename_number();
				finish();
			}
		});
		
		chakan1.setOnClickListener(new OnClickListener() {//chakan1按钮的事件监听			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Chakan();//执行查看方法
			}
		});
		send1.setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(blu.btSocket==null){
					Toast.makeText(Changyong.this, "请确保您的蓝牙已经连接！", Toast.LENGTH_SHORT).show();
				}else{
				new output().start();
				}
				}	
		});					//send1按钮的事件监听
		
	}	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String str1=name1.getText().toString();
		String str2=name2.getText().toString();
		String str3=name3.getText().toString();
		String str4=name4.getText().toString();
		String str5=name5.getText().toString();
		
		if(requestCode==0x11&&resultCode==0x11){
			Bundle bundle=data.getExtras();
			String Name=bundle.getString("peopleName");//第一个常用联系人的姓名及号码
			String Number=bundle.getString("phoneNum");
			while(true){
					if(str1.equals("")){
						name1.setText(Name);
						number1.setText(Number);
						number1.setSelection(Number.length());break;
					}
					else if(str2.equals("")){
						name2.setText(Name);
						number2.setText(Number);
						number2.setSelection(Number.length());break;
					}
					else if(str3.equals("")){
						name3.setText(Name);
						number3.setText(Number);
						number3.setSelection(Number.length());break;
					}
					else if(str4.equals("")){
						name4.setText(Name);
						number4.setText(Number);
						number4.setSelection(Number.length());break;
					}
					else if(str5.equals("")){
						name5.setText(Name);
						number5.setText(Number);
						number5.setSelection(Number.length());break;
					}
					else{
						Toast.makeText(Changyong.this, "常用联系人已设置完毕,修改请清空", Toast.LENGTH_SHORT).show();
						break;
					}
			}
			
		}
	
	}
	class output extends Thread{
		@Override
		public void run(){			
			faSong("Change_Begin");
					while(true){
						if(blu.sucess==true){
							String str1=name1.getText().toString();
							String str1_pinyin=fanyi(str1);
							faSong(blu.a+" "+str1_pinyin);
							blu.a++;
							blu.sucess=false;
							break;
							}					
											}
					while(true){
						if(blu.sucess==true){
							String str2=name2.getText().toString();
							String str2_pinyin=fanyi(str2);
							faSong(blu.a+" "+str2_pinyin);
							blu.a++;
							blu.sucess=false;
							break;
							}					
											}
					while(true){
						if(blu.sucess==true){
							String str3=name3.getText().toString();
							String str3_pinyin=fanyi(str3);
							faSong(blu.a+" "+str3_pinyin);
							blu.a++;
							blu.sucess=false;
							break;
							}				
											}
					while(true){
						if(blu.sucess==true){
							String str4=name4.getText().toString();
							String str4_pinyin=fanyi(str4);
							faSong(blu.a+" "+str4_pinyin);
							blu.a++;
							blu.sucess=false;
							break;
						}					
											}
					while(true){
						if(blu.sucess==true){
   						   String str5=name5.getText().toString();
   						String str5_pinyin=fanyi(str5);
							faSong(blu.a+" "+str5_pinyin);	
							blu.a++;
							blu.sucess=false;
							break;
							}					
											}
					while(true){
						if(blu.sucess==true){							
							faSong("Change_Finish");
							blu.a='6';
							blu.sucess=false;
							break;	
							}																				
						}
		}
	}
	 public  void faSong(String v)
		{
			String message;
			byte[] msgBuffer;
			try{
				outStream=blu.btSocket.getOutputStream();
			}catch(IOException e){
				Log.e(TAG, "ON RESUME:  Output stream creation failed.", e);
			}
			message=v;
			msgBuffer=message.getBytes();
			try{
				outStream.write(msgBuffer);
				
			}catch(IOException e){
				Log.e(TAG, "ON RESUME:  Exception during write.", e);
			}
			finally
			{
				try {
					outStream.flush();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	public void Chakan() {
		try {
			str_newname=read("name.txt");
			str_newnumber=read("number.txt");
			 String[] newstr_name=str_newname.split(",");
			 
			String[] newstr_number=str_newnumber.split(",");
					name1.setText(newstr_name[0]);
					number1.setText(newstr_number[0]);
		
					name2.setText(newstr_name[1]);
					number2.setText(newstr_number[1]);
			
					name3.setText(newstr_name[2]);
					number3.setText(newstr_number[2]);
			
					name4.setText(newstr_name[3]);
					number4.setText(newstr_number[3]);
			
					name5.setText(newstr_name[4]);
					number5.setText(newstr_number[4]);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(Changyong.this, "请务必将联系人补充完整！", Toast.LENGTH_SHORT).show();
		}
	}
	//save保存数据到name.txt文件的方法
		public void save(String file,String name)throws Exception {
			FileOutputStream fileOutputStream=openFileOutput(file,Context.MODE_PRIVATE);
			fileOutputStream.write(name.getBytes());
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
		public void savename_number() {

			// TODO 自动生成的方法存根
			String str1=name1.getText().toString();
			String str2=name2.getText().toString();
			String str3=name3.getText().toString();
			String str4=name4.getText().toString();
			String str5=name5.getText().toString();
			
			String number1_1=number1.getText().toString();
			String number2_1=number2.getText().toString();
			String number3_1=number3.getText().toString();
			String number4_1=number4.getText().toString();
			String number5_1=number5.getText().toString();
			
			if(str5.equals("")){
				str5="默认";
				number5_1="10086";
				name5.setText("默认");
				number5.setText("10086");
			}
			if(str4.equals("")){
				str4="默认";
				number4_1="10086";
				name4.setText("默认");
				number4.setText("10086");
			}
			if(str3.equals("")){
				str3="默认";
				number3_1="10086";
				name3.setText("默认");
				number3.setText("10086");
			}
			if(str2.equals("")){
				str2="默认";
				number2_1="10086";
				name2.setText("默认");
				number2.setText("10086");
			}
			if(str1.equals("")){
				str1="默认";
				number1_1="10086";
				name1.setText("默认");
				number1.setText("10086");
			}
			String str_name=str1+","+str2+","+str3+","+str4+","+str5;
			String str_number=number1_1+","+number2_1+","+number3_1+","+number4_1+","+number5_1+",";
			try { 					
				save("name.txt",str_name);
				save("number.txt",str_number);
				Toast.makeText(Changyong.this, "保存成功", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(Changyong.this, "保存失败", Toast.LENGTH_SHORT).show();
			}		
		}
		public String fanyi(String str) {//把汉字逐个翻译成拼音
			String str_fin="";
			char [] stringArr = str.toCharArray(); //注意返回值是char数组
			for(int i=0;i<stringArr.length-1;i++){
				String str_pinyin=HanziToPinyin.getPinYin(stringArr[i]+" ");				
				str_fin+=(str_pinyin);			
			}
			String str_pinyin1=HanziToPinyin.getPinYin(stringArr[str.length()-1]+"");
			return str_fin+str_pinyin1;
		}

}
