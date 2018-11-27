package com.development.blackbox.showup.Helpers.Network;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;

public class GcmKeepAlive {

    protected CountDownTimer timer;
    protected Context mContext;
    protected Intent gTalkHeartBeatIntent;
    protected Intent mcsHeartBeatIntent;

    public GcmKeepAlive(Context context) {
        mContext = context;
        gTalkHeartBeatIntent = new Intent("com.google.android.intent.action.GTALK_HEARTBEAT");
        mcsHeartBeatIntent = new Intent("com.google.android.intent.action.MCS_HEARTBEAT");
    }

    public void broadcastIntents() {
        //System.out.println("sending heart beat to keep gcm alive");
        mContext.sendBroadcast(gTalkHeartBeatIntent);
        mContext.sendBroadcast(mcsHeartBeatIntent);
    }

    public void isAliveRequest(Context context, long userID, boolean isShowMessage) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.IS_ALIVE.getCode();
            objParams[1] = userID;
            new WebAPIAsyncTaskService(null).execute(objParams);

        } else {

            if(isShowMessage) {
                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setMessage("Check internet connection.")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }

            return;
        }

    }


}
