package com.example.christian.pldt_hackathon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class Log_In extends AppCompatActivity{

    private EditText pass;
    private String SSID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_log__in);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        if(bd != null)
        {
            SSID = (String) bd.get("SSID");
            Toast.makeText(Log_In.this, SSID,
            Toast.LENGTH_LONG).show();
        }

        Button log = (Button)findViewById(R.id.button);
        pass = (EditText) findViewById(R.id.pass);

        log.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                try {
                    connect(SSID, pass.getText().toString());
                }catch (Exception e){
                    Toast.makeText(Log_In.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
                }
                Log_In.this.finish();
            }
        });
    }

    public void connect(String networkSSID, String password) throws IOException, InterruptedException{

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
        //conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.preSharedKey = "\""+ password +"\"";

        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        int logIn_Counter = 0;


        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {

            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
            /*if(logIn_Counter >= list.size()){
                wifiManager.disconnect();
                break;
            }
            logIn_Counter++;*/
        }

        /*if(logIn_Counter >= list.size()){
            Toast.makeText(this, "Invalid Password",
            Toast.LENGTH_LONG).show();
        }
        this.finish();*/
    }
}
