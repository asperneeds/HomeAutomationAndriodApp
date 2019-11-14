package com.ngxtech.homeautomation.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.SwitchDetail;
import com.ngxtech.homeautomation.Switches;
import com.ngxtech.homeautomation.bean.Switch;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.db.DatabaseHelper;
import com.ngxtech.homeautomation.notification.MyNotificationManager;
import com.ngxtech.homeautomation.service.Mymqtt;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SwitchAdapter extends RecyclerView.Adapter<SwitchAdapter.MyViewHolder> {

    private ArrayList<Switch> arrayList;
    private Context context;
    private Switch sw;
    private DatabaseHelper helper;
    private int pos;



    public SwitchAdapter(ArrayList<Switch> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        helper = DatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public SwitchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.switch_item, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SwitchAdapter.MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        Switch sw = arrayList.get(i);
        myViewHolder.switchName.setText(sw.getSwitchName());
     @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor =context.getSharedPreferences("switchmode", MODE_PRIVATE).edit();
       int prevpos=i;
        if(sw.getStatus()) {
        //    MyNotificationManager.getInstance(context).displayNotification(sw.getSwitchName(),"is Turned_On");

            myViewHolder.power_off.setVisibility(View.GONE);
            myViewHolder.power_on.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.power_off.setVisibility(View.VISIBLE);
            myViewHolder.power_on.setVisibility(View.GONE);
        }
        myViewHolder.switchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch switches = arrayList.get(i);
                Intent intent=new Intent(context,SwitchDetail.class);
                Bundle bundle=new Bundle();
                bundle.putInt("position",i);


                intent.putExtras(bundle);
                context.startActivity(intent);


                //  OnEditClick(switches.get_id(), i);
                //   OnClick(switches.get_id(), i);


            }
        });



        myViewHolder.switchonoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                ((Switches) context).readMeter(i,b);
//                if (b) {
              //  Switchon(i,arrayList.get(i),b);
                ((Switches) context).connectcleint(i,b);


                SharedPreferences settings = context.getSharedPreferences("Switchstate", 0);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = settings.edit();

                    if (compoundButton.isPressed()) {
                        for (int j=0;j<i;j++){
                        editor.putBoolean("switchkey", b);
                        editor.putInt("position", j);
                        editor.commit();
                    }
                }



            }
        });
//
        SharedPreferences sharedPreferences = context.getSharedPreferences("Switchstate", 0);

        boolean silent = sharedPreferences.getBoolean("switchkey", false);
        int pos=sharedPreferences.getInt("position",0);
//        for (int k=0;k<pos;k++) {
//            if (pos == prevpos) {
//                prevpos++;
//                myViewHolder.switchonoff.setChecked(silent);
//            }
//        }

       // myViewHolder.switchonoff.setChecked(sharedPreferences.getBoolean("NameOfThingToSave", true));
    }

    private void OnClick(String id, int i) {
        Intent intent=new Intent(context,SwitchDetail.class);
        Bundle bundle=new Bundle();
        bundle.putInt("pos",i);


        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void OnEditClick(String id, int position) {
        showNoteDialog(true, sw, id,position);
    }

    private void Switchon(int position,Switch id,boolean b) {
        Mymqtt mymqtt=Mymqtt.getInstance(context,App.getInstance());
//        SharedPreferences settings = context.getSharedPreferences("Switchstate", 0);
//        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("switchkey", b);
//        editor.putInt("position",position);
//        editor.commit();
       //mymqtt.connectMQtt(position,b,context);
//        Switches switches=new Switches();
//        switches.readMeter1(position,b,context);
      //  ((Switches) context).readMeter(position,b);

         // ((Switches) context).readMeter1(position,b,context);
   //     mymqtt.readMeter(position,b,context);
     //   mymqtt.connectcleint(context);
       // mymqtt.subscribe(context);


        MyNotificationManager.getInstance(context).displayNotification(id.getSwitchName(),"is_Turned_On");
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView switchName;
        ImageView edit;
        android.widget.Switch switchonoff;
        DatabaseHelper helper;
        ImageView power_off, power_on;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            switchName = itemView.findViewById(R.id.switchname);
            power_off = itemView.findViewById(R.id.power_off);
            power_on = itemView.findViewById(R.id.power_on);
//            edit = (ImageView) itemView.findViewById(R.id.e);
            switchonoff = (android.widget.Switch) itemView.findViewById(R.id.switchonoff);
            pos = getAdapterPosition();


        }
    }


    private void showNoteDialog(final boolean shouldUpdate, Switch switches, final String id, final int pos) {

        switches=arrayList.get(pos);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View view = layoutInflaterAndroid.inflate(R.layout.category_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? context.getString(R.string.lbl_new_note_title) : context.getString(R.string.lbl_edit_note_title2));


        if (shouldUpdate) {
              inputNote.setText(switches.getSwitchName());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(context, "Update Switch Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate) {


                    // update note by it's id
                    updateSwitchname(inputNote.getText().toString(), id, pos);
                } else {
                    // create new note
                    //  createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private void updateSwitchname(String swname, String id, int pos) {
//        helper = new DatabaseHelper(context);

        Switch s = new Switch();
        s.setSwitchName(swname);
        helper.UpdateSwitchName(s, id);
        ((Switches)context).refresh();

//        notifyItemChanged(pos);

//        notifyItemRangeChanged(pos, arrayList.size());

//        notifyDataSetChanged();


    }
}
