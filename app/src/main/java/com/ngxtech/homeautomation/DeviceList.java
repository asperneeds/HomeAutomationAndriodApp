package com.ngxtech.homeautomation;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.bean.Category;
import com.ngxtech.homeautomation.bean.DeviceItem;
import com.ngxtech.homeautomation.bean.Switch;
import com.ngxtech.homeautomation.adapter.DeviceListItemsAdapter;
import com.ngxtech.homeautomation.db.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceList extends AppCompatActivity {
   private ArrayList<DeviceItem> deviceList;
    private  App app;
    private  DeviceListItemsAdapter adapter;
    private  ProgressBar progressBar;
    private  FloatingActionButton add;
    private  RecyclerView recyclerView;
    private   String ssid ="";
    private  DatabaseHelper dbHelper;
    private   Category category;
    private   String itemid="";
    Toolbar toolbar;
    View view,view1;
    private  ImageButton back;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        deviceList = new ArrayList<>();
        //add=(FloatingActionButton)findViewById(R.id.addboard);
        recyclerView = (RecyclerView) findViewById(R.id.rv_board);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        add=(FloatingActionButton)findViewById(R.id.addboard);
        category = new Category();
        view=(ImageView)findViewById(R.id.no_devices);
        view1=(TextView)findViewById(R.id.nodevices);
        toolbar=(Toolbar)findViewById(R.id.add_toolbar);

        this.setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (getIntent()!=null){
            itemid = intent.getStringExtra("Itemid");
        }


        WifiManager wifiManager = (WifiManager) Objects.requireNonNull(getApplicationContext()).getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo info = wifiManager.getConnectionInfo();
        ssid = info.getSSID();

        WifiConfiguration wifiConfig = new WifiConfiguration();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeviceList.this));
        app = (App) getApplication();

        dbHelper = DatabaseHelper.getInstance(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DeviceList.this,DeviceConfiguration.class);
                i.putExtra("categoryid",itemid);
                startActivity(i);

            }
        });

    }

    public void getDeviceList() {

       deviceList.clear();
     progressBar.setVisibility(View.VISIBLE);
        String url;
        url = App.api + "devicelist/query/query?";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deviceList.clear();
                String devicetype="";



                JSONArray array = null;
                try {
                    array = new JSONArray(response);
                    Log.d("R", "response" + response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                         DeviceItem item = new DeviceItem();
                         String macid=object.getString("macId");
                           String MACID= macid.substring(0,17);
                        int   noOfSwitches = Integer.parseInt(macid.split("R")[1]);
                      item.setSwitchCount(noOfSwitches);

                        item.setDeviceMACID(object.getString("macId"));
                        item.set_id(object.getString("_id"));
                        try {
                            item.setDeviceName(object.getString("deviceName"));
                        } catch (Exception e) {
                            item.setDeviceName("");
                        }
                        try {
                            devicetype=object.getString("deviceType");
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




                        adapter = new DeviceListItemsAdapter(DeviceList.this, deviceList);


//

                    if (deviceList.size()!=0||adapter.getItemCount()!=0){
                        progressBar.setVisibility(View.GONE);

                        if (!devicetype.equals(itemid)) {
                            progressBar.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            view1.setVisibility(View.VISIBLE);
                        }else {
                            recyclerView.setAdapter(adapter);
                            view.setVisibility(View.GONE);
                            view1.setVisibility(View.GONE);
                        }
                    }else {
                        progressBar.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.VISIBLE);
                    }
//
//
//                            }













                  //  dialog.dismiss();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(DeviceList.this, "No Device Found", Toast.LENGTH_SHORT).show();
//                        DebugLog.logTrace(String.valueOf(error.networkResponse.statusCode));
                        if (error.networkResponse.statusCode == 400) {


                        }
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
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
        super.onResume();
        if (!isInternetConnection()) {
            progressBar.setVisibility(View.GONE);


        }else {
            getDeviceList();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dbHelper != null) {
            dbHelper.close();
        }

    }


}
