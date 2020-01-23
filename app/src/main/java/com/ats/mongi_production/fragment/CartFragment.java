package com.ats.mongi_production.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.activity.OrderReviewActivity;
import com.ats.mongi_production.adapter.CartAdapter;
import com.ats.mongi_production.adapter.UserMasterAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.CartListData;
import com.ats.mongi_production.model.Info;
import com.ats.mongi_production.model.StockTransfDetail;
import com.ats.mongi_production.model.StockTransferHeader;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.stockTransferMenuId;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;

public class CartFragment extends Fragment implements View.OnClickListener {

    private TextView tvSubmit;
    private RecyclerView recyclerView;
    public static TextView tvTotal, tvGrandTotal;
    CartAdapter adapter;

    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        tvTitle.setText("Cart");

        tvSubmit = view.findViewById(R.id.tvCart_Submit);
        tvTotal = view.findViewById(R.id.tvCart_Total);
        tvGrandTotal = view.findViewById(R.id.tvCart_GrandTotal);
        recyclerView = view.findViewById(R.id.recyclerView);

        String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_USER);
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        Log.e("User Bean : ", "---------------" + user);

        if (user != null) {
            userId = user.getUserId();
        }

        tvSubmit.setOnClickListener(this);

        if (cartArray.size() > 0) {
            adapter = new CartAdapter(cartArray, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            double totalSum = 0;
            for (int i = 0; i < cartArray.size(); i++) {
                totalSum = totalSum + (cartArray.get(i).getItemRate() * cartArray.get(i).getQuantity());
            }
            //tvTotal.setText("" + Math.round(totalSum));
            tvTotal.setText("" + String.format("%.2f", totalSum));

            tvGrandTotal.setText("" + String.format("%.2f", (totalSum)));

        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvCart_Submit) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String date = sdf.format(System.currentTimeMillis());

            if (cartArray.size() > 0) {

                ArrayList<StockTransfDetail> detailList = new ArrayList<>();

                for (int i = 0; i < cartArray.size(); i++) {

                    CartListData cart = cartArray.get(i);

                    int itemId = Integer.parseInt(cart.getItemId());
                    int catId = Integer.parseInt(cart.getCatId());
                    int subCatId = Integer.parseInt(cart.getSubCatId());
                    int qty = cart.getQuantity();
                    double rate = cart.getItemRate1();
                    double total = rate * qty;

                    StockTransfDetail detail = new StockTransfDetail(0, 0, itemId, catId, subCatId, 0, qty, qty, qty, qty, (float) rate, (float) total, 0, 0, 0, 0f, "");
                    detailList.add(detail);
                }
                StockTransferHeader header = new StockTransferHeader(0, date, stockTransferMenuId, userId, 0, 0, 0, date, date, date, 0, "", 0, 0, 0, "", 0, detailList);

                insertStockTransfer(header);

            } else {
                Toast.makeText(getContext(), "Please add products to cart", Toast.LENGTH_SHORT).show();
            }


        }
    }


    public void insertStockTransfer(StockTransferHeader stockTransferHeader) {

        Log.e("PARAMETER : ", "   StockTransferHeader : " + stockTransferHeader);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<StockTransferHeader> adminDataCall = Constants.myInterface.insertStockTransfer(stockTransferHeader);
            adminDataCall.enqueue(new Callback<StockTransferHeader>() {
                @Override
                public void onResponse(Call<StockTransferHeader> call, Response<StockTransferHeader> response) {
                    try {
                        if (response.body() != null) {
                            StockTransferHeader data = response.body();
                            Log.e("StockTransferHeader  : ", " DATA : " + data);
                            commonDialog.dismiss();

                            if (data.getStockTransfHeaderId() > 0) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                Fragment fragment = new StockTransferFragment();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, fragment, "DashboardFragment");
                                ft.commit();
                            } else {
                                Toast.makeText(getContext(), "Unable To Process", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("Login : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("Login : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<StockTransferHeader> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("Login : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

}
