package com.ngxtech.homeautomation;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngxtech.homeautomation.bean.DeviceItem;
import com.ngxtech.homeautomation.bean.Switch;
import com.ngxtech.homeautomation.adapter.SwitchAdapter;
import com.ngxtech.homeautomation.db.DatabaseHelper;
import com.ngxtech.homeautomation.notification.MyNotificationManager;
import com.ngxtech.homeautomation.service.Mymqtt;
import com.ngxtech.homeautomation.utils.DebugLog;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.ArrayList;
import java.util.Arrays;

public class Switches extends AppCompatActivity {
    ArrayList<Switch> list;
    TextView textView;
    App app;
    RecyclerView recyclerView;
    SwitchAdapter switchAdapter;
    DatabaseHelper dbHelper;
    Switch aSwitch;
    String deviceid;
    boolean result1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    final MqttConnectOptions options = new MqttConnectOptions();
    byte[] payload;
    MqttAndroidClient client;
    DeviceItem deviceItem;
    int i;
    int swcount;
    String clientId;
    ImageView power_off, power_on;
    String MACID = "";
    String is_switch_on = "";
    Mymqtt mymqtt;

    //    DeviceItem deviceItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switches);
        recyclerView = (RecyclerView) findViewById(R.id.rvswitches);
        power_off = findViewById(R.id.power_off);
        power_on = findViewById(R.id.power_on);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Switches.this));
        app = (App) getApplication();
        dbHelper = DatabaseHelper.getInstance(this);
        aSwitch = new Switch();
        deviceItem = new DeviceItem();
        list = new ArrayList<>();
        mymqtt = Mymqtt.getInstance(getApplicationContext(),app);


         clientId = MqttClient.generateClientId();

        this.client = new MqttAndroidClient(Switches.this, "tcp://" + "207.148.79.61" + ":" + "1883",
                clientId);

        Cursor res = dbHelper.GetDeviceData();
        if (res.getCount() > 0) {
            res.moveToFirst();
            do{
                swcount = res.getInt(res.getColumnIndex("switchcount"));
                deviceid = res.getString(0);
                MACID=res.getString(res.getColumnIndex("devicemacid"));
//                deviceItem = new DeviceItem();devicemacid
                deviceItem.setDeviceMACID(res.getString(res.getColumnIndex("devicemacid")));
//                if(deviceItem.getDeviceMACID() != null || deviceItem.getDeviceMACID().isEmpty())

            }
            while (res.moveToNext());
        }
        if (!dbHelper.isItemSwitchAvailable(getApplicationContext())) {


            for (i = 1; i <= swcount; i++) {
                aSwitch = new Switch();
                aSwitch.setSwitchName("Switch" + i);
                aSwitch.setDevice_id(deviceid);
                result1 = dbHelper.insertSwitchData(aSwitch);
        }
        }


        MACID = MACID.substring(0, 17);

        checkPermission(this);
    }

    public void connectcleint(int pos,boolean ison_off){
     //207.148.79.61:1883



        connectMQtt(pos,ison_off);

        this.client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                String msg = new String(message.getPayload());
                int mSwitch = Integer.parseInt(msg.substring(2,3));
                String status = msg.split("_")[1];
                boolean sStatus = false;

                if(status.equals("ON")){
                    sStatus = true;


                }


//                if(mSwitch == 1){

                if(mSwitch <= list.size())
                    list.get(mSwitch-1).setStatus(sStatus);
//                }

                switchAdapter.notifyDataSetChanged();
                Toast.makeText(Switches.this, msg, Toast.LENGTH_SHORT).show();
                DebugLog.logTrace(msg);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }


    public void readMeter(int position, boolean switch_on_off) {

        if (switch_on_off) {
            is_switch_on = "SW" + (position + 1) + "_ON";

        } else {
            is_switch_on = "SW" + (position + 1) + "_OFF";
        }

      //  byte[] payload = (deviceItem.getDeviceMACID() + ":16_STS").getBytes();
        try {
      client.publish(MACID + "_CMD", is_switch_on.getBytes(), 2, true);
            // MyNotificationManager.getInstance(getApplicationContext()).addNotification();
//            client.subscribe()
            Toast.makeText(getApplicationContext(),"Data Published",Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(),"Data Published Failed",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        client.setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) {
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                String msg = new String(message.getPayload());
//                Toast.makeText(Switches.this, msg, Toast.LENGTH_SHORT).show();
//                DebugLog.logTrace(msg);
//            }
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });
//        subscribe();
    }


    public void subscribe(final int pos, final boolean ison_off) {
        try {
//            IMqttToken subToken = client.subscribe(app.getMQttData().getTopic(), 2);
            IMqttToken subToken = this.client.subscribe(MACID+"_STS", 2);
//            IMqttToken subToken = client.subscribe("INTOPIC", 2);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Switches.this, "Subscribed Successfully", Toast.LENGTH_SHORT).show();
                    readMeter(pos,ison_off);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {

                    Toast.makeText(Switches.this, " Subscribe failure", Toast.LENGTH_SHORT).show();
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void refresh(){
        list = dbHelper.GetSwitchdata();
        switchAdapter = new SwitchAdapter(list, Switches.this);
        recyclerView.setAdapter(switchAdapter);
        switchAdapter.notifyDataSetChanged();
    }



    public void checkPermission(final Activity activity) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
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
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET, Manifest.permission.CHANGE_WIFI_STATE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            }
        }
    }


    void connectMQtt(final int pos, final boolean ison_off) {
        try {
            options.setUserName("ngxmqtt");
            options.setPassword("NGX@321".toCharArray());
//            options.setWill("2C:3A:E8:0C:90:16_STS","2C:3A:E8:0C:90:16_STS".getBytes(),2,true);
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            IMqttToken token = this.client.connect(options);

            DebugLog.logTrace(token.isComplete());
//            subscribe();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Switches.this, "Connected", Toast.LENGTH_SHORT).show();
//                    readMeter();

                    subscribe(pos,ison_off);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    DebugLog.logTrace("on failure " + Arrays.toString(asyncActionToken.getTopics()));
                    Toast.makeText(Switches.this, "Failed", Toast.LENGTH_SHORT).show();
//                    tv_mqtt_status.setText("Failed");

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
            DebugLog.logException(e);
        }

    }

    @Override
    protected void onResume() {
        list = dbHelper.GetSwitchdata();
        switchAdapter = new SwitchAdapter(list, Switches.this);
        recyclerView.setAdapter(switchAdapter);
        switchAdapter.notifyDataSetChanged();
       // connectcleint();
        super.onResume();
    }
}
