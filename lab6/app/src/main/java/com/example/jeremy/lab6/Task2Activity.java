package com.example.jeremy.lab6;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Task2Activity extends AppCompatActivity {
    Intent batteryStatus ;
    TextView txt_1, txt_2, txt_3, txt_4;
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    private Context context;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task2 );
        txt_1 = findViewById( R.id.txt_1 );
        txt_2 = findViewById( R.id.txt_2 );
        txt_3 = findViewById( R.id.txt_3 );
        txt_4 = findViewById( R.id.txt_4 );

        final IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null, ifilter);

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean isWifi = mWifi.isConnected();
        boolean isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (! isWifi && ! isGps) {
            txt_1.setText( "Normal usage of mobile phone for 10 minutes" );
        } else if(isWifi) {
            txt_1.setText( "Using GPS for 10 minutes" );
        } else if (isGps) {
            txt_1.setText( "Using Wi-Fi for 10 minutes" );
        }

        float initial = currentBat();
        txt_2.setText( "Initial level of batery:"+initial+"%" );


    }

    protected float currentBat() {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale * 100;
        return batteryPct;
    }


}
