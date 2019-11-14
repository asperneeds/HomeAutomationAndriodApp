package com.ngxtech.homeautomation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.ngxtech.homeautomation.bean.Category;
import com.ngxtech.homeautomation.bean.Switch;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableCatogeryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Category> expandableListTitle;
    private HashMap<String, ArrayList<String>> expandableListDetail;


    public ExpandableCatogeryAdapter(Context context, ArrayList<Category> expandableListTitle, HashMap<String, ArrayList<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {

        return  this.expandableListDetail.get(this.expandableListTitle.get(i))
                .get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
