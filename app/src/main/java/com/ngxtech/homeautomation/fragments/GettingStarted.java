package com.ngxtech.homeautomation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ngxtech.homeautomation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GettingStarted extends Fragment {
    Button next;
    ViewPager viewPager;
    int PAGE = 0;


    public GettingStarted() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_getting__started, container, false);
        next=(Button)view.findViewById(R.id.next);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);


            }
        });




        return view;
    }




}
