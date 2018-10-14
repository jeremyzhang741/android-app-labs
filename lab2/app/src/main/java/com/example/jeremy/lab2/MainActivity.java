package com.example.jeremy.lab2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;

public class MainActivity extends AppCompatActivity {
    private static final int MY_WIFI = 10;
    private List<MyWifi> wifiList = new ArrayList<>();
    private List<ScanResult> results = new ArrayList<>();
    private RecyclerView recyclerView;
    private WifiAdapter mAdapter;
    private WifiManager wifiManager;
    private Button scanButton;
    private String wifiPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = findViewById(R.id.button);
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new WifiAdapter(wifiList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        scanWifi();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final MyWifi wifi = wifiList.get(position);



                // Password input Ref: https://stackoverflow.com/questions/10903754/input-text-dialog-android
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle(wifi.getName());
                final EditText ssidPass = new EditText(MainActivity.this);

                ssidPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                alertDialog.setView(ssidPass);

                alertDialog.setMessage("Alert message to be shown");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                wifiPass = ssidPass.getText().toString();
                                Toast.makeText(getApplicationContext(), wifiPass, Toast.LENGTH_SHORT).show();
                                connectToWifi(wifi.getName(),wifiPass);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();


//                Toast.makeText(getApplicationContext(), wifi.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }



    private void scanWifi() {
        if ((ContextCompat.checkSelfPermission(this, ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||

                (ContextCompat.checkSelfPermission(this, CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
                ) {
            ActivityCompat.requestPermissions(this,new String[] {
                    ACCESS_WIFI_STATE,
                    ACCESS_COARSE_LOCATION,
                    CHANGE_WIFI_STATE
            },MY_WIFI);
        } else {
            _scanWifi();
        }

    }

    private void _scanWifi() {


        registerReceiver(wifiReceiver, new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        Toast.makeText(this,"Scanning WiFi...",Toast.LENGTH_LONG).show();

    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults(); // get the list of Wifi Access Point

            // Sorting ref: https://stackoverflow.com/questions/17285337/how-can-i-sort-the-a-list-of-getscanresults-based-on-signal-strength-in-ascend
            Comparator<ScanResult> compare_signal_strength = new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult scanResult, ScanResult t1) {
                    return (scanResult.level > t1.level ? -1 : (scanResult.level == t1.level ? 0 : 1));
                }
            };
            Collections.sort(results,compare_signal_strength); // sorting the result

            wifiList.clear();
            MyWifi wifi;
            int counter = 0;
            for (ScanResult scanResult: results) {
                wifi = new MyWifi(scanResult.SSID, scanResult.level);
                wifiList.add(wifi);
                if (counter == 3) {
                    break;
                }
                counter += 1;
            }
            mAdapter.notifyDataSetChanged();




        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_WIFI:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    _scanWifi();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_WIFI_STATE)) {
                        new AlertDialog.Builder(this).setTitle("ACCESS_WIFI_STATE Permission").setMessage("ACCESS_WIFI_STATE Permission").show();
                    }
                }
        }
    }


    // ref https://stackoverflow.com/questions/35539273/android-6-connect-to-specific-wifi-network-programmatically-not-working
    private void connectToWifi(final String networkSSID, final String networkPass) {
        try{
            WifiConfiguration wifiConf = new WifiConfiguration();
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wifiConf.SSID = "\""+ networkSSID +"\"";
            wifiConf.preSharedKey = "\"" + networkPass + "\"";
            wifiConf.status = WifiConfiguration.Status.ENABLED;
            wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

            wifiManager.setWifiEnabled(true);
            int netId = wifiManager.addNetwork(wifiConf);
            if (netId == -1) {
                netId = getExistingNetworkId(wifiConf.SSID);
            }
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getExistingNetworkId(String SSID) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (existingConfig.SSID.equals(SSID)) {
                    return existingConfig.networkId;
                }
            }
        }
        return -1;
    };


}