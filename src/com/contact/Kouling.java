package com.contact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.example.smart_bag.Bluetooth;
import com.example.smart_bag.R;
import com.zhuanhua.HanziToPinyin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Kouling extends Activity {
	
	private EditText editext1,editext2,editext3,editext4,editext5,editext6;
	private Button button1,button2,button3,button4;//保存，查看，发送
	File kouling_file=new File("kouling.txt");
	private static boolean s=false;
	
	private static Bluetooth blu;
	public OutputStream outStream = null;
	private static final String TAG = "THINBTCLIENT";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kouling);
		editext1=(EditText)findViewById(R.id.kouling_EditText1);
		editext2=(EditText)findViewById(R.id.kouling_EditText2);
		editext3=(EditText)findViewById(R.id.kouling_EditText3);
		editext4=(EditText)findViewById(R.id.kouling_EditText4);
		editext5=(EditText)findViewById(R.id.kouling_EditText5);
		editext6=(EditText)findViewById(R.id.kouling_EditText6);
		//救命 接电话  拒接 重拨 挂断 打电话
		editext1.setText("流水灯");
		editext2.setText("接");
		editext3.setText("挂断电话");
		editext4.setText("重拨电话");
		editext5.setText("再见拜拜");
		editext6.setText("打电话");
		chakan_content();
		button1=(Button)findViewById(R.id.kouling_button1);
		button2=(Button)findViewById(R.id.kouling_button2);
		button3=(Button)findViewById(R.id.kouling_button3);
		button4=(Button)findViewById(R.id.kouling_button4);
		
		button1.setOnClickListener(new OnClickListener() {//保存按钮的事件监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				save_content();
			}
		});
		button2.setOnClickListener(new OnClickListener() {//查看按钮的事件监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				chakan_content();
			}
		});
		button3.setOnClickListener(new OnClickListener() {//发送按钮的事件监听
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(blu.btSocket==null){
					Toast.makeText(Kouling.this, "请确保您的蓝牙已经连接！", Toast.LENGTH_SHORT).show();
				}else{
				new send().start();			
				}
			}
		});
		button4.setOnClickListener(new OnClickListener() {//查看按钮的事件监听			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(s==true){
					Toast.makeText(Kouling.this, "发送成功", Toast.LENGTH_SHORT).show();
					s=false;
				}else{
					Toast.makeText(Kouling.this, "发送失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void save_content() {
		
		String str1=editext1.getText().toString();
		String str2=editext2.getText().toString();
		String str3=editext3.getText().toString();
		String str4=editext4.getText().toString();
		String str5=editext5.getText().toString();
		String str6=editext6.getText().toString();
		
		if((str1.equals(""))||(str2.equals(""))||(str3.equals(""))||(str4.equals(""))||(str5.equals(""))||(str6.equals(""))){
			Toast.makeText(Kouling.this, "请正确设置基本口令信息", Toast.LENGTH_SHORT).show();
		}
		
		String str_all=str1+","+str2+","+str3+","+str4+","+str5+","+str6;
		try {
			save("kouling.txt", str_all);
			Toast.makeText(Kouling.this, "保存成功", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(Kouling.this, "保存失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void chakan_content() {
		try {
			String str_read=read("kouling.txt");
			String[] str_read_1=str_read.split(",");
			
			editext1.setText(str_read_1[0]);
			editext2.setText(str_read_1[1]);
			editext3.setText(str_read_1[2]);
			editext4.setText(str_read_1[3]);
			editext5.setText(str_read_1[4]);
			editext6.setText(str_read_1[5]);
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Toast.makeText(Kouling.this, "请务必正确输入口令", Toast.LENGTH_SHORT).show();
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
			class send extends Thread{
				@Override
				
				public void run(){
					faSong("Change_Begin");
					while(true){
						if(blu.sucess==true){
							String str1=editext1.getText().toString();
							String str1_pinyin=fanyi(str1);
							faSong(blu.b+" "+str1_pinyin);
							blu.b++;
							blu.sucess=false;
							break;
						}
					}
						while(true){
							if(blu.sucess==true){
								String str2=editext2.getText().toString();
								String str2_pinyin=fanyi(str2);
								faSong(blu.b+" "+str2_pinyin);
								blu.b++;
								blu.sucess=false;
								break;
							}
					}
						
						while(true){
							if(blu.sucess==true){
								String str3=editext3.getText().toString();
								String str3_pinyin=fanyi(str3);
								faSong(blu.b+" "+str3_pinyin);
								blu.b++;
								blu.sucess=false;
								break;
							}
					}
						
						while(true){
							if(blu.sucess==true){
								String str4=editext4.getText().toString();
								String str4_pinyin=fanyi(str4);
								faSong(blu.b+" "+str4_pinyin);
								blu.b++;
								blu.sucess=false;
								break;
							}
					}
						
						while(true){
							if(blu.sucess==true){
								String str5=editext5.getText().toString();
								String str5_pinyin=fanyi(str5);
								faSong(blu.b+" "+str5_pinyin);
								blu.b++;
								blu.sucess=false;
								break;
							}
					}
						
						while(true){
							if(blu.sucess==true){
								String str6=editext6.getText().toString();
								String str6_pinyin=fanyi(str6);
								faSong(blu.b+" "+str6_pinyin);
								blu.b++;
								blu.sucess=false;
								break;
							}
					}
						
						while(true){
							if(blu.sucess==true){
								faSong("Change_Finish");
								blu.b='0';
								s=true;
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

