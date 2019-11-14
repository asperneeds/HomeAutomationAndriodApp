package com.ngxtech.homeautomation.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ConnectivityReceiver;
import com.ngxtech.homeautomation.R;

import java.util.HashMap;
import java.util.Map;




public class ResetPassword extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 0;
    EditText et_mobile;
    Button get_otp;
    SharedPreferences pref;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        et_mobile = findViewById(R.id.et_mobile);
        get_otp = findViewById(R.id.get_otp);
        pref = getApplicationContext().getSharedPreferences("NGX_Admin", MODE_PRIVATE);
        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
        app = (App) getApplication();
        checkPermission(this);
    }

    public void postLogin() {
        String url = App.api + "user/mobile/otp/" + et_mobile.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse - ", response.toString());
                Intent intent = new Intent(ResetPassword.this, OTPSend.class);
                intent.putExtra("mobile", et_mobile.getText().toString());
                startActivity(intent);
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 607) {
                            Toast.makeText(getApplicationContext(), "User account not Activated", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 606) {
                            Toast.makeText(getApplicationContext(), "User has been removed", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                        }
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("user", et_mobile.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-access-token", app.getUserToken());
                params.put("x-access-orgntoken", app.getOrgToken());
                return params;
            }
        };
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(sr);
    }

    public void checkPermission(final Activity activity) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("permission is necessary !!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{
                                    Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            }
        }
    }

    void verify() {
        if (et_mobile.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter User ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ConnectivityReceiver.isConnected()) {
            postLogin();
        } else {
            Toast.makeText(ResetPassword.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(ResetPassword.this, "Check Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
    }
}
