package com.ats.mongi_production.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.fragment.AddUserFragment;
import com.ats.mongi_production.fragment.CartFragment;
import com.ats.mongi_production.fragment.DashboardFragment;
import com.ats.mongi_production.fragment.FrWiseItemListFragment;
import com.ats.mongi_production.fragment.GateSaleItemsFragment;
import com.ats.mongi_production.fragment.RegularCakeAsSpFragment;
import com.ats.mongi_production.fragment.ReportsFragment;
import com.ats.mongi_production.fragment.SPCheckingFragment;
import com.ats.mongi_production.fragment.SpecialCakeOrderFragment;
import com.ats.mongi_production.fragment.StockTransferFragment;
import com.ats.mongi_production.fragment.UserMasterFragment;
import com.ats.mongi_production.model.CartListData;
import com.ats.mongi_production.model.Info;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.ats.mongi_production.util.PermissionsUtil;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static TextView tvTitle, tvCartCount;
    public static LinearLayout llCart;

    public static int stockTransferMenuId = 0;

    public static ArrayList<CartListData> cartArray = new ArrayList<>();


    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "Production_Dispatch");
    File f;

    User user;

    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }

        createFolder();

        String userStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_USER);
        Gson gson = new Gson();
        user = gson.fromJson(userStr, User.class);
        Log.e("User Bean : ", "---------------" + user);

        try {
            if (user == null) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }else{
                hideItem();
            }

        } catch (Exception e) {
        }


        tvTitle = toolbar.findViewById(R.id.tvTitle);
        llCart = toolbar.findViewById(R.id.llCartLayout);
        tvCartCount = toolbar.findViewById(R.id.tvCartCount);

        llCart.setOnClickListener(this);

        createFolder();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.tvHeader_Name);

        if (user != null) {
            tvName.setText("" + user.getuName());
        }

        try {
            String resultFrg = getIntent().getStringExtra("fragment");
            if (resultFrg.equalsIgnoreCase("SP")) {

                Fragment adf = new SpecialCakeOrderFragment();
                Bundle args = new Bundle();
                args.putString("type","SP");
                adf.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

            }else if (resultFrg.equalsIgnoreCase("ALBUM")) {
                Fragment adf = new SpecialCakeOrderFragment();
                Bundle args = new Bundle();
                args.putString("type","ALBUM");
                adf.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

            } else if (resultFrg.equalsIgnoreCase("REG")) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new RegularCakeAsSpFragment(), "DashboardFragment");
                ft.commit();
            } else {
                if (savedInstanceState == null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new DashboardFragment(), "Home");
                    ft.commit();
                }

            }

        } catch (Exception e) {
            if (savedInstanceState == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new DashboardFragment(), "Home");
                ft.commit();
            }
        }


    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment home = getSupportFragmentManager().findFragmentByTag("Home");
        Fragment dashboardFragment = getSupportFragmentManager().findFragmentByTag("DashboardFragment");
        Fragment userMasterFragment = getSupportFragmentManager().findFragmentByTag("UserMasterFragment");
        Fragment stockTransferFragment = getSupportFragmentManager().findFragmentByTag("StockTransferFragment");
        Fragment gateSaleItemFragment = getSupportFragmentManager().findFragmentByTag("GateSaleItemFragment");
        Fragment reportsFragment = getSupportFragmentManager().findFragmentByTag("ReportsFragment");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (home instanceof DashboardFragment && home.isVisible()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Confirm Action");
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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

        } else if (dashboardFragment instanceof UserMasterFragment && dashboardFragment.isVisible() ||
                dashboardFragment instanceof SpecialCakeOrderFragment && dashboardFragment.isVisible() ||
                dashboardFragment instanceof RegularCakeAsSpFragment && dashboardFragment.isVisible() ||
                dashboardFragment instanceof StockTransferFragment && dashboardFragment.isVisible() ||
                dashboardFragment instanceof SPCheckingFragment && dashboardFragment.isVisible() ||
                dashboardFragment instanceof ReportsFragment && dashboardFragment.isVisible()) {

            Fragment fragment = new DashboardFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "Home");
            ft.commit();

        } else if (userMasterFragment instanceof AddUserFragment && userMasterFragment.isVisible()) {

            Fragment fragment = new UserMasterFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "DashboardFragment");
            ft.commit();

        } else if (stockTransferFragment instanceof GateSaleItemsFragment && stockTransferFragment.isVisible() ||
                stockTransferFragment instanceof FrWiseItemListFragment && stockTransferFragment.isVisible()) {

            Fragment fragment = new StockTransferFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "DashboardFragment");
            ft.commit();

        } else if (gateSaleItemFragment instanceof CartFragment && gateSaleItemFragment.isVisible()) {

            Fragment fragment = new GateSaleItemsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "StockTransferFragment");
            ft.commit();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_dashboard) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DashboardFragment(), "Home");
            ft.commit();
        } else if (id == R.id.nav_user) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserMasterFragment(), "DashboardFragment");
            ft.commit();
        } else if (id == R.id.nav_special_cake_order) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Fragment adf = new SpecialCakeOrderFragment();
            Bundle args = new Bundle();
            args.putString("date", sdf.format(System.currentTimeMillis()));
            args.putString("type","SP");
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

          /*  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new SpecialCakeOrderFragment(), "DashboardFragment");
            ft.commit();*/
        } else if (id == R.id.nav_album_cake_order) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Fragment adf = new SpecialCakeOrderFragment();
            Bundle args = new Bundle();
            args.putString("date", sdf.format(System.currentTimeMillis()));
            args.putString("type","ALBUM");
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

          /*  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new SpecialCakeOrderFragment(), "DashboardFragment");
            ft.commit();*/
        }else if (id == R.id.nav_regular_cake_as_special_order) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Fragment adf = new RegularCakeAsSpFragment();
            Bundle args = new Bundle();
            args.putString("date", sdf.format(System.currentTimeMillis()));
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

            /*  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new RegularCakeAsSpFragment(), "DashboardFragment");
            ft.commit();*/
        } else if (id == R.id.nav_stock_transfer) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new StockTransferFragment(), "DashboardFragment");
            ft.commit();
        } else if (id == R.id.nav_sp_checking) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Fragment adf = new SPCheckingFragment();
            Bundle args = new Bundle();
            args.putString("date", sdf.format(System.currentTimeMillis()));
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

        } else if (id == R.id.nav_report) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ReportsFragment(), "DashboardFragment");
            ft.commit();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Logout");
            builder.setMessage("Are You Sure You Want To Logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    updateUserToken(user.getUserId(), "");

                    CustomSharedPreference.deletePreference(HomeActivity.this);

                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    finish();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateUserToken(int userId, String token) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(HomeActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.updateToken(userId, token);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    Log.e("Response : ", "--------------------" + response.body());
                    commonDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    Log.e("Failure : ", "---------------------" + t.getMessage());
                    t.printStackTrace();

                }
            });

        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llCartLayout) {

            Fragment adf = new CartFragment();
            Bundle args = new Bundle();
            args.putInt("id", stockTransferMenuId);
            adf.setArguments(args);
            HomeActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "GateSaleItemFragment").commit();

        }
    }

    private void hideItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if (user.getuType() == 1) {

            nav_Menu.findItem(R.id.nav_sp_checking).setVisible(false);
            nav_Menu.findItem(R.id.nav_stock_transfer).setVisible(false);
            nav_Menu.findItem(R.id.nav_report).setVisible(false);
            nav_Menu.findItem(R.id.nav_user).setVisible(false);

        } else if (user.getuType() == 2) {

            nav_Menu.findItem(R.id.nav_special_cake_order).setVisible(false);
            nav_Menu.findItem(R.id.nav_regular_cake_as_special_order).setVisible(false);
            nav_Menu.findItem(R.id.nav_stock_transfer).setVisible(false);
            nav_Menu.findItem(R.id.nav_report).setVisible(false);
            nav_Menu.findItem(R.id.nav_user).setVisible(false);

        } else if (user.getuType() == 3) {

            nav_Menu.findItem(R.id.nav_stock_transfer).setVisible(false);
            nav_Menu.findItem(R.id.nav_report).setVisible(false);
            nav_Menu.findItem(R.id.nav_user).setVisible(false);

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_DASH_DATE);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_SP_FROM_DATE);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_SP_TO_DATE);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_REG_FROM_DATE);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_REG_TO_DATE);

        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_ALBUM_FROM_DATE);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_ALBUM_TO_DATE);


        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_SP_MENU_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_SP_MENU_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_SP_MENU_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_SP_MENU_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_DASH_SP_COUNT_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_DASH_REG_COUNT_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_DASH_GATE_SALE_COUNT_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_DASH_CONSUME_COUNT_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_DISPATCH_MENU_ARRAY);

        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_ALBUM_MENU_ARRAY);
        CustomSharedPreference.deletePreferenceByKey(this, CustomSharedPreference.KEY_DASH_ALBUM_COUNT_ARRAY);


    }
}
