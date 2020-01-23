package com.ats.mongi_production.constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ats.mongi_production.interfaces.InterfaceApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {

    public static final String MY_PREF = "ProdAndDispatch";

    public static final String IMAGE_PATH_ALBUM_CAKE = "http://132.148.151.41:8080/uploads/ALBUM/";
    public static final String IMAGE_PATH_MSP_CAKE = "http://132.148.151.41:8080/uploads/MSPCAKE/";
    public static final String IMAGE_PATH_SP_CAKE = "http://132.148.151.41:8080/uploads/SPCAKE/";
    public static final String IMAGE_PATH_CUST_CHOICE_PHOTO_CAKE = "http://132.148.151.41:8080/uploads/CUSTCHOICEPHOTOCAKE/";
    public static final String BASE_URL = "http://132.148.151.41:8080/webapi/";
     //public static final String BASE_URL = "http://192.168.2.8:8099/";

    public static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);
                    return response;
                }
            })
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build();


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build();


    public static InterfaceApi myInterface = retrofit.create(InterfaceApi.class);

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(context, "No Internet Connection ! ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}
