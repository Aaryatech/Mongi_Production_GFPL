package com.ats.mongi_production.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.ats.mongi_production.R;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.activity.HomeActivity.llCart;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;

public class AddUserFragment extends Fragment implements View.OnClickListener {

    private EditText edUserName, edMobile, edPass;
    private Button btnSubmit;
    private Spinner spinner;

    int userId, userType;
    String userName, mobile, pass, token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        tvTitle.setText("Add User");

        llCart.setVisibility(View.INVISIBLE);

        edUserName = view.findViewById(R.id.edUserName);
        edMobile = view.findViewById(R.id.edMobile);
        edPass = view.findViewById(R.id.edPass);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        spinner = view.findViewById(R.id.spinner);

        btnSubmit.setOnClickListener(this);

        ArrayList<String> typeArray = new ArrayList<>();
        typeArray.add("1");
        typeArray.add("2");
        typeArray.add("3");
        typeArray.add("4");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, typeArray);
        spinner.setAdapter(typeAdapter);

        try {
            userId = getArguments().getInt("userId");
            userName = getArguments().getString("userName");
            mobile = getArguments().getString("userMobile");
            pass = getArguments().getString("userPass");
            userType = getArguments().getInt("userType");
            token = getArguments().getString("userToken");

            if (userId != 0) {
                edUserName.setText("" + userName);
                edMobile.setText("" + mobile);
                edPass.setText("" + pass);
            }

            if (userType > 0) {
                spinner.setSelection(userType - 1);
            } else {
                spinner.setSelection(0);
            }


        } catch (Exception e) {
        }


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {

            String name = edUserName.getText().toString();
            String mob = edMobile.getText().toString();
            String pwd = edPass.getText().toString();
            int type = spinner.getSelectedItemPosition();

            boolean isValidName = false, isValidMob = false, isValidPass = false;

            if (name.isEmpty()) {
                edUserName.setError("required");
            } else {
                edUserName.setError(null);
                isValidName = true;
            }

            if (mob.isEmpty()) {
                edMobile.setError("required");
            } else if (mob.length() != 10) {
                edMobile.setError("required");
            } else {
                edMobile.setError(null);
                isValidMob = true;
            }

            if (pwd.isEmpty()) {
                edPass.setError("required");
            } else {
                edPass.setError(null);
                isValidPass = true;
            }

            if (isValidName && isValidMob && isValidPass) {

                int uType = type + 1;
                if (token == null) {
                    token = "token";
                }

                User user = new User(userId, name, mob, pwd, uType, token, 0);
                addNewUser(user);

            }


        }
    }

    public void addNewUser(User model) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<User> listCall = Constants.myInterface.saveUser(model);
            listCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("User Data : ", "------------" + response.body());

                            User data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
                            } else {

                                if (data.getUserId() != 0) {

                                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                    Fragment adf = new UserMasterFragment();
                                    Bundle args = new Bundle();
                                    adf.setArguments(args);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();


                                } else {
                                    Toast.makeText(getContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                }
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


}
