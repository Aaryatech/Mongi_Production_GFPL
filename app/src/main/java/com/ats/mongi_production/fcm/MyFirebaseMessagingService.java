package com.ats.mongi_production.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0 || !remoteMessage.getNotification().equals(null)) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                // JSONObject json1 = new JSONObject(remoteMessage.getNotification().toString());

                Log.e("JSON DATA", "-----------------------------" + json);
                // Log.e("JSON NOTIFICATION", "-----------------------------" + json1);

                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "-----------------------------Exception: " + e.getMessage());
                e.printStackTrace();
            }

            super.onMessageReceived(remoteMessage);

        } else {
            Log.e("FIREBASE", "----------------------------------");
        }
    }

//    @Override
//    public void handleIntent(Intent intent) {
//        super.handleIntent(intent);
//    }

    private void sendPushNotification(JSONObject json) {

        Log.e(TAG, "--------------------------------JSON String" + json.toString());
        try {

            String tempTitle = json.getString("title");

            String[] tempArray = tempTitle.split("#");

            String title = "";
            String frName = "";
            Log.e("LENGTH : -----------","--------------"+tempArray.length);
            if (tempArray.length > 1) {
                title = tempArray[0];
                frName = tempArray[1];
            } else {
                title = tempTitle;
            }

            // String title = json.getString("title");
            String message = json.getString("body");
            String imageUrl = "";
            String tag = json.getString("tag");

            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: -----------" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: ------------" + e.getMessage());
            e.printStackTrace();
        }

    }


}
