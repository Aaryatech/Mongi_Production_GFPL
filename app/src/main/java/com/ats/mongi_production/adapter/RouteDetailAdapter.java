package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.RegSpOrd;
import com.ats.mongi_production.model.RouteOrderData;
import com.ats.mongi_production.model.SpOrder;

import java.util.ArrayList;

public class RouteDetailAdapter extends RecyclerView.Adapter<RouteDetailAdapter.MyViewHolder> {

    private ArrayList<RouteOrderData> resultList;
    private Context context;

    public RouteDetailAdapter(ArrayList<RouteOrderData> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFrName;
        public RecyclerView recyclerViewSP, recyclerViewREG,recyclerViewAlbum;
        public LinearLayout llSP, llReg,llAlbum;

        public MyViewHolder(View view) {
            super(view);
            tvFrName = view.findViewById(R.id.tvFrName);
            recyclerViewSP = view.findViewById(R.id.recyclerViewSP);
            recyclerViewAlbum = view.findViewById(R.id.recyclerViewAlbum);
            recyclerViewREG = view.findViewById(R.id.recyclerViewREG);
            llSP = view.findViewById(R.id.llSP);
            llAlbum = view.findViewById(R.id.llAlbum);
            llReg = view.findViewById(R.id.llReg);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_route_detail, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RouteOrderData model = resultList.get(position);
      //  Log.e("Adapter : ", " model : " + model);

        holder.tvFrName.setText("" + model.getFrName());

        if (model.getSpCakeOrdList() != null) {
            if (model.getSpCakeOrdList().size() > 0) {
                ArrayList<SpOrder> spList = new ArrayList<>();
                for (int i = 0; i < model.getSpCakeOrdList().size(); i++) {
                    spList.add(model.getSpCakeOrdList().get(i));
                }

                SPRouteDetailAdapter adapterSP = new SPRouteDetailAdapter(spList, context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                holder.recyclerViewSP.setLayoutManager(mLayoutManager);
                holder.recyclerViewSP.setItemAnimator(new DefaultItemAnimator());
                holder.recyclerViewSP.setAdapter(adapterSP);
            } else {
                holder.llSP.setVisibility(View.GONE);
            }
        } else {
            holder.llSP.setVisibility(View.GONE);
        }

        if (model.getSpCakeAlbumOrdList() != null) {
            if (model.getSpCakeAlbumOrdList().size() > 0) {
                ArrayList<SpOrder> spList = new ArrayList<>();
                for (int i = 0; i < model.getSpCakeAlbumOrdList().size(); i++) {
                    spList.add(model.getSpCakeAlbumOrdList().get(i));
                }

                SPRouteDetailAdapter adapterSP = new SPRouteDetailAdapter(spList, context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                holder.recyclerViewAlbum.setLayoutManager(mLayoutManager);
                holder.recyclerViewAlbum.setItemAnimator(new DefaultItemAnimator());
                holder.recyclerViewAlbum.setAdapter(adapterSP);
            } else {
                holder.llAlbum.setVisibility(View.GONE);
            }
        } else {
            holder.llAlbum.setVisibility(View.GONE);
        }

        if (model.getRegSpCakeOrdList() != null) {
            if (model.getRegSpCakeOrdList().size() > 0) {
                ArrayList<RegSpOrd> regList = new ArrayList<>();
                for (int i = 0; i < model.getRegSpCakeOrdList().size(); i++) {
                    regList.add(model.getRegSpCakeOrdList().get(i));
                }

                RegRouteDetailAdapter adapterReg = new RegRouteDetailAdapter(regList, context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                holder.recyclerViewREG.setLayoutManager(mLayoutManager);
                holder.recyclerViewREG.setItemAnimator(new DefaultItemAnimator());
                holder.recyclerViewREG.setAdapter(adapterReg);

            } else {
                holder.llReg.setVisibility(View.GONE);
            }
        } else {
            holder.llReg.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}
