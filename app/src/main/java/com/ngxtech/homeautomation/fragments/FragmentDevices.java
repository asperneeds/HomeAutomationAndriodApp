package com.ngxtech.homeautomation.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ConnectToDevice;
import com.ngxtech.homeautomation.bean.Category;
import com.ngxtech.homeautomation.bean.DeviceItem;
import com.ngxtech.homeautomation.bean.Switch;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.adapter.DeviceListItemsAdapter;
import com.ngxtech.homeautomation.db.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDevices extends Fragment {
    ArrayList<DeviceItem> deviceList;
    WifiConfiguration wifiConfig;
    WifiManager wifiManager;
    Button button;
    ArrayList<Switch> list;
    TextView textView;
    App app;
    DeviceListItemsAdapter adapter;
    FloatingActionButton add;
    RecyclerView recyclerView;
    String ssid=null;
    Switch aSwitch;
    DatabaseHelper dbHelper;

    Category category;



    public FragmentDevices() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_devicelist, container, false);
        deviceList = new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.rv_device);
        aSwitch=new Switch();
        category =new Category();
        textView=(TextView)view.findViewById(R.id.text2);
        FloatingActionButton button = view.findViewById(R.id.fab1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ConnectToDevice.class);
                startActivity(i);


            }
        });

        wifiManager = (WifiManager) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService (Context.WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo info = wifiManager.getConnectionInfo ();
        ssid  = info.getSSID();
        list=new ArrayList<>();
        wifiConfig = new WifiConfiguration();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        app= (App)getActivity().getApplication();
        dbHelper = DatabaseHelper.getInstance(getActivity());


        return view;
    }
    public void getDeviceList() {
      //  dialog.show();
//        btn_login.setEnabled(false);

        String url;
        url = App.api + "devicelist/query/query?";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                deviceList.clear();

                JSONArray array = null;
                try {
                    array = new JSONArray(response);
                    Log.d("R", "response" + response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        DeviceItem item = new DeviceItem();


                       // item.setSwitchCount(k);

                        item.setDeviceMACID(object.getString("macId"));
                        item.set_id(object.getString("_id"));
                        try {
                            item.setDeviceName(object.getString("deviceName"));
                        } catch (Exception e) {
                            item.setDeviceName("");
                        }
                        try {
                            item.setDeviceType(object.getString("deviceType"));
                        } catch (Exception e) {
                            item.setDeviceType("");
                        }
                        try {
                            item.setDate(object.getString("date"));
                        } catch (Exception e) {
                            item.setDate("");
                        }
                        try {
                            item.setSsid(object.getString("SSID"));
                        } catch (Exception e) {
                            item.setSsid("");
                        }
                        item.setCategory_id(category.getId());
                        deviceList.add(item);
                        dbHelper.insertDeviceData(item);


                    }
                        adapter = new DeviceListItemsAdapter(getActivity(), deviceList);

                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                } catch (Throwable tx) {
                    // DebugLog.logTrace(tx.getMessage());
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pd.dismiss();
//                        DebugLog.logTrace(String.valueOf(error.networkResponse.statusCode));
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(getActivity(), "No Device Found", Toast.LENGTH_SHORT).show();
                        }

                        //  progress.setVisibility(View.GONE);
//                        btn_login.setEnabled(true);
                    }
                }
        ) {

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

    }

    public boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
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
        if (isInternetConnection()) {
          //  dialog.show();
            getDeviceList();


        }else {
          //  dialog.dismiss();
            textView.setText("No Internet Connection");
        }

        super.onResume();
    }
    }

