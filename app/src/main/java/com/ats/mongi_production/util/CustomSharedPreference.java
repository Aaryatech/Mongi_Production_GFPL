package com.ats.mongi_production.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class CustomSharedPreference {

    private static String PREFERENCE_NAME = "ProdApp";
    public static String KEY_USER = "User";
    public static String KEY_MENU = "MenuIds";
    public static String KEY_ALL_MENU = "AllMenu";
    public static String KEY_REG_MENU = "RegMenuIds";
    public static String KEY_REG_ALL_MENU = "RegAllMenu";
    public static String KEY_SP_FROM_DATE = "SpFromDate";
    public static String KEY_SP_TO_DATE = "SpToDate";
    public static String KEY_REG_FROM_DATE = "RegFromDate";
    public static String KEY_REG_TO_DATE = "RegToDate";
    public static String KEY_ALBUM_FROM_DATE = "AlbumFromDate";
    public static String KEY_ALBUM_TO_DATE = "AlbumToDate";

    public static String KEY_ALBUM_MENU = "AlbumMenuIds";
    public static String KEY_ALBUM_ALL_MENU = "AlbumAllMenu";

    public static String KEY_SP_SORT_SLOT = "SpSortSlot";
    public static String KEY_SP_SORT_ORDER_SEQ = "SpSortOrderSeq";
    public static String KEY_REG_SORT_ORDER_SEQ = "RegSortOrderSeq";
    public static String KEY_ALBUM_SORT_SLOT = "AlbumSortSlot";
    public static String KEY_ALBUM_SORT_ORDER_SEQ = "AlbumSortOrderSeq";

    public static String KEY_DASH_SP_MENU = "DashSPMenuIds";
    public static String KEY_DASH_SP_ALL_MENU = "DashSPAllMenu";

    public static String KEY_DASH_REG_MENU = "DashRegMenuIds";
    public static String KEY_DASH_REG_ALL_MENU = "DashRegAllMenu";

    public static String KEY_DASH_ALBUM_MENU = "DashAlbumMenuIds";
    public static String KEY_DASH_ALBUM_ALL_MENU = "DashAlbumAllMenu";


    public static String KEY_LOAD_MENU = "LoadMenu";

    public static String KEY_SP_MENU_ARRAY = "SPMenuArray";
    public static String KEY_REG_MENU_ARRAY = "RegMenuArray";
    public static String KEY_DASH_SP_MENU_ARRAY = "DashSPMenuArray";
    public static String KEY_DASH_REG_MENU_ARRAY = "DashRegMenuArray";
    public static String KEY_DASH_ALBUM_MENU_ARRAY = "DashAlbumMenuArray";
    public static String KEY_ALBUM_MENU_ARRAY = "AlbumMenuArray";

    public static String KEY_DASH_SP_COUNT_ARRAY = "DashSPCountArray";
    public static String KEY_DASH_REG_COUNT_ARRAY = "DashRegCountArray";
    public static String KEY_DASH_GATE_SALE_COUNT_ARRAY = "DashGateSaleCountArray";
    public static String KEY_DASH_CONSUME_COUNT_ARRAY = "DashConsumeCountArray";
    public static String KEY_DASH_ALBUM_COUNT_ARRAY = "DashAlbumCountArray";

    public static String KEY_DISPATCH_MENU = "DispatchMenuIds";
    public static String KEY_DISPATCH_ALL_MENU = "DispatchAllMenu";
    public static String KEY_DISPATCH_MENU_ARRAY = "DispatchMenuArray";

    public static String KEY_ROUTE_DETAIL_DATE = "RouteDetailDate";
    public static String KEY_ROUTE_DETAIL_MENU_LIST = "RouteDetailMenuList";

    public static String KEY_DASH_DATE = "DashDate";
    public static String KEY_DISPATCH_CHECKING_DATE = "DispatchCheckingDate";


    public static List<String> lstAppData = new ArrayList<>();

    public static void putString(Context activity, String key, String value) {
        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = shared.edit();
        edt.putString(key, value);
        edt.commit();
    }

    public static void putInt(Context activity, String key, int value) {
        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = shared.edit();
        edt.putInt(key, value);
        edt.commit();
    }


    public static String getString(Context activity, String key) {
        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String val = shared.getString(key, null);
        return shared.getString(key, null);
    }

    public static int getInt(Activity activity, String key) {
        SharedPreferences shared = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return shared.getInt(key, 0);
    }


    public static void deletePreference(Context context) {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = shared.edit().clear();
        edt.commit();
    }

    public static void deletePreferenceByKey(Context context, String key) {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(key);
        editor.apply();
    }

    //FirstTime
    public static void setForFirstTime(Context c, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FirstTime", userId);
        editor.commit();
    }

    public static boolean saveArray(Context context, List<String> sKey) {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        /* sKey is an array */
        editor.putInt("Status_size", sKey.size());

        for (int i = 0; i < sKey.size(); i++) {
            editor.remove("Status_" + i);
            lstAppData.add(sKey.get(i));
            editor.putString("Status_" + i, lstAppData.get(i));
        }

        return editor.commit();
    }

    public static void loadArray(Context context, List<String> sKey) {
        SharedPreferences shared = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        //sKey.clear();
        int size = shared.getInt("Status_size", 0);

        for (int i = 0; i < size; i++) {
            //sKey.add(shared.getString("Status_" + i, null));
            lstAppData.add(shared.getString("Status_" + i, null));
        }

    }

}