package com.ats.mongi_production.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.adapter.GenerateBillListAdapter;
import com.ats.mongi_production.adapter.MenuAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.filterview.ChipView;
import com.ats.mongi_production.model.AllMenu;
import com.ats.mongi_production.model.CartListData;
import com.ats.mongi_production.model.FranchiseList;
import com.ats.mongi_production.model.GenerateBill;
import com.ats.mongi_production.model.GeneratedBillList;
import com.ats.mongi_production.model.StockTransfDetail;
import com.ats.mongi_production.model.StockTransferHeader;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.ats.mongi_production.util.FlowLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.llCart;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;


public class FrWiseItemListFragment extends Fragment implements View.OnClickListener {

    public static ArrayList<GenerateBill> staticQtyList = new ArrayList<>();
    public static int frId;
    public static String frName;

    private RecyclerView recyclerView;
    private Button btnSubmit;

    GenerateBillListAdapter adapter;

    private ArrayList<GenerateBill> billList = new ArrayList<>();

    private ArrayList<String> frNameArray = new ArrayList<>();
    private ArrayList<Integer> frIdArray = new ArrayList<>();

    private ArrayList<AllMenu> menuArrayList = new ArrayList<>();

    Dialog dialogSpinner;
    FlowLayout flowLayout;
    CheckBox checkBox;

    MenuAdapter adapterMenu;
    boolean isTouched;
    TextView tvMenu;

    long dateInMillis;
    int yyyy, mm, dd;

    public static Map<Integer, Boolean> menuMapFrWise = new HashMap<Integer, Boolean>();

    int userId, stockTransfId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fr_wise_item_list, container, false);
        tvTitle.setText("Stock Transfer");

        recyclerView = view.findViewById(R.id.recyclerView);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        try {
            stockTransfId = getArguments().getInt("id");
        } catch (Exception e) {
        }


        String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_USER);
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        Log.e("User Bean : ", "---------------" + user);

        if (user != null) {
            userId = user.getUserId();
        }

        getAllFranchise();
        getMenu(5, 0);

        return view;
    }

    public void getAllFranchise() {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<FranchiseList> arrayListCall = Constants.myInterface.getAllFranchise();
            arrayListCall.enqueue(new Callback<FranchiseList>() {
                @Override
                public void onResponse(Call<FranchiseList> call, Response<FranchiseList> response) {
                    try {
                        if (response.body() != null) {
                            FranchiseList data = response.body();

                            Log.e("FR DATA : ", "" + data);

                            frIdArray.clear();
                            frNameArray.clear();

                            if (data.getFranchiseeList().size() > 0) {
                                for (int i = 0; i < data.getFranchiseeList().size(); i++) {
                                    frIdArray.add(data.getFranchiseeList().get(i).getFrId());
                                    frNameArray.add(data.getFranchiseeList().get(i).getFrName());
                                }

                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<FranchiseList> call, Throwable t) {
                    commonDialog.dismiss();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    public void getMenu(int catId, int isSameDay) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<AllMenu>> listCall = Constants.myInterface.getMenu(catId, isSameDay);
            listCall.enqueue(new Callback<ArrayList<AllMenu>>() {
                @Override
                public void onResponse(Call<ArrayList<AllMenu>> call, Response<ArrayList<AllMenu>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Menu Data : ", "------------" + response.body());

                            ArrayList<AllMenu> data = response.body();

                            menuArrayList.clear();
                            menuArrayList = data;

                            getRegMenu(2, 3);

                            commonDialog.dismiss();


                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (
                            Exception e)

                    {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AllMenu>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else

        {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    public void getRegMenu(int catId, int isSameDay) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<AllMenu>> listCall = Constants.myInterface.getMenu(catId, isSameDay);
            listCall.enqueue(new Callback<ArrayList<AllMenu>>() {
                @Override
                public void onResponse(Call<ArrayList<AllMenu>> call, Response<ArrayList<AllMenu>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Menu Data : ", "------------" + response.body());

                            ArrayList<AllMenu> data = response.body();

                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    menuArrayList.add(data.get(i));
                                }
                            }

                            commonDialog.dismiss();


                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (
                            Exception e)

                    {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AllMenu>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else

        {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }


    public void getGenerateBills(ArrayList<String> frId, ArrayList<String> menuId, String deliveryDate) {

        Log.e("PARAMETER : ", "-------------------- FR ID : " + frId + "                     Menu Id : " + menuId + "                    Delivery Date : " + deliveryDate);

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<GeneratedBillList> arrayListCall = Constants.myInterface.getGenerateBills(frId, menuId, deliveryDate);
            arrayListCall.enqueue(new Callback<GeneratedBillList>() {
                @Override
                public void onResponse(Call<GeneratedBillList> call, Response<GeneratedBillList> response) {
                    try {
                        if (response.body() != null) {
                            GeneratedBillList data = response.body();

                            Log.e("BILL DATA : ", "----------- " + response.body());

                            billList.clear();

                            if (data.getGenerateBills().size() > 0) {
                                for (int i = 0; i < data.getGenerateBills().size(); i++) {
                                    billList.add(data.getGenerateBills().get(i));
                                    staticQtyList.add(data.getGenerateBills().get(i));
                                }

                                adapter = new GenerateBillListAdapter(billList, getContext());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                            }

                            commonDialog.dismiss();

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
                public void onFailure(Call<GeneratedBillList> call, Throwable t) {
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
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
            case R.id.action_filter:
                showFilterDialog(frIdArray, frNameArray, menuArrayList);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFilterDialog(final ArrayList<Integer> frIdList, final ArrayList<String> frNameList, final ArrayList<AllMenu> menuList) {
        final Dialog openDialog = new Dialog(getContext());
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_generate_bills_filter);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        Button btnSubmit = openDialog.findViewById(R.id.btnSubmit);
        Button btnCancel = openDialog.findViewById(R.id.btnCancel);

        final EditText edDate = openDialog.findViewById(R.id.edDate);
        final TextView tvDate = openDialog.findViewById(R.id.tvDate);

        final Spinner spinner = openDialog.findViewById(R.id.spinner);
        LinearLayout llChooseMenu = openDialog.findViewById(R.id.llChooseMenu);
        flowLayout = openDialog.findViewById(R.id.flowLayout);
        tvMenu = openDialog.findViewById(R.id.tvMenu);

        ArrayAdapter<String> frNameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, frNameList);
        spinner.setAdapter(frNameAdapter);


        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edDate.setText(dd + "-" + mm + "-" + yyyy);
                tvDate.setText(dd + "-" + mm + "-" + yyyy);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                dateInMillis = calendar.getTimeInMillis();
            }
        };

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int yr, mn, dy;
                if (dateInMillis > 0) {
                    Calendar purchaseCal = Calendar.getInstance();
                    purchaseCal.setTimeInMillis(dateInMillis);
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                frId = frIdList.get(spinner.getSelectedItemPosition());
                frName = frNameList.get(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        llChooseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuList != null) {
                    showMenuDialog(menuList);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvDate.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please select delivery date", Toast.LENGTH_SHORT).show();
                } else if (menuMapFrWise.size() > 0) {

                    ArrayList<Integer> tempIdArray = new ArrayList<>(menuMapFrWise.keySet());
                    ArrayList<String> menuIdListString = new ArrayList<>();
                    for (int i = 0; i < tempIdArray.size(); i++) {
                        menuIdListString.add("" + tempIdArray.get(i));
                    }

                    ArrayList<String> frIdArray = new ArrayList<>();
                    frIdArray.add("" + frIdList.get(spinner.getSelectedItemPosition()));

                    getGenerateBills(frIdArray, menuIdListString, tvDate.getText().toString());
                    openDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Please select menu", Toast.LENGTH_SHORT).show();
                }

            }
        });

        openDialog.show();
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

                    if (menuArrayList.size() > 0) {
                        for (int i = 0; i < menuArrayList.size(); i++) {
                            menuMapFrWise.put(menuArrayList.get(i).getMenuId(), true);
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
                                menuMapFrWise.put(menuArrayList.get(i).getMenuId(), false);
                                menuArrayList.get(i).setCheckedStatus(false);
                            }
                            adapterMenu.notifyDataSetChanged();
                        }
                    }

                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuArrayList.size() > 0) {
                    for (int i = 0; i < menuArrayList.size(); i++) {
                        menuArrayList.get(i).setCheckedStatus(false);
                    }
                    checkBox.setChecked(false);
                    adapterMenu.notifyDataSetChanged();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSpinner.dismiss();


                ArrayList<Integer> menuIds = new ArrayList<>();

                try {
                    Log.e("STOCK TRANSFER", "-------------MENU ARRAY------------- " + menuArrayList);
                    for (int i = 0; i < menuArrayList.size(); i++) {
                        if (menuMapFrWise.get(menuArrayList.get(i).getMenuId())) {
                            menuIds.add(menuArrayList.get(i).getMenuId());
                        }
                    }
                } catch (Exception e) {
                }

                Log.e("Selected Ids", "--------------" + menuIds);

                flowLayout.removeAllViews();

                Log.e("Dialog Menu List", "--------------" + menuArrayList);

                for (int i = 0; i < menuArrayList.size(); i++) {
                    for (int j = 0; j < menuIds.size(); j++) {
                        if (menuArrayList.get(i).getMenuId() == menuIds.get(j)) {
                            ChipView chipView1 = new ChipView(getContext());
                            chipView1.setLabel(menuArrayList.get(i).getMenuTitle());
                            chipView1.setHasAvatarIcon(false);
                            chipView1.setDeletable(false);
                            chipView1.setPadding(5, 2, 5, 2);

                            flowLayout.addView(chipView1);
                        }
                    }
                }

                if (checkBox.isChecked()) {
                    tvMenu.setText("All Menu");
                } else {
                    tvMenu.setText("");
                }


            }
        });

        dialogSpinner.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        llCart.setVisibility(View.INVISIBLE);
        cartArray.clear();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {

            if (staticQtyList.size() > 0) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(System.currentTimeMillis());

                ArrayList<StockTransfDetail> detailList = new ArrayList<>();

                for (int i = 0; i < staticQtyList.size(); i++) {

                    GenerateBill bill = staticQtyList.get(i);

                    Log.e("STATIC : ", "----------------------- " + bill);

                    int itemId = bill.getItemId();
                    int catId = bill.getCatId();
                    int subCatId = bill.getSubCatId();
                    int qty = 0;

                    if (bill.getNewQty() == 0) {
                        qty = bill.getOrderQty();
                    } else {
                        qty = bill.getNewQty();
                    }

                    double rate = bill.getOrderRate();
                    double total = rate * qty;

                    StockTransfDetail detail = new StockTransfDetail(0, 0, itemId, catId, subCatId, 0, qty, qty, qty, qty, (float) rate, (float) total, 0, 0, 0, 0f, "");
                    detailList.add(detail);
                }

                StockTransferHeader header = new StockTransferHeader(0, date, stockTransfId, userId, 0, 0, 0, date, date, date, frId, frName, 0, 0, 0, "", 0f, detailList);

                insertStockTransfer(header);


            } else {
                Toast.makeText(getContext(), "No Items Found", Toast.LENGTH_SHORT).show();
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
