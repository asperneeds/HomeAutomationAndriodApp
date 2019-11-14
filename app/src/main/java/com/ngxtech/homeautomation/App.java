package com.ngxtech.homeautomation;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ngxtech.homeautomation.bean.MQttData;
import com.ngxtech.homeautomation.bean.UserProfile;
import com.ngxtech.homeautomation.service.Mymqtt;

public class App extends Application {
    public static final String TAG = App.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private static App mInstance;
    SharedPreferences sp;
    public static String user = "user";
    public static String password = "password";
    public static String USER_ID = "userId";
    public static String USER_STATUS = "user_status";
    public static String USER_TOKEN = "token";
    public static String api;
    public static String ORG_TOKEN = "org_token";
    public static String public_id = "public_id";
    public static String port = "port";

    public static String topic = "topic";
    public static String payroll = "payroll";

    public static String PUBLIC_IP = "207.148.79.61";
    Mymqtt mymqtt;



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//
//        mymqtt = Mymqtt.getInstance(getApplicationContext(),this);
//        mymqtt.connectcleint();


        sp=getSharedPreferences("NgxTECH",Context.MODE_PRIVATE);
        api="http://207.148.79.61:3008/api/app/";

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public static synchronized App getInstance() {
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setUserKey(String token, String user_id,String status) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(USER_ID, user_id);
        edit.putString(USER_TOKEN, token);
        edit.putString(USER_STATUS, status);
        edit.apply();
        sp.getString(USER_ID, "");
    }
    public void setOrgToken(String orgToken){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ORG_TOKEN, orgToken);
        editor.apply();
    }
    public String getOrgToken(){
        return sp.getString(ORG_TOKEN,"");
    }

    void setData(MQttData data){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(public_id,data.getPublic_id());
        editor.putString(port,data.getPort());
        editor.putString(user,data.getUser());
        editor.putString(password,data.getPassword());
//        editor.putString(topic,data.getTopic());
//        editor.putString(payroll,data.getPayroll());
        editor.apply();
    }
    public MQttData getMQttData(){
        MQttData data = new MQttData();
        data.setPassword(sp.getString(password,""));
        data.setPort(sp.getString(port,""));
        data.setUser(sp.getString(user,""));
        data.setPublic_id(PUBLIC_IP);
//        data.setTopic(sp.getString(topic,""));
//        data.setPayroll(sp.getString(payroll,""));

        return data;
    }

    public String getUserToken() {
        return sp.getString(USER_TOKEN, "");
    }

    public String getMqttPublicId() {
        return PUBLIC_IP;
    }


    public void setData(com.ngxtech.homeautomation.bean.UserData data){
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(user,data.getUser());
        editor.putString(password,data.getPassword());
        editor.apply();
    }

    public com.ngxtech.homeautomation.bean.UserData getData(){

        com.ngxtech.homeautomation.bean.UserData data=new com.ngxtech.homeautomation.bean.UserData();
        data.setUser_id(sp.getString("user_id",data.getUser_id()));
        data.setUser(sp.getString("user",data.getUser()));
        data.setPassword(sp.getString("password",data.getPassword()));
        return data;



    }
    public void removeKey() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_TOKEN, "");
        editor.putString(USER_ID, "");
        editor.apply();
        Log.d("sp:..", sp.getString(USER_ID, null));
        Log.d("sp:..", sp.getString(USER_TOKEN, null));
    }
    public void clearProfileData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("user_name");
        editor.remove("first_name");
        editor.remove("last_name");
        editor.remove("user_email");
        editor.remove("user_mobile");
        editor.remove("business_name");
        editor.remove("user_profile_pic");
        editor.remove("user_city");
        editor.remove("user_address");
        editor.remove("user_state");
        editor.remove("user_pin");
        editor.remove("profile_pic");
        editor.remove("country");
        editor.remove("imageUrl");
        editor.apply();
    }
    public String getUserId() {
        return sp.getString(USER_ID, "");
    }

    public void setProfileData(UserProfile profile) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_name", profile.getUserName());
        editor.putString("first_name", profile.getFirstName());
        editor.putString("last_name", profile.getLastName());
        editor.putString("business_name", profile.getBusinessName());
        editor.putString("user_email", profile.getEmail());
        editor.putString("user_mobile", profile.getMobile());
        editor.putString("user_profile_pic", profile.getProfile_pic());
        editor.putString("user_pin", profile.getPin());
        editor.putString("user_state", profile.getState());
        editor.putString("user_city", profile.getCity());
        editor.putString("user_address", profile.getAddress());
        editor.putString("user_country", profile.getCountry());
        editor.putString("profile_pic", profile.getProfile_pic());
        editor.putString("imageUrl", profile.getImageUrl());
        editor.apply();
    }

    public UserProfile getUserProfile() {
        UserProfile profile = new UserProfile();
        profile.setUserName(sp.getString("user_name", null));
        profile.setFirstName(sp.getString("first_name", null));
        profile.setLastName(sp.getString("last_name", null));
        profile.setBusinessName(sp.getString("business_name", null));
        profile.setEmail(sp.getString("user_email", null));
        profile.setMobile(sp.getString("user_mobile", null));
        profile.setProfile_pic(sp.getString("user_profile_pic", null));
        profile.setCity(sp.getString("user_city", null));
        profile.setAddress(sp.getString("user_address", null));
        profile.setState(sp.getString("user_state", null));
        profile.setPin(sp.getString("user_pin", null));
        profile.setCountry(sp.getString("user_country", null));
        profile.setProfile_pic(sp.getString("profile_pic", null));
        profile.setImageUrl(sp.getString("imageUrl", null));
        return profile;
    }
}
