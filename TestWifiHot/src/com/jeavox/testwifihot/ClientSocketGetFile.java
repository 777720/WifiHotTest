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
 * ��Ϊ��wifi�ȵ�֮���ֻ�������û��IP������ֻ�ܴ�DJ5677A��ȡIP
 * �ֻ���Ϊ�ͻ��ˣ���wifi�ȵ㣬��ȡ�����IP
 * �ļ����ͷ�ʽ������˴��ͣ��ͻ��˽���
 * @author Administrator
 *
 */
public class ClientSocketGetFile {
	
	private static String sip;
	private static  int sport;
	private static Socket s;
	private String savePath=Environment.getExternalStorageDirectory()+"/���յ����ļ�/";
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
	 * �����ļ�
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
								System.out.println("��ǰ���ȣ�" + passedLen * 100 / len + "%");
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
