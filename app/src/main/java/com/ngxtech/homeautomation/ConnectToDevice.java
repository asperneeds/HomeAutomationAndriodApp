package com.ngxtech.homeautomation;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectToDevice extends AppCompatActivity {

    Button b;
    WifiConfiguration wifiConfig;
    WifiManager wifiManager;
    Button button;
    ProgressDialog pd;
    String categoryid="";
    NetworkReceiver receiver;
    Toolbar toolbar;



    public ConnectToDevice() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_device);

        b = findViewById(R.id.wifi_connect);
        toolbar=(Toolbar)findViewById(R.id.adddevicetoolbar);
        setSupportActionBar(toolbar);

        pd = new ProgressDialog(ConnectToDevice.this);
        pd.setTitle("Connectiong to Device");
        pd.setMessage("Please Wait.....");
        Intent i=getIntent();
       categoryid= i.getStringExtra("categoryid");
       receiver=new NetworkReceiver();
        wifiManager = (WifiManager) ConnectToDevice.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiConfig = new WifiConfiguration();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connectDeviceWifi();

            }
        });
    }

    void connectDeviceWifi() {
        pd.show();
        wifiManager.setWifiEnabled(true);
        String networkSSID = "TitanC2C";
        String networkPass = "123456789";

        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);


        assert wifiManager != null;
        int netId = wifiManager.addNetwork(wifiConfig);
//        wifiManager.disconnect();

        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        @SuppressLint("HardwareIds") String macID = wifiManager.getConnectionInfo().getMacAddress();





            Validate();





    }

    private void Validate() {




        assert wifiManager != null;
        String desiredMacAddress = "\"TitanC2C\"";
        String bssid = "";

        WifiInfo wifi = wifiManager.getConnectionInfo();
        if (wifi != null) {
            // get current router Mac address
            bssid = wifi.getSSID();
            boolean connected = desiredMacAddress.equals(bssid);
            Log.i("Wifi1", "Connected" + connected);


            if (connected) {
                pd.dismiss();
                Intent intent = new Intent(ConnectToDevice.this, Wifilist.class);
                intent.putExtra("categoryid", categoryid);
                startActivity(intent);
                Toast.makeText(ConnectToDevice.this, "Connected Successfully ", Toast.LENGTH_SHORT).show();
                finish();
            }else timerDelayRemoveDialog(5000, pd);


        }
    }


        public void timerDelayRemoveDialog ( final long time, final Dialog d){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    d.dismiss();

                }
            }, time);
            this.pd.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface arg0) {

                    Toast.makeText(ConnectToDevice.this, "No Device Found Please Try Again ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        protected void onPause () {
            super.onPause();


        }



    }
