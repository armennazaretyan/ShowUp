package com.development.blackbox.showup.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        /*if(storeRegIdInPref(getApplicationContext(), refreshedToken)) {
            sendRegistrationToServer(getApplicationContext(), refreshedToken);
        }*/
        storeRegIdInPref(getApplicationContext(), refreshedToken);
        sendRegistrationToServer(getApplicationContext(), refreshedToken);
    }

    // TODO: Saving reg id to shared preferences
    // TODO: ????????????????????
    // TODO: Just save ? No using ?? Need to check !
    public static boolean storeRegIdInPref(Context appContext, String token) {

        SharedPreferences userInfoShPref = appContext.getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
        long userID = userInfoShPref.getLong(Config.USER_ID_KEY, -1);
        if (userID != -1) {

            SharedPreferences pref = appContext.getSharedPreferences(Config.REG_TOKEN_SETTINGS, MODE_PRIVATE);
            String existsToken = pref.getString(Config.REG_TOKEN_KEY, "");

            //if ("".equals(existsToken) || !existsToken.equals(String.valueOf(userID) + "__" + token)) {
            if ("".equals(existsToken) || !existsToken.equals(token)) {

                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Config.REG_TOKEN_KEY, token);
                editor.commit();

                return true;
            }
        }

        return false;
    }

    // TODO: Send to server
    public static void sendRegistrationToServer(Context appContext, String refreshedToken) {

        ConnectivityManager connMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            SharedPreferences userInfoShPref = appContext.getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
            long userID = userInfoShPref.getLong(Config.USER_ID_KEY, -1);

            if(userID != -1) {
                String android_id = Settings.Secure.getString(appContext.getContentResolver(), Settings.Secure.ANDROID_ID);

                Object objParams[] = new Object[4];
                objParams[0] = AsyncCallType.SEND_TOKEN.getCode();
                objParams[1] = userID;
                objParams[2] = android_id;
                objParams[3] = refreshedToken;
                new WebAPIAsyncTaskService(null).execute(objParams);
            }
        }
    }

}
