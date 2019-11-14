package com.ngxtech.homeautomation.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ConnectivityReceiver;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.SmsListener;
import com.ngxtech.homeautomation.SmsReceiver;


import java.util.HashMap;
import java.util.Map;


public class OTPSend extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    EditText otp;
    Button send;
    SharedPreferences pref;
    String mobile = "";
    App app;
    Button submit;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    EditText newPassword;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_send);
        otp = findViewById(R.id.et_otp);
        send = findViewById(R.id.btn_send);
        submit = findViewById(R.id.btn_submit);
        relativeLayout = findViewById(R.id.relative1);
        newPassword = findViewById(R.id.et_password);
        confirmPassword = findViewById(R.id.et_confirm_password);
        linearLayout = findViewById(R.id.linearLayout2);
        app = (App) getApplication();
        pref = getApplicationContext().getSharedPreferences("NGX_Admin", MODE_PRIVATE);
        if (getIntent().getExtras() != null) {
            mobile = getIntent().getExtras().getString("mobile");
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    postOTP();
                } else {
                    Toast.makeText(OTPSend.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    if (newPassword.getText().toString().isEmpty()) {
                        Toast.makeText(OTPSend.this, "Enter New Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (confirmPassword.getText().toString().isEmpty()) {
                        Toast.makeText(OTPSend.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                        Toast.makeText(OTPSend.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setNewPassword();
                } else {
                    Toast.makeText(OTPSend.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
           //     DebugLog.logTrace(messageText);
                Toast.makeText(OTPSend.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                otp.setText(messageText);
            }
        });
    }

    public void postOTP() {
        Log.d("parameters ", mobile + " " + otp.getText().toString());
        String url = App.api + "user/verifyotp";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                linearLayout.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 607) {
                            Toast.makeText(getApplicationContext(), "User account not Activated", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 606) {
                            Toast.makeText(getApplicationContext(), "User has been removed", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(getApplicationContext(), "Wrong Mobile Number", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 401) {
                            Toast.makeText(getApplicationContext(), "Wrong OTP Entered", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 402) {
                            Toast.makeText(getApplicationContext(), "OTP Expired", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 403) {
                            Toast.makeText(getApplicationContext(), "OTP Already Used", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("otp", otp.getText().toString());
                Log.d("parm ", params.toString());
                return params;
            }

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

    void setNewPassword() {
        Log.d("parameters ", mobile + " " + otp.getText().toString());
        String url = App.api + "user/forgetpassword";
        StringRequest sr = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse - ", response);
                Intent intent = new Intent(OTPSend.this, Login.class);
                startActivity(intent);
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse - ", error.toString());
                        // TODO: 05-02-2018 if internet connectivity issue then show error msg as Please connect internet
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(getApplicationContext(), "Please Check the entered number", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Try Again ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("newpassword", newPassword.getText().toString());
                Log.d("parm ", params.toString());
                return params;
            }

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
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(OTPSend.this, "Check Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
    }
}
