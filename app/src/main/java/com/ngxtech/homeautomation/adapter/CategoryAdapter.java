package com.ngxtech.homeautomation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngxtech.homeautomation.bean.Category;
import com.ngxtech.homeautomation.DeviceList;
import com.ngxtech.homeautomation.db.DatabaseHelper;
import com.ngxtech.homeautomation.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Category> names;

    public CategoryAdapter(Context context, ArrayList<Category> names) {

        this.context = context;
        this.names = names;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(context).inflate(R.layout.homeitem,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Category obj=names.get(i);
        myViewHolder.name.setText(obj.getCategory_name());

        myViewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(names.get(i).getId());
            }
        });
    }

    private void Click(String id) {
        Intent i=new Intent(context, DeviceList.class);
        i.putExtra("Itemid",id);
        context.startActivity(i);

    }


    @Override
    public int getItemCount() {
        return names.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView name,expand;
        CardView cardView;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);

           name=(TextView)itemView.findViewById(R.id.textViewName);
          cardView=(CardView)itemView.findViewById(R.id.cardcategory);
      //    expand=(TextView)itemView.findViewById(R.id.expand);

        }

    }

}
