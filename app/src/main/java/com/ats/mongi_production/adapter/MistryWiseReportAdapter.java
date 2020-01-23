package com.ats.mongi_production.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.MistryWiseReportModel;

import java.util.ArrayList;

public class MistryWiseReportAdapter extends RecyclerView.Adapter<MistryWiseReportAdapter.MyViewHolder> {

    private ArrayList<MistryWiseReportModel> resultList;
    private Context context;

    public MistryWiseReportAdapter(ArrayList<MistryWiseReportModel> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tvName, tvNoOfCakes, tvTimeReq;

        public MyViewHolder(View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvName = view.findViewById(R.id.tvName);
            tvNoOfCakes = view.findViewById(R.id.tvNoOfCakes);
            tvTimeReq = view.findViewById(R.id.tvTimeReq);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mistry_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MistryWiseReportModel model = resultList.get(position);

        holder.tvSrNo.setText("" + (position + 1));
        holder.tvName.setText("" + model.getEmpName());
        holder.tvNoOfCakes.setText("" + model.getNoOfCakes());
        holder.tvTimeReq.setText("" + model.getTimeRequired());

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}
