package com.ats.mongi_production.interfaces;


import com.ats.mongi_production.model.AllMenu;
import com.ats.mongi_production.model.ConsumeCount;
import com.ats.mongi_production.model.DashRegCakeCount;
import com.ats.mongi_production.model.DashSPCakeCount;
import com.ats.mongi_production.model.FrWiseSpCakeOrd;
import com.ats.mongi_production.model.FranchiseList;
import com.ats.mongi_production.model.GateSaleCount;
import com.ats.mongi_production.model.GeneratedBillList;
import com.ats.mongi_production.model.Info;
import com.ats.mongi_production.model.Item;
import com.ats.mongi_production.model.MistryWiseItemWiseModel;
import com.ats.mongi_production.model.MistryWiseReportModel;
import com.ats.mongi_production.model.RateChangedModel;
import com.ats.mongi_production.model.RegCakeOrder;
import com.ats.mongi_production.model.RouteOrderData;
import com.ats.mongi_production.model.RouteWiseCount;
import com.ats.mongi_production.model.SPCakeOrder;
import com.ats.mongi_production.model.StockTransfReportModel;
import com.ats.mongi_production.model.StockTransferHeader;
import com.ats.mongi_production.model.StockTransferType;
import com.ats.mongi_production.model.User;
import com.ats.mongi_production.model.WorkerList;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InterfaceApi {

    @POST("loginProdAppUser")
    Call<User> doLogin(@Query("uMobNo") String uMobNo, @Query("uPass") String uPass);

    @POST("updateDeviceToken")
    Call<Info> updateToken(@Query("userId") int userId, @Query("uDeviceToken") String uDeviceToken);

    @GET("getAllProdAppUser")
    Call<ArrayList<User>> getAllUsers();

    @POST("addProdAppUser")
    Call<User> saveUser(@Body User user);

    @POST("deleteProdAppUser")
    Call<Info> deleteUser(@Query("userId") int userId);

    @POST("getSpCakeOrdersForApp")
    Call<ArrayList<SPCakeOrder>> getSPCakeOrder(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("menuIdList") ArrayList<Integer> menuIdList, @Query("isSlotUsed") ArrayList<Integer> isSlotUsed, @Query("isOrderBy") int isOrderBy);


    @POST("getSpCakeAlbumOrdersForApp")
    Call<ArrayList<SPCakeOrder>> getAlbumCakeOrder(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("menuIdList") ArrayList<Integer> menuIdList, @Query("isSlotUsed") ArrayList<Integer> isSlotUsed, @Query("isOrderBy") int isOrderBy);

    @POST("getMenusByCatId")
    Call<ArrayList<AllMenu>> getMenu(@Query("catId") int catId, @Query("isSameDayApp") int isSameDayApp);

    @POST("geRegSpCakeOrdersForApp")
    Call<ArrayList<RegCakeOrder>> getRegCakeOrder(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("menuIdList") ArrayList<Integer> menuIdList, @Query("isOrderBy") int isOrderBy);

    @POST("getAllMistryByType")
    Call<WorkerList> getWorkerList(@Query("empType") int empType);

    @POST("startSpCakeProd")
    Call<Info> startSPOrder(@Query("startTimeStamp") long startTimeStamp, @Query("tSpCakeSupNo") int tSpCakeSupNo, @Query("status") int status);

    @POST("endSpCakeProd")
    Call<Info> endSPOrder(@Query("endTimeStamp") long endTimeStamp, @Query("inputKgProd") float inputKgProd, @Query("photo1") String photo1, @Query("photo2") String photo2, @Query("tSpCakeSupNo") int tSpCakeSupNo, @Query("mistryId") int mistryId, @Query("mistryName") String mistryName, @Query("isCharUsed") String isCharUsed, @Query("status") int status, @Query("isRateChange") int isRateChange, @Query("timeRequired") String timeRequired);

    @Multipart
    @POST("photoUpload")
    Call<JSONObject> imageUpload(@Part MultipartBody.Part file, @Part("imageName") RequestBody name, @Part("type") RequestBody type);

    @POST("startRegSpCakeProd")
    Call<Info> startRegOrder(@Query("startTimeStamp") long startTimeStamp, @Query("supId") int supId, @Query("status") int status);

    @POST("endRegSpCakeProd")
    Call<Info> endRegOrder(@Query("enTime") long enTime, @Query("inputKgProd") float inputKgProd, @Query("photo1") String photo1, @Query("photo2") String photo2, @Query("supId") int supId, @Query("mistryId") int mistryId, @Query("mistryName") String mistryName, @Query("isCharUsed") String isCharUsed, @Query("status") int status, @Query("timeRequired") String timeRequired);

    @GET("getAllTrasnfTyPe")
    Call<ArrayList<StockTransferType>> getStockTransferType();

    @POST("getItemsByCatIdForProdApp")
    Call<ArrayList<Item>> getItemsByCategory(@Query("itemGrp1") String itemGrp1);

    @POST("addStockTransfHeaderDetail")
    Call<StockTransferHeader> insertStockTransfer(@Body StockTransferHeader stockTransferHeader);

    @POST("generateBill")
    Call<GeneratedBillList> getGenerateBills(@Query("frId") ArrayList<String> frId, @Query("menuId") ArrayList<String> menuId, @Query("delDate") String delDate);

    @GET("getAllFranchisee")
    Call<FranchiseList> getAllFranchise();

    @POST("getDashRegSpCakeCount")
    Call<ArrayList<DashRegCakeCount>> getDashRegCakeCount(@Query("prodDate") String prodDate, @Query("menuIdList") ArrayList<Integer> menuIdList);

    @POST("getDashSpCakeCount")
    Call<ArrayList<DashSPCakeCount>> getDashSPCakeCount(@Query("prodDate") String prodDate, @Query("menuIdList") ArrayList<Integer> menuIdList);

    @POST("getDashAlbumSpCakeCount")
    Call<ArrayList<DashSPCakeCount>> getDashAlbumCakeCount(@Query("prodDate") String prodDate, @Query("menuIdList") ArrayList<Integer> menuIdList);

    @POST("getDataForGateSaleDayEnd")
    Call<ArrayList<GateSaleCount>> getGateSaleCount(@Query("stockType") int stockType);

    @POST("getStockTransfDataByTypeGrpBySubcat")
    Call<ArrayList<ConsumeCount>> getConsumeCount(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("typeIdList") ArrayList<Integer> typeIdList);

    @POST("getRoutewiseOrdCount")
    Call<ArrayList<RouteWiseCount>> getRouteWiseCount(@Query("prodDate") String prodDate, @Query("menuIdList") ArrayList<Integer> menuIdList);

    @POST("getGetRoutewiseOrderData")
    Call<ArrayList<RouteOrderData>> getRouteDetail(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("menuIdList") ArrayList<Integer> menuIdList, @Query("isOrderBy") int isOrderBy, @Query("routeId") int routeId);

    @POST("getFrWiseSpCakeOrd")
    Call<ArrayList<FrWiseSpCakeOrd>> getFrWiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getMistrywiseReport")
    Call<ArrayList<MistryWiseReportModel>> getMistryWiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("mistryIdList") ArrayList<Integer> mistryIdList, @Query("table") String table, @Query("groupBy") String groupBy);

    @POST("getMisrtrywiseFrItemReport")
    Call<ArrayList<MistryWiseItemWiseModel>> getMistryWiseItemWiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("mistryIdList") ArrayList<Integer> mistryIdList, @Query("table") String table);

    @POST("getFrwiseRateChangedItemReport")
    Call<ArrayList<RateChangedModel>> getRateChangedReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("frIdList") ArrayList<Integer> frIdList);

    @POST("getStockTransfDataByTypeGrpBySubcat")
    Call<ArrayList<StockTransfReportModel>> getStockTransfReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("typeIdList") ArrayList<Integer> typeIdList);

}
