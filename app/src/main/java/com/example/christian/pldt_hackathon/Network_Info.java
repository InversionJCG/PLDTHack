package com.example.christian.pldt_hackathon;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.*;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;

public class Network_Info extends AppCompatActivity {

    public String   dns1 ;
    public String   dns2;
    public String   dgateway;
    public String   ipAddress;
    public String   netmask;
    private int ping_count = 0;
    TextView info;
    DhcpInfo d;
    WifiManager wifii;

    public long averagespeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_network__info);



        wifii= (WifiManager) getSystemService(Context.WIFI_SERVICE);


        try {
            ping();
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        d=wifii.getDhcpInfo();
        dns1="DNS 1: "+intToIp(d.dns1);
        dns2="DNS 2: "+intToIp(d.dns2);
        dgateway="Default Gateway: "+intToIp(d.gateway);
        ipAddress="IP Address: "+intToIp(d.ipAddress);
        netmask="Subnet Mask: "+intToIp(d.netmask);


        //dispaly them
        info= (TextView) findViewById(R.id.info);
        info.setText("Network Info:\n\n"+ipAddress+"\n"+dgateway+"\n"+netmask+"\n"+dns1+"\n"+dns2);

        if(ping_count>5){
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            info.setText(info.getText()+"\nLatency: "+averagespeed+" ms\n\nYour network is connected to internet.");
        }
        else{
            Toast.makeText(this, "Restart Router", Toast.LENGTH_LONG).show();
            try{
            ping();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            ping_count = 0;
            if(ping_count>5){
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                info.setText(info.getText()+"\n\nLatency: "+averagespeed+" ms\n\n" +
                        "Your network is connected to internet. :)");
            }
            else{
                Toast.makeText(this, "Sending Report...", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String intToIp(int i) {
        return (i & 0xFF)+"."+((i>>8)&0xFF)+"."+((i>>16)&0xFF)+"."+((i>>24)&0xFF);
    }

    // Get ping
    public void ping() throws IOException, InterruptedException{
        long totalspeed=0;
        for(int x = 0; x<10; x++) {
            long pingspeed, start = System.currentTimeMillis();
            Runtime runtime = Runtime.getRuntime();
            Process p1 = runtime.exec("/system/bin/ping -c 1 google.com");
            pingspeed = System.currentTimeMillis() - start;
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);

            if (reachable == true) {
                ping_count++;
                totalspeed=pingspeed+totalspeed;
            }
        }
        averagespeed = totalspeed/10;

    }
}
