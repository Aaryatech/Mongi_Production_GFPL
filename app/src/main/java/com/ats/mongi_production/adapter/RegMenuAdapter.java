package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.AllMenu;

import java.util.ArrayList;

import static com.ats.mongi_production.fragment.DashboardFragment.menuMapReg;

public class RegMenuAdapter  extends RecyclerView.Adapter<RegMenuAdapter.MyViewHolder> {

    private ArrayList<AllMenu> menuList;
    private Context context;
    boolean isTouched = false;

    public RegMenuAdapter(ArrayList<AllMenu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AllMenu model = menuList.get(position);
       // Log.e("Adapter : ", " model : " + model);

        holder.checkBox.setText(model.getMenuTitle());

        if (model.isCheckedStatus()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }


        isTouched = false;
        holder.checkBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {
                        model.setCheckedStatus(true);
                        menuMapReg.put(model.getMenuId(), true);
                    } else {
                        model.setCheckedStatus(false);
                        menuMapReg.put(model.getMenuId(), false);

                        Intent pushNotificationIntent = new Intent();
                        pushNotificationIntent.setAction("Checkbox");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);

                    }

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }



}
