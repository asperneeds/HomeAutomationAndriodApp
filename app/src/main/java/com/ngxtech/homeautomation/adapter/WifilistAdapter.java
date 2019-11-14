package com.ngxtech.homeautomation.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ngxtech.homeautomation.R;

import java.util.ArrayList;

public class WifilistAdapter extends BaseAdapter {

    private ArrayList<ScanResult> log_list;
    private final LayoutInflater mLayoutInflater;

    public WifilistAdapter(Context context, ArrayList<ScanResult> model_name) {
        this.log_list = model_name;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return log_list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final WifiViewHolder viewHolder;


        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.wifi_list_item, null);
            viewHolder = new WifiViewHolder();
            viewHolder.wifiName = convertView.findViewById(R.id.wifi_name);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (WifiViewHolder) convertView.getTag();
        }

        viewHolder.wifiName.setText(log_list.get(position).SSID);
        return convertView;
    }
}
