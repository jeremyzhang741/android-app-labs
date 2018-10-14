package com.example.jeremy.lab10;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Task2Activity extends AppCompatActivity {
    private TextView textView;
    private WifiManager mWifiManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task2 );
        textView = findViewById( R.id.textView2 );
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if(!mWifiManager.isWifiEnabled() && mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
            mWifiManager.setWifiEnabled( true );
        }

        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        if (connectionInfo != null) {
            textView.setText( "Frequency: "+(connectionInfo.getFrequency()/1000) +"GHz\nBitRate: " + connectionInfo.getLinkSpeed()+"Mbps" );
        }
    }
}