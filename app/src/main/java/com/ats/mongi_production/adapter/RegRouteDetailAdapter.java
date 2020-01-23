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
import com.ats.mongi_production.activity.RegOrderReviewActivity;
import com.ats.mongi_production.model.RegSpOrd;
import com.ats.mongi_production.model.SpOrder;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RegRouteDetailAdapter extends RecyclerView.Adapter<RegRouteDetailAdapter.MyViewHolder> {

    private ArrayList<RegSpOrd> resultList;
    private Context context;

    public RegRouteDetailAdapter(ArrayList<RegSpOrd> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCode, tvName, tvFlavour, tvDelivery, tvSrNo;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            // tvCode = view.findViewById(R.id.tvCode);
            tvName = view.findViewById(R.id.tvName);
            tvFlavour = view.findViewById(R.id.tvFlavour);
            tvDelivery = view.findViewById(R.id.tvDelivery);
            linearLayout = view.findViewById(R.id.linearLayout);
            tvSrNo = view.findViewById(R.id.tvSrNo);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sp_route_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RegSpOrd model = resultList.get(position);
        // Log.e("Adapter : ", " model : " + model);

        holder.tvFlavour.setVisibility(View.GONE);

        // holder.tvCode.setText("" + model.getItemId());
        holder.tvName.setText("" + model.getItemName());
        holder.tvDelivery.setText("" + model.getRspPlace());
        holder.tvSrNo.setText("Sr No. : " + model.getSrNo());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(model);

                Intent intent = new Intent(context, RegOrderReviewActivity.class);
                intent.putExtra("model", jsonStr);
                intent.putExtra("dialog", 0);
                intent.putExtra("isDispatch", 1);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}
