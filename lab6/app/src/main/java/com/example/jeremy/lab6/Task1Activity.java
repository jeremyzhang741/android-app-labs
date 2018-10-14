package com.example.jeremy.lab6;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Task1Activity extends AppCompatActivity {
    Intent batteryStatus ;
    Button btn_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task1 );
        final IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null, ifilter);

        btn_status = findViewById( R.id.btn_status );
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txt_status;
                txt_status = findViewById( R.id.txt_status );
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                // How are we charging?
                int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

                float batteryPct = level / (float)scale * 100;
                if (usbCharge) {
                    txt_status.setText( "Current level of battery is: "+batteryPct+"%\n Mobile is charging via usb" );

                } else {
                    txt_status.setText( "Current level of battery is: "+batteryPct+"%" );

                }
            }
        });
    }
}

