package com.example.jeremy.lab10;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Task1Activity extends AppCompatActivity {
    private TextView textView;
    private WifiManager mWifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task1 );

        textView = findViewById( R.id.textView );
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if(!mWifiManager.isWifiEnabled() && mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
            mWifiManager.setWifiEnabled( true );
        }
        is5Ghz();

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void is5Ghz() {
        if (mWifiManager.is5GHzBandSupported() ) {
            textView.setText( "5 Ghz is supported" );
        } else {
            textView.setText( "5 Ghz is not supported" );
        }

    };


}