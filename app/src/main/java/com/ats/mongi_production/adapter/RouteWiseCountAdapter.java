package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.RouteDetailActivity;
import com.ats.mongi_production.model.RouteWiseCount;

import java.util.ArrayList;

public class RouteWiseCountAdapter extends RecyclerView.Adapter<RouteWiseCountAdapter.MyViewHolder> {

    private ArrayList<RouteWiseCount> countList;
    private Context context;

    public RouteWiseCountAdapter(ArrayList<RouteWiseCount> countList, Context context) {
        this.countList = countList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRouteName, tvSPTC, tvSPP, tvSPH, tvREGTC, tvREGP, tvREGH,tvAlbumH,tvAlbumTC,tvAlbumP;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvRouteName = view.findViewById(R.id.tvRouteName);
            tvSPTC = view.findViewById(R.id.tvSPTC);
            tvSPP = view.findViewById(R.id.tvSPP);
            tvSPH = view.findViewById(R.id.tvSPH);
            tvREGTC = view.findViewById(R.id.tvREGTC);
            tvREGP = view.findViewById(R.id.tvREGP);
            tvREGH = view.findViewById(R.id.tvREGH);
            linearLayout = view.findViewById(R.id.linearLayout);

            tvAlbumTC = view.findViewById(R.id.tvAlbumTC);
            tvAlbumP = view.findViewById(R.id.tvAlbumP);
            tvAlbumH = view.findViewById(R.id.tvAlbumH);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sp_checking, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RouteWiseCount model = countList.get(position);
       // Log.e("Adapter : ", " model : " + model);

        holder.tvRouteName.setText("" + model.getRouteName());

        int spPending = model.getSpStatusZeroOrdCount() + model.getSpStatusOneOrdCount();
        int totalSP = spPending + model.getSpStatusTwoOrdCount();

        holder.tvSPTC.setText("TC: " + totalSP);
        holder.tvSPP.setText("P: " + spPending);
        holder.tvSPH.setText("H: " + model.getSpStatusTwoOrdCount());

        int regPending = model.getRegSpStatusZeroOrdCount() + model.getRegSpStatusOneOrdCount();
        int totalReg = regPending + model.getRegSpStatusTwoOrdCount();

        holder.tvREGTC.setText("TC: " + totalReg);
        holder.tvREGP.setText("P: " + regPending);
        holder.tvREGH.setText("H: " + model.getRegSpStatusTwoOrdCount());

        int albumPending = model.getAlbumStatusZeroOrdCount() + model.getAlbumStatusOneOrdCount();
        int totalAlbum = albumPending + model.getAlbumStatusTwoOrdCount();

        holder.tvAlbumTC.setText("TC: " + totalAlbum);
        holder.tvAlbumP.setText("P: " + albumPending);
        holder.tvAlbumH.setText("H: " + model.getAlbumStatusTwoOrdCount());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,RouteDetailActivity.class);
                intent.putExtra("routeId",model.getRouteId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return countList.size();
    }

}
