package com.ngxtech.homeautomation;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.ngxtech.homeautomation.fragments.GettingStarted;
import com.ngxtech.homeautomation.fragments.PairtoDevice;
import com.ngxtech.homeautomation.fragments.SetupDevice;

import java.util.ArrayList;
import java.util.List;

public class DeviceConfiguration extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_configuration);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        setupViewPager(viewPager);
        int defaultValue = 0;
        int page = getIntent().getIntExtra("TAB", defaultValue);
        viewPager.setCurrentItem(page);

       TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.light_blue));

      // tabLayout.getTabAt(0).setCustomView(R.layout.);
//        tabLayout.getTabAt(1).setCustomView(R.layout.orangeprocess);
//        tabLayout.getTabAt(2).setCustomView(R.layout.shipping);
//        tabLayout.getTabAt(3).setCustomView(R.layout.deliver);
//        tabLayout.getTabAt(4).setCustomView(R.layout.completedtxt);
//        tabLayout.getTabAt(5).setCustomView(R.layout.cancelled);


        viewPager.setCurrentItem(0);
     //   DottedProgressBar progressBar = (DottedProgressBar) findViewById(R.id.progress);
     //   progressBar.startProgress();
      //  progressBar.stopProgress();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new GettingStarted(), "Getting Started");
        adapter.addFrag(new SetupDevice(), "SetupDevice");
        adapter.addFrag(new PairtoDevice(),"Pair to Device");

//        adapter.addFrag(new ShippingFragment(), "Shipping");
//        adapter.addFrag(new DeliveredFragment(), "Delivered");
//        adapter.addFrag(new CompletedFragment(), "Completed");
//        adapter.addFrag(new CancelledFragment(), "Cancelled");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {

            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
