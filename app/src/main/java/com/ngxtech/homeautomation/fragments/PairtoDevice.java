package com.ngxtech.homeautomation.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ConnectivityReceiver;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.Wifilist;
import com.ngxtech.homeautomation.adapter.WifilistAdapter;
import com.ngxtech.homeautomation.bean.DeviceItem;
import com.ngxtech.homeautomation.utils.DebugLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.WIFI_SERVICE;
import static com.ngxtech.homeautomation.Switches.MY_PERMISSIONS_REQUEST_WRITE_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PairtoDevice extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ArrayList<ScanResult> mScanResults;
    private ArrayList<DeviceItem> deviceList;
    private WifilistAdapter wifiListAdapter;
    private LinearLayout layout1, layout2;
    private CardView cardView;
    private ProgressBar ssidbar, wifilistbar;
    private TextView tryagain, wifissid;
    private ListView lv1;
    private App app;
    private WifiManager wifiManager;
    private String MACID = "";
    private String SSID = "";
    private String deviceName = "";
    private ProgressDialog progressDialog;
    private ProgressBar progress;
    private TextView refresh;
    WifiInfo wifi;
    String categoryid = "";

    public PairtoDevice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_wifilist, container, false);

        deviceList = new ArrayList<>();
        progress = view.findViewById(R.id.progress);
        deviceList = new ArrayList<>();
        layout1 = (LinearLayout) view.findViewById(R.id.linearlayoutwifi);
        cardView = (CardView) view.findViewById(R.id.linearlayoutwifilist);
        ssidbar = (ProgressBar) view.findViewById(R.id.ssidprogressbar);
        wifilistbar = (ProgressBar) view.findViewById(R.id.listprogressbar);
        tryagain = (TextView) view.findViewById(R.id.tryagain);
        wifissid = (TextView) view.findViewById(R.id.ssidname);
        refresh = (TextView) view.findViewById(R.id.refreshwifilist);
        lv1 = view.findViewById(R.id.wifilistview);

        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);

        mScanResults = new ArrayList<>();
        app = (App) getActivity().getApplication();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Pairing To Device ");
        progressDialog.setCancelable(false);
        assert wifiManager != null;
        Intent i = getActivity().getIntent();
        categoryid = i.getStringExtra("categoryid");


        wifi = wifiManager.getConnectionInfo();
        String bssid = wifi.getSSID();
        String result = bssid.replaceAll("^[\"']+|[\"']+$", "");
        wifissid.setText(result);
        showList();

        refresh.setVisibility(View.GONE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().registerReceiver(wifireciever1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                showList();
            }
        });
        checkPermission(getActivity());

        return view;
    }

    void showList() {


        Objects.requireNonNull(getActivity()).registerReceiver(wifireciever1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        refresh.setVisibility(View.GONE);
        wifilistbar.setVisibility(View.VISIBLE);
        lv1.setAdapter(wifiListAdapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDialog(mScanResults.get(position).SSID);
            }
        });

    }

    public void showDialog(final String userName) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View convertView = (View) inflater.inflate(R.layout.text_password, null);
        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(userName);
        final EditText et_password = (EditText) convertView.findViewById(R.id.et_password);
        //  final EditText et_device_name = (EditText) convertView.findViewById(R.id.et_device_name);
        Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
        // Setting Negative "NO" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();

                if (et_password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (et_device_name.getText().toString().isEmpty()) {
//                    Toast.makeText(getActivity(), "Enter Device Name", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else {

                SSID = userName;
                //  deviceName = et_device_name.getText().toString();
                setUserPwd(userName, et_password.getText().toString());
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertDialog.show();

    }


    public void setUserPwd(String userName, String pswd) {
        String url = "http://192.168.4.1:80/config?ssid=" + userName + "&pass=" + pswd;


        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponseMac", response);

                MACID = response;
                wifiManager.disconnect();
                showCustomDialog();
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse - ", error.toString());
                        NetworkResponse response = error.networkResponse;
//                        DebugLog.logTrace(response.statusCode);
                        //  progress.setVisibility(View.GONE);
                    }
                }
        ) {
        };
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(sr);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    public void checkPermission(final Activity activity) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("permissions are necessary !!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET, Manifest.permission.CHANGE_WIFI_STATE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            }
        }
    }

    private BroadcastReceiver  wifireciever1= new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {


            // intent = Objects.requireNonNull(getActivity()).getApplicationContext().registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


            mScanResults = (ArrayList<ScanResult>) wifiManager.getScanResults();
            Log.d("Wifilist", "list" + mScanResults);
            //  DebugLog.logTrace("@#$ @#$ " + String.valueOf(mScanResults));
            wifiListAdapter = new WifilistAdapter(Objects.requireNonNull(getActivity()), mScanResults);
            if (mScanResults != null) {

                lv1.setAdapter(wifiListAdapter);
                wifilistbar.setVisibility(View.GONE);
                refresh.setVisibility(View.VISIBLE);
            } else progressDialog.dismiss();


            wifiListAdapter.notifyDataSetChanged();

        }
    };

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_custom_dialog, viewGroup, false);
        Button button = (Button) dialogView.findViewById(R.id.button);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetConnection()) {
                    addDevice(deviceName, SSID, MACID);
                    alertDialog.dismiss();


                } else
                    Toast.makeText(getActivity(), "Please Connect to Internet  First to Proceed ", Toast.LENGTH_SHORT).show();


            }
        });
        alertDialog.show();

    }

    public void addDevice(final String dname, final String Ssid, final String MACid) {

        progressDialog.show();


        String url;
        url = App.api + "devicelist";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    progressDialog.dismiss();
                    obj = new JSONObject(response);
                    getActivity().finish();


                    Toast.makeText(getActivity(), "Device Registered Successfully", Toast.LENGTH_SHORT).show();


//                    item.set_id(obj.getString());
                    DebugLog.logTrace("response == " + response);

                } catch (Throwable tx) {
                    DebugLog.logTrace(tx.getMessage());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                        DebugLog.logTrace(String.valueOf(error.networkResponse.statusCode));
//                        if (error.networkResponse.statusCode == 400) {
                        Log.d("ErrorMessage", "" + error);

//                        }
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(getActivity(), "Device is already Registered", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getActivity(), "Please Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("macId", MACid);
                params.put("SSID", Ssid);
                params.put("deviceName", dname);
                params.put("deviceType", categoryid);
                //DebugLog.logTrace(MACID);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-access-orgntoken", app.getOrgToken());
                params.put("x-access-token", app.getUserToken());
                return params;
            }
        };
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(sr);
        DebugLog.logTrace(url);

    }

    public boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }





    }

    @Override
    public void onResume() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).registerReceiver(this.wifireciever1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }


        super.onResume();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(this.wifireciever1);

    }
}
