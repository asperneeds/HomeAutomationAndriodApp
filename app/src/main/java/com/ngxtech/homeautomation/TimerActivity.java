package com.ngxtech.homeautomation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ngxtech.homeautomation.db.DatabaseHelper;
import com.ngxtech.homeautomation.service.Mymqtt;
import com.ngxtech.homeautomation.service.TimeService;
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

public class TimerActivity extends AppCompatActivity {

    Button on,off,savestate;
    TextView currenttime,timeend,totaltime,day,hour,minute,timecount;
    NumberPicker np;
    String pos="";
    int pos1;
    int seconds;
    int hours;
    DatabaseHelper dbHelper;
    int minutes,totalminits;
    Context context;
    private String MACID="";
    MqttAndroidClient  client;
    Switches switches;
    String clientId;
    boolean turnon=false;
    String status;
    String str="";
    String hms="";
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        NumberPicker np = findViewById(R.id.numberPicker);
        NumberPicker np1 = findViewById(R.id.hours);
        NumberPicker np2 = findViewById(R.id.minutepicker);
        on=(Button)findViewById(R.id.seton);
       off=(Button)findViewById(R.id.setoFF);
        savestate=(Button)findViewById(R.id.save);
        currenttime=(TextView) findViewById(R.id.currenttime);
       timeend=(TextView) findViewById(R.id.timeend);
       totaltime=(TextView) findViewById(R.id.totaltime);
      day=(TextView) findViewById(R.id.totalday);
       hour=(TextView) findViewById(R.id.totalhour);
       minute=(TextView) findViewById(R.id.totalminute);
        Intent i=getIntent();
        timecount=(TextView)findViewById(R.id.count);
        Bundle bundle=getIntent().getExtras();
        pos1=bundle.getInt("position");
        dbHelper = DatabaseHelper.getInstance(this);
        Cursor res = dbHelper.GetDeviceData();

        clientId = MqttClient.generateClientId();

        client = new MqttAndroidClient(TimerActivity.this, "tcp://" + "207.148.79.61" + ":" + "1883",
                clientId);
        if (res.getCount() > 0) {
            res.moveToFirst();
            do{
                MACID=res.getString(res.getColumnIndex("devicemacid"));
            }
            while (res.moveToNext());
        }



        MACID = MACID.substring(0, 17);
//         switches=new Switches();



        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
       currenttime.setText(today.monthDay + ":"+(today.month+1) + ":"+today.year + "\t"+today.format("%k:%M:%S"));             // Day of the month (1-31)

        np.setMinValue(0);
        np.setMaxValue(30);
        np1.setMinValue(0);
        np1.setMaxValue(23);
        np2.setMinValue(0);
        np2.setMaxValue(59);

        np.setOnValueChangedListener(onValueChangeListener );
        np1.setOnValueChangedListener(onValueChangeListener1 );
        np2.setOnValueChangedListener(onValueChangeListener2 );
        on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                status="true";
                turnon=true;
                on.setBackgroundColor(getColor(R.color.green));
                off.setBackgroundColor(getColor(R.color.colorbackground));
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                status="false";
                turnon=false;
                on.setBackgroundColor(getColor(R.color.colorbackground));
                off.setBackgroundColor(getColor(R.color.red));
            }
        });

        savestate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (status==null){
              Toast.makeText(getApplicationContext(), "Please Select State", Toast.LENGTH_SHORT).show();

             }else {
              //   startService(new Intent(getApplicationContext(), TimeService.class));

              setTimer(pos1, turnon);
             }
              //  }
            }
        });

           getAnyTimer();

    }

    private void getAnyTimer() {
    sp=getSharedPreferences("Duration",0);
        hms = sp.getString("hms", "");
        int position=sp.getInt("pos",0);
        if (pos1==position) {
            if (!hms.equals("")) {


                confirmDialog();

                timecount.setText("00:00:00");

            }
        }

    }

    private void confirmDialog() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                TimerActivity.this);

// Setting Dialog Title
        alertDialog2.setTitle(" Stop Running Timer ");

// Setting Dialog Message
        alertDialog2.setMessage("Are you sure to Stop this Timer\n"+hms);


// Setting Icon to Dialog
//        alertDialog2.setIcon(R.drawable.delete);

// Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        stopService(new Intent(getApplicationContext(), TimeService.class));
                        SharedPreferences sp=getSharedPreferences("Duration",0);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.remove("hms");
                        editor.commit();
                        dialog.cancel();
                    }
                });

// Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                     finish();
                        dialog.cancel();
                    }
                });

// Showing Alert Dialog
        alertDialog2.show();
    }


    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                   int value= numberPicker.getValue();

                       day.setText(String.valueOf(value)+"days:");

                }
            };
    NumberPicker.OnValueChangeListener onValueChangeListener1 =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    hours= numberPicker.getValue();

                        hour.setText(String.valueOf(hours+"hours:"));


                }
            };
    NumberPicker.OnValueChangeListener onValueChangeListener2 =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    minutes= numberPicker.getValue();

                        minute.setText(String.valueOf(minutes)+"minutes");


                }
            };


    private void setTimer(final int pos, final boolean turnon) {


         if (hour.getText().toString().equals("")){

                 seconds = ((1000) * (minutes) * 60);

         } else {
             totalminits=hours*60;
             seconds = ((1000) * (totalminits+minutes) * 60);
         }


         if (seconds!=0) {
             SharedPreferences mpref;
             mpref=getSharedPreferences("Timer",0);
             SharedPreferences.Editor mEditor=mpref.edit();


           mEditor.putInt("seconds",seconds);
           mEditor.putBoolean("state",turnon);
           mEditor.putInt("pos",pos);
           mEditor.commit();
//
//             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                 startForegroundService(new Intent(getApplicationContext(), TimeService.class));
//                 registerReceiver(receiver, new IntentFilter(TimeService.str_receiver));
//             }
             startService(new Intent(getApplicationContext(), TimeService.class));
             registerReceiver(receiver, new IntentFilter(TimeService.str_receiver));
             finish();
//             new CountDownTimer(seconds, 1000) {
//                 @Override
//                 public void onTick(long l) {
//                     @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(l),
//                             TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
//                             TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
//
//                     timecount.setText(hms);
//
//
//                 }
//
//                 @Override
//                 public void onFinish() {
////
//                     connectMQtt(pos, turnon);
//                     Toast.makeText(TimerActivity.this, "Finish!", Toast.LENGTH_SHORT).show();
//
//                 }
//             }.start();

         }else        Toast.makeText(TimerActivity.this, "Please Select Time", Toast.LENGTH_SHORT).show();

    }


    public   void connectMQtt(final int position, final boolean switch_on_off) {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ngxmqtt");
            options.setPassword("NGX@321".toCharArray());
           // String clientId = MqttClient.generateClientId();

           TimerActivity.this.client = new MqttAndroidClient(TimerActivity.this, "tcp://" + "207.148.79.61" + ":" + "1883",
                    clientId); //207.148.79.61:1883

//            options.setWill("2C:3A:E8:0C:90:16_STS","2C:3A:E8:0C:90:16_STS".getBytes(),2,true);
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            IMqttToken token = TimerActivity.this.client.connect(options);

            DebugLog.logTrace(token.isComplete());
//            subscribe();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
//                    readMeter();
                    IMqttToken subToken = null;
                    try {
                        subToken =  TimerActivity.this.client.subscribe(MACID+"_STS", 2);
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
            TimerActivity.this.client.publish(MACID + "_CMD", is_switch_on.getBytes(), 2, true);
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
    BroadcastReceiver receiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
               // long millisUntilFinished = intent.getLongExtra("countdown", 0);
               // Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
                 str=intent.getStringExtra("hms");
                int pos2=intent.getIntExtra("pos",0);
                //  String pos=intent.getStringExtra("pos");
                //  count.setText(String.valueOf(pos2));
                if (pos1==pos2){
                   timecount.setText(str);
                }

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(TimeService.str_receiver));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
