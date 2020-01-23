package com.ats.mongi_production.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.adapter.GateSaleItemAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.Item;
import com.ats.mongi_production.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.llCart;
import static com.ats.mongi_production.activity.HomeActivity.stockTransferMenuId;
import static com.ats.mongi_production.activity.HomeActivity.tvCartCount;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;

public class GateSaleItemsFragment extends Fragment implements View.OnClickListener {

    private GridView gvProducts;
    private LinearLayout llCakes, llPackProduct, llSavouries, llTradingMaterial;

    private TextView tvPackItem, tvCake, tvSavouries, tvTradingMaterial, tvCakeIcon, tvSavouriesIcon, tvPackProdIcon, tvTradingMatIcon;
    private EditText edSearch;

    GateSaleItemAdapter adapter;
    private ArrayList<Item> itemArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gate_sale_items, container, false);
        tvTitle.setText("Cakes & Pastries");

        try {
            stockTransferMenuId = getArguments().getInt("id");
        } catch (Exception e) {
        }

        gvProducts = view.findViewById(R.id.gvProducts);
        llCakes = view.findViewById(R.id.llEmpProducts_Cakes);
        llPackProduct = view.findViewById(R.id.llEmpProducts_PackProduct);
        llSavouries = view.findViewById(R.id.llEmpProducts_Savouries);
        edSearch = view.findViewById(R.id.edProduct_Search);
        tvCake = view.findViewById(R.id.tvProduct_Cake);
        tvSavouries = view.findViewById(R.id.tvProduct_Savouries);
        tvPackItem = view.findViewById(R.id.tvProduct_PackItem);
        tvCakeIcon = view.findViewById(R.id.tvProduct_CakeIcon);
        tvSavouriesIcon = view.findViewById(R.id.tvProduct_SavouriesIcon);
        tvPackProdIcon = view.findViewById(R.id.tvProduct_PackItemIcon);
        llTradingMaterial = view.findViewById(R.id.llEmpProducts_TradingMaterial);
        tvTradingMaterial = view.findViewById(R.id.tvProduct_TradingMaterial);
        tvTradingMatIcon = view.findViewById(R.id.tvProduct_TradingMatIcon);

        llCakes.setOnClickListener(this);
        llPackProduct.setOnClickListener(this);
        llSavouries.setOnClickListener(this);
        llTradingMaterial.setOnClickListener(this);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tvCake.setTextColor(getResources().getColor(R.color.colorAccent));
        tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
        tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
        tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
        tvCakeIcon.setTextColor(getResources().getColor(R.color.colorAccent));
        tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));

        getItemList("2");

        return view;
    }


    public void getItemList(String catId) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ArrayList<Item>> arrayListCall = Constants.myInterface.getItemsByCategory(catId);
            arrayListCall.enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<Item> data = response.body();

                            commonDialog.dismiss();
                            itemArray.clear();
                            itemArray = data;

                            for (int i = 0; i < data.size(); i++) {
                                data.get(i).setQty(1);
                            }

                            adapter = new GateSaleItemAdapter(itemArray, getContext());
                            gvProducts.setAdapter(adapter);


                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llEmpProducts_Cakes) {
            getActivity().setTitle("Cakes & Pastries");
            getItemList("2");
            tvCake.setTextColor(getResources().getColor(R.color.colorAccent));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorAccent));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));

        } else if (view.getId() == R.id.llEmpProducts_Savouries) {
            getActivity().setTitle("Savouries");
            getItemList("1");
            tvCake.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorAccent));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorAccent));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (view.getId() == R.id.llEmpProducts_PackProduct) {
            getActivity().setTitle("Packed Products");
            getItemList("4");
            tvCake.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorAccent));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorAccent));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (view.getId() == R.id.llEmpProducts_TradingMaterial) {
            getActivity().setTitle("Trading Materials");
            getItemList("3");
            tvCake.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorAccent));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        llCart.setVisibility(View.VISIBLE);
        if (cartArray.size() > 0) {
            int totalQty = 0;
            for (int i = 0; i < cartArray.size(); i++) {

                totalQty = totalQty + cartArray.get(i).getQuantity();
            }
            tvCartCount.setText("" + totalQty);
        } else {
            tvCartCount.setText("0");
        }

    }


}
