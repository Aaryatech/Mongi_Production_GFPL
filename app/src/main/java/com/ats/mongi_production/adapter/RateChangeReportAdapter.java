package com.ats.mongi_production.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.RateChangedModel;

import java.util.ArrayList;

public class RateChangeReportAdapter extends RecyclerView.Adapter<RateChangeReportAdapter.MyViewHolder> {

    private ArrayList<RateChangedModel> resultList;
    private Context context;

    public RateChangeReportAdapter(ArrayList<RateChangedModel> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tvFranchise, tvItem, tvDate;

        public MyViewHolder(View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvFranchise = view.findViewById(R.id.tvFranchise);
            tvItem = view.findViewById(R.id.tvItem);
            tvDate = view.findViewById(R.id.tvDate);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_rate_change_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RateChangedModel model = resultList.get(position);

        holder.tvSrNo.setText("" + (position + 1));
        holder.tvFranchise.setText("" + model.getFrName());
        holder.tvItem.setText("" + model.getSpName());
        holder.tvDate.setText("" + model.getProdDate());

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}
