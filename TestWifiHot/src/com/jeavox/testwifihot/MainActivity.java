package com.jeavox.testwifihot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private Button sendFile,getFile,WifiHot;
	private WifiManager sWifiManager;
	private boolean flag=false;
	private int port=8888;
	private String IP="192.168.1.2";
	private StringBuilder resultList;
	private ClientSocketGetFile getfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sendFile=(Button)findViewById(R.id.sendFile);
		getFile=(Button)findViewById(R.id.getFile);
		WifiHot=(Button)findViewById(R.id.startWifiHot);
		
		sendFile.setOnClickListener(this);
		getFile.setOnClickListener(this);
		WifiHot.setOnClickListener(this);
		
		sWifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		IpCallBack();
		getfile=new ClientSocketGetFile(MainActivity.this,IP, port);
		getfile.CreateConnection();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sendFile:
			ServerSocketSendFile send=new ServerSocketSendFile(port);
			send.serverSendFile();
			break;
		case R.id.getFile:
			getfile.getMessage();
			break;
		case R.id.startWifiHot:
			flag =! flag;
			setWifiEnable(flag);
			break;
		}
	}
	
	private void setWifiEnable(boolean flag2) {
		if (sWifiManager.isWifiEnabled()) {
			sWifiManager.setWifiEnabled(false);
		}

		try {
			WifiConfiguration config = new WifiConfiguration();
			config.SSID = "\"hu123456\"";
			config.preSharedKey = "12345678";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

			Method method = sWifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, boolean.class);
			method.invoke(sWifiManager, config, flag2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//查不到DJ5677A的IP
/*	private String  getIPInfo(){
		WifiInfo info=sWifiManager.getConnectionInfo();
		int Address=info.getIpAddress();
		System.out.println("Address="+Address);
		return (Address & 0xFF)+ "." + ((Address >> 8 ) & 0xFF) + "." + ((Address >> 16 ) & 0xFF) +"."+((Address >> 24 ) & 0xFF );
	}*/
	
	private void IpCallBack(){
		ArrayList<String> connectedIP = getIPInfo();  
        resultList = new StringBuilder();  
        for(String ip : connectedIP){  
        	if(!ip.equals("IP")){
	            resultList.append(ip);  
	            resultList.append("\n");  
        	}
        }  
        IP=resultList.toString();
	}
	
	private ArrayList<String> getIPInfo(){
		ArrayList<String> connectedIP = new ArrayList<String>();  
        try {  
            BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));  
            String line;  
            while ((line = br.readLine()) != null) {  
                String[] splitted = line.split(" +");  
                if (splitted != null && splitted.length >= 4) {  
                    String ip = splitted[0];  
                    connectedIP.add(ip);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        return connectedIP;  
	}
	
}
