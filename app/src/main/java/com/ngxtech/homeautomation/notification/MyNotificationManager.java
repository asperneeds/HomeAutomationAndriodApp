package com.ngxtech.homeautomation.notification;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ngxtech.homeautomation.Switches;
import com.ngxtech.homeautomation.ui.Login;
import com.ngxtech.homeautomation.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotificationManager {

    private Context mCtx;
    @SuppressLint("StaticFieldLeak")
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }
//
//    public void displayNotification(String title,String body){
//        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(mCtx,Constants.CHANNEL_ID)
//                .setSmallIcon(R.drawable.icon)
//                .setContentTitle(title)
//                .setContentText(body);
//        Intent resultIntent = new Intent(mCtx, Switches.class);
//        PendingIntent Pendingintent=PendingIntent.getActivity(mCtx,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(Pendingintent);
//        @SuppressLint("ServiceCast") NotificationManager mNotifyMgr =
//                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
//
//
////        if (mNotifyMgr != null) {
//////            mNotifyMgr.notify();
////        }
//
//    }

    public void displayNotification(String title,String body) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mCtx)
                        .setSmallIcon(R.drawable.icon1) //set icon for notification
                        .setContentTitle(title) //set title of notification
                        .setContentText(body)//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(mCtx,Switches.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager)mCtx. getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(0, builder.build());
    }

    public void removenotification(){
        NotificationManager manager = (NotificationManager)mCtx. getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancelAll();

    }


}
