package com.ngxtech.homeautomation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import com.ngxtech.homeautomation.bean.MQttData;
import com.ngxtech.homeautomation.bean.UserData;
import com.ngxtech.homeautomation.ui.Login;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    App app;
    String token=null;
    UserData data;
    private boolean bHideSystemBar = false;
    protected View mDecorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app = (App)getApplication();
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i;
                if (!app.getUserId().isEmpty()) {
                    i = new Intent(Splash.this,Dashboard.class);
                } else {
//                    FragmentManager f=getSupportFragmentManager();
//                    Login login=new Login();
//                    FragmentTransaction fragmentTransaction=f.beginTransaction();
//                    fragmentTransaction.add(R.id.container,login);

                    i = new Intent(Splash.this, Login.class);


                }

                startActivity(i);



                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    public void getOrgToken() {

        String db = "";
//        if(App.is_testing) {
        db = "7890";
//        }else {
//            db = "123459";
//        }

        StringRequest sr = new StringRequest(Request.Method.GET, App.api + "dbtoken/" + db, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                data=new UserData();
                try {
                    obj = new JSONObject(response);



                    app.setOrgToken(obj.getString("orgntoken"));

                } catch (Throwable tx) {

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(getApplicationContext(), "Check User data", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Try Again ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
             params.put("x-access-orgntoken", app.getOrgToken());
                return params;
            }
        };
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(sr);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getOrgToken();
//        setMQttData();
    }

   public void setMQttData(){
        MQttData settings = new MQttData();
        settings.setPublic_id("207.148.79.61");
        settings.setPort("1883");
        settings.setUser("ngxmqtt");
        settings.setPassword("NGX@321");
        app.setData(settings);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && bHideSystemBar) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            //  | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

