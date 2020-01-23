package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.fragment.AddUserFragment;
import com.ats.mongi_production.fragment.UserMasterFragment;
import com.ats.mongi_production.model.Info;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMasterAdapter extends RecyclerView.Adapter<UserMasterAdapter.MyViewHolder> {

    private ArrayList<User> userList;
    private Context context;

    public UserMasterAdapter(ArrayList<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvMobile, tvPass, tvType;
        public ImageView ivMenu;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvPass = view.findViewById(R.id.tvPass);
            tvType = view.findViewById(R.id.tvType);
            ivMenu = view.findViewById(R.id.ivMenu);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_user_master, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User model = userList.get(position);
      //  Log.e("Adapter : ", " model : " + model);

        holder.tvName.setText("" + model.getuName());
        holder.tvMobile.setText("" + model.getuMobNo());
        holder.tvPass.setText("" + model.getuPass());
        holder.tvType.setText("" + model.getuType());

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.crud_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            HomeActivity activity = (HomeActivity) context;
                            Fragment adf = new AddUserFragment();
                            Bundle args = new Bundle();
                            args.putInt("userId", model.getUserId());
                            args.putString("userName", model.getuName());
                            args.putString("userPass", model.getuPass());
                            args.putString("userMobile", model.getuMobNo());
                            args.putInt("userType", model.getuType());
                            args.putInt("userToken", model.getDelStatus());
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "UserMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do You Want To Delete User?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteUser(model.getUserId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void deleteUser(int userId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.deleteUser(userId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Info Data : ", "------------" + response.body());

                            Info data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(context, "unable to process", Toast.LENGTH_SHORT).show();
                            } else {

                                if (!data.isError()) {

                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                    HomeActivity activity = (HomeActivity) context;

                                    Fragment adf = new UserMasterFragment();
                                    Bundle args = new Bundle();
                                    adf.setArguments(args);
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();


                                } else {
                                    Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                }
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(context, "unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "unable to process", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


}
