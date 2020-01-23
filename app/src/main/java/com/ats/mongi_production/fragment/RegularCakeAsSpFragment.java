package com.ats.mongi_production.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.activity.LoginActivity;
import com.ats.mongi_production.adapter.RegCakeOrderAdapter;
import com.ats.mongi_production.adapter.RegMenuAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.filterview.ChipView;
import com.ats.mongi_production.model.AllMenu;
import com.ats.mongi_production.model.DashRegCakeCount;
import com.ats.mongi_production.model.RegCakeOrder;
import com.ats.mongi_production.model.SPCakeOrder;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.ats.mongi_production.util.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.llCart;
import static com.ats.mongi_production.activity.HomeActivity.tvTitle;
import static com.ats.mongi_production.fragment.DashboardFragment.menuMapReg;
import static com.ats.mongi_production.fragment.DashboardFragment.staticAllMenuReg;

public class RegularCakeAsSpFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private EditText edSearch;

    RegCakeOrderAdapter orderAdapter;
    ArrayList<RegCakeOrder> orderList = new ArrayList<>();


    Dialog dialogSpinner;
    CheckBox checkBox;

    RegMenuAdapter adapterMenu;
    Boolean isTouched = false;
    FlowLayout flowLayout;
    TextView tvMenu;

    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    ArrayList<Integer> menuIdArrayList;

    String fromDate, toDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regular_cake_as_sp, container, false);

        tvTitle.setText("REG");

        llCart.setVisibility(View.INVISIBLE);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);

        edSearch = view.findViewById(R.id.edSearch);

        Gson gson = new Gson();
        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_MENU);
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
            if (dt == null) {
                fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE);
                toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_TO_DATE);

                if (fromDate == null) {
                    fromDate = sdf.format(calendar.getTimeInMillis());
                }

                if (toDate == null) {
                    toDate = sdf.format(calendar.getTimeInMillis());
                }
            } else {
                fromDate = dt;
                toDate = dt;
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE, fromDate);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_REG_TO_DATE, fromDate);
            }

        } catch (Exception e) {
            fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE);
            toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_TO_DATE);

            if (fromDate == null) {
                fromDate = sdf.format(calendar.getTimeInMillis());
            }

            if (toDate == null) {
                toDate = sdf.format(calendar.getTimeInMillis());
            }

        }

       /* String fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE);
        String toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_TO_DATE);

        if (fromDate == null) {
            fromDate = sdf.format(calendar.getTimeInMillis());
        }

        if (toDate == null) {
            toDate = sdf.format(calendar.getTimeInMillis());
        }

        ArrayList<Integer> menuList = new ArrayList<>();
        menuList.add(-1);*/

        Gson gson1 = new Gson();
        String json1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU);
        Type type1 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdArrayListREG = gson1.fromJson(json1, type1);

        if (menuIdArrayListREG == null) {
            menuIdArrayListREG = new ArrayList<>();
            menuIdArrayListREG.add(-1);
        }

        if (menuIdArrayListREG.size() <= 0) {
            menuIdArrayListREG.add(-1);
        }


        int orderByType = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_REG_SORT_ORDER_SEQ);


        getDashREGCount(fromDate, menuIdArrayListREG);
        getRegCakeOrders(fromDate, toDate, menuIdArrayListREG, orderByType);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (orderList != null) {
                    filter(editable.toString());
                }
            }
        });


        return view;
    }


    void filter(String text) {
        ArrayList<RegCakeOrder> temp = new ArrayList();
        for (RegCakeOrder d : orderList) {
            if (d.getFrName().toLowerCase().equalsIgnoreCase(text.toLowerCase()) || d.getFrName().toLowerCase().contains(text.toLowerCase()) || String.valueOf(d.getSrNo()).equalsIgnoreCase(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        orderAdapter.updateList(temp);
    }

    public void getRegCakeOrders(String fromDate, String toDate, ArrayList<Integer> menuIdList, int isOrderBy) {

        Log.e("REG PARAMETERS : ", "----------- FROM DATE : " + fromDate + "             TO DATE : " + toDate + "               MenuIdList : " + menuIdList + "                   isOrderBy : " + isOrderBy);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<RegCakeOrder>> listCall = Constants.myInterface.getRegCakeOrder(fromDate, toDate, menuIdList, isOrderBy);
            listCall.enqueue(new Callback<ArrayList<RegCakeOrder>>() {
                @Override
                public void onResponse(Call<ArrayList<RegCakeOrder>> call, Response<ArrayList<RegCakeOrder>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Reg Data : ", "------------" + response.body());

                            ArrayList<RegCakeOrder> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                                builder.setTitle("Caution");
                                builder.setMessage("No Data Found!");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();


                                orderList.clear();

                                orderAdapter = new RegCakeOrderAdapter(orderList, getContext());
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
                                        if (orderList.get(i).getStartTime() == null) {
                                            orderList.get(i).setStartTime(0l);
                                        }
                                        if (orderList.get(i).getEndTime() == null) {
                                            orderList.get(i).setEndTime(0l);
                                        }
                                    }
                                    orderAdapter = new RegCakeOrderAdapter(orderList, getContext());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(orderAdapter);

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                                    builder.setTitle("Caution");
                                    builder.setMessage("No Data Found!");

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                    orderList.clear();

                                    orderAdapter = new RegCakeOrderAdapter(orderList, getContext());
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
                public void onFailure(Call<ArrayList<RegCakeOrder>> call, Throwable t) {
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

        adapterMenu = new RegMenuAdapter(menuArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterMenu);

     /*   final int allMenu = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_REG_ALL_MENU);
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
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_REG_ALL_MENU, 0);

                    if (menuArrayList.size() > 0) {
                        for (int i = 0; i < menuArrayList.size(); i++) {
                            menuMapReg.put(menuArrayList.get(i).getMenuId(), true);
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
                                menuMapReg.put(menuArrayList.get(i).getMenuId(), false);
                                menuArrayList.get(i).setCheckedStatus(false);
                            }
                            adapterMenu.notifyDataSetChanged();
                        }
                    }
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_REG_ALL_MENU, 1);


                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuArrayList.size() > 0) {
                    for (int i = 0; i < menuArrayList.size(); i++) {
                        menuMapReg.put(menuArrayList.get(i).getMenuId(), false);
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
                for (int i = 0; i < menuArrayList.size(); i++) {
                    if (menuMapReg.get(menuArrayList.get(i).getMenuId())) {
                        menuIds.add(menuArrayList.get(i).getMenuId());
                    }
                }

                Log.e("Selected Ids", "--------------" + menuIds);

                Gson gson = new Gson();
                String jsonMenu = gson.toJson(menuIds);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_REG_MENU, jsonMenu);

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
        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_MENU);
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

            // displayMenuChips();

            String frmDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE);
            String toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_TO_DATE);

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


            llChooseMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (staticAllMenuReg != null) {
                        showMenuDialog(staticAllMenuReg);
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

                      /*  Gson gson = new Gson();
                        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_MENU);
                        Type type = new TypeToken<ArrayList<Integer>>() {
                        }.getType();
                        ArrayList<Integer> menuIdArrayList = gson.fromJson(json, type);

                        if (menuIdArrayList == null) {
                            menuIdArrayList = new ArrayList<>();
                            menuIdArrayList.add(-1);
                        }

                        if (menuIdArrayList.size() <= 0) {
                            menuIdArrayList.add(-1);
                        }*/

                        Gson gson1 = new Gson();
                        String json1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU);
                        Type type1 = new TypeToken<ArrayList<Integer>>() {
                        }.getType();
                        ArrayList<Integer> menuIdArrayListReg = gson1.fromJson(json1, type1);

                        if (menuIdArrayListReg == null) {
                            menuIdArrayListReg = new ArrayList<>();
                            menuIdArrayListReg.add(-1);
                        }

                        if (menuIdArrayListReg.size() <= 0) {
                            menuIdArrayListReg.add(-1);
                        }


                        int orderByType = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_REG_SORT_ORDER_SEQ);


                        getDashREGCount(fromDate, menuIdArrayListReg);
                        getRegCakeOrders(fromDate, toDate, menuIdArrayListReg, orderByType);

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
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE, yyyy + "-" + mm + "-" + dd);

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
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_REG_TO_DATE, yyyy + "-" + mm + "-" + dd);

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
        openDialog.setContentView(R.layout.dialog_sort_reg);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        final RadioButton rbYes = openDialog.findViewById(R.id.rbYes);
        final RadioButton rbNo = openDialog.findViewById(R.id.rbNo);

        int type = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_REG_SORT_ORDER_SEQ);
        if (type == 1) {
            rbYes.setChecked(true);
        } else if (type == 0) {
            rbNo.setChecked(true);
        }

        ImageView ivClose = openDialog.findViewById(R.id.ivClose);

        Button btnSort = openDialog.findViewById(R.id.btnSort);
        Button btnCancel = openDialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Gson gson1 = new Gson();
                String json1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU);
                Type type1 = new TypeToken<ArrayList<Integer>>() {
                }.getType();
                ArrayList<Integer> menuIdArrayListReg = gson1.fromJson(json1, type1);

                if (menuIdArrayListReg == null) {
                    menuIdArrayListReg = new ArrayList<>();
                    menuIdArrayListReg.add(-1);
                }

                if (menuIdArrayListReg.size() <= 0) {
                    menuIdArrayListReg.add(-1);
                }

                int type = 0;
                if (!rbYes.isChecked() && !rbNo.isChecked()) {
                    Toast.makeText(getContext(), "Please select type", Toast.LENGTH_SHORT).show();
                } else if (rbYes.isChecked()) {
                    type = 1;
                } else if (rbNo.isChecked()) {
                    type = 0;
                }

                CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_REG_SORT_ORDER_SEQ, type);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();

                String fromDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_FROM_DATE);
                String toDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_TO_DATE);

                if (fromDate == null) {
                    fromDate = sdf.format(calendar.getTimeInMillis());
                }

                if (toDate == null) {
                    toDate = sdf.format(calendar.getTimeInMillis());
                }

                getDashREGCount(fromDate, menuIdArrayListReg);
                getRegCakeOrders(fromDate, toDate, menuIdArrayListReg, type);

                openDialog.dismiss();

            }
        });

        openDialog.show();
    }

    public void getDashREGCount(String prodDate, ArrayList<Integer> menuIdList) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DashRegCakeCount>> listCall = Constants.myInterface.getDashRegCakeCount(prodDate, menuIdList);
            listCall.enqueue(new Callback<ArrayList<DashRegCakeCount>>() {
                @Override
                public void onResponse(Call<ArrayList<DashRegCakeCount>> call, Response<ArrayList<DashRegCakeCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DASH REG COUNT : ", "------------" + response.body());

                            ArrayList<DashRegCakeCount> data = response.body();

                           /* Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_REG_COUNT_ARRAY, json);
*/
                            if (data.size() > 0) {

                                int pending = 0;
                                int handover = 0;
                                int totalCount = 0;
                                int started=0;

                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getStatus() == 0) {
                                        pending = pending + data.get(i).getRegSpOrderCount();
                                    } else if (data.get(i).getStatus() == 1) {
                                        started=data.get(i).getRegSpOrderCount();
                                        pending = pending + data.get(i).getRegSpOrderCount();
                                    } else if (data.get(i).getStatus() == 2) {
                                        handover = data.get(i).getRegSpOrderCount();
                                    }
                                }

                                totalCount = pending + handover;

                                tvTitle.setText("REG TC:" + totalCount + "  P:" + started + "  H:" + handover);

                            }

                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DashRegCakeCount>> call, Throwable t) {
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
