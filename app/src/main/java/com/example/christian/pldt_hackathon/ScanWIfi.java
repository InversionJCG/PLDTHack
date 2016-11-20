package com.example.christian.pldt_hackathon;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanWIfi extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;

    private StringBuilder sb = new StringBuilder();
    private ArrayList<String> connections;

    private final Handler handler = new Handler();

    private ArrayAdapter<String> mArrayAdapter;
    private ListView list;

    int checker = 0;

    private ConnectivityManager connManager;
    private NetworkInfo mWifi;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_scan_wifi);

        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();

        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if(mainWifi.isWifiEnabled()==false){
            mainWifi.setWifiEnabled(true);
        }

        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.mylistview);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(mArrayAdapter);
        list.setOnItemClickListener(this);

        doInback(); // Scan WIFI

    }

    // Connecting Paired
    public void connect(String networkSSID) throws IOException, InterruptedException{

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

            boolean temp = false;


            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {

                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    temp = true;
                    break;
                }
            }
        if (mWifi.isConnected()&&checker==0) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Network_Info.class);
            startActivity(intent);
            checker++;
        }
        /*else if(!mWifi.isConnected()&&checker==10){
            Intent intent = new Intent(getApplicationContext(), Log_In.class);
            intent.putExtra("SSID", networkSSID);
            startActivity(intent);
        }*/

    }

    // Scan WIFI Results
    public void doInback()
    {
        handler.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                receiverWifi = new WifiReceiver();
                registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                if (mWifi.isConnected()&&checker==0) {
                    Intent intent = new Intent(getApplicationContext(), Network_Info.class);
                    startActivity(intent);
                    checker++;
                }
                else {
                    doInback();
                }
            }
        }, 1000);

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                try {
                    TextView temp = (TextView) view;
                    connect(temp.getText().toString());

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()&&checker==0) {
            Intent intent = new Intent(getApplicationContext(), Network_Info.class);
            startActivity(intent);
            checker++;
        }
    }

    @Override
    protected void onPause()
    {
        unregisterReceiver(receiverWifi);
        super.onPause();
        this.finish();
    }

    @Override
    protected void onResume()
    {
        //registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {
            connections = new ArrayList<>();

            sb = new StringBuilder();
            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            for(int i = 0; i < wifiList.size(); i++)
            {
                String SSID = wifiList.get(i).SSID;
                if(mArrayAdapter.getPosition(SSID) < 0 && !SSID.isEmpty()) {
                    mArrayAdapter.add(SSID);
                }
            }
        }
    }
}
