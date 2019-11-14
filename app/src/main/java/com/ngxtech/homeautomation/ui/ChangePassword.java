package com.ngxtech.homeautomation.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ConnectivityReceiver;
import com.ngxtech.homeautomation.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    EditText currentPassword;
    EditText newPassword;
    EditText confirmPassword;
    Button btn_submit;
    ProgressDialog pd;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = findViewById(R.id.et_current_password);
        newPassword = findViewById(R.id.et_new_password);
        confirmPassword = findViewById(R.id.et_confirm_password);
        btn_submit = findViewById(R.id.btn_submit);
        app = (App) getApplication();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPassword.getText().toString().isEmpty()) {
                    Toast.makeText(ChangePassword.this, "Enter Current Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.getText().toString().isEmpty()) {
                    Toast.makeText(ChangePassword.this, "Enter New Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(ChangePassword.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirmPassword.getText().toString().equals(newPassword.getText().toString())) {
                    Toast.makeText(ChangePassword.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                updatePassword();
            }
        });


    }

    public void updatePassword() {
        btn_submit.setEnabled(false);
        pd.show();
        String url = App.api;
        StringRequest sr = new StringRequest(Request.Method.PUT, url + "user/editpassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse $b - ", response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    btn_submit.setEnabled(true);
                    Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception ignored) {
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse - ", error.toString());
                        if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(ChangePassword.this, "Entered Wrong data", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 607) {
                            Toast.makeText(getApplicationContext(), "User account not Activated", Toast.LENGTH_SHORT).show();
                        } else if (error.networkResponse.statusCode == 606) {
                            Toast.makeText(getApplicationContext(), "User has been removed", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();
                        btn_submit.setEnabled(true);
                    }
                }
                ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("currentpassword", currentPassword.getText().toString());
                params.put("newpassword", newPassword.getText().toString());

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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(ChangePassword.this, "Check Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
    }
}
