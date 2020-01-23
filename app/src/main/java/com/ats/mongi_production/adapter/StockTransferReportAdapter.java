package com.ats.mongi_production.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.MistryWiseReportModel;
import com.ats.mongi_production.model.StockTransfReportModel;

import java.util.ArrayList;

public class StockTransferReportAdapter extends RecyclerView.Adapter<StockTransferReportAdapter.MyViewHolder> {

    private ArrayList<StockTransfReportModel> resultList;
    private Context context;

    public StockTransferReportAdapter(ArrayList<StockTransfReportModel> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tvCatName, tvSubCatName, tvQty, tvType;

        public MyViewHolder(View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvCatName = view.findViewById(R.id.tvCatName);
            tvSubCatName = view.findViewById(R.id.tvSubCatName);
            tvQty = view.findViewById(R.id.tvQty);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_stock_transfer_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final StockTransfReportModel model = resultList.get(position);

        holder.tvSrNo.setText("" + (position + 1));
        holder.tvCatName.setText("" + model.getCatName());
        holder.tvSubCatName.setText("" + model.getSubCatName());
        holder.tvQty.setText("" + model.getQty());

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


}
