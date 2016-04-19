package com.jeavox.testwifihot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 因为打开wifi热点之后，手机本身是没有IP的所以只能从DJ5677A获取IP
 * 手机作为客户端，打开wifi热点，获取服务端IP
 * 文件传送方式：服务端传送，客户端接收
 * @author Administrator
 *
 */
public class ClientSocketGetFile {
	
	private static String sip;
	private static  int sport;
	private static Socket s;
	private String savePath=Environment.getExternalStorageDirectory()+"/接收到的文件/";
	private DataOutputStream output;
	private DataInputStream inputStream;
	private Context mcontext;
	
	public ClientSocketGetFile(Context context,String ip,int port){
		mcontext=context;
		sip=ip;
		sport=port;
	}
	
    public void  CreateConnection(){
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					s=new Socket(sip, sport);
					System.out.println("s="+s);
				} catch (Exception e) {
					e.printStackTrace();
					if (s != null)
						try {
							s.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				} finally {
				}
			}
		}).start();
    }
	/**
	 * 接收文件
	 * @throws Exception
	 */
	public  void getMessage() {
		try {
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdir();
			}
			System.out.println("savePath="+savePath+" ;file="+file.exists());
			inputStream = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			System.out.println("1234567890");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						savePath += inputStream.readUTF();
						output = new DataOutputStream(new BufferedOutputStream(
								new FileOutputStream(savePath)));
						long len = inputStream.readLong();
						System.out.println("len=" + len);
						int passedLen = 0;
						int read = 0;
						int buffSize = 1024 * 8;
						byte[] buf = new byte[buffSize];
						if (inputStream != null) {
							while ((read = inputStream.read(buf)) != -1) {
								passedLen += read;
								System.out.println("当前进度：" + passedLen * 100 / len + "%");
								output.write(buf, 0, read);
							}
						}
						inputStream.close();
						output.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
}
