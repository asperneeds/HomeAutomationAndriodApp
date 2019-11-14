package com.ngxtech.homeautomation.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.Splash;
import com.ngxtech.homeautomation.bean.UserData;
import com.ngxtech.homeautomation.utils.DebugLog;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    private EditText firstname, lastname, emailid, pwd, cpwd, mobileno, city, state, coutry, pin, address, businessname;
    private Button signup;
   private ProgressDialog pd;
   private App app;
   private Splash splash;
    private   UserData data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstname = (EditText) findViewById(R.id.reg_fname);
        lastname = (EditText) findViewById(R.id.reg_lname);
        emailid = (EditText) findViewById(R.id.reg_email);
        pwd = (EditText) findViewById(R.id.reg_password);
        cpwd = (EditText) findViewById(R.id.reg_confirm_pwd);
        mobileno = (EditText) findViewById(R.id.reg_mobile);

        splash = new Splash();
        app = (App) getApplication();
        signup = (Button) findViewById(R.id.reg_signup);

        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (firstname.getText().toString().isEmpty()) {
                    firstname.setError("Enter First Name");
                }
            }
        });

        mobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus)
                if (lastname.getText().toString().isEmpty()) {
                    lastname.setError("Enter Mobile Number");
                    return;
                }
                if (mobileno.getText().length() > 10 || mobileno.getText().length() < 10) {
                    mobileno.setError("10 digits Number");
                }
            }
        });
        emailid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (emailid.getText().toString().isEmpty()) {
                    emailid.setError("Enter Email ID");
                    return;
                }

            }
        });
        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (pwd.getText().toString().isEmpty()) {
                    pwd.setError("Enter Password");
                }
            }
        });
        cpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (cpwd.getText().toString().isEmpty()) {
                    cpwd.setError("Password");
                    return;
                }
                if (!cpwd.getText().toString().equals(cpwd.getText().toString())) {
                    cpwd.setError("Confirm Password");
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate() {
        if (firstname.getText().toString().isEmpty()) {
            Toast.makeText(Signup.this, "Enter FirstName", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (businessname.getText().toString().isEmpty()) {
//            Toast.makeText(Signup.this, "Enter Business Name", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (emailid.getText().toString().isEmpty()) {
            Toast.makeText(Signup.this, "Enter Email ID", Toast.LENGTH_SHORT).show();
            emailid.setError("Enter Email ID");
            return;
        }

        if (mobileno.getText().toString().isEmpty()) {
            Toast.makeText(Signup.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobileno.getText().length() > 10 || mobileno.getText().length() < 10) {
            Toast.makeText(Signup.this, "Enter 10 digits Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.getText().toString().isEmpty() || cpwd.getText().toString().isEmpty()) {
            Toast.makeText(Signup.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!pwd.getText().toString().equals(cpwd.getText().toString())) {
                Toast.makeText(Signup.this, "Please confirm Password", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        registration();


    }

    private void registration() {
        pd = new ProgressDialog(Signup.this);
        pd.setMessage("Loading...");
        pd.show();

        final String url = App.api+"user/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                DebugLog.logTrace("Response "+ response);
                System.out.println(response);
                Toast.makeText(getApplicationContext(), "Signup Successfully", Toast.LENGTH_SHORT).show();

                Intent i=new Intent(getApplicationContext(), Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();


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
                    Toast.makeText(Signup.this, "Try Again ", Toast.LENGTH_SHORT).show();
                    //}
                }
            }
        })

        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("email", emailid.getText().toString());
                params.put("firstname", firstname.getText().toString().trim());
                params.put("lastname", lastname.getText().toString().trim());
                params.put("mobile", mobileno.getText().toString().trim());
                params.put("password", pwd.getText().toString().trim());
//                params.put("businessname", businessname.getText().toString().trim());
//                params.put("address", address.getText().toString());
//                params.put("city", city.getText().toString().trim());
//                params.put("pin", pin.getText().toString().trim());
//                params.put("state", state.getText().toString().trim());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();
                params.put("x-access-token", app.getUserToken());
                params.put("x-access-orgntoken", app.getOrgToken());
                return params;

            }
        };

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(stringRequest);


    }
}
