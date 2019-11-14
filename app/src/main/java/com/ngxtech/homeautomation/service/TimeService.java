package com.ngxtech.homeautomation.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.ngxtech.homeautomation.Switches;
import com.ngxtech.homeautomation.TimerActivity;
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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TimeService extends Service {
    public static String str_receiver = "com.ngxtech.receiver";
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    private Handler mHandler = new Handler();
    Intent intent1;
    DatabaseHelper dbHelper;
    private String MACID="";
    MqttAndroidClient  client;
    String clientId;
    CountDownTimer countDownTimer;
    public TimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return  null;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
//        intent1 =new Intent(str_receiver);
//        mpref = getSharedPreferences("Timer",0);
//       final boolean status= mpref.getBoolean("state",false);
//       final int second= mpref.getInt("seconds",0);
//       final int pos= mpref.getInt("pos",0);
//        intent1 =new Intent(str_receiver);
//        dbHelper = DatabaseHelper.getInstance(this);
//        Cursor res = dbHelper.GetDeviceData();
//
//        clientId = MqttClient.generateClientId();
//
//        client = new MqttAndroidClient(getApplicationContext(), "tcp://" + "207.148.79.61" + ":" + "1883",
//                clientId);
//        if (res.getCount() > 0) {
//            res.moveToFirst();
//            do{
//                MACID=res.getString(res.getColumnIndex("devicemacid"));
//            }
//            while (res.moveToNext());
//        }
//
//
//
//        MACID = MACID.substring(0, 17);
//     countDownTimer= new CountDownTimer(second, 1000) {
//            @Override
//            public void onTick(long l) {
//                @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(l),
//                        TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
//                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
//
//         SharedPreferences sharedPreferences=getSharedPreferences("Duration",0);
//         SharedPreferences.Editor editor=sharedPreferences.edit();
//         editor.putString("hms",hms);
//
//                Toast.makeText(getApplicationContext(), "!"+hms, Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onFinish() {
////
//
//             connectMQtt(pos,status);
//
//                Toast.makeText(getApplicationContext(), "Finish!", Toast.LENGTH_SHORT).show();
//
//            }
//        }.start();
        super.onCreate();

    }
    public   void connectMQtt(final int position, final boolean switch_on_off) {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ngxmqtt");
            options.setPassword("NGX@321".toCharArray());
            // String clientId = MqttClient.generateClientId();

       client = new MqttAndroidClient(TimeService.this, "tcp://" + "207.148.79.61" + ":" + "1883",
                    clientId); //207.148.79.61:1883

//            options.setWill("2C:3A:E8:0C:90:16_STS","2C:3A:E8:0C:90:16_STS".getBytes(),2,true);
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            IMqttToken token = client.connect(options);

            DebugLog.logTrace(token.isComplete());
//            subscribe();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "Subscribed Successfully", Toast.LENGTH_SHORT).show();
                            readMeter(position,switch_on_off);

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken,
                                              Throwable exception) {

                            Toast.makeText(getApplicationContext(), " Subscribe failure", Toast.LENGTH_SHORT).show();
                            // some error occurred, this is very unlikely as even if the client
                            //                            // did not had a subscription to the topic the unsubscribe action
                            //
                            // will be successfully
                        }
                    });
                    //  subscribe(context);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    DebugLog.logTrace("on failure " + Arrays.toString(asyncActionToken.getTopics()));
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
//                    tv_mqtt_status.setText("Failed");

                }
            });


        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(),"Data Published Failed",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }



    }
    public void readMeter(int position, boolean switch_on_off) {

        String is_switch_on = "";
        //

        if (switch_on_off) {
            is_switch_on = "SW" + (position + 1) + "_ON";

        } else {
            is_switch_on = "SW" + (position + 1) + "_OFF";


        }

        //  byte[] payload = (deviceItem.getDeviceMACID() + ":16_STS").getBytes();

        try {
         client.publish(MACID + "_CMD", is_switch_on.getBytes(), 2, true);
            Toast.makeText(getApplicationContext(),"Data Published",Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(),"Data Published Failed",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // MyNotificationManager.getInstance(getApplicationContext()).addNotification();
//            client.subscribe()

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


//                if(mSwitch == 1){

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                DebugLog.logTrace(msg);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        intent1 =new Intent(str_receiver);
        mpref = getSharedPreferences("Timer",0);
        final boolean status= mpref.getBoolean("state",false);
        final int second= mpref.getInt("seconds",0);
        final int pos= mpref.getInt("pos",0);
        intent1 =new Intent(str_receiver);
        dbHelper = DatabaseHelper.getInstance(this);
        Cursor res = dbHelper.GetDeviceData();

        clientId = MqttClient.generateClientId();

        client = new MqttAndroidClient(getApplicationContext(), "tcp://" + "207.148.79.61" + ":" + "1883",
                clientId);
        if (res.getCount() > 0) {
            res.moveToFirst();
            do{
                MACID=res.getString(res.getColumnIndex("devicemacid"));
            }
            while (res.moveToNext());
        }



        MACID = MACID.substring(0, 17);
        countDownTimer= new CountDownTimer(second, 1000) {
            @Override
            public void onTick(long l) {
                @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(l),
                        TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                SharedPreferences sharedPreferences=getSharedPreferences("Duration",0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("hms",hms);
                editor.putInt("pos",pos);
                editor.commit();
                intent1.putExtra("hms",hms);
                intent1.putExtra("pos",pos);
                sendBroadcast(intent1);

                Toast.makeText(getApplicationContext(), "!"+hms, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFinish() {
//

                connectMQtt(pos,status);

                Toast.makeText(getApplicationContext(), "Finish!", Toast.LENGTH_SHORT).show();

            }
        }.start();

        return START_STICKY;
    }

//    public boolean stopService(Intent name) {
//        // TODO Auto-generated method stub
//        countDownTimer.cancel();
//      countDownTimer.cancel();
//        return super.stopService(name);
//    }
    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        countDownTimer.cancel();

    }

}
