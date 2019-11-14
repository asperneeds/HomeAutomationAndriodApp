package com.ngxtech.homeautomation.utils;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class DebugLog {
    private static boolean debugMode = false;

    public static void logTrace() {
        if (debugMode) {
            final StackTraceElement[] ste = Thread.currentThread()
                    .getStackTrace();
            Log.d(ste[3].getClassName(), ste[3].getMethodName());
        }
    }

    public static void logTrace(String s) {
        if (debugMode) {
            final StackTraceElement[] ste = Thread.currentThread()
                    .getStackTrace();
            Log.d(ste[3].getClassName(), ste[3].getMethodName() + " --- " + s);
        }
    }

    public static void logTrace(int s) {
        if (debugMode) {
            final StackTraceElement[] ste = Thread.currentThread()
                    .getStackTrace();
            Log.d(ste[3].getClassName(), ste[3].getMethodName() + " --- " + s);
        }
    }

    public static void logTrace(double s) {
        if (debugMode) {
            final StackTraceElement[] ste = Thread.currentThread()
                    .getStackTrace();
            Log.d(ste[3].getClassName(), ste[3].getMethodName() + " --- " + s);
        }
    }

    public static void logTrace(boolean s) {
        if (debugMode) {
            final StackTraceElement[] ste = Thread.currentThread()
                    .getStackTrace();
            Log.d(ste[3].getClassName(), ste[3].getMethodName() + " --- " + s);
        }
    }

    public static void logTrace(Bundle b) {
        if (debugMode) {
            final StackTraceElement[] ste = Thread.currentThread()
                    .getStackTrace();
            JSONObject json = new JSONObject();
            Set<String> keys = b.keySet();
            for (String key : keys) {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        json.put(key, b.get(key));
                    } else {
                        json.put(key, JSONObject.wrap(b.get(key)));
                    }
                } catch(JSONException e) {
                   logException(e);
                }
            }
            Log.d(ste[3].getClassName(), ste[3].getMethodName() + " --- " + json.toString());
        }
    }

    public static void logException(Exception e) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        Log.d(ste[3].getClassName(),
                ste[3].getMethodName() + " --- " + e.toString());
        Log.e(ste[3].getMethodName() + " --- ", e.toString(), e);
    }

    public static void logException(String msg, Exception e) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        Log.d(ste[3].getClassName(),
                ste[3].getMethodName() + " --- " + e.toString());
        Log.e(ste[3].getMethodName() + " --- ", msg, e);
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean debugMode) {
        DebugLog.debugMode = debugMode;
    }
}
