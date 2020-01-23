package com.ats.mongi_production.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.adapter.MenuAdapter;
import com.ats.mongi_production.adapter.SpecialCakeOrderAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.filterview.ChipView;
import com.ats.mongi_production.model.AllMenu;
import com.ats.mongi_production.model.DashSPCakeCount;
import com.ats.mongi_production.model.SPCakeOrder;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.ats.mongi_production.util.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.llCart;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;
import static com.ats.mongi_production.fragment.DashboardFragment.menuMap;
import static com.ats.mongi_production.fragment.DashboardFragment.staticAllMenu;

import static com.ats.mongi_production.activity.HomeActivity.tvTitle;
import static com.ats.mongi_production.fragment.DashboardFragment.staticDashAllMenuAlbum;
import static com.ats.mongi_production.fragment.DashboardFragment.staticDashAllMenuSP;

public class SpecialCakeOrderFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fab;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private EditText edSearch;

    SpecialCakeOrderAdapter orderAdapter;

    ArrayList<SPCakeOrder> orderList = new ArrayList<>();


    Dialog dialogSpinner;
    CheckBox checkBox;

    MenuAdapter adapterMenu;
    Boolean isTouched = false;
    FlowLayout flowLayout;
    TextView tvMenu;

    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    ArrayList<Integer> menuIdArrayList;

    String fromDate, toDate;

    String fragType="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_cake_order, container, false);


        try{

            fragType = getArguments().getString("type");

            if (fragType.equalsIgnoreCase("SP")){
                HomeActivity.tvTitle.setText("SP");
            }else if (fragType.equalsIgnoreCase("ALBUM")){
                HomeActivity.tvTitle.setText("ALBUM");
            }

        }catch (Exception e){
            e.printStackTrace();
        }



        llCart.setVisibility(View.INVISIBLE);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        edSearch = view.findViewById(R.id.edSearch);

        tvTitle = view.findViewById(R.id.tvTitle);
        recyclerView = view.findViewById(R.id.recyclerView);


        Gson gson = new Gson();
        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_MENU);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        menuIdArrayList = gson.fromJson(json, type);

        if (menuIdArrayList == null) {
            menuIdArrayList = new ArrayList<>();
            menuIdArrayList.add(-1);
        }

        if (menuIdArrayList.size() <= 0) {
            menuIdArrayList.add(-1);
        }

        ArrayList<Integer> slotList = new ArrayList<>();
        slotList.add(0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try {
            String dt = getArguments().getString("date");

            Log.e("SAVED DATE SP ", "---------------------------***********-------------------------- " + dt);


            if (fragType.equalsIgnoreCase("SP")) {

                if (dt == null) {
                    fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_FROM_DATE);
                    toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_TO_DATE);

                    if (fromDate == null) {
                        fromDate = sdf.format(calendar.getTimeInMillis());
                    }

                    if (toDate == null) {
                        toDate = sdf.format(calendar.getTimeInMillis());
                    }
                } else {
                    fromDate = dt;
                    toDate = dt;
                    CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_SP_FROM_DATE, fromDate);
                    CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_SP_TO_DATE, fromDate);
                }

            }else if (fragType.equalsIgnoreCase("ALBUM")){

                if (dt == null) {
                    fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_FROM_DATE);
                    toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_TO_DATE);

                    if (fromDate == null) {
                        fromDate = sdf.format(calendar.getTimeInMillis());
                    }

                    if (toDate == null) {
                        toDate = sdf.format(calendar.getTimeInMillis());
                    }
                } else {
                    fromDate = dt;
                    toDate = dt;
                    CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ALBUM_FROM_DATE, fromDate);
                    CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ALBUM_TO_DATE, fromDate);
                }
            }

        } catch (Exception e) {

            if (fragType.equalsIgnoreCase("SP")) {
                fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_FROM_DATE);
                toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_TO_DATE);
            }else if (fragType.equalsIgnoreCase("ALBUM")){
                fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_FROM_DATE);
                toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_TO_DATE);
            }

            if (fromDate == null) {
                fromDate = sdf.format(calendar.getTimeInMillis());
            }

            if (toDate == null) {
                toDate = sdf.format(calendar.getTimeInMillis());
            }

        }


        int sortType=2;
        if (fragType.equalsIgnoreCase("SP")) {
            sortType = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_SP_SORT_SLOT);
        }else if (fragType.equalsIgnoreCase("ALBUM")){
            sortType = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_ALBUM_SORT_SLOT);
        }



        if (fragType.equalsIgnoreCase("SP")){


            int sortOrder = 1;
            if (sortType == 1) {
                slotList.clear();
                slotList.add(0);
                slotList.add(1);
                sortOrder = 0;
                tvTitle.setText("SP Order Sequence");
            } else if (sortType == 2) {
                slotList.clear();
                slotList.add(0);
                sortOrder = 1;
                tvTitle.setText("SP Production Sequence");
            } else if (sortType == 3) {
                slotList.clear();
                slotList.add(1);
                sortOrder = 1;
                tvTitle.setText("SP Margipan");
            } else {
                slotList.clear();
                slotList.add(0);
                sortOrder = 1;
            }


            Gson gson11 = new Gson();
            String json11 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU);
            Type type11 = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            ArrayList<Integer> menuIdArrayListSP = gson11.fromJson(json11, type11);

            Log.e("DASH FILTER SP MENU ","----------------------------  1116 ********************* ----------------------------------- "+menuIdArrayListSP);

            if (menuIdArrayListSP == null) {
                menuIdArrayListSP = new ArrayList<>();
                menuIdArrayListSP.add(-1);
            }

            if (menuIdArrayListSP.size() <= 0) {
                menuIdArrayListSP.add(-1);
            }



            getDashSPCount(fromDate, menuIdArrayListSP);

            getSPCakeOrders(fromDate, toDate, menuIdArrayListSP, slotList, sortOrder);


        }else if (fragType.equalsIgnoreCase("ALBUM")){


            int sortOrder = 1;
            if (sortType == 1) {
                slotList.clear();
                slotList.add(0);
                slotList.add(1);
                sortOrder = 0;
                tvTitle.setText("Album Order Sequence");
            } else if (sortType == 2) {
                slotList.clear();
                slotList.add(0);
                sortOrder = 1;
                tvTitle.setText("Album Production Sequence");
            } else if (sortType == 3) {
                slotList.clear();
                slotList.add(1);
                sortOrder = 1;
                tvTitle.setText("Album Margipan");
            } else {
                slotList.clear();
                slotList.add(0);
                sortOrder = 1;
            }


            Gson gsonA = new Gson();
            String jsonA = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU);
            Type typeA = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            ArrayList<Integer> menuIdArrayListAlbum = gsonA.fromJson(jsonA, typeA);

            Log.e("DASH FILTER ALBUM MENU ","----------------------------  1116 ********************* ----------------------------------- "+menuIdArrayListAlbum);

            if (menuIdArrayListAlbum == null) {
                menuIdArrayListAlbum = new ArrayList<>();
                menuIdArrayListAlbum.add(-1);
            }

            if (menuIdArrayListAlbum.size() <= 0) {
                menuIdArrayListAlbum.add(-1);
            }



            getDashAlbumCount(fromDate, menuIdArrayListAlbum);

            getAlbumCakeOrders(fromDate, toDate, menuIdArrayListAlbum, slotList, sortOrder);


        }







        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return view;
    }

    void filter(String text) {
        ArrayList<SPCakeOrder> temp = new ArrayList();
        for (SPCakeOrder d : orderList) {
            if (d.getFrName().toLowerCase().equalsIgnoreCase(text.toLowerCase()) || d.getFrName().toLowerCase().contains(text.toLowerCase()) || String.valueOf(d.getSrNo()).equalsIgnoreCase(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        orderAdapter.updateList(temp);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            new FilterDialog(getContext()).show();
        }
    }

    public void getSPCakeOrders(String fromDate, String toDate, ArrayList<Integer> menuIdList, ArrayList<Integer> isSlotUsed, int isOrderBy) {

        Log.e("SP PARAMETER : ", "---------------- From Date : " + fromDate + "                      To Date : " + toDate + "              Menu ID List : " + menuIdList + "                     Slot list : " + isSlotUsed + "                Is Order By : " + isOrderBy);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<SPCakeOrder>> listCall = Constants.myInterface.getSPCakeOrder(fromDate, toDate, menuIdList, isSlotUsed, isOrderBy);
            listCall.enqueue(new Callback<ArrayList<SPCakeOrder>>() {
                @Override
                public void onResponse(Call<ArrayList<SPCakeOrder>> call, Response<ArrayList<SPCakeOrder>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SP Data : ", "------------" + response.body());

                            ArrayList<SPCakeOrder> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();

                                orderList.clear();

                                orderAdapter = new SpecialCakeOrderAdapter(orderList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(orderAdapter);

                                //Toast.makeText(SelectCityActivity.this, "No Cities Found !", Toast.LENGTH_SHORT).show();
                            } else {

                                if (data.size() > 0) {
                                    orderList.clear();
                                    orderList = data;
                                    for (int i = 0; i < orderList.size(); i++) {
                                        if (orderList.get(i).getStartTimeStamp() == null) {
                                            orderList.get(i).setStartTimeStamp(0l);
                                        }
                                        if (orderList.get(i).getEndTimeStamp() == null) {
                                            orderList.get(i).setEndTimeStamp(0l);
                                        }
                                    }
                                    orderAdapter = new SpecialCakeOrderAdapter(orderList, getContext());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(orderAdapter);
                                } else {

                                    Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();

                                    orderList.clear();

                                    orderAdapter = new SpecialCakeOrderAdapter(orderList, getContext());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(orderAdapter);
                                }


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
                public void onFailure(Call<ArrayList<SPCakeOrder>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }



    public void getAlbumCakeOrders(String fromDate, String toDate, ArrayList<Integer> menuIdList, ArrayList<Integer> isSlotUsed, int isOrderBy) {

        Log.e("ALBUM PARAMETER : ", "---------------- From Date : " + fromDate + "                      To Date : " + toDate + "              Menu ID List : " + menuIdList + "                     Slot list : " + isSlotUsed + "                Is Order By : " + isOrderBy);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<SPCakeOrder>> listCall = Constants.myInterface.getAlbumCakeOrder(fromDate, toDate, menuIdList, isSlotUsed, isOrderBy);
            listCall.enqueue(new Callback<ArrayList<SPCakeOrder>>() {
                @Override
                public void onResponse(Call<ArrayList<SPCakeOrder>> call, Response<ArrayList<SPCakeOrder>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ALBUM Data : ", "------------" + response.body());

                            ArrayList<SPCakeOrder> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();

                                orderList.clear();

                                orderAdapter = new SpecialCakeOrderAdapter(orderList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(orderAdapter);

                                //Toast.makeText(SelectCityActivity.this, "No Cities Found !", Toast.LENGTH_SHORT).show();
                            } else {

                                if (data.size() > 0) {
                                    orderList.clear();
                                    orderList = data;
                                    for (int i = 0; i < orderList.size(); i++) {
                                        if (orderList.get(i).getStartTimeStamp() == null) {
                                            orderList.get(i).setStartTimeStamp(0l);
                                        }
                                        if (orderList.get(i).getEndTimeStamp() == null) {
                                            orderList.get(i).setEndTimeStamp(0l);
                                        }
                                    }
                                    orderAdapter = new SpecialCakeOrderAdapter(orderList, getContext());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(orderAdapter);
                                } else {

                                    Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();

                                    orderList.clear();

                                    orderAdapter = new SpecialCakeOrderAdapter(orderList, getContext());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(orderAdapter);
                                }


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
                public void onFailure(Call<ArrayList<SPCakeOrder>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMenuDialog(final ArrayList<AllMenu> menuArrayList) {
        dialogSpinner = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_choose_menu, null, false);
        dialogSpinner.setContentView(v);
        dialogSpinner.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView tvClear = dialogSpinner.findViewById(R.id.tvClear);
        checkBox = dialogSpinner.findViewById(R.id.allMenuCheckbox);
        final RecyclerView recyclerView = dialogSpinner.findViewById(R.id.recyclerView);
        Button btnSubmit = dialogSpinner.findViewById(R.id.btnSubmit);

        adapterMenu = new MenuAdapter(menuArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterMenu);

      /*  final int allMenu = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_ALL_MENU);
        if (allMenu == 0) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
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
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_ALL_MENU, 0);

                    if (menuArrayList.size() > 0) {
                        for (int i = 0; i < menuArrayList.size(); i++) {

                            menuMap.put(menuArrayList.get(i).getMenuId(), true);
                            menuArrayList.get(i).setCheckedStatus(true);
                        }
                        adapterMenu.notifyDataSetChanged();
                    }
                } else {

                    if (isTouched) {
                        Log.e("CHECKBOX", "-----------ISTOUCHED");
                        isTouched = false;
                        if (menuArrayList.size() > 0) {
                            for (int i = 0; i < menuArrayList.size(); i++) {
                                menuMap.put(menuArrayList.get(i).getMenuId(), false);
                                menuArrayList.get(i).setCheckedStatus(false);
                            }
                            adapterMenu.notifyDataSetChanged();
                        }
                    }
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_ALL_MENU, 1);


                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSpinner.dismiss();

                ArrayList<Integer> menuIds = new ArrayList<>();
                for (int i = 0; i < menuArrayList.size(); i++) {
                    if (menuMap.get(menuArrayList.get(i).getMenuId())) {
                        Log.e("MENU MAP : ", "--------------------------FOR----------------- " + menuMap.get(menuArrayList.get(i).getMenuId()));
                        menuIds.add(menuArrayList.get(i).getMenuId());
                    }
                }

                Log.e("Selected Ids", "--------------" + menuIds);

                Gson gson = new Gson();
                String jsonMenu = gson.toJson(menuIds);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_MENU, jsonMenu);

                if (checkBox.isChecked()) {
                    tvMenu.setText("All Menu");
                } else {
                    tvMenu.setText("");
                }


            }
        });

        dialogSpinner.show();
    }

    public void displayMenuChips() {
        Gson gson = new Gson();
        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_MENU);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();

        Log.e("JSON", "------------COMPANY----------" + json);

        ArrayList<Integer> menuIds = new ArrayList<>();

        if (json != null) {
            menuIds = gson.fromJson(json, type);
        }


    }

    public class FilterDialog extends Dialog {

        EditText edFromDate, edToDate;
        TextView tvFromDate, tvToDate;
        ImageView ivClose;


        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_filter);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edFromDate = findViewById(R.id.edFromDate);
            edToDate = findViewById(R.id.edToDate);
            tvFromDate = findViewById(R.id.tvFromDate);
            tvToDate = findViewById(R.id.tvToDate);
            Button btnFilter = findViewById(R.id.btnFilter);
            LinearLayout llChooseMenu = findViewById(R.id.llChooseMenu);
            flowLayout = findViewById(R.id.flowLayout);
            tvMenu = findViewById(R.id.tvMenu);
            ivClose = findViewById(R.id.ivClose);

            final String frmDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_FROM_DATE);
            String toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_TO_DATE);

            tvFromDate.setText("" + frmDate);
            tvToDate.setText("" + toDate);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

            try {

                Date d1 = sdf.parse(frmDate);
                Date d2 = sdf.parse(toDate);

                edFromDate.setText("" + sdf1.format(d1.getTime()));
                edToDate.setText("" + sdf1.format(d2.getTime()));

            } catch (Exception e) {
                e.printStackTrace();

                edFromDate.setText("" + frmDate);
                edToDate.setText("" + toDate);

            }


            //  displayMenuChips();

            llChooseMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (staticAllMenu != null) {
                        showMenuDialog(staticAllMenu);
                    } else {
                        Toast.makeText(getContext(), "Reload Data", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            edFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (fromDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(fromDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), fromDateListener, yr, mn, dy);
                    dialog.show();
                }
            });

            edToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (toDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(toDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), toDateListener, yr, mn, dy);
                    dialog.show();
                }
            });


            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edFromDate.getText().toString().isEmpty()) {
                        edFromDate.setError("Select From Date");
                        edFromDate.requestFocus();
                    } else if (edToDate.getText().toString().isEmpty()) {
                        edToDate.setError("Select To Date");
                        edToDate.requestFocus();
                    } else {
                        dismiss();

                        String fromDate = tvFromDate.getText().toString();
                        String toDate = tvToDate.getText().toString();

                       /* Gson gson = new Gson();
                        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_MENU);
                        Type type = new TypeToken<ArrayList<Integer>>() {
                        }.getType();
                        ArrayList<Integer> menuIdArrayList = gson.fromJson(json, type);

                        if (menuIdArrayList != null) {
                            if (menuIdArrayList.size() <= 0) {
                                menuIdArrayList.add(-1);
                            }
                        } else {
                            menuIdArrayList = new ArrayList<>();
                            menuIdArrayList.add(-1);
                        }*/


                       /* ArrayList<Integer> menuIdArrayListSP = new ArrayList<>();

                        if (staticDashAllMenuSP != null) {
                            if (staticDashAllMenuSP.size() > 0) {
                                for (int i = 0; i < staticDashAllMenuSP.size(); i++) {
                                    if (staticDashAllMenuSP.get(i).isCheckedStatus()) {
                                        menuIdArrayListSP.add(staticDashAllMenuSP.get(i).getMenuId());
                                    }
                                }
                            }
                        } else {
                            menuIdArrayListSP.add(-1);
                        }

                        if (menuIdArrayListSP.isEmpty()) {
                            menuIdArrayListSP.clear();
                            menuIdArrayListSP.add(-1);
                        }*/


                        Gson gson = new Gson();
                        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU);
                        Type type = new TypeToken<ArrayList<Integer>>() {
                        }.getType();
                        ArrayList<Integer> menuIdArrayListSP = gson.fromJson(json, type);

                        Log.e("DASH FILTER SP MENU ","----------------------------  1116 ********************* ----------------------------------- "+menuIdArrayListSP);

                        if (menuIdArrayListSP == null) {
                            menuIdArrayListSP = new ArrayList<>();
                            menuIdArrayListSP.add(-1);
                        }

                        if (menuIdArrayListSP.size() <= 0) {
                            menuIdArrayListSP.add(-1);
                        }


                        ArrayList<Integer> slotList = new ArrayList<>();

                        int sortType = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_SP_SORT_SLOT);

                        Log.e("sortType : ", "----------------------------------- " + sortType);

                        if (sortType == 1) {
                            slotList.clear();
                            slotList.add(0);
                            slotList.add(1);
                        } else if (sortType == 2) {
                            slotList.clear();
                            slotList.add(0);
                        } else if (sortType == 3) {
                            slotList.clear();
                            slotList.add(1);
                        } else if (sortType == 0) {
                            slotList.clear();
                            slotList.add(0);
                        }

                        int sortOrder = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_SP_SORT_ORDER_SEQ);


                        getDashSPCount(frmDate, menuIdArrayListSP);
                        getSPCakeOrders(fromDate, toDate, menuIdArrayListSP, slotList, sortOrder);
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

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edFromDate.setText(dd + "-" + mm + "-" + yyyy);
                tvFromDate.setText(yyyy + "-" + mm + "-" + dd);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_SP_FROM_DATE, yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                fromDateMillis = calendar.getTimeInMillis();
            }
        };

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edToDate.setText(dd + "-" + mm + "-" + yyyy);
                tvToDate.setText(yyyy + "-" + mm + "-" + dd);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_SP_TO_DATE, yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                toDateMillis = calendar.getTimeInMillis();
            }
        };
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_sort);
        item.setVisible(true);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                showSortDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void showSortDialog() {
        final Dialog openDialog = new Dialog(getContext());
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_sort);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        final RadioButton rbOrderSeq = openDialog.findViewById(R.id.rbOrderSeq);
        final RadioButton rbProdSeq = openDialog.findViewById(R.id.rbProdSeq);
        final RadioButton rbMargipan = openDialog.findViewById(R.id.rbMargipan);

        ImageView ivClose = openDialog.findViewById(R.id.ivClose);

        Button btnSort = openDialog.findViewById(R.id.btnSort);
        Button btnCancel = openDialog.findViewById(R.id.btnCancel);

        int sortType = 2;

        if (fragType.equalsIgnoreCase("SP")){
            sortType = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_SP_SORT_SLOT);

        }else if (fragType.equalsIgnoreCase("ALBUM")){
            sortType = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_ALBUM_SORT_SLOT);

        }


        Log.e("SORT TYPE ", "------------------*******------------------------- " + sortType);

        if (sortType == 1) {
            rbOrderSeq.setChecked(true);
        } else if (sortType == 2) {
            rbProdSeq.setChecked(true);
        } else if (sortType == 3) {
            rbMargipan.setChecked(true);
        } else {
            rbProdSeq.setChecked(true);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Integer> slotUsed = new ArrayList<>();
                int orderSeq = 0;
                int sortType = 0;

                if (!rbOrderSeq.isChecked() && !rbProdSeq.isChecked() && !rbMargipan.isChecked()) {
                    Toast.makeText(getContext(), "Please select sort type", Toast.LENGTH_SHORT).show();
                } else {


                    if (fragType.equalsIgnoreCase("SP")){

                        if (rbOrderSeq.isChecked()) {
                            slotUsed.clear();
                            slotUsed.add(0);
                            slotUsed.add(1);
                            orderSeq = 0;
                            sortType = 1;
                            tvTitle.setText("SP Order Sequence");
                        } else if (rbProdSeq.isChecked()) {
                            slotUsed.clear();
                            slotUsed.add(0);
                            orderSeq = 1;
                            sortType = 2;
                            tvTitle.setText("SP Production Sequence");
                        } else if (rbMargipan.isChecked()) {
                            slotUsed.clear();
                            slotUsed.add(1);
                            orderSeq = 1;
                            sortType = 3;
                            tvTitle.setText("SP Margipan");
                        } else {
                            slotUsed.clear();
                            slotUsed.add(0);
                            orderSeq = 1;
                        }

                        String fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_FROM_DATE);
                        String toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_TO_DATE);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        if (fromDate == null) {
                            fromDate = sdf.format(System.currentTimeMillis());
                        }

                        if (toDate == null) {
                            toDate = sdf.format(System.currentTimeMillis());
                        }

                        CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_SP_SORT_SLOT, sortType);
                        CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_SP_SORT_ORDER_SEQ, orderSeq);


                        ArrayList<Integer> menuIdArrayListSP = new ArrayList<>();

                        if (staticDashAllMenuSP != null) {
                            if (staticDashAllMenuSP.size() > 0) {
                                for (int i = 0; i < staticDashAllMenuSP.size(); i++) {
                                    if (staticDashAllMenuSP.get(i).isCheckedStatus()) {
                                        menuIdArrayListSP.add(staticDashAllMenuSP.get(i).getMenuId());
                                    }
                                }
                            }
                        } else {
                            menuIdArrayListSP.add(-1);
                        }

                        if (menuIdArrayListSP.isEmpty()) {
                            menuIdArrayListSP.clear();
                            menuIdArrayListSP.add(-1);
                        }


                        getSPCakeOrders(fromDate, toDate, menuIdArrayListSP, slotUsed, orderSeq);
                        openDialog.dismiss();


                    }else if (fragType.equalsIgnoreCase("ALBUM")){

                        if (rbOrderSeq.isChecked()) {
                            slotUsed.clear();
                            slotUsed.add(0);
                            slotUsed.add(1);
                            orderSeq = 0;
                            sortType = 1;
                            tvTitle.setText("Album Order Sequence");
                        } else if (rbProdSeq.isChecked()) {
                            slotUsed.clear();
                            slotUsed.add(0);
                            orderSeq = 1;
                            sortType = 2;
                            tvTitle.setText("Album Production Sequence");
                        } else if (rbMargipan.isChecked()) {
                            slotUsed.clear();
                            slotUsed.add(1);
                            orderSeq = 1;
                            sortType = 3;
                            tvTitle.setText("Album Margipan");
                        } else {
                            slotUsed.clear();
                            slotUsed.add(0);
                            orderSeq = 1;
                        }

                        String fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_FROM_DATE);
                        String toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_TO_DATE);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        if (fromDate == null) {
                            fromDate = sdf.format(System.currentTimeMillis());
                        }

                        if (toDate == null) {
                            toDate = sdf.format(System.currentTimeMillis());
                        }

                        CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_ALBUM_SORT_SLOT, sortType);
                        CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_ALBUM_SORT_ORDER_SEQ, orderSeq);


                        ArrayList<Integer> menuIdArrayListAlbum = new ArrayList<>();

                        if (staticDashAllMenuAlbum != null) {
                            if (staticDashAllMenuAlbum.size() > 0) {
                                for (int i = 0; i < staticDashAllMenuAlbum.size(); i++) {
                                    if (staticDashAllMenuAlbum.get(i).isCheckedStatus()) {
                                        menuIdArrayListAlbum.add(staticDashAllMenuAlbum.get(i).getMenuId());
                                    }
                                }
                            }
                        } else {
                            menuIdArrayListAlbum.add(-1);
                        }

                        if (menuIdArrayListAlbum.isEmpty()) {
                            menuIdArrayListAlbum.clear();
                            menuIdArrayListAlbum.add(-1);
                        }


                        getAlbumCakeOrders(fromDate, toDate, menuIdArrayListAlbum, slotUsed, orderSeq);
                        openDialog.dismiss();


                    }




                }


            }
        });

        openDialog.show();
    }


    public void getDashSPCount(String prodDate, ArrayList<Integer> menuIdList) {

        Log.e("SP CAKE ","------------------------getDashSPCount---------------------------------- Date : "+prodDate+"                            MENU : "+menuIdList);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DashSPCakeCount>> listCall = Constants.myInterface.getDashSPCakeCount(prodDate, menuIdList);
            listCall.enqueue(new Callback<ArrayList<DashSPCakeCount>>() {
                @Override
                public void onResponse(Call<ArrayList<DashSPCakeCount>> call, Response<ArrayList<DashSPCakeCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DASH SP COUNT : ", "------------" + response.body());

                            ArrayList<DashSPCakeCount> data = response.body();

                          /*  Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_SP_COUNT_ARRAY, json);
*/
                            if (data.size() > 0) {

                                int pending = 0;
                                int handover = 0;
                                int totalCount = 0;
                                int started = 0;

                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getStatus() == 0) {
                                        pending = pending + data.get(i).getSpOrderCount();
                                    } else if (data.get(i).getStatus() == 1) {
                                        started = data.get(i).getSpOrderCount();
                                        pending = pending + data.get(i).getSpOrderCount();
                                    } else if (data.get(i).getStatus() == 2) {
                                        handover = data.get(i).getSpOrderCount();
                                    }
                                }

                                totalCount = pending + handover;

                                HomeActivity.tvTitle.setText("SP  TC:" + totalCount + "  P:" + started + "  H:" + handover);

                            } else {
                                HomeActivity.tvTitle.setText("SP  TC:" + "0" + "  P:" + "0" + "  H:" + "0");
                            }


                            commonDialog.dismiss();
                        } else {
                            HomeActivity.tvTitle.setText("SP  TC:" + "0" + "  P:" + "0" + "  H:" + "0");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        //HomeActivity.tvTitle.setText("SP  TC:" + "0" + "  P:" + "0" + "  H:" + "0");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DashSPCakeCount>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }



    public void getDashAlbumCount(String prodDate, ArrayList<Integer> menuIdList) {

        Log.e("ALBUM CAKE ","------------------------getDashAlbumCount---------------------------------- Date : "+prodDate+"                            MENU : "+menuIdList);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DashSPCakeCount>> listCall = Constants.myInterface.getDashAlbumCakeCount(prodDate, menuIdList);
            listCall.enqueue(new Callback<ArrayList<DashSPCakeCount>>() {
                @Override
                public void onResponse(Call<ArrayList<DashSPCakeCount>> call, Response<ArrayList<DashSPCakeCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DASH ALBUM COUNT : ", "------------" + response.body());

                            ArrayList<DashSPCakeCount> data = response.body();

                          /*  Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_SP_COUNT_ARRAY, json);
*/
                            if (data.size() > 0) {

                                int pending = 0;
                                int handover = 0;
                                int totalCount = 0;
                                int started = 0;

                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getStatus() == 0) {
                                        pending = pending + data.get(i).getSpOrderCount();
                                    } else if (data.get(i).getStatus() == 1) {
                                        started = data.get(i).getSpOrderCount();
                                        pending = pending + data.get(i).getSpOrderCount();
                                    } else if (data.get(i).getStatus() == 2) {
                                        handover = data.get(i).getSpOrderCount();
                                    }
                                }

                                totalCount = pending + handover;

                                HomeActivity.tvTitle.setText("ALBUM  TC:" + totalCount + "  P:" + started + "  H:" + handover);

                            } else {
                                HomeActivity.tvTitle.setText("ALBUM  TC:" + "0" + "  P:" + "0" + "  H:" + "0");
                            }


                            commonDialog.dismiss();
                        } else {
                            HomeActivity.tvTitle.setText("ALBUM  TC:" + "0" + "  P:" + "0" + "  H:" + "0");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        //HomeActivity.tvTitle.setText("SP  TC:" + "0" + "  P:" + "0" + "  H:" + "0");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DashSPCakeCount>> call, Throwable t) {
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
