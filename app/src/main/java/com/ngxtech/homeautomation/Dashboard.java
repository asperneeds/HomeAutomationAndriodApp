package com.ngxtech.homeautomation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.ngxtech.homeautomation.fragments.FragmentDevices;
import com.ngxtech.homeautomation.fragments.HomeCategory;

import com.ngxtech.homeautomation.fragments.Settings;
import com.ngxtech.homeautomation.adapter.ViewPagerAdapter;

public class Dashboard extends AppCompatActivity  {
    private ViewPager viewPager;


    //Fragments


    Settings profileFragment;
    HomeCategory homeCategaryFragment;

    MenuItem prevMenuItem;


    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        mTextMessage = (TextView) findViewById(R.id.message);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
       // navigation.setOnNavigationItemSelectedListener(this);

       navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               Fragment fragment = null;
               switch (menuItem.getItemId()) {
                   case R.id.navigation_home:
                       viewPager.setCurrentItem(0);
                     //  fragment=new CategoryList();
                       break;
                   case R.id.profile:
                       viewPager.setCurrentItem(1);
                    //   fragment=new HomeCategory();


//                       break;
//                   case R.id.profile:
//
//                     //  viewPager.setCurrentItem(2);
//                    //   fragment=new Settings();
//                       break;



               }
               return loadFragment(fragment);

           }
       }) ;


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {




            }
        });

        setupViewPager(viewPager);

    }


    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setupViewPager(ViewPager viewPager)

    {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        profileFragment = new Settings();
        homeCategaryFragment = new HomeCategory();

        FragmentDevices devices=new FragmentDevices();




        adapter.addFragment(homeCategaryFragment);

      //  adapter.addFragment(devices);

        adapter.addFragment(profileFragment);

        viewPager.setAdapter(adapter);

    }


}