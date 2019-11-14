package com.ngxtech.homeautomation.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ConnectivityReceiver;
import com.ngxtech.homeautomation.Dashboard;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.db.DatabaseHelper;
import com.ngxtech.homeautomation.utils.DebugLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Button login;
    App app;
    EditText usertext, pwwd;
    SharedPreferences sp;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signup = (TextView) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);
        TextView forgetpwd = (TextView) findViewById(R.id.forgetpassword);
        usertext = (EditText) findViewById(R.id.username);
        pwwd = (EditText) findViewById(R.id.password);

        app = (App) getApplication();
        sp = getSharedPreferences("NgxTECH", Context.MODE_PRIVATE);

        dbHelper = DatabaseHelper.getInstance(Login.this);;



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//


          Intent intent=new Intent(Login.this, Signup.class);
          startActivity(intent);


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();


            }
        });
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(Login.this,ResetPassword.class);
                startActivity(intent);


            }
        });

    }


    public void login() {
        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        String url=App.api+"user/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    app.setUserKey(obj.getString("token"), obj.getString("userId"), obj.getString("accountstatus"));
//                    if (dbHelper.isItemAvailable(Login.this)) {
                     //   dbHelper.Rollback(Login.this);
                   // }
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                   finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                DebugLog.logTrace(String.valueOf(error.networkResponse.statusCode));
                if (error.networkResponse.statusCode == 400) {
                    Toast.makeText(getApplicationContext(), "Entered Wrong User data", Toast.LENGTH_SHORT).show();
                } else if (error.networkResponse.statusCode == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid user type", Toast.LENGTH_SHORT).show();
                } else if (error.networkResponse.statusCode == 402) {
                    Toast.makeText(getApplicationContext(), "Invalid user ", Toast.LENGTH_SHORT).show();
                } else if (error.networkResponse.statusCode == 403) {
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                } else if (error.networkResponse.statusCode == 607) {
                    Toast.makeText(getApplicationContext(), "User account not Activated", Toast.LENGTH_SHORT).show();
                } else if (error.networkResponse.statusCode == 606) {
                    Toast.makeText(getApplicationContext(), "User has been removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Try Again ", Toast.LENGTH_SHORT).show();
                    //}
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user", usertext.getText().toString().trim());
                params.put("password", pwwd.getText().toString().trim());



                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-access-orgntoken", app.getOrgToken());
                return params;
            }
        };


        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(stringRequest);

    }

    boolean findSessionKey() {
        if (sp.getString("token", null) != null || sp.getString("userId", null) != null) {
            return sp.getString("token", null).isEmpty() || sp.getString("userId", null).isEmpty();
        } else {
            return true;
        }
    }


    void verify() {
        if (usertext.getText().toString().isEmpty()) {
            Toast.makeText(Login.this, "Please enter User ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwwd.getText().toString().isEmpty()) {
            Toast.makeText(Login.this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        login();
    }


    @Override
    protected void onResume() {
        if (!findSessionKey()) {
            finish();
        }
        super.onResume();
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(Login.this, "Check Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
    }
}

