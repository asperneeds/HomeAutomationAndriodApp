package com.ngxtech.homeautomation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngxtech.homeautomation.service.TimeService;

import java.util.Objects;

public class SwitchDetail extends AppCompatActivity {
    Toolbar toolbar;

    ImageView timer, shedule, editname, poweron, poweroff;
    TextView on, off, count;
    int pos1;
    String str="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        timer = (ImageView) findViewById(R.id.timer);
        count = (TextView) findViewById(R.id.counter);

        Bundle bundle = getIntent().getExtras();
        pos1 = bundle.getInt("position");

//        SharedPreferences sharedPreferences = getSharedPreferences("Duration", 0);
//        String duration = sharedPreferences.getString("hms", "00:00:00");
//        count.setText(duration);
        // pos1=i.getIntExtra("position");

        //  pos=i.getStringExtra("position");

        //    setSupportActionBar(toolbar);
//        (Objects.requireNonNull(getSupportActionBar())).setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
//            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SwitchDetail.this, TimerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("position", pos1);


                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                // long millisUntilFinished = intent.getLongExtra("countdown", 0);
                // Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
             str = intent.getStringExtra("hms");
                int pos2=intent.getIntExtra("pos",0);
              //  String pos=intent.getStringExtra("pos");
              //  count.setText(String.valueOf(pos2));
                if (pos1==pos2){
                    if (str==null) {
                        count.setText("00:00:00");

                    }else {
                        count.setText(str);
                    }
                }
//

            }
        }




    };


    @Override
    protected void onResume() {
        if (str.equals(""))
        {
            count.setText("00:00:00");
        }
        registerReceiver(broadcastReceiver, new IntentFilter(TimeService.str_receiver));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}