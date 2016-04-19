package com.jeavox.testwifihot;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * ��DJ5677A��Ϊ����ˣ���wifi����
 * �ļ����ͷ�ʽ������˴��ͣ��ͻ��˽���
 * @author Administrator
 *
 */
public class ServerSocketSendFile {
	
	private static ServerSocket ss;
	private  int PORT;
	private String path="/storage/emulated/0/mediaresource/ɽˮ.jpg";
	private Socket s=null;
	
	public ServerSocketSendFile(int port){
		PORT=port;
	}
	
	public  void serverSendFile(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				serverConnect();
			}
		}).start();
		
	}
	
	private void serverConnect(){
			try {
				ss=new ServerSocket(PORT);
				while(true){
					s=ss.accept();
					if(s !=null){
						sendMessage();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					if(s!=null){
						s.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("************"+e);
			}
	}

	private void sendMessage(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File filePath=new File(path);
					BufferedInputStream buffered=new BufferedInputStream(new FileInputStream(path));
					DataInputStream dataInput=new DataInputStream(buffered);
					DataOutputStream dataOutput=new DataOutputStream(s.getOutputStream());
					dataOutput.writeUTF(filePath.getName());
					System.out.println("�ļ�����"+filePath.getName());
					dataOutput.flush();
					dataOutput.writeLong((long)filePath.length());
					System.out.println("�ļ�����="+(long)filePath.length());
					dataOutput.flush();
					byte[] buf=new byte[1024*8];
					int read=0;
					while((read=dataInput.read(buf)) != -1){
						dataOutput.write(buf, 0, read);
					}
					dataOutput.flush();
					dataInput.close();
					s.close();
					System.out.println("�ļ��������");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
