package com.ngxtech.homeautomation.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ConnectivityReceiver;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.bean.UserProfile;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class EditProfile extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    EditText et_first_name, et_last_name, et_email_id, et_password, et_mobile, et_confirm_password;
    EditText et_telephone, et_business_name, et_address, et_city, et_state, et_country, et_pin;
    Button btn_update;
    CircleImageView profile_icon;
    ImageView img_edit_mobile;
    ImageView img_edit_password;
    ProgressDialog pd;
    int PIC_GALLERY = 2;
    UserProfile profile;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_confirm_password.setVisibility(View.GONE);
        et_mobile = findViewById(R.id.et_mobile);
        et_mobile.setEnabled(false);
//        img_edit_password = findViewById(R.id.img_edit_password);
//        img_edit_mobile = findViewById(R.id.img_edit_mobile);
        et_telephone = findViewById(R.id.et_telephone);
        et_business_name = findViewById(R.id.et_business_name);
        et_address = findViewById(R.id.et_address);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_country = findViewById(R.id.et_country);
        et_pin = findViewById(R.id.et_pin);
        btn_update = findViewById(R.id.btn_sign_up);
        et_email_id = findViewById(R.id.et_email);
        et_email_id.setEnabled(false);
        app = (App) getApplication();
        profile = new UserProfile();
        profile = app.getUserProfile();
        profile_icon = findViewById(R.id.profile_icon);
        profile_icon.setVisibility(View.VISIBLE);
        pd = new ProgressDialog(this);
        pd.setMessage("Updating...");
        pd.setCanceledOnTouchOutside(false);
        init();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkConnection()) {
                    return;
                }
                btn_update.setEnabled(false);
                updateProfile();
            }


        });

//        img_edit_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(EditProfile.this, ChangePassword.class);
//                startActivity(intent);
//            }
//        });
//        img_edit_mobile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(EditProfile.this, EditMobileActivity.class);
//                intent.putExtra("mobile", et_mobile.getText().toString());
//                startActivity(intent);
//            }
//        });
    }

    void init() {
        btn_update.setText("Update");
        et_first_name.setText(profile.getFirstName());
        et_last_name.setText(profile.getLastName());
        et_mobile.setText(profile.getMobile());
        et_business_name.setText(profile.getBusinessName());
        et_address.setText(profile.getAddress());
        et_city.setText(profile.getCity());
        et_state.setText(profile.getState());
        et_country.setText(profile.getCountry());
        et_pin.setText(profile.getPin());
        et_email_id.setText(profile.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
        return true;
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PIC_GALLERY);
    }

    private void takePhotoFromCamera() {
        File f = null;
        File mydir = new File(Environment.getExternalStorageDirectory() + "/NGXTech/");
        if (!mydir.exists()) {
            mydir.mkdir();
        }
        try {
          //  DebugLog.logTrace(mydir.getAbsolutePath());
            f = new File(mydir, "Temp.jpg");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (Exception e) {
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
      //  Uri imageUri = FileProvider.getUriForFile(this, authorities, f);
     //   intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA);
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
    }




    public void updateProfile() {
        pd.show();
        String url = App.api;
        StringRequest sr = new StringRequest(Request.Method.PUT, url+"user/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse $b - ", response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    UserProfile profile = new UserProfile();
                    try {
                        profile.setUserName(obj.getString("username"));
                    } catch (Exception e) {
                        profile.setUserName("");
                    }
                    try {
                        profile.setFirstName(obj.getString("firstname"));
                    } catch (Exception e) {
                        profile.setFirstName("");
                    }
                    try {
                        profile.setBusinessName(obj.getString("businessname"));
                    } catch (Exception e) {
                        profile.setBusinessName("");
                    }
                    try {
                        profile.setMobile(obj.getString("mobile"));
                    } catch (Exception e) {
                        profile.setMobile("");
                    }
                    try {
                        profile.setEmail(obj.getString("email"));
                    } catch (Exception e) {
                        profile.setEmail("");
                    }
                    try {
                        profile.setAddress(obj.getString("address"));
                    } catch (Exception e) {
                        profile.setAddress("");
                    }
                    try {
                        profile.setCity(obj.getString("city"));
                    } catch (Exception e) {
                        profile.setCity("");
                    }
                    try {
                        profile.setState(obj.getString("state"));
                    } catch (Exception e) {
                        profile.setState("");
                    }
                    try {
                        profile.setPin(obj.getString("pin"));
                    } catch (Exception e) {
                        profile.setPin("");
                    }
                    try {
                        profile.setCountry(obj.getString("country"));
                    } catch (Exception e) {
                        profile.setCountry("");
                    }
                    try {
                        profile.setProfile_pic(obj.getString("profilepic"));
                    } catch (Exception e) {
                    }

                    app.clearProfileData();
                    app.setProfileData(profile);
//                    relativeLayout.setVisibility(View.GONE);
                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
//                    relativeLayout.setVisibility(View.GONE);
                }
                btn_update.setEnabled(true);
                pd.dismiss();
                Toast.makeText(EditProfile.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.networkResponse.statusCode == 607){
                    Toast.makeText(getApplicationContext(), "User account not Activated", Toast.LENGTH_SHORT).show();
                } else if (error.networkResponse.statusCode == 606){
                    Toast.makeText(getApplicationContext(), "User has been removed", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();

            }}) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", et_email_id.getText().toString().trim());
                params.put("firstname", et_first_name.getText().toString().trim());
                params.put("lastname", et_last_name.getText().toString().trim());
                params.put("businessname", et_business_name.getText().toString().trim());
            //    params.put("mobile", et_mobile.getText().toString().trim());
//                params.put("password", et_password.getText().toString().trim());
                params.put("address", et_address.getText().toString().trim());
                params.put("city", et_city.getText().toString().trim());
                params.put("pin", et_pin.getText().toString().trim());
                params.put("state", et_state.getText().toString().trim());
                params.put("country", et_country.getText().toString().trim());
                params.put("gps", "");
           //     params.put("profilepic", app.getUserProfile().getProfile_pic());
                Log.d("parms ", params.toString());
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
            Toast.makeText(EditProfile.this, "Check Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    private void showSnack(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(this, "Check internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
