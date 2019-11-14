package com.ngxtech.homeautomation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngxtech.homeautomation.Switches;
import com.ngxtech.homeautomation.bean.DeviceItem;
import com.ngxtech.homeautomation.R;


import java.util.ArrayList;

public class DeviceListItemsAdapter extends RecyclerView.Adapter<DeviceListItemsAdapter.ViewHolder> {


    private ArrayList<DeviceItem> deviceList;
    private Context mContext;
    private Activity activity;


    public DeviceListItemsAdapter(Context context, ArrayList<DeviceItem> list){

        this.deviceList = list;
        this.mContext = context;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.device_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final DeviceItem item=deviceList.get(i);
        viewHolder.device_type.setText(item.getDeviceType());
        viewHolder.deviceName.setText(item.getDeviceName());
        viewHolder.tv_ssid.setText(item.getDeviceMACID());
     //   viewHolder.device_type.setText(item.getDeviceType());
        ((Activity) mContext).getFragmentManager();

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                 Click(deviceList.get(i).get_id(),i);


                }
            });

    }

    private void Click(String id, int i) {
        Intent intent=new Intent(mContext, Switches.class);
        String macid=deviceList.get(i).getDeviceMACID();
        intent.putExtra("macid",macid);
        mContext.startActivity(intent);

    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
//

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName,tv_ssid,edit,share_device,device_type;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name);
           tv_ssid = itemView.findViewById(R.id.tv_ssid);
        //  edit = itemView.findViewById(R.id.edit);
      //    share_device = itemView.findViewById(R.id.share_device);
            device_type = itemView.findViewById(R.id.device_type);
            cardView=(CardView)itemView.findViewById(R.id.card_device);

        }
    }



}
