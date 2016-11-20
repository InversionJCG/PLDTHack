package com.example.christian.pldt_hackathon;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

    }

    public void troubleshoot_click(View view){
        Intent intent = new Intent(getApplicationContext(), ScanWIfi.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        TextView status = (TextView) findViewById(R.id.textStatus);

        if (mWifi.isConnected()) {
            status.setText("Status:\nConnected to " + wifi.getConnectionInfo().getSSID().toString());
        }else{
            status.setText("Status: Not Connected");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }
}
