package com.ats.mongi_production.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.MistryWiseItemWiseModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MistryWiseItemWiseReportAdapter extends RecyclerView.Adapter<MistryWiseItemWiseReportAdapter.MyViewHolder> {

    private ArrayList<MistryWiseItemWiseModel> resultList;
    private Context context;

    public MistryWiseItemWiseReportAdapter(ArrayList<MistryWiseItemWiseModel> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tvMistry, tvItem, tvStartTime, tvEndTime;

        public MyViewHolder(View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvMistry = view.findViewById(R.id.tvMistry);
            tvItem = view.findViewById(R.id.tvItem);
            tvStartTime = view.findViewById(R.id.tvStartTime);
            tvEndTime = view.findViewById(R.id.tvEndTime);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mistry_wise_item_wise_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MistryWiseItemWiseModel model = resultList.get(position);

        holder.tvSrNo.setText("" + (position + 1));
        holder.tvMistry.setText("" + model.getEmpName());
        holder.tvItem.setText("" + model.getItemName());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

        Date startDate = new Date();
        startDate.setTime(model.getStartTimeStamp());

        Date endDate = new Date();
        endDate.setTime(model.getEndTimeStamp());

        holder.tvStartTime.setText("" + sdf.format(startDate.getTime()));
        holder.tvEndTime.setText("" + sdf.format(endDate.getTime()));

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}

