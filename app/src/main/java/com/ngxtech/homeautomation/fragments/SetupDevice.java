package com.ngxtech.homeautomation.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngxtech.homeautomation.ConnectToDevice;
import com.ngxtech.homeautomation.NetworkReceiver;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.Wifilist;
import com.trncic.library.DottedProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupDevice extends Fragment {

    Button next,proceed;
    ViewPager viewPager;
    int PAGE = 1;
    DottedProgressBar progressBar;

    WifiConfiguration wifiConfig;
    WifiManager wifiManager;
    ImageView imageView;

    ProgressDialog pd;
    String categoryid = "";
    NetworkReceiver receiver;

    public SetupDevice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_device, container, false);
        next = (Button) view.findViewById(R.id.nextsetup);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager1);
        pd = new ProgressDialog(getContext());
        pd.setTitle("Connectiong to Device");
        progressBar = (DottedProgressBar) view.findViewById(R.id.progress);
        pd.setMessage("Please Wait.....");
        proceed=(Button)view.findViewById(R.id.proceed);
        Intent i = getActivity().getIntent();
        imageView = (ImageView) view.findViewById(R.id.successful);
        categoryid = i.getStringExtra("categoryid");
        receiver = new NetworkReceiver();
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiConfig = new WifiConfiguration();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // viewPager.setCurrentItem(2);
                progressBar.startProgress();

                connectDeviceWifi();


            }
        });
       proceed.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
           viewPager.setCurrentItem(2);

           }
       });

        return view;
    }

    void connectDeviceWifi() {


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
                progressBar.stopProgress();
                next.setVisibility(View.GONE);
                proceed.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),"Connected Successfully\t"+bssid,Toast.LENGTH_SHORT).show();


            } else timerDelayRemoveDialog(5000, progressBar);


        }
    }

    public void timerDelayRemoveDialog(final long time, final DottedProgressBar d) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.stopProgress();

                imageView.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),"No Device Found Please Try Again",Toast.LENGTH_SHORT).show();

            }
        }, time);


    }
}
