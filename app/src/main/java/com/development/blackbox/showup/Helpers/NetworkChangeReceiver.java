package com.development.blackbox.showup.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.development.blackbox.showup.Helpers.Network.GcmKeepAlive;
import com.google.firebase.iid.FirebaseInstanceId;

import static android.content.Context.MODE_PRIVATE;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static boolean IsConnected = false;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        IsConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (IsConnected) {

            GcmKeepAlive gcmKeepAlive = new GcmKeepAlive(mContext);

            try {
                SharedPreferences userInfoShPref = mContext.getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
                long userID = userInfoShPref.getLong(Config.USER_ID_KEY, -1);
                if (userID != -1) {
                    gcmKeepAlive.isAliveRequest(context, userID, false);
                }
            } catch (Exception ex) {

            }

            try {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    gcmKeepAlive.broadcastIntents();
                }
            } catch (Exception ex) {

            }

            try {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                MyFirebaseInstanceIDService.storeRegIdInPref(context, refreshedToken);
                MyFirebaseInstanceIDService.sendRegistrationToServer(context, refreshedToken);
                /*if(MyFirebaseInstanceIDService.storeRegIdInPref(context, refreshedToken)) {
                    MyFirebaseInstanceIDService.sendRegistrationToServer(context, refreshedToken);
                }*/
            } catch (Exception ex) {

            }

        }
    }
}