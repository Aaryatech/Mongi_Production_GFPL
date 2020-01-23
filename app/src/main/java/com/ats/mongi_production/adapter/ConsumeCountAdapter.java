package com.ats.mongi_production.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.ConsumeCount;

import java.util.ArrayList;

public class ConsumeCountAdapter extends RecyclerView.Adapter<ConsumeCountAdapter.MyViewHolder> {

    private ArrayList<ConsumeCount> countList;
    private Context context;

    public ConsumeCountAdapter(ArrayList<ConsumeCount> countList, Context context) {
        this.countList = countList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCatName, tvSubCatName, tvCount;

        public MyViewHolder(View view) {
            super(view);
            tvCatName = view.findViewById(R.id.tvCatName);
            tvSubCatName = view.findViewById(R.id.tvSubCatName);
            tvCount = view.findViewById(R.id.tvCount);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gate_sale_count, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ConsumeCount model = countList.get(position);
      //  Log.e("Adapter : ", " model : " + model);

        holder.tvCatName.setText("" + model.getCatName());
        holder.tvSubCatName.setText("" + model.getSubCatName());

        holder.tvCount.setText("" + model.getAprQty1());

    }

    @Override
    public int getItemCount() {
        return countList.size();
    }


}
