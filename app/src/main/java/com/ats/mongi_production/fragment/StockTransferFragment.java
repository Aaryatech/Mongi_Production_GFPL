package com.ats.mongi_production.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.adapter.SpecialCakeOrderAdapter;
import com.ats.mongi_production.adapter.StockTransferAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.SPCakeOrder;
import com.ats.mongi_production.model.StockTransferType;
import com.ats.mongi_production.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.llCart;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;

public class StockTransferFragment extends Fragment {

    private GridView gridView;

    StockTransferAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_transfer, container, false);
        tvTitle.setText("Stock Transfer");

        llCart.setVisibility(View.INVISIBLE);

        gridView = view.findViewById(R.id.gridView);

        getStockTransferType();

        return view;
    }


    public void getStockTransferType() {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<StockTransferType>> listCall = Constants.myInterface.getStockTransferType();
            listCall.enqueue(new Callback<ArrayList<StockTransferType>>() {
                @Override
                public void onResponse(Call<ArrayList<StockTransferType>> call, Response<ArrayList<StockTransferType>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("STOCK TRANSFER ", "DATA ------------" + response.body());

                            ArrayList<StockTransferType> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                //Toast.makeText(SelectCityActivity.this, "No Cities Found !", Toast.LENGTH_SHORT).show();
                            } else {

                                adapter = new StockTransferAdapter(getContext(), data);
                                gridView.setAdapter(adapter);

                                commonDialog.dismiss();

                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<StockTransferType>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        llCart.setVisibility(View.INVISIBLE);
        cartArray.clear();

    }

}
