package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.OrderReviewActivity;
import com.ats.mongi_production.model.SpOrder;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SPRouteDetailAdapter extends RecyclerView.Adapter<SPRouteDetailAdapter.MyViewHolder> {

    private ArrayList<SpOrder> resultList;
    private Context context;

    public SPRouteDetailAdapter(ArrayList<SpOrder> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCode, tvName, tvFlavour, tvDelivery, tvSrNo;
        public LinearLayout linearLayout;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            //tvCode = view.findViewById(R.id.tvCode);
            tvName = view.findViewById(R.id.tvName);
            tvFlavour = view.findViewById(R.id.tvFlavour);
            tvDelivery = view.findViewById(R.id.tvDelivery);
            linearLayout = view.findViewById(R.id.linearLayout);
            tvSrNo = view.findViewById(R.id.tvSrNo);
            cardView = view.findViewById(R.id.cardView);
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
        final SpOrder model = resultList.get(position);
        //   Log.e("Adapter : ", " model : " + model);

        // holder.tvCode.setText("" + model.getSpCode());

        if (model.getIsAllocated()==1){

            try{
                String strArr[]=model.getSpCode().split("#",3);
                holder.tvName.setText("" + strArr[1] + " (" + strArr[2] + ")  Category - "+model.getSpName());
            }catch (Exception e){
                holder.tvName.setText("" + model.getSpName() + " (" + model.getSpfName() + ")");
            }


        }else{
            holder.tvName.setText("" + model.getSpName() + " (" + model.getSpfName() + ")");
        }

        //holder.tvName.setText("" + model.getSpName() + " (" + model.getSpfName() + ")");
        holder.tvFlavour.setText("" + model.getSpfName());
        holder.tvDelivery.setText("" + model.getSpDeliveryPlace());
        holder.tvSrNo.setText("Sr No. : " + model.getSrNo());

        try {
            if (model.getIsCharUsed() != null) {
                if (model.getIsCharUsed().equalsIgnoreCase("yes")) {
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue));
                } else {
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                }
            } else {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            }
        }catch (Exception e){
            e.printStackTrace();
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(model);

                Intent intent = new Intent(context, OrderReviewActivity.class);
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
