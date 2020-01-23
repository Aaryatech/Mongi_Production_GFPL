package com.ats.mongi_production.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.adapter.RouteDetailAdapter;
import com.ats.mongi_production.adapter.RouteWiseCountAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.DashSPCakeCount;
import com.ats.mongi_production.model.RouteOrderData;
import com.ats.mongi_production.model.RouteWiseCount;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    RouteDetailAdapter detailAdapter;

    int routeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Route Details");

        recyclerView = findViewById(R.id.recyclerView);

        routeId = getIntent().getIntExtra("routeId", 0);

        Gson gsonSP = new Gson();
        String jsonSP = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_ROUTE_DETAIL_MENU_LIST);
        Type typeSP = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdList = gsonSP.fromJson(jsonSP, typeSP);

        String date = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_ROUTE_DETAIL_DATE);

        getRouteDetails(date, menuIdList, 0, routeId);


    }


    public void getRouteDetails(String date, ArrayList<Integer> menuList, int orderBy, int routeId) {

        Log.e("PARAMETER : ", "   DATE : " + date + "                   menuList : " + menuList + "             orderBy : " + orderBy + "                  routeId : " + routeId);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<RouteOrderData>> listCall = Constants.myInterface.getRouteDetail(date, date, menuList, orderBy, routeId);
            listCall.enqueue(new Callback<ArrayList<RouteOrderData>>() {
                @Override
                public void onResponse(Call<ArrayList<RouteOrderData>> call, Response<ArrayList<RouteOrderData>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ROUTE DETAIL : ", "------------" + response.body());

                            ArrayList<RouteOrderData> data = response.body();

                            detailAdapter = new RouteDetailAdapter(data, RouteDetailActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RouteDetailActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(detailAdapter);

                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<RouteOrderData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
