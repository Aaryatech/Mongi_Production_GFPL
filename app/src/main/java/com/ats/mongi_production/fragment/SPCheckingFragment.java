package com.ats.mongi_production.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.adapter.DashboardSPMenuAdapter;
import com.ats.mongi_production.adapter.DispatchMenuAdapter;
import com.ats.mongi_production.adapter.GateSaleCountAdapter;
import com.ats.mongi_production.adapter.RouteWiseCountAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.AllMenu;
import com.ats.mongi_production.model.GateSaleCount;
import com.ats.mongi_production.model.RouteWiseCount;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.fragment.DashboardFragment.dispatchAllMenu;
import static com.ats.mongi_production.fragment.DashboardFragment.dispatchMenuMap;

public class SPCheckingFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    RouteWiseCountAdapter routeWiseCountAdapter;

    long dateMillis;
    int yyyy, mm, dd;

    TextView tvMenu;
    Boolean isTouched = false;
    CheckBox checkBox;
    Dialog dialogMenu;

    DispatchMenuAdapter dispatchMenuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spchecking, container, false);

        HomeActivity.tvTitle.setText("Dispatch Checking");

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);
        if (date == null) {
            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ROUTE_DETAIL_DATE, sdf.format(System.currentTimeMillis()));
        } else {
            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ROUTE_DETAIL_DATE, date);
        }



        Gson gson = new Gson();
        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU);
        Log.e("JSON : ", "--------------------- " + json);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuList = gson.fromJson(json, type);

        if (menuList == null) {
            menuList = new ArrayList<>();
            menuList.add(-1);

            Gson gsn = new Gson();
            String jsnMenu = gsn.toJson(menuList);
            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ROUTE_DETAIL_MENU_LIST, jsnMenu);

        }

      /*  if (menuList == null) {
            Toast.makeText(getContext(), "Please reload data", Toast.LENGTH_SHORT).show();
        } else {
            getRouteWiseCount(sdf.format(System.currentTimeMillis()), menuList);
        }
*/

        try {

            String dt = getArguments().getString("date");
            if (dt == null) {


                getRouteWiseCount(sdf.format(System.currentTimeMillis()), menuList);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DISPATCH_CHECKING_DATE, sdf.format(System.currentTimeMillis()));
            } else {
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DISPATCH_CHECKING_DATE, dt);

                getRouteWiseCount(dt, menuList);
            }

        } catch (Exception e) {
            getRouteWiseCount(sdf.format(System.currentTimeMillis()), menuList);
        }

        return view;
    }

    public void getRouteWiseCount(String date, ArrayList<Integer> menuList) {

        Log.e("PARAMETER : ", "------------ DATE : " + date + "                  MenuList : " + menuList);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<RouteWiseCount>> listCall = Constants.myInterface.getRouteWiseCount(date, menuList);
            listCall.enqueue(new Callback<ArrayList<RouteWiseCount>>() {
                @Override
                public void onResponse(Call<ArrayList<RouteWiseCount>> call, Response<ArrayList<RouteWiseCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ROUTE WISE COUNT : ", "------------" + response.body());

                            ArrayList<RouteWiseCount> data = response.body();

                            routeWiseCountAdapter = new RouteWiseCountAdapter(data, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(routeWiseCountAdapter);

                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<RouteWiseCount>> call, Throwable t) {
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
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            new FilterDialog(getContext()).show();
        }
    }

    public class FilterDialog extends Dialog {

        EditText edDate;
        TextView tvDate;
        ImageView ivClose;
        LinearLayout llChooseMenu;


        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_filter_sp_checking);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edDate = findViewById(R.id.edDate);
            tvDate = findViewById(R.id.tvDate);
            Button btnFilter = findViewById(R.id.btnFilter);
            ivClose = findViewById(R.id.ivClose);
            llChooseMenu = findViewById(R.id.llChooseMenu);
            tvMenu = findViewById(R.id.tvMenu);

            String dt = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DISPATCH_CHECKING_DATE);
            tvDate.setText("" + dt);
            edDate.setText("" + dt);

            llChooseMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dispatchAllMenu != null) {
                        showMenuDialog(dispatchAllMenu);
                    } else {
                        Toast.makeText(getContext(), "Reload Data", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            edDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (dateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(dateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateListener, yr, mn, dy);
                    dialog.show();
                }
            });

            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edDate.getText().toString().isEmpty()) {
                        edDate.setError("Select Date");
                        edDate.requestFocus();
                    } else {
                        dismiss();

                        String date = tvDate.getText().toString();

                        CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ROUTE_DETAIL_DATE, date);

                        Gson gson = new Gson();
                        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU);
                        Type type = new TypeToken<ArrayList<Integer>>() {
                        }.getType();
                        ArrayList<Integer> menuIdArrayList = gson.fromJson(json, type);

                        if (menuIdArrayList == null) {
                            menuIdArrayList = new ArrayList<>();
                            menuIdArrayList.add(-1);
                        }

                        if (menuIdArrayList.size() <= 0) {
                            menuIdArrayList.add(-1);
                        }

                        Gson gsn = new Gson();
                        String jsnMenu = gsn.toJson(menuIdArrayList);
                        CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ROUTE_DETAIL_MENU_LIST, jsnMenu);

                        CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DISPATCH_CHECKING_DATE, date);

                        getRouteWiseCount(date, menuIdArrayList);

                        dismiss();

                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edDate.setText(dd + "-" + mm + "-" + yyyy);
                tvDate.setText(yyyy + "-" + mm + "-" + dd);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE, yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                dateMillis = calendar.getTimeInMillis();
            }
        };

    }

    public void showMenuDialog(final ArrayList<AllMenu> menuArrayList) {
        dialogMenu = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_choose_menu, null, false);
        dialogMenu.setContentView(v);
        dialogMenu.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView tvClear = dialogMenu.findViewById(R.id.tvClear);
        checkBox = dialogMenu.findViewById(R.id.allMenuCheckbox);
        final RecyclerView recyclerView = dialogMenu.findViewById(R.id.recyclerView);
        Button btnSubmit = dialogMenu.findViewById(R.id.btnSubmit);

        dispatchMenuAdapter = new DispatchMenuAdapter(menuArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dispatchMenuAdapter);

       /* final int allMenu = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_DASH_SP_ALL_MENU);
        if (allMenu == 0) {
            cbSPMenu.setChecked(true);
        } else {
            cbSPMenu.setChecked(false);
        }*/

        isTouched = false;
        checkBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isTouched = false;
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DISPATCH_ALL_MENU, 0);

                    if (menuArrayList.size() > 0) {
                        for (int i = 0; i < menuArrayList.size(); i++) {
                            dispatchMenuMap.put(menuArrayList.get(i).getMenuId(), true);
                            menuArrayList.get(i).setCheckedStatus(true);
                        }
                        dispatchMenuAdapter.notifyDataSetChanged();
                    }
                } else {

                    if (isTouched) {
                        isTouched = false;
                        if (menuArrayList.size() > 0) {
                            for (int i = 0; i < menuArrayList.size(); i++) {
                                dispatchMenuMap.put(menuArrayList.get(i).getMenuId(), false);
                                menuArrayList.get(i).setCheckedStatus(false);
                            }
                            dispatchMenuAdapter.notifyDataSetChanged();
                        }
                    }
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DISPATCH_ALL_MENU, 1);


                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuArrayList.size() > 0) {
                    for (int i = 0; i < menuArrayList.size(); i++) {
                        dispatchMenuMap.put(menuArrayList.get(i).getMenuId(), false);
                        menuArrayList.get(i).setCheckedStatus(false);
                    }
                    checkBox.setChecked(false);
                    dispatchMenuAdapter.notifyDataSetChanged();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.dismiss();

                ArrayList<Integer> menuIds = new ArrayList<>();
                for (int i = 0; i < menuArrayList.size(); i++) {
                    if (dispatchMenuMap.get(menuArrayList.get(i).getMenuId())) {
                        menuIds.add(menuArrayList.get(i).getMenuId());
                    }
                }

                Gson gson = new Gson();
                String jsonMenu = gson.toJson(menuIds);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU, jsonMenu);

                if (checkBox.isChecked()) {
                    tvMenu.setText("All Menu");
                } else {
                    tvMenu.setText("");
                }

            }
        });

        dialogMenu.show();
    }


}
