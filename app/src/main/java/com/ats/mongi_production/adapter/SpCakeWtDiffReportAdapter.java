package com.ats.mongi_production.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.FrWiseSpCakeOrd;

import java.util.ArrayList;

public class SpCakeWtDiffReportAdapter extends RecyclerView.Adapter<SpCakeWtDiffReportAdapter.MyViewHolder> {

    private ArrayList<FrWiseSpCakeOrd> resultList;
    private Context context;

    public SpCakeWtDiffReportAdapter(ArrayList<FrWiseSpCakeOrd> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSrNo, tvFrName, tvCode, tvCake, tvFrWt, tvProdWt, tvWtDiff;

        public MyViewHolder(View view) {
            super(view);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            tvFrName = view.findViewById(R.id.tvFrName);
            tvCode = view.findViewById(R.id.tvCode);
            tvCake = view.findViewById(R.id.tvCake);
            tvFrWt = view.findViewById(R.id.tvFrWt);
            tvProdWt = view.findViewById(R.id.tvProdWt);
            tvWtDiff = view.findViewById(R.id.tvWtDiff);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sp_cake_wt_diff_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FrWiseSpCakeOrd model = resultList.get(position);

        holder.tvSrNo.setText("" + (position + 1));
        holder.tvFrName.setText("" + model.getFrName());
        holder.tvCode.setText("" + model.getSpCode());
        holder.tvCake.setText("" + model.getSpName());
        holder.tvFrWt.setText("" + model.getInputKgFr());
        holder.tvProdWt.setText("" + model.getInputKgProd());
        holder.tvWtDiff.setText("" + model.getWeightDiff());

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

}
