package com.ats.mongi_production.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.fragment.FrWiseItemListFragment;
import com.ats.mongi_production.fragment.GateSaleItemsFragment;
import com.ats.mongi_production.model.StockTransferType;

import java.util.ArrayList;

public class StockTransferAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<StockTransferType> stockArray;
    private static LayoutInflater inflater = null;

    public StockTransferAdapter(Context mContext, ArrayList<StockTransferType> stockArray) {
        this.mContext = mContext;
        this.stockArray = stockArray;
        this.inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stockArray.size();
    }

    @Override
    public Object getItem(int position) {
        return stockArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tvName, tvDiscount;
        ImageView ivIcon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.adapter_stock_transfer, null);
        holder.tvName = rowView.findViewById(R.id.tvName);

        holder.tvName.setText("" + stockArray.get(position).getName());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stockArray.get(position).getType() == 0) {
                    HomeActivity activity = (HomeActivity) mContext;
                    Fragment adf = new GateSaleItemsFragment();
                    Bundle args = new Bundle();
                    args.putInt("id", stockArray.get(position).getStockTransfTypeId());
                    adf.setArguments(args);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "StockTransferFragment").commit();
                } else if (stockArray.get(position).getType() == 1) {
                    HomeActivity activity = (HomeActivity) mContext;
                    Fragment adf = new FrWiseItemListFragment();
                    Bundle args = new Bundle();
                    args.putInt("id", stockArray.get(position).getStockTransfTypeId());
                    adf.setArguments(args);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "StockTransferFragment").commit();
                }
            }
        });

        return rowView;
    }
}
