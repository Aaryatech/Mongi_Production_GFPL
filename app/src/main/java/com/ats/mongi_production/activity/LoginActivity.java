package com.ats.mongi_production.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.fcm.SharedPrefManager;
import com.ats.mongi_production.model.Info;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.ats.mongi_production.util.PermissionsUtil;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edMobile, edPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edMobile = findViewById(R.id.edMobile);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);


        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {

            String mobile = edMobile.getText().toString();
            String pass = edPassword.getText().toString();

            boolean isValidMobile = false, isValidPass = false;

            if (mobile.isEmpty()) {
                edMobile.setError("required");
            } else if (mobile.length() != 10) {
                edMobile.setError("required");
            } else {
                edMobile.setError(null);
                isValidMobile = true;
            }

            if (pass.isEmpty()) {
                edPassword.setError("required");
            } else {
                edPassword.setError(null);
                isValidPass = true;
            }

            if (isValidMobile && isValidPass) {

               // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                doLogin(mobile,pass);
            }

        }
    }


    public void doLogin(String username, String pass) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(LoginActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<User> adminDataCall = Constants.myInterface.doLogin(username, pass);
            adminDataCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    try {
                        if (response.body() != null) {
                            User data = response.body();
                            commonDialog.dismiss();

                            Log.e("Login : ", " DATA : " + data);

                            if (data.getUserId() != 0) {
                                Gson gson = new Gson();
                                String json = gson.toJson(data);
                                CustomSharedPreference.putString(LoginActivity.this, CustomSharedPreference.KEY_USER, json);

                                String token = SharedPrefManager.getmInstance(LoginActivity.this).getDeviceToken();
                                Log.e("Token : ", "---------" + token);
                                updateUserToken(data.getUserId(), token);
                            } else {
                                Toast.makeText(LoginActivity.this, "Unable To Login", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Unable To Login", Toast.LENGTH_SHORT).show();
                            Log.e("Login : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Unable To Login", Toast.LENGTH_SHORT).show();
                        Log.e("Login : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Unable To Login", Toast.LENGTH_SHORT).show();
                    Log.e("Login : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateUserToken(int userId, String token) {
        Log.e("PARAMETER : ","------------ User Id : "+userId+"                       Token : "+token);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(LoginActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.updateToken(userId, token);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    Log.e("Response : ", "--------------------" + response.body());
                    commonDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    Log.e("Failure : ", "---------------------" + t.getMessage());
                    t.printStackTrace();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

        } else {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
