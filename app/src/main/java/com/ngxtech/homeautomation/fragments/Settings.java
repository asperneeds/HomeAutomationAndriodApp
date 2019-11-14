package com.ngxtech.homeautomation.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ngxtech.homeautomation.ui.About;
import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.ui.ChangePassword;
import com.ngxtech.homeautomation.ui.Help;
import com.ngxtech.homeautomation.ui.Login;
import com.ngxtech.homeautomation.ui.ProfileDetail;
import com.ngxtech.homeautomation.bean.UserProfile;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.db.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment implements View.OnClickListener {

    TextView name,emaail,mobile,changepwd;
    Button edit;
    TextView logout,changepassword;
    UserProfile profile;
    App app;
    TextView profile_click,profile_click2,privacyPolicy,help,about;
    DatabaseHelper dbHelper;




    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_setting, container, false);

         changepwd=(TextView)view.findViewById(R.id.profile_change_password);

         profile_click=(TextView)view.findViewById(R.id.profileview);

      //   privacyPolicy=(TextView)view.findViewById(R.id.privacypolicy);
//         help=(TextView)view.findViewById(R.id.help);
//         about=(TextView)view.findViewById(R.id.);
         profile=new UserProfile();
        logout=(TextView)view.findViewById(R.id.profile_logout);
        app = (App)getActivity(). getApplication();
        profile=app.getUserProfile();
        dbHelper = DatabaseHelper.getInstance(getContext());



        profile_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ProfileDetail.class);
                startActivity(intent);
            }
        });

//        about.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(),About.class);
//                startActivity(intent);
//            }
//        });
//
//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(getActivity(),Help.class);
//                startActivity(i);
//            }
//        });
//        profile_click2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getActivity(),ProfileDetail.class);
//                startActivity(intent);
//
//            }
//        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        changepwd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(getActivity(), ChangePassword.class);
                 startActivity(intent);
             }
         });



        return view;
    }



    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ngx_launcher_icon));
        alertDialogBuilder.setMessage("Are you sure You want to Logout?");
        alertDialogBuilder.setPositiveButton("Logout",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        app.removeKey();
                      //  dbHelper.removeAll();
//                        try {
//                            LoginManager.getInstance().logOut();
//                        }catch (Exception e){}
                        app.clearProfileData();

                        Toast.makeText(getActivity(), R.string.logout, Toast.LENGTH_SHORT).show();


                        Intent i=new Intent(getActivity(), Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);




                        getActivity().finish();
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


    private void init(UserProfile profile) {
        name.setText(profile.getFirstName());
        emaail.setText(profile.getEmail());
        mobile.setText(profile.getMobile());
    }

    @Override
    public void onClick(View view) {

    }
}
