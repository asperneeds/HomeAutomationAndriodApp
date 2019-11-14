package com.ngxtech.homeautomation;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;




public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();

        String abcd, xyz;
        Boolean b = false;

    //    DebugLog.logTrace(data);
        try {
            Object[] pdus = (Object[]) data.get("pdus");

            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                String sender = smsMessage.getDisplayOriginatingAddress();
                //You must check here if the sender is your provider and not another one with same text.

                String messageBody = smsMessage.getMessageBody();


                abcd = messageBody.replaceAll("[^0-9]", "");   // here abcd contains otp which is in number format
                //Pass on the text to our listener.
//            if (b == true) {
//                mListener.messageReceived(abcd);  // attach value to interface object
//            }
//            else
//            {
//            }
//        }

                //Pass on the text to our listener.

                abcd = abcd.substring(0, 4);
                mListener.messageReceived(abcd);
            }
//        }
        }catch (Exception e){
          //  DebugLog.logTrace();
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
