package com.ats.mongi_production.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ats.mongi_production.R;
import com.ats.mongi_production.adapter.ConsumeCountAdapter;
import com.ats.mongi_production.adapter.DashboardAlbumMenuAdapter;
import com.ats.mongi_production.adapter.DashboardREGMenuAdapter;
import com.ats.mongi_production.adapter.DashboardSPMenuAdapter;
import com.ats.mongi_production.adapter.GateSaleCountAdapter;
import com.ats.mongi_production.adapter.StockTransferAdapter;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.AllMenu;
import com.ats.mongi_production.model.ConsumeCount;
import com.ats.mongi_production.model.DashRegCakeCount;
import com.ats.mongi_production.model.DashSPCakeCount;
import com.ats.mongi_production.model.GateSaleCount;
import com.ats.mongi_production.model.StockTransferType;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.util.CommonDialog;
import com.ats.mongi_production.util.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private Spinner spSpecialCake, spRegCake;
    private TextView tvAlbumTC, tvAlbumP, tvAlbumH, tvSPTC, tvSPP, tvSPH, tvRegTC, tvRegP, tvRegH, tvGateTC, tvGateP, tvGateH, tvConsumeTC, tvConsumeP, tvConsumeH;
    private Button btnAlbumProd, btnAlbumCheck, btnSPProd, btnSPCheck, btnRegProd, btnRegCheck;
    private RecyclerView recyclerView, recyclerViewConsumption;
    private LinearLayout linearLayout;
    private FloatingActionButton fab;
    private CardView cvGateSaleStock, cvFGS;

    User user;

    public static ArrayList<AllMenu> staticAllMenu = new ArrayList<>();
    public static Map<Integer, Boolean> menuMap = new HashMap<Integer, Boolean>();

    public static ArrayList<AllMenu> staticAllMenuAlbum = new ArrayList<>();
    public static Map<Integer, Boolean> menuMapAlbum = new HashMap<Integer, Boolean>();

    public static ArrayList<AllMenu> staticAllMenuReg = new ArrayList<>();
    public static Map<Integer, Boolean> menuMapReg = new HashMap<Integer, Boolean>();

    public static ArrayList<AllMenu> staticDashAllMenuSP = new ArrayList<>();
    public static Map<Integer, Boolean> dashMenuMapSP = new HashMap<Integer, Boolean>();

    public static ArrayList<AllMenu> staticDashAllMenuREG = new ArrayList<>();
    public static Map<Integer, Boolean> dashMenuMapREG = new HashMap<Integer, Boolean>();

    public static ArrayList<AllMenu> dispatchAllMenu = new ArrayList<>();
    public static Map<Integer, Boolean> dispatchMenuMap = new HashMap<Integer, Boolean>();

    public static ArrayList<AllMenu> staticDashAllMenuAlbum = new ArrayList<>();
    public static Map<Integer, Boolean> dashMenuMapAlbum = new HashMap<Integer, Boolean>();


    private NestedScrollView nestedScrollView;


    long dateMillis;
    int yyyy, mm, dd;

    Dialog dialogSPMenu, dialogRegMenu, dialogAlbumMenu;
    CheckBox cbSPMenu, cbRegMenu, cbAlbumMenu;
    TextView tvSPMenu, tvRegMenu, tvAlbumMenu;
    Boolean isSPTouched = false, isRegTouched = false, isAlbumTouched = false;
    DashboardSPMenuAdapter adapterSPMenu;
    DashboardAlbumMenuAdapter adapterAlbumMenu;
    DashboardREGMenuAdapter adapterREGMenu;

    GateSaleCountAdapter gateSaleCountAdapter;
    ConsumeCountAdapter consumeCountAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvTitle.setText("Dashboard");

        llCart.setVisibility(View.INVISIBLE);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        linearLayout = view.findViewById(R.id.linearLayout);
        fab = view.findViewById(R.id.fab);

        cvGateSaleStock = view.findViewById(R.id.cvGateSaleStock);
        cvFGS = view.findViewById(R.id.cvFGS);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewConsumption = view.findViewById(R.id.recyclerViewConsumption);

        spSpecialCake = view.findViewById(R.id.spSpecialCake);
        spRegCake = view.findViewById(R.id.spRegCake);

        tvAlbumTC = view.findViewById(R.id.tvAlbumTC);
        tvAlbumP = view.findViewById(R.id.tvAlbumP);
        tvAlbumH = view.findViewById(R.id.tvAlbumH);

        tvSPTC = view.findViewById(R.id.tvSPTC);
        tvSPP = view.findViewById(R.id.tvSPP);
        tvSPH = view.findViewById(R.id.tvSPH);

        tvRegTC = view.findViewById(R.id.tvRegTC);
        tvRegP = view.findViewById(R.id.tvRegP);
        tvRegH = view.findViewById(R.id.tvRegH);

        tvGateTC = view.findViewById(R.id.tvGateTC);
        tvGateP = view.findViewById(R.id.tvGateP);
        tvGateH = view.findViewById(R.id.tvGateH);

        tvConsumeTC = view.findViewById(R.id.tvConsumeTC);
        tvConsumeP = view.findViewById(R.id.tvConsumeP);
        tvConsumeH = view.findViewById(R.id.tvConsumeH);

        btnAlbumProd = view.findViewById(R.id.btnAlbumProd);
        btnAlbumCheck = view.findViewById(R.id.btnAlbumCheck);
        btnSPProd = view.findViewById(R.id.btnSPProd);
        btnSPCheck = view.findViewById(R.id.btnSPCheck);
        btnRegProd = view.findViewById(R.id.btnRegProd);
        btnRegCheck = view.findViewById(R.id.btnRegCheck);

        btnAlbumProd.setOnClickListener(this);
        btnAlbumCheck.setOnClickListener(this);
        btnSPProd.setOnClickListener(this);
        btnSPCheck.setOnClickListener(this);
        btnRegProd.setOnClickListener(this);
        btnRegCheck.setOnClickListener(this);

        fab.setOnClickListener(this);


        String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_USER);
        Gson gsonUser = new Gson();
        user = gsonUser.fromJson(userStr, User.class);
        Log.e("User Bean : ", "---------------" + user);

        if (user != null) {
            if (user.getuType() < 4) {
                cvFGS.setVisibility(View.GONE);
                cvGateSaleStock.setVisibility(View.GONE);
            }

            if (user.getuType() == 1) {
                btnSPCheck.setVisibility(View.GONE);
                btnRegCheck.setVisibility(View.GONE);
                btnAlbumCheck.setVisibility(View.GONE);

            } else if (user.getuType() == 2) {
                btnSPProd.setVisibility(View.GONE);
                btnRegProd.setVisibility(View.GONE);
                btnAlbumProd.setVisibility(View.GONE);
            }

        }

        //getAlbumMenu(5, 4);
        Log.e("SP MENU ARRAY", "----------------STATIC----------------" + staticAllMenu);

        //---------------------SP MENU--------------------------------------
        Gson gson1 = new Gson();
        String json1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_SP_MENU_ARRAY);
        Type type1 = new TypeToken<ArrayList<AllMenu>>() {
        }.getType();
        staticAllMenu = gson1.fromJson(json1, type1);

        Log.e("SP MENU ARRAY", "----------------STATIC---------SHARED PREF-------" + staticAllMenu);

        if (staticAllMenu != null) {
            if (staticAllMenu.size() > 0) {
                for (int i = 0; i < staticAllMenu.size(); i++) {
                    menuMap.put(staticAllMenu.get(i).getMenuId(), false);
                    staticAllMenu.get(i).setCheckedStatus(false);
                }
            }
        } else {
            staticAllMenu = new ArrayList<>();
        }

        Gson gson2 = new Gson();
        String json2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_MENU);
        Type type2 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdListSP = gson2.fromJson(json2, type2);
        if (menuIdListSP != null) {
            if (menuIdListSP.size() > 0) {
                for (int j = 0; j < menuIdListSP.size(); j++) {
                    if (menuMap.containsKey(menuIdListSP.get(j))) {
                        menuMap.put(menuIdListSP.get(j), true);
                    }
                    for (int i = 0; i < staticAllMenu.size(); i++) {
                        if (menuIdListSP.get(j) == staticAllMenu.get(i).getMenuId()) {
                            staticAllMenu.get(i).setCheckedStatus(true);
                        }
                    }
                }
            }
        }
        Log.e("SP MENU MAP", "----------------STATIC---------SHARED PREF-------" + menuMap);

        //--------------------------------------------------------------------------------------------------

        //-----------------------REG MENU------------------------------------------------------------------

        Gson gsonReg1 = new Gson();
        String jsonReg1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_MENU_ARRAY);
        Type typeReg1 = new TypeToken<ArrayList<AllMenu>>() {
        }.getType();
        staticAllMenuReg = gsonReg1.fromJson(jsonReg1, typeReg1);

        Log.e("REG MENU ARRAY", "----------------STATIC---------SHARED PREF-------" + staticAllMenuReg);

        if (staticAllMenuReg != null) {
            if (staticAllMenuReg.size() > 0) {
                for (int i = 0; i < staticAllMenuReg.size(); i++) {
                    menuMapReg.put(staticAllMenuReg.get(i).getMenuId(), false);
                    staticAllMenuReg.get(i).setCheckedStatus(false);
                }
            }
        } else {
            staticAllMenuReg = new ArrayList<>();
        }

        Gson gsonReg2 = new Gson();
        String jsonReg2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_MENU);
        Type typeReg2 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdListReg = gsonReg2.fromJson(jsonReg2, typeReg2);

        if (menuIdListReg != null) {
            if (menuIdListReg.size() > 0) {
                for (int j = 0; j < menuIdListReg.size(); j++) {
                    if (menuMapReg.containsKey(menuIdListReg.get(j))) {
                        menuMapReg.put(menuIdListReg.get(j), true);
                    }
                    for (int i = 0; i < staticAllMenuReg.size(); i++) {
                        if (menuIdListReg.get(j) == staticAllMenuReg.get(i).getMenuId()) {
                            staticAllMenuReg.get(i).setCheckedStatus(true);
                        }
                    }
                }
            }
        }
        //-------------------------------------------------------------------------------------------

        Log.e("ALBUM MENU ARRAY", "----------------STATIC----------------" + staticAllMenuAlbum);

        //---------------------ALBUM MENU--------------------------------------
        Gson gsonA1 = new Gson();
        String jsonA1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_MENU_ARRAY);
        Type typeA1 = new TypeToken<ArrayList<AllMenu>>() {
        }.getType();
        staticAllMenuAlbum = gsonA1.fromJson(jsonA1, typeA1);

        Log.e("ALBUM MENU ARRAY", "----------------STATIC---------SHARED PREF-------" + staticAllMenuAlbum);

        if (staticAllMenuAlbum != null) {
            if (staticAllMenuAlbum.size() > 0) {
                for (int i = 0; i < staticAllMenuAlbum.size(); i++) {
                    menuMapAlbum.put(staticAllMenuAlbum.get(i).getMenuId(), false);
                    staticAllMenuAlbum.get(i).setCheckedStatus(false);
                }
            }
        } else {
            staticAllMenuAlbum = new ArrayList<>();
        }

        Gson gsonA2 = new Gson();
        String jsonA2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_MENU);
        Type typeA2 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdListAlbum = gsonA2.fromJson(jsonA2, typeA2);
        if (menuIdListAlbum != null) {
            if (menuIdListAlbum.size() > 0) {
                for (int j = 0; j < menuIdListAlbum.size(); j++) {
                    if (menuMapAlbum.containsKey(menuIdListAlbum.get(j))) {
                        menuMapAlbum.put(menuIdListAlbum.get(j), true);
                    }
                    for (int i = 0; i < staticAllMenuAlbum.size(); i++) {
                        if (menuIdListAlbum.get(j) == staticAllMenuAlbum.get(i).getMenuId()) {
                            staticAllMenuAlbum.get(i).setCheckedStatus(true);
                        }
                    }
                }
            }
        }
        Log.e("SP MENU MAP", "----------------STATIC---------SHARED PREF-------" + menuMapAlbum);

        //--------------------------------------------------------------------------------------------------


        //---------------------DASHBOARD SP MENU--------------------------------------
        Gson gsonDSP1 = new Gson();
        String jsonDSP1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU_ARRAY);
        Type typeDSP1 = new TypeToken<ArrayList<AllMenu>>() {
        }.getType();
        staticDashAllMenuSP = gsonDSP1.fromJson(jsonDSP1, typeDSP1);

        Log.e("DASH SP MENU ARRAY", "----------------STATIC---------SHARED PREF-------" + staticAllMenu);

        if (staticDashAllMenuSP != null) {
            if (staticDashAllMenuSP.size() > 0) {
                for (int i = 0; i < staticDashAllMenuSP.size(); i++) {
                    dashMenuMapSP.put(staticDashAllMenuSP.get(i).getMenuId(), false);
                    staticDashAllMenuSP.get(i).setCheckedStatus(false);
                }
            }
        } else {
            staticDashAllMenuSP = new ArrayList<>();
        }

        Gson gsonDSP2 = new Gson();
        String jsonDSP2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU);
        Type typeDSP2 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdListDSP = gsonDSP2.fromJson(jsonDSP2, typeDSP2);
        if (menuIdListDSP != null) {
            if (menuIdListDSP.size() > 0) {
                for (int j = 0; j < menuIdListDSP.size(); j++) {
                    if (dashMenuMapSP.containsKey(menuIdListDSP.get(j))) {
                        dashMenuMapSP.put(menuIdListDSP.get(j), true);
                    }
                    for (int i = 0; i < staticDashAllMenuSP.size(); i++) {
                        if (menuIdListDSP.get(j) == staticDashAllMenuSP.get(i).getMenuId()) {
                            staticDashAllMenuSP.get(i).setCheckedStatus(true);
                        }
                    }
                }
            }
        }

        //--------------------------------------------------------------------------------------------------

        //-----------------------DASHBOARD REG MENU------------------------------------------------------------------

        Gson gsonDReg1 = new Gson();
        String jsonDReg1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU_ARRAY);
        Type typeDReg1 = new TypeToken<ArrayList<AllMenu>>() {
        }.getType();
        staticDashAllMenuREG = gsonDReg1.fromJson(jsonDReg1, typeDReg1);

        Log.e("DASH REG MENU ARRAY", "----------------STATIC---------SHARED PREF-------" + staticDashAllMenuREG);

        if (staticDashAllMenuREG != null) {
            if (staticDashAllMenuREG.size() > 0) {
                for (int i = 0; i < staticDashAllMenuREG.size(); i++) {
                    dashMenuMapREG.put(staticDashAllMenuREG.get(i).getMenuId(), false);
                    staticDashAllMenuREG.get(i).setCheckedStatus(false);
                }
            }
        } else {
            staticDashAllMenuREG = new ArrayList<>();
        }

        Gson gsonDReg2 = new Gson();
        String jsonDReg2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU);
        Type typeDReg2 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdListDReg = gsonDReg2.fromJson(jsonDReg2, typeDReg2);

        if (menuIdListDReg != null) {
            if (menuIdListDReg.size() > 0) {
                for (int j = 0; j < menuIdListDReg.size(); j++) {
                    if (dashMenuMapREG.containsKey(menuIdListDReg.get(j))) {
                        dashMenuMapREG.put(menuIdListDReg.get(j), true);
                    }
                    for (int i = 0; i < staticDashAllMenuREG.size(); i++) {
                        if (menuIdListDReg.get(j) == staticDashAllMenuREG.get(i).getMenuId()) {
                            staticDashAllMenuREG.get(i).setCheckedStatus(true);
                        }
                    }
                }
            }
        }
        //-------------------------------------------------------------------------------------------


        //---------------------DASHBOARD ALBUM MENU--------------------------------------
        Gson gsonDA1 = new Gson();
        String jsonDA1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU_ARRAY);
        Type typeDA1 = new TypeToken<ArrayList<AllMenu>>() {
        }.getType();
        staticDashAllMenuAlbum = gsonDA1.fromJson(jsonDA1, typeDA1);

        Log.e("DASH Album MENU ARRAY", "----------------STATIC---------SHARED PREF-------" + staticAllMenuAlbum);

        if (staticDashAllMenuAlbum != null) {
            if (staticDashAllMenuAlbum.size() > 0) {
                for (int i = 0; i < staticDashAllMenuAlbum.size(); i++) {
                    dashMenuMapAlbum.put(staticDashAllMenuAlbum.get(i).getMenuId(), false);
                    staticDashAllMenuAlbum.get(i).setCheckedStatus(false);
                }
            }
        } else {
            staticDashAllMenuAlbum = new ArrayList<>();
        }

        Gson gsonDA2 = new Gson();
        String jsonDA2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU);
        Type typeDA2 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdListDAlbum = gsonDA2.fromJson(jsonDA2, typeDA2);
        if (menuIdListDAlbum != null) {
            if (menuIdListDAlbum.size() > 0) {
                for (int j = 0; j < menuIdListDAlbum.size(); j++) {
                    if (dashMenuMapAlbum.containsKey(menuIdListDAlbum.get(j))) {
                        dashMenuMapAlbum.put(menuIdListDAlbum.get(j), true);
                    }
                    for (int i = 0; i < staticDashAllMenuAlbum.size(); i++) {
                        if (menuIdListDAlbum.get(j) == staticDashAllMenuAlbum.get(i).getMenuId()) {
                            staticDashAllMenuAlbum.get(i).setCheckedStatus(true);
                        }
                    }
                }
            }
        }

        //--------------------------------------------------------------------------------------------------


        //-----------------------DISPATCH ALL MENU------------------------------------------------------------------

        Gson gsonDisp1 = new Gson();
        String jsonDisp1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU_ARRAY);
        Type typeDisp1 = new TypeToken<ArrayList<AllMenu>>() {
        }.getType();
        dispatchAllMenu = gsonDisp1.fromJson(jsonDisp1, typeDisp1);

        Log.e("DISPATCH MENU ARRAY", "----------------STATIC---------SHARED PREF-------" + dispatchAllMenu);

        if (dispatchAllMenu != null) {
            if (dispatchAllMenu.size() > 0) {
                for (int i = 0; i < dispatchAllMenu.size(); i++) {
                    dispatchMenuMap.put(dispatchAllMenu.get(i).getMenuId(), false);
                    dispatchAllMenu.get(i).setCheckedStatus(false);
                }
            }
        } else {
            dispatchAllMenu = new ArrayList<>();
        }

        Gson gsonDisp2 = new Gson();
        String jsonDisp2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU);
        Type typeDisp2 = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> menuIdListDisp = gsonDisp2.fromJson(jsonDisp2, typeDisp2);

        Log.e("DISPATCH MENU LIST", "-------------------SHARED PREF-------" + menuIdListDisp);

        if (menuIdListDisp != null) {
            if (menuIdListDisp.size() > 0) {
                for (int j = 0; j < menuIdListDisp.size(); j++) {
                    if (dispatchMenuMap.containsKey(menuIdListDisp.get(j))) {
                        dispatchMenuMap.put(menuIdListDisp.get(j), true);
                    }
                    for (int i = 0; i < dispatchAllMenu.size(); i++) {
                        if (menuIdListDisp.get(j) == dispatchAllMenu.get(i).getMenuId()) {
                            dispatchAllMenu.get(i).setCheckedStatus(true);
                        }
                    }
                }
            }
        }
        //-------------------------------------------------------------------------------------------

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        int loadMenu = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_LOAD_MENU);

        Log.e("LOAD MENU","*************************************************************** "+loadMenu);

        if (loadMenu == 0) {
            getMenu(5, 0);
            getRegMenu(2, 3);
            getAlbumMenu(5, 4);
        }

        Gson gsonSP = new Gson();
        String jsonSP = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_COUNT_ARRAY);
        Type typeSP = new TypeToken<ArrayList<DashSPCakeCount>>() {
        }.getType();
        ArrayList<DashSPCakeCount> spCount = gsonSP.fromJson(jsonSP, typeSP);
        Log.e("SP COUNT SHPREF","--------------------------"+spCount);

        if (spCount != null) {
            int pending = 0;
            int handover = 0;
            int totalCount = 0;
            int start = 0;

            for (int i = 0; i < spCount.size(); i++) {
                if (spCount.get(i).getStatus() == 0) {
                    pending = pending + spCount.get(i).getSpOrderCount();
                } else if (spCount.get(i).getStatus() == 1) {
                    start = spCount.get(i).getSpOrderCount();
                    pending = pending + spCount.get(i).getSpOrderCount();
                } else if (spCount.get(i).getStatus() == 2) {
                    handover = spCount.get(i).getSpOrderCount();
                }
            }

            totalCount = pending + handover;
            tvSPP.setText("" + start);
            tvSPH.setText("" + handover);
            tvSPTC.setText("" + totalCount);
        } else {

            Log.e("SP DATA COUNT","***************************************");

           /* ArrayList<Integer> menuIdArray = new ArrayList<>();
            menuIdArray.add(-1);*/

            Gson gsonSP1 = new Gson();
            String jsonSP1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU);
            Type typeSP1 = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            ArrayList<Integer> menuIdArrayListSP = gsonSP1.fromJson(jsonSP1, typeSP1);

            Log.e("DASH SP MENU ", "--------------------------*****************  456  *************** ------------------------------- " + menuIdArrayListSP);

            if (menuIdArrayListSP == null) {
                menuIdArrayListSP = new ArrayList<>();
                menuIdArrayListSP.add(-1);
            }

            if (menuIdArrayListSP.size() <= 0) {
                menuIdArrayListSP.add(-1);
            }


            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);
            if (date == null) {

                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_DATE, sdf.format(System.currentTimeMillis()));
                date = sdf.format(System.currentTimeMillis());
            }

            Log.e("DASH COUNT SP ", "-**********************************---------------------------- Line :  474");
            getDashSPCount(date, menuIdArrayListSP);
        }

        Gson gsonREG = new Gson();
        String jsonREG = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_COUNT_ARRAY);
        Type typeREG = new TypeToken<ArrayList<DashRegCakeCount>>() {
        }.getType();
        ArrayList<DashRegCakeCount> regCount = gsonREG.fromJson(jsonREG, typeREG);

        if (regCount != null) {
            int pending = 0;
            int handover = 0;
            int totalCount = 0;
            int start = 0;

            for (int i = 0; i < regCount.size(); i++) {
                if (regCount.get(i).getStatus() == 0) {
                    pending = pending + regCount.get(i).getRegSpOrderCount();
                } else if (regCount.get(i).getStatus() == 1) {
                    start = regCount.get(i).getRegSpOrderCount();
                    pending = pending + regCount.get(i).getRegSpOrderCount();
                } else if (regCount.get(i).getStatus() == 2) {
                    handover = regCount.get(i).getRegSpOrderCount();
                }
            }

            totalCount = pending + handover;
            tvRegP.setText("" + start);
            tvRegH.setText("" + handover);
            tvRegTC.setText("" + totalCount);
        } else {
           /* ArrayList<Integer> menuIdArray = new ArrayList<>();
            menuIdArray.add(-1);*/

            Gson gsonREG1 = new Gson();
            String jsonREG1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU);
            Type typeREG1 = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            ArrayList<Integer> menuIdArrayListREG = gsonREG1.fromJson(jsonREG1, typeREG1);

            if (menuIdArrayListREG == null) {
                menuIdArrayListREG = new ArrayList<>();
                menuIdArrayListREG.add(-1);
            }

            if (menuIdArrayListREG.size() <= 0) {
                menuIdArrayListREG.add(-1);
            }


            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);
            if (date == null) {

                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_DATE, sdf.format(System.currentTimeMillis()));
                date = sdf.format(System.currentTimeMillis());
            }

            getDashREGCount(date, menuIdArrayListREG);
        }


        //-----------ALBUM------------
        Gson gsonA = new Gson();
        String jsonA = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_COUNT_ARRAY);
        Type typeA = new TypeToken<ArrayList<DashSPCakeCount>>() {
        }.getType();
        ArrayList<DashSPCakeCount> albumCount = gsonA.fromJson(jsonA, typeA);

        if (albumCount != null) {
            int pending = 0;
            int handover = 0;
            int totalCount = 0;
            int start = 0;

            for (int i = 0; i < albumCount.size(); i++) {
                if (albumCount.get(i).getStatus() == 0) {
                    pending = pending + albumCount.get(i).getSpOrderCount();
                } else if (albumCount.get(i).getStatus() == 1) {
                    start = albumCount.get(i).getSpOrderCount();
                    pending = pending + albumCount.get(i).getSpOrderCount();
                } else if (albumCount.get(i).getStatus() == 2) {
                    handover = albumCount.get(i).getSpOrderCount();
                }
            }

            totalCount = pending + handover;
            tvAlbumP.setText("" + start);
            tvAlbumH.setText("" + handover);
            tvAlbumTC.setText("" + totalCount);
        } else {

           /* ArrayList<Integer> menuIdArray = new ArrayList<>();
            menuIdArray.add(-1);*/

            Gson gsonAB1 = new Gson();
            String jsonAB1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU);
            Type typeAB1 = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            ArrayList<Integer> menuIdArrayListAlbum = gsonAB1.fromJson(jsonAB1, typeAB1);

            Log.e("DASH ALBUM MENU ", "--------------------------*****************  456  *************** ------------------------------- " + menuIdArrayListAlbum);

            if (menuIdArrayListAlbum == null) {
                menuIdArrayListAlbum = new ArrayList<>();
                menuIdArrayListAlbum.add(-1);
            }


            if (menuIdArrayListAlbum.size() <= 0) {
                menuIdArrayListAlbum.add(-1);
            }


            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);
            if (date == null) {

                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_DATE, sdf.format(System.currentTimeMillis()));
                date = sdf.format(System.currentTimeMillis());
            }

            Log.e("DASH COUNT ALBUM ", "-**********************************---------------------------- Line :  695");
            getDashAlbumCount(date, menuIdArrayListAlbum);
        }


        Gson gsonGateSale = new Gson();
        String jsonGateSale = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_GATE_SALE_COUNT_ARRAY);
        Type typeGateSale = new TypeToken<ArrayList<GateSaleCount>>() {
        }.getType();
        ArrayList<GateSaleCount> gateSaleCount = gsonGateSale.fromJson(jsonGateSale, typeGateSale);

        if (gateSaleCount != null) {

            gateSaleCountAdapter = new GateSaleCountAdapter(gateSaleCount, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(gateSaleCountAdapter);
        } else {
            getGateSaleCount(1);
        }

        Gson gsonConsume = new Gson();
        String jsonConsume = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_GATE_SALE_COUNT_ARRAY);
        Type typeConsume = new TypeToken<ArrayList<ConsumeCount>>() {
        }.getType();
        ArrayList<ConsumeCount> consumeCount = gsonConsume.fromJson(jsonConsume, typeConsume);

        if (consumeCount != null) {
            consumeCountAdapter = new ConsumeCountAdapter(consumeCount, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerViewConsumption.setLayoutManager(mLayoutManager);
            recyclerViewConsumption.setItemAnimator(new DefaultItemAnimator());
            recyclerViewConsumption.setAdapter(consumeCountAdapter);
        } else {

            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_DATE, sdf.format(System.currentTimeMillis()));

            getStockTransferType(sdf.format(System.currentTimeMillis()));
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSPProd) {

            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);

            Fragment adf = new SpecialCakeOrderFragment();
            Bundle args = new Bundle();
            args.putString("date", date);
            args.putString("type", "SP");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

        } else if (view.getId() == R.id.btnSPCheck) {

            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);

            Fragment adf = new SPCheckingFragment();
            Bundle args = new Bundle();
            args.putString("date", date);
            args.putString("type", "SP");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

        } else if (view.getId() == R.id.btnAlbumProd) {

            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);

            Fragment adf = new SpecialCakeOrderFragment();
            Bundle args = new Bundle();
            args.putString("date", date);
            args.putString("type", "ALBUM");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

        } else if (view.getId() == R.id.btnAlbumCheck) {

            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);

            Fragment adf = new SPCheckingFragment();
            Bundle args = new Bundle();
            args.putString("date", date);
            args.putString("type", "ALBUM");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

        } else if (view.getId() == R.id.btnRegProd) {

            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);

            Fragment adf = new RegularCakeAsSpFragment();
            Bundle args = new Bundle();
            args.putString("date", date);
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

        } else if (view.getId() == R.id.btnRegCheck) {

            String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);

            Fragment adf = new SPCheckingFragment();
            Bundle args = new Bundle();
            args.putString("date", date);
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DashboardFragment").commit();

        } else if (view.getId() == R.id.fab) {
            new FilterDialog(getContext()).show();
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

                            CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_LOAD_MENU, 1);

                            ArrayList<AllMenu> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                //Toast.makeText(SelectCityActivity.this, "No Cities Found !", Toast.LENGTH_SHORT).show();
                            } else {

                                if (staticAllMenu != null) {
                                    staticAllMenu.clear();
                                }
                                staticAllMenu = data;

                                if (staticDashAllMenuSP != null) {
                                    staticDashAllMenuSP.clear();
                                }
                                staticDashAllMenuSP = data;

                                if (dispatchAllMenu != null) {
                                    dispatchAllMenu.clear();
                                } else {
                                    dispatchAllMenu = new ArrayList<>();
                                }

                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        dispatchAllMenu.add(data.get(i));
                                    }
                                }

                                Gson gson = new Gson();
                                String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_MENU);
                                Type type = new TypeToken<ArrayList<Integer>>() {
                                }.getType();

                                ArrayList<Integer> menuArrayList = gson.fromJson(json, type);

                                if (menuArrayList != null && menuArrayList.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        for (int j = 0; j < menuArrayList.size(); j++) {
                                            if (data.get(i).getMenuId() == menuArrayList.get(j)) {
                                                data.get(i).setCheckedStatus(true);
                                                menuMap.put(data.get(i).getMenuId(), true);

                                                staticAllMenu.get(i).setCheckedStatus(true);
                                                Log.e("MENU : --------- " + staticAllMenu.get(i).getMenuId(), "------------------ TRUE");
                                                break;

                                            } else {
                                                data.get(i).setCheckedStatus(false);
                                                menuMap.put(data.get(i).getMenuId(), false);

                                                staticAllMenu.get(i).setCheckedStatus(false);
                                                Log.e("MENU : --------- " + staticAllMenu.get(i).getMenuId(), "------------------ FALSE");


                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < data.size(); i++) {
                                        data.get(i).setCheckedStatus(false);
                                        menuMap.put(data.get(i).getMenuId(), false);

                                        staticAllMenu.get(i).setCheckedStatus(false);
                                        Log.e("MENU ELSE : --------- " + staticAllMenu.get(i).getMenuId(), "------------------ FALSE");

                                    }
                                }

                                Gson gson2 = new Gson();
                                String json2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU);
                                Type type2 = new TypeToken<ArrayList<Integer>>() {
                                }.getType();

                                ArrayList<Integer> menuArrayListDashSP = gson2.fromJson(json2, type2);

                                if (menuArrayListDashSP != null && menuArrayListDashSP.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        for (int j = 0; j < menuArrayListDashSP.size(); j++) {
                                            if (data.get(i).getMenuId() == menuArrayListDashSP.get(j)) {
                                                data.get(i).setCheckedStatus(true);
                                                dashMenuMapSP.put(data.get(i).getMenuId(), true);

                                                staticDashAllMenuSP.get(i).setCheckedStatus(true);
                                                break;

                                            } else {
                                                data.get(i).setCheckedStatus(false);
                                                dashMenuMapSP.put(data.get(i).getMenuId(), false);
                                                staticDashAllMenuSP.get(i).setCheckedStatus(false);
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < data.size(); i++) {
                                        data.get(i).setCheckedStatus(false);
                                        dashMenuMapSP.put(data.get(i).getMenuId(), false);
                                        staticDashAllMenuSP.get(i).setCheckedStatus(false);
                                    }
                                }

                                Gson gson1 = new Gson();
                                String jsonMenu = gson1.toJson(staticAllMenu);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_SP_MENU_ARRAY, jsonMenu);

                                Gson gsn = new Gson();
                                String jsnMenu = gsn.toJson(staticDashAllMenuSP);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU_ARRAY, jsnMenu);

                                Gson gsnDisp = new Gson();
                                String jsnMenuDisp = gsnDisp.toJson(dispatchAllMenu);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU_ARRAY, jsnMenuDisp);

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
                public void onFailure(Call<ArrayList<AllMenu>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAlbumMenu(int catId, int isSameDay) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<AllMenu>> listCall = Constants.myInterface.getMenu(catId, isSameDay);
            listCall.enqueue(new Callback<ArrayList<AllMenu>>() {
                @Override
                public void onResponse(Call<ArrayList<AllMenu>> call, Response<ArrayList<AllMenu>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ALBUM Menu Data : ", "------------" + response.body());
                            CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_LOAD_MENU, 1);

                            ArrayList<AllMenu> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                //Toast.makeText(SelectCityActivity.this, "No Cities Found !", Toast.LENGTH_SHORT).show();
                            } else {

                                if (staticAllMenuAlbum != null) {
                                    staticAllMenuAlbum.clear();
                                }
                                staticAllMenuAlbum = data;

                                if (staticDashAllMenuAlbum != null) {
                                    staticDashAllMenuAlbum.clear();
                                }
                                staticDashAllMenuAlbum = data;

                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        dispatchAllMenu.add(data.get(i));
                                    }
                                }

                                Gson gson1 = new Gson();
                                String json1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_ALBUM_MENU);
                                Type type1 = new TypeToken<ArrayList<Integer>>() {
                                }.getType();

                                ArrayList<Integer> menuArrayListAlbum = gson1.fromJson(json1, type1);

                                if (menuArrayListAlbum != null && menuArrayListAlbum.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        for (int j = 0; j < menuArrayListAlbum.size(); j++) {
                                            if (data.get(i).getMenuId() == menuArrayListAlbum.get(j)) {
                                                data.get(i).setCheckedStatus(true);
                                                menuMapAlbum.put(data.get(i).getMenuId(), true);

                                                staticAllMenuAlbum.get(i).setCheckedStatus(true);
                                                break;

                                            } else {
                                                data.get(i).setCheckedStatus(false);
                                                menuMapAlbum.put(data.get(i).getMenuId(), false);

                                                staticAllMenuAlbum.get(i).setCheckedStatus(false);
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < data.size(); i++) {
                                        data.get(i).setCheckedStatus(false);
                                        menuMapAlbum.put(data.get(i).getMenuId(), false);

                                        staticAllMenuAlbum.get(i).setCheckedStatus(false);
                                    }
                                }

                                Gson gson2 = new Gson();
                                String json2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU);
                                Type type2 = new TypeToken<ArrayList<Integer>>() {
                                }.getType();

                                ArrayList<Integer> menuArrayListDashAlbum = gson2.fromJson(json2, type2);

                                if (menuArrayListDashAlbum != null && menuArrayListDashAlbum.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        for (int j = 0; j < menuArrayListDashAlbum.size(); j++) {
                                            if (data.get(i).getMenuId() == menuArrayListDashAlbum.get(j)) {
                                                data.get(i).setCheckedStatus(true);
                                                dashMenuMapAlbum.put(data.get(i).getMenuId(), true);

                                                staticDashAllMenuAlbum.get(i).setCheckedStatus(true);
                                                break;

                                            } else {
                                                data.get(i).setCheckedStatus(false);
                                                dashMenuMapAlbum.put(data.get(i).getMenuId(), false);

                                                staticDashAllMenuAlbum.get(i).setCheckedStatus(false);
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < data.size(); i++) {
                                        data.get(i).setCheckedStatus(false);
                                        dashMenuMapAlbum.put(data.get(i).getMenuId(), false);

                                        staticDashAllMenuAlbum.get(i).setCheckedStatus(false);
                                    }
                                }


                                for (int i = 0; i < data.size(); i++) {
                                    data.get(i).setCheckedStatus(false);
                                    dispatchMenuMap.put(data.get(i).getMenuId(), false);

                                    dispatchAllMenu.get(i).setCheckedStatus(false);
                                }

                                Gson gsn = new Gson();
                                String jsnMenu = gsn.toJson(staticAllMenuAlbum);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_ALBUM_MENU_ARRAY, jsnMenu);

                                Gson gsn1 = new Gson();
                                String jsnMenu1 = gsn1.toJson(staticDashAllMenuAlbum);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU_ARRAY, jsnMenu1);

                                Gson gsnDisp = new Gson();
                                String jsnMenuDisp = gsnDisp.toJson(dispatchAllMenu);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU_ARRAY, jsnMenuDisp);

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
                public void onFailure(Call<ArrayList<AllMenu>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
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

                            Log.e("REG Menu Data : ", "------------" + response.body());
                            CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_LOAD_MENU, 1);

                            ArrayList<AllMenu> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                //Toast.makeText(SelectCityActivity.this, "No Cities Found !", Toast.LENGTH_SHORT).show();
                            } else {

                                if (staticAllMenuReg != null) {
                                    staticAllMenuReg.clear();
                                }
                                staticAllMenuReg = data;

                                if (staticDashAllMenuREG != null) {
                                    staticDashAllMenuREG.clear();
                                }
                                staticDashAllMenuREG = data;

                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        dispatchAllMenu.add(data.get(i));
                                    }
                                }

                                Gson gson1 = new Gson();
                                String json1 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_REG_MENU);
                                Type type1 = new TypeToken<ArrayList<Integer>>() {
                                }.getType();

                                ArrayList<Integer> menuArrayListReg = gson1.fromJson(json1, type1);

                                if (menuArrayListReg != null && menuArrayListReg.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        for (int j = 0; j < menuArrayListReg.size(); j++) {
                                            if (data.get(i).getMenuId() == menuArrayListReg.get(j)) {
                                                data.get(i).setCheckedStatus(true);
                                                menuMapReg.put(data.get(i).getMenuId(), true);

                                                staticAllMenuReg.get(i).setCheckedStatus(true);
                                                break;

                                            } else {
                                                data.get(i).setCheckedStatus(false);
                                                menuMapReg.put(data.get(i).getMenuId(), false);

                                                staticAllMenuReg.get(i).setCheckedStatus(false);
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < data.size(); i++) {
                                        data.get(i).setCheckedStatus(false);
                                        menuMapReg.put(data.get(i).getMenuId(), false);

                                        staticAllMenuReg.get(i).setCheckedStatus(false);
                                    }
                                }

                                Gson gson2 = new Gson();
                                String json2 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU);
                                Type type2 = new TypeToken<ArrayList<Integer>>() {
                                }.getType();

                                ArrayList<Integer> menuArrayListDashReg = gson2.fromJson(json2, type2);

                                if (menuArrayListDashReg != null && menuArrayListDashReg.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        for (int j = 0; j < menuArrayListDashReg.size(); j++) {
                                            if (data.get(i).getMenuId() == menuArrayListDashReg.get(j)) {
                                                data.get(i).setCheckedStatus(true);
                                                dashMenuMapREG.put(data.get(i).getMenuId(), true);

                                                staticDashAllMenuREG.get(i).setCheckedStatus(true);
                                                break;

                                            } else {
                                                data.get(i).setCheckedStatus(false);
                                                dashMenuMapREG.put(data.get(i).getMenuId(), false);

                                                staticDashAllMenuREG.get(i).setCheckedStatus(false);
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < data.size(); i++) {
                                        data.get(i).setCheckedStatus(false);
                                        dashMenuMapREG.put(data.get(i).getMenuId(), false);

                                        staticDashAllMenuREG.get(i).setCheckedStatus(false);
                                    }
                                }


                                for (int i = 0; i < data.size(); i++) {
                                    data.get(i).setCheckedStatus(false);
                                    dispatchMenuMap.put(data.get(i).getMenuId(), false);

                                    dispatchAllMenu.get(i).setCheckedStatus(false);
                                }

                                Gson gsn = new Gson();
                                String jsnMenu = gsn.toJson(staticAllMenuReg);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_REG_MENU_ARRAY, jsnMenu);

                                Gson gsn1 = new Gson();
                                String jsnMenu1 = gsn1.toJson(staticDashAllMenuREG);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU_ARRAY, jsnMenu1);

                                Gson gsnDisp = new Gson();
                                String jsnMenuDisp = gsnDisp.toJson(dispatchAllMenu);
                                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DISPATCH_MENU_ARRAY, jsnMenuDisp);

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
                public void onFailure(Call<ArrayList<AllMenu>> call, Throwable t) {
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item1 = menu.findItem(R.id.action_reload);
        item1.setVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                //getMenu(5, 0);
                //getRegMenu(2, 3);
                // getDashSPCount();


                Gson gson = new Gson();
                String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU);
                Type type = new TypeToken<ArrayList<Integer>>() {
                }.getType();
                ArrayList<Integer> menuIdArrayListSP = gson.fromJson(json, type);

                if (menuIdArrayListSP == null) {
                    menuIdArrayListSP = new ArrayList<>();
                    menuIdArrayListSP.add(-1);
                }

                if (menuIdArrayListSP.size() <= 0) {
                    menuIdArrayListSP.add(-1);
                }

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

                Gson gson3 = new Gson();
                String json3 = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU);
                Type type3 = new TypeToken<ArrayList<Integer>>() {
                }.getType();
                ArrayList<Integer> menuIdArrayListAlbum = gson3.fromJson(json3, type3);

                if (menuIdArrayListAlbum == null) {
                    menuIdArrayListAlbum = new ArrayList<>();
                    menuIdArrayListAlbum.add(-1);
                }

                if (menuIdArrayListAlbum.size() <= 0) {
                    menuIdArrayListAlbum.add(-1);
                }


                //  CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_DATE, date);
                String date = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);
                Log.e("DASH SAVED DATE ", "-------------------------************------------------------------- " + date);

                Log.e("DASH COUNT SP ", "-**********************************---------------------------- Line :  993");
                getDashSPCount(date, menuIdArrayListSP);
                getDashREGCount(date, menuIdArrayListReg);
                getDashAlbumCount(date, menuIdArrayListAlbum);
                getGateSaleCount(1);
                getStockTransferType(date);



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FilterDialog extends Dialog {

        EditText edDate;
        TextView tvDate;
        ImageView ivClose;
        LinearLayout llChooseSPMenu, llChooseRegMenu, llChooseAlbumMenu;


        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_dashboard_filter);
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
            llChooseSPMenu = findViewById(R.id.llChooseSPMenu);
            llChooseAlbumMenu = findViewById(R.id.llChooseAlbumMenu);
            llChooseRegMenu = findViewById(R.id.llChooseRegMenu);
            tvSPMenu = findViewById(R.id.tvSPMenu);
            tvRegMenu = findViewById(R.id.tvRegMenu);
            tvAlbumMenu = findViewById(R.id.tvAlbumMenu);

            String prefDate = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_DATE);
            edDate.setText("" + prefDate);
            tvDate.setText("" + prefDate);

            llChooseSPMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("DASH SP MENU ", "-----------------------------********---------------------------------- " + staticDashAllMenuSP);


                    if (staticDashAllMenuSP != null) {
                        showSPMenuDialog(staticDashAllMenuSP);
                    } else {
                        Toast.makeText(getContext(), "Reload Data", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            llChooseAlbumMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("DASH ALBUM MENU ", "-----------------------------********---------------------------------- " + staticDashAllMenuSP);


                    if (staticDashAllMenuAlbum != null) {
                        showAlbumMenuDialog(staticDashAllMenuAlbum);
                    } else {
                        Toast.makeText(getContext(), "Reload Data", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            llChooseRegMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("DASH SP MENU ", "-----------------------------********---------------------------------- " + staticDashAllMenuREG);


                    if (staticDashAllMenuREG != null) {
                        showRegMenuDialog(staticDashAllMenuREG);
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

                        Gson gson = new Gson();
                        String json = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU);
                        Type type = new TypeToken<ArrayList<Integer>>() {
                        }.getType();
                        ArrayList<Integer> menuIdArrayListSP = gson.fromJson(json, type);

                        Log.e("DASH FILTER SP MENU ", "----------------------------  1116 ********************* ----------------------------------- " + menuIdArrayListSP);

                        if (menuIdArrayListSP == null) {
                            menuIdArrayListSP = new ArrayList<>();
                            menuIdArrayListSP.add(-1);
                        }

                        if (menuIdArrayListSP.size() <= 0) {
                            menuIdArrayListSP.add(-1);
                        }

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


                        Gson gsonA = new Gson();
                        String jsonA = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU);
                        Type typeA = new TypeToken<ArrayList<Integer>>() {
                        }.getType();
                        ArrayList<Integer> menuIdArrayListAlbum = gsonA.fromJson(jsonA, typeA);

                        Log.e("DASH FILTER ALBUM MENU ", "----------------------------  1116 ********************* ----------------------------------- " + menuIdArrayListAlbum);

                        if (menuIdArrayListAlbum == null) {
                            menuIdArrayListAlbum = new ArrayList<>();
                            menuIdArrayListAlbum.add(-1);
                        }

                        if (menuIdArrayListAlbum.size() <= 0) {
                            menuIdArrayListAlbum.add(-1);
                        }


                        CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_DATE, date);

                        Log.e("DASH COUNT SP ", "-**********************************---------------------------- Line :  1142");
                        getDashSPCount(date, menuIdArrayListSP);
                        getDashAlbumCount(date, menuIdArrayListAlbum);
                        getDashREGCount(date, menuIdArrayListReg);
                        getGateSaleCount(1);
                        getStockTransferType(date);

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

    public void showSPMenuDialog(final ArrayList<AllMenu> menuArrayList) {

        dialogSPMenu = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_choose_sp_menu, null, false);
        dialogSPMenu.setContentView(v);
        dialogSPMenu.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView tvClear = dialogSPMenu.findViewById(R.id.tvClear);
        cbSPMenu = dialogSPMenu.findViewById(R.id.allSPMenuCheckbox);
        final RecyclerView recyclerView = dialogSPMenu.findViewById(R.id.recyclerView);
        Button btnSubmit = dialogSPMenu.findViewById(R.id.btnSubmit);

        adapterSPMenu = new DashboardSPMenuAdapter(menuArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterSPMenu);

       /* final int allMenu = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_DASH_SP_ALL_MENU);
        if (allMenu == 0) {
            cbSPMenu.setChecked(true);
        } else {
            cbSPMenu.setChecked(false);
        }*/

        isSPTouched = false;
        cbSPMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSPTouched = true;
                return false;
            }
        });

        cbSPMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSPTouched = false;
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DASH_SP_ALL_MENU, 0);

                    if (menuArrayList.size() > 0) {
                        for (int i = 0; i < menuArrayList.size(); i++) {
                            dashMenuMapSP.put(menuArrayList.get(i).getMenuId(), true);
                            menuArrayList.get(i).setCheckedStatus(true);
                        }
                        adapterSPMenu.notifyDataSetChanged();
                    }
                } else {

                    if (isSPTouched) {
                        isSPTouched = false;
                        if (menuArrayList.size() > 0) {
                            for (int i = 0; i < menuArrayList.size(); i++) {
                                dashMenuMapSP.put(menuArrayList.get(i).getMenuId(), false);
                                menuArrayList.get(i).setCheckedStatus(false);
                            }
                            adapterSPMenu.notifyDataSetChanged();
                        }
                    }
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DASH_SP_ALL_MENU, 1);


                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuArrayList.size() > 0) {
                    for (int i = 0; i < menuArrayList.size(); i++) {
                        dashMenuMapSP.put(menuArrayList.get(i).getMenuId(), false);
                        menuArrayList.get(i).setCheckedStatus(false);
                    }
                    cbSPMenu.setChecked(false);
                    adapterSPMenu.notifyDataSetChanged();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSPMenu.dismiss();

                ArrayList<Integer> menuIds = new ArrayList<>();
                for (int i = 0; i < menuArrayList.size(); i++) {
                    if (dashMenuMapSP.get(menuArrayList.get(i).getMenuId())) {
                        menuIds.add(menuArrayList.get(i).getMenuId());
                    }
                }

                Gson gson = new Gson();
                String jsonMenu = gson.toJson(menuIds);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_SP_MENU, jsonMenu);

                if (cbSPMenu.isChecked()) {
                    tvSPMenu.setText("All Menu");
                } else {
                    tvSPMenu.setText("");
                }

            }
        });

        dialogSPMenu.show();
    }


    public void showAlbumMenuDialog(final ArrayList<AllMenu> menuArrayList) {

        dialogAlbumMenu = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_choose_album_menu, null, false);
        dialogAlbumMenu.setContentView(v);
        dialogAlbumMenu.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView tvClear = dialogAlbumMenu.findViewById(R.id.tvClear);
        cbAlbumMenu = dialogAlbumMenu.findViewById(R.id.allSPMenuCheckbox);
        final RecyclerView recyclerView = dialogAlbumMenu.findViewById(R.id.recyclerView);
        Button btnSubmit = dialogAlbumMenu.findViewById(R.id.btnSubmit);

        adapterAlbumMenu = new DashboardAlbumMenuAdapter(menuArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterAlbumMenu);

       /* final int allMenu = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_DASH_SP_ALL_MENU);
        if (allMenu == 0) {
            cbSPMenu.setChecked(true);
        } else {
            cbSPMenu.setChecked(false);
        }*/

        isAlbumTouched = false;
        cbAlbumMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isAlbumTouched = true;
                return false;
            }
        });

        cbAlbumMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAlbumTouched = false;
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_ALL_MENU, 0);

                    if (menuArrayList.size() > 0) {
                        for (int i = 0; i < menuArrayList.size(); i++) {
                            dashMenuMapAlbum.put(menuArrayList.get(i).getMenuId(), true);
                            menuArrayList.get(i).setCheckedStatus(true);
                        }
                        adapterAlbumMenu.notifyDataSetChanged();
                    }
                } else {

                    if (isAlbumTouched) {
                        isAlbumTouched = false;
                        if (menuArrayList.size() > 0) {
                            for (int i = 0; i < menuArrayList.size(); i++) {
                                dashMenuMapAlbum.put(menuArrayList.get(i).getMenuId(), false);
                                menuArrayList.get(i).setCheckedStatus(false);
                            }
                            adapterAlbumMenu.notifyDataSetChanged();
                        }
                    }
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_ALL_MENU, 1);


                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuArrayList.size() > 0) {
                    for (int i = 0; i < menuArrayList.size(); i++) {
                        dashMenuMapAlbum.put(menuArrayList.get(i).getMenuId(), false);
                        menuArrayList.get(i).setCheckedStatus(false);
                    }
                    cbAlbumMenu.setChecked(false);
                    adapterAlbumMenu.notifyDataSetChanged();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlbumMenu.dismiss();

                ArrayList<Integer> menuIds = new ArrayList<>();
                for (int i = 0; i < menuArrayList.size(); i++) {
                    if (dashMenuMapAlbum.get(menuArrayList.get(i).getMenuId())) {
                        menuIds.add(menuArrayList.get(i).getMenuId());
                    }
                }

                Gson gson = new Gson();
                String jsonMenu = gson.toJson(menuIds);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_MENU, jsonMenu);

                if (cbAlbumMenu.isChecked()) {
                    tvAlbumMenu.setText("All Menu");
                } else {
                    tvAlbumMenu.setText("");
                }

            }
        });

        dialogAlbumMenu.show();
    }

    public void showRegMenuDialog(final ArrayList<AllMenu> menuArrayList) {
        dialogRegMenu = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_choose_sp_menu, null, false);
        dialogRegMenu.setContentView(v);
        dialogRegMenu.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView tvTitle = dialogRegMenu.findViewById(R.id.tvTitle);
        TextView tvClear = dialogRegMenu.findViewById(R.id.tvClear);
        cbRegMenu = dialogRegMenu.findViewById(R.id.allSPMenuCheckbox);
        final RecyclerView recyclerView = dialogRegMenu.findViewById(R.id.recyclerView);
        Button btnSubmit = dialogRegMenu.findViewById(R.id.btnSubmit);

        adapterREGMenu = new DashboardREGMenuAdapter(menuArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterREGMenu);

       /* final int allMenu = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_DASH_SP_ALL_MENU);
        if (allMenu == 0) {
            cbSPMenu.setChecked(true);
        } else {
            cbSPMenu.setChecked(false);
        }*/

        isRegTouched = false;
        cbRegMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isRegTouched = true;
                return false;
            }
        });

        cbRegMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isRegTouched = false;
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DASH_REG_ALL_MENU, 0);

                    if (menuArrayList.size() > 0) {
                        for (int i = 0; i < menuArrayList.size(); i++) {
                            dashMenuMapREG.put(menuArrayList.get(i).getMenuId(), true);
                            menuArrayList.get(i).setCheckedStatus(true);
                        }
                        adapterREGMenu.notifyDataSetChanged();
                    }
                } else {

                    if (isSPTouched) {
                        Log.e("CHECKBOX", "-----------ISTOUCHED");
                        isRegTouched = false;
                        if (menuArrayList.size() > 0) {
                            for (int i = 0; i < menuArrayList.size(); i++) {
                                dashMenuMapREG.put(menuArrayList.get(i).getMenuId(), false);
                                menuArrayList.get(i).setCheckedStatus(false);
                            }
                            adapterREGMenu.notifyDataSetChanged();
                        }
                    }
                    CustomSharedPreference.putInt(getActivity(), CustomSharedPreference.KEY_DASH_REG_ALL_MENU, 1);


                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuArrayList.size() > 0) {
                    for (int i = 0; i < menuArrayList.size(); i++) {
                        dashMenuMapREG.put(menuArrayList.get(i).getMenuId(), false);
                        menuArrayList.get(i).setCheckedStatus(false);
                    }
                    cbRegMenu.setChecked(false);
                    adapterREGMenu.notifyDataSetChanged();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRegMenu.dismiss();

                ArrayList<Integer> menuIds = new ArrayList<>();
                for (int i = 0; i < menuArrayList.size(); i++) {
                    if (dashMenuMapREG.get(menuArrayList.get(i).getMenuId())) {
                        menuIds.add(menuArrayList.get(i).getMenuId());
                    }
                }

                Gson gson = new Gson();
                String jsonMenu = gson.toJson(menuIds);
                CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_REG_MENU, jsonMenu);

                if (cbRegMenu.isChecked()) {
                    tvRegMenu.setText("All Menu");
                } else {
                    tvRegMenu.setText("");
                }

            }
        });

        dialogRegMenu.show();
    }

    public void getDashSPCount(String prodDate, ArrayList<Integer> menuIdList) {

        Log.e("PARAMETER", "************getDashSPCount********* DATE : " + prodDate + "         Menu List : " + menuIdList);

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

                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_SP_COUNT_ARRAY, json);

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

                                tvSPP.setText("" + started);
                                tvSPH.setText("" + handover);
                                tvSPTC.setText("" + totalCount);

                            } else {
                                tvSPP.setText("0");
                                tvSPH.setText("0");
                                tvSPTC.setText("0");
                            }


                            commonDialog.dismiss();
                        } else {
                            tvSPP.setText("0");
                            tvSPH.setText("0");
                            tvSPTC.setText("0");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        tvSPP.setText("0");
                        tvSPH.setText("0");
                        tvSPTC.setText("0");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DashSPCakeCount>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                    tvSPP.setText("0");
                    tvSPH.setText("0");
                    tvSPTC.setText("0");
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    public void getDashAlbumCount(String prodDate, ArrayList<Integer> menuIdList) {

        Log.e("PARAMETER", "************getDashAlbumCount********* DATE : " + prodDate + "         Menu List : " + menuIdList);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DashSPCakeCount>> listCall = Constants.myInterface.getDashAlbumCakeCount(prodDate, menuIdList);
            listCall.enqueue(new Callback<ArrayList<DashSPCakeCount>>() {
                @Override
                public void onResponse(Call<ArrayList<DashSPCakeCount>> call, Response<ArrayList<DashSPCakeCount>> response) {
                    try {

                        Log.e("DASH ALBUM COUNT ", "-------------- RESPONSE--------------------- " + response.body());
                        if (response.body() != null) {

                            Log.e("DASH ALBUM COUNT : ", "------------" + response.body());

                            ArrayList<DashSPCakeCount> data = response.body();

                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_ALBUM_COUNT_ARRAY, json);

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

                                tvAlbumP.setText("" + started);
                                tvAlbumH.setText("" + handover);
                                tvAlbumTC.setText("" + totalCount);

                            } else {
                                tvAlbumP.setText("0");
                                tvAlbumH.setText("0");
                                tvAlbumTC.setText("0");
                            }


                            commonDialog.dismiss();
                        } else {
                            tvAlbumP.setText("0");
                            tvAlbumH.setText("0");
                            tvAlbumTC.setText("0");
                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        tvAlbumP.setText("0");
                        tvAlbumH.setText("0");
                        tvAlbumTC.setText("0");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DashSPCakeCount>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                    tvAlbumP.setText("0");
                    tvAlbumH.setText("0");
                    tvAlbumTC.setText("0");
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    public void getDashREGCount(String prodDate, ArrayList<Integer> menuIdList) {
        if (Constants.isOnline(getContext())) {

            Log.e("PARAMETER ----------- ", "DASH REG COUNT--------------------------- date : " + prodDate + "                             menu : " + menuIdList);

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

                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_REG_COUNT_ARRAY, json);

                            if (data.size() > 0) {

                                int pending = 0;
                                int handover = 0;
                                int totalCount = 0;
                                int started = 0;

                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getStatus() == 0) {
                                        pending = pending + data.get(i).getRegSpOrderCount();
                                    } else if (data.get(i).getStatus() == 1) {
                                        started = data.get(i).getRegSpOrderCount();
                                        pending = pending + data.get(i).getRegSpOrderCount();
                                    } else if (data.get(i).getStatus() == 2) {
                                        handover = data.get(i).getRegSpOrderCount();
                                    }
                                }

                                totalCount = pending + handover;

                                tvRegP.setText("" + started);
                                tvRegH.setText("" + handover);
                                tvRegTC.setText("" + totalCount);

                            } else {
                                tvRegP.setText("0");
                                tvRegH.setText("0");
                                tvRegTC.setText("0");
                            }

                            commonDialog.dismiss();
                        } else {
                            tvRegP.setText("0");
                            tvRegH.setText("0");
                            tvRegTC.setText("0");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        tvRegP.setText("0");
                        tvRegH.setText("0");
                        tvRegTC.setText("0");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DashRegCakeCount>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                    tvRegP.setText("0");
                    tvRegH.setText("0");
                    tvRegTC.setText("0");
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getGateSaleCount(int stockType) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<GateSaleCount>> listCall = Constants.myInterface.getGateSaleCount(stockType);
            listCall.enqueue(new Callback<ArrayList<GateSaleCount>>() {
                @Override
                public void onResponse(Call<ArrayList<GateSaleCount>> call, Response<ArrayList<GateSaleCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("GATE SALE COUNT : ", "------------" + response.body());

                            ArrayList<GateSaleCount> data = response.body();

                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_GATE_SALE_COUNT_ARRAY, json);

                            gateSaleCountAdapter = new GateSaleCountAdapter(data, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(gateSaleCountAdapter);

                            nestedScrollView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    nestedScrollView.smoothScrollTo(0, linearLayout.getScrollY());
                                }
                            }, 10);

                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GateSaleCount>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getConsumeCount(String date, ArrayList<Integer> typeList) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ConsumeCount>> listCall = Constants.myInterface.getConsumeCount(date, date, typeList);
            listCall.enqueue(new Callback<ArrayList<ConsumeCount>>() {
                @Override
                public void onResponse(Call<ArrayList<ConsumeCount>> call, Response<ArrayList<ConsumeCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CONSUME COUNT : ", "------------" + response.body());

                            ArrayList<ConsumeCount> data = response.body();

                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            CustomSharedPreference.putString(getActivity(), CustomSharedPreference.KEY_DASH_CONSUME_COUNT_ARRAY, json);

                            consumeCountAdapter = new ConsumeCountAdapter(data, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerViewConsumption.setLayoutManager(mLayoutManager);
                            recyclerViewConsumption.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewConsumption.setAdapter(consumeCountAdapter);

                            nestedScrollView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    nestedScrollView.smoothScrollTo(0, linearLayout.getScrollY());
                                }
                            }, 10);

                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ConsumeCount>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getStockTransferType(final String date) {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<StockTransferType>> listCall = Constants.myInterface.getStockTransferType();
            listCall.enqueue(new Callback<ArrayList<StockTransferType>>() {
                @Override
                public void onResponse(Call<ArrayList<StockTransferType>> call, Response<ArrayList<StockTransferType>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("STOCK TRANSFER ", "DATA ------------" + response.body());

                            ArrayList<StockTransferType> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                            } else {

                                ArrayList<Integer> typleidList = new ArrayList<>();
                                if (data.size() > 0) {
                                    for (int i = 0; i < data.size(); i++) {
                                        typleidList.add(data.get(i).getStockTransfTypeId());
                                    }
                                    getConsumeCount(date, typleidList);
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
                public void onFailure(Call<ArrayList<StockTransferType>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
