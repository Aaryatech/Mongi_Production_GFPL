package com.ats.mongi_production.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.ats.mongi_production.R;
import com.ats.mongi_production.adapter.UserMasterAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.llCart;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;

public class UserMasterFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    UserMasterAdapter adapter;

    private ArrayList<User> userList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_master, container, false);

        llCart.setVisibility(View.INVISIBLE);

        tvTitle.setText("User");

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);


        getAllUsers();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AddUserFragment(), "UserMasterFragment");
            ft.commit();
        }
    }

    public void getAllUsers() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<User>> listCall = Constants.myInterface.getAllUsers();
            listCall.enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("User Data : ", "------------" + response.body());

                            ArrayList<User> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                //Toast.makeText(SelectCityActivity.this, "No Cities Found !", Toast.LENGTH_SHORT).show();
                            } else {

                                userList.clear();
                                userList = data;

                                adapter = new UserMasterAdapter(userList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);

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
                public void onFailure(Call<ArrayList<User>> call, Throwable t) {
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
