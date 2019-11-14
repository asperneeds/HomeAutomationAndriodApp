package com.ngxtech.homeautomation.service;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


import android.database.Cursor;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.Switches;
import com.ngxtech.homeautomation.bean.DeviceItem;
import com.ngxtech.homeautomation.bean.Switch;
import com.ngxtech.homeautomation.db.DatabaseHelper;
import com.ngxtech.homeautomation.utils.DebugLog;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Arrays;

public class Mymqtt extends Application {


    ArrayList<Switch> list;
    @SuppressLint("StaticFieldLeak")
    private static  Mymqtt mInstance;

    MqttAndroidClient client1;

    private String MACID="";
    private Context mContext;
    private App app;
    DatabaseHelper dbHelper;
    MqttConnectOptions options = new MqttConnectOptions();
    Switch aSwitch;
    IMqttToken token;

    String deviceid;
    boolean result1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;

    DeviceItem deviceItem;
    int i;
    int swcount;

    ImageView power_off, power_on;
    public Mymqtt(Context mContext, App app){
        this.mContext=mContext;
        this.app=app;
    }
    public static synchronized Mymqtt getInstance(Context mContext,App app) {

        if (mInstance == null) {
            mInstance = new Mymqtt(mContext,app);
        }
        return mInstance;

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        dbHelper = DatabaseHelper.getInstance(this);
        String clientId = MqttClient.generateClientId();

        this.client1 = new MqttAndroidClient(this, "tcp://" + "207.148.79.61" + ":" + "1883",
                clientId);

        Cursor res = dbHelper.GetDeviceData();
        if (res.getCount() > 0) {
            res.moveToFirst();
            do{
                MACID=res.getString(res.getColumnIndex("devicemacid"));
            }
            while (res.moveToNext());
        }



        MACID = MACID.substring(0, 17);
    }
    public void connectcleint(Context context){
        String clientId = MqttClient.generateClientId();

      MqttAndroidClient  client = new MqttAndroidClient(context, "tcp://" + "207.148.79.61" + ":" + "1883",
                clientId); //207.148.79.61:1883



      //  connectMQtt(context);

       client.setCallback(new MqttCallback() {
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
                if(mSwitch <= list.size())
                    list.get(mSwitch-1).setStatus(sStatus);
//                }

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                DebugLog.logTrace(msg);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void readMeter(int position, boolean switch_on_off,Context context) {

        String is_switch_on = "";
        //

        if (switch_on_off) {
            is_switch_on = "SW" + (position + 1) + "_ON";

        } else {
            is_switch_on = "SW" + (position + 1) + "_OFF";


        }

        //  byte[] payload = (deviceItem.getDeviceMACID() + ":16_STS").getBytes();
        try {
            String clientId = MqttClient.generateClientId();

            MqttAndroidClient  client = new MqttAndroidClient(context, "tcp://" + "207.148.79.61" + ":" + "1883",
                    clientId);
            client.publish(MACID + "_CMD", is_switch_on.getBytes(), 2, true);
            // MyNotificationManager.getInstance(getApplicationContext()).addNotification();
//            client.subscribe()
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String msg = new String(message.getPayload());
                    Toast.makeText(Mymqtt.this, msg, Toast.LENGTH_SHORT).show();
                    DebugLog.logTrace(msg);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            Toast.makeText(context,"Data Published",Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            Toast.makeText(context,"Data Published Failed",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    public void subscribe(final Context context) {
        try {
            String clientId = MqttClient.generateClientId();

            MqttAndroidClient  client = new MqttAndroidClient(context, "tcp://" + "207.148.79.61" + ":" + "1883",
                    clientId);
//            IMqttToken subToken = client.subscribe(app.getMQttData().getTopic(), 2);
            IMqttToken subToken = client.subscribe(MACID+"_STS", 2);
//            IMqttToken subToken = client.subscribe("INTOPIC", 2);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(context, "Subscribed Successfully", Toast.LENGTH_SHORT).show();
                    // readMeter();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {

                    Toast.makeText(context, " Subscribe failure", Toast.LENGTH_SHORT).show();
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }




  public   void connectMQtt(final int position, final boolean switch_on_off, final Context context) {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ngxmqtt");
            options.setPassword("NGX@321".toCharArray());
                    String clientId = MqttClient.generateClientId();

      final MqttAndroidClient  client = new MqttAndroidClient(context, "tcp://" + "207.148.79.61" + ":" + "1883",
                clientId); //207.148.79.61:1883

//            options.setWill("2C:3A:E8:0C:90:16_STS","2C:3A:E8:0C:90:16_STS".getBytes(),2,true);
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            IMqttToken token =client.connect(options);

            DebugLog.logTrace(token.isComplete());
//            subscribe();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
//                    readMeter();
                    IMqttToken subToken = null;
                    try {
                        subToken = client.subscribe(MACID+"_STS", 2);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
//            IMqttToken subToken = client.subscribe("INTOPIC", 2);
                    subToken.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Toast.makeText(context, "Subscribed Successfully", Toast.LENGTH_SHORT).show();
                            readMeter(position,switch_on_off,context);

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken,
                                              Throwable exception) {

                            Toast.makeText(context, " Subscribe failure", Toast.LENGTH_SHORT).show();
                            // some error occurred, this is very unlikely as even if the client
                            // did not had a subscription to the topic the unsubscribe action
                            // will be successfully
                        }
                    });
                //  subscribe(context);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    DebugLog.logTrace("on failure " + Arrays.toString(asyncActionToken.getTopics()));
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//                    tv_mqtt_status.setText("Failed");

                }
            });


            } catch (MqttException e) {
                Toast.makeText(context,"Data Published Failed",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }



    }






}


