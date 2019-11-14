package com.ngxtech.homeautomation.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.bean.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileDetail extends AppCompatActivity {


    TextView name,emaail,mobile,changepwd;
   ImageView edit;
    TextView logout;
    UserProfile profile;
    App app;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileinfo);

        name=(TextView)findViewById(R.id.profile_name);
        emaail=(TextView)findViewById(R.id.profile_email);
        mobile=(TextView)findViewById(R.id.profile_phone);
        changepwd=(TextView)findViewById(R.id.profile_change_password);
        edit=(ImageView)findViewById(R.id.profile_edit);
        setTitle("Profile");
//        toolbar=(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

  //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile=new UserProfile();
        app = (App) getApplication();
        profile=app.getUserProfile();



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProfileDetail.this,EditProfile.class);
                startActivity(i);
            }
        });
    }

    private void loadprofile() {

        String url=App.api+"user/";

        final StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject object=null;
                try {
                    object=new JSONObject(response);
                    Log.e("onResponse - ", response);
                    profile.setFirstName(object.getString("firstname"));
                    profile.setEmail(object.getString("email"));
                    profile.setMobile(object.getString("mobile"));

                     app.setProfileData(profile);
                    init(profile);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileDetail.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("parms ", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", app.getUserToken());
                params.put("x-access-orgntoken", app.getOrgToken());
                Log.d("header - ", params.toString());
                return params;
            }
        };
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(stringRequest);
    }
    private void init(UserProfile profile) {
        name.setText(profile.getFirstName());
        emaail.setText(profile.getEmail());
        mobile.setText(profile.getMobile());
    }
    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileDetail.this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ngx_launcher_icon));
        alertDialogBuilder.setMessage("Are you sure You want to Logout?");
        alertDialogBuilder.setPositiveButton("Logout",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        app.removeKey();
//                        try {
////                            LoginManager.getInstance().logOut();
////                        }catch (Exception e){}
                        app.clearProfileData();
                        Toast.makeText(ProfileDetail.this, R.string.logout, Toast.LENGTH_SHORT).show();


                        Intent i=new Intent(ProfileDetail.this, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);



                      finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadprofile();
    }
}
