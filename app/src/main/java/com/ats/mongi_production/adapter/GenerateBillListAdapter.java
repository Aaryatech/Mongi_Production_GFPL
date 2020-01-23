package com.ats.mongi_production.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.model.GenerateBill;

import java.util.ArrayList;

import static com.ats.mongi_production.fragment.FrWiseItemListFragment.staticQtyList;

public class GenerateBillListAdapter extends RecyclerView.Adapter<GenerateBillListAdapter.MyViewHolder> {

    private ArrayList<GenerateBill> billList;
    private Context context;

    public GenerateBillListAdapter(ArrayList<GenerateBill> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRate, tvTotal;
        EditText edQty;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvRate = view.findViewById(R.id.tvRate);
            tvTotal = view.findViewById(R.id.tvTotal);
            edQty = view.findViewById(R.id.edQty);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_generate_bill_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GenerateBill model = billList.get(position);
      //  Log.e("Adapter : ", " model : " + model);

        holder.tvName.setText("" + model.getItemName());
        holder.tvRate.setText("" + model.getOrderRate());

        if (model.getNewQty() == 0) {
            holder.edQty.setText("" + model.getOrderQty());
            holder.tvTotal.setText("" + (model.getOrderQty() * model.getOrderRate()));
        } else {
            holder.edQty.setText("" + model.getNewQty());
            holder.tvTotal.setText("" + model.getNewTotal());
        }


        holder.edQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int qty = Integer.parseInt(charSequence.toString());
                    model.setNewQty(qty);
                    staticQtyList.get(position).setNewQty(qty);

                    double total = model.getOrderRate() * qty;
                    model.setNewTotal(total);
                    holder.tvTotal.setText("" + model.getNewTotal());

                } catch (Exception e) {
                    model.setNewQty(0);
                    staticQtyList.get(position).setNewQty(0);
                    holder.tvTotal.setText("" + (model.getOrderQty() * model.getOrderRate()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

}
