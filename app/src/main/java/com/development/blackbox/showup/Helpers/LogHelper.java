package com.development.blackbox.showup.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LogHelper {

    private Context mContext;

    public LogHelper(Context context) {
        mContext = context;
    }

    public void logError(Exception ex) {

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        String stackTrace = "";
        StackTraceElement[] ste = ex.getStackTrace();
        for (StackTraceElement item:
                ste) {
            stackTrace += "cn:" + item.getClassName() + "mn:" + item.getMethodName() + "ln" + item.getLineNumber() + "<br>";
        }

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            Object objParams[] = new Object[4];
            objParams[0] = AsyncCallType.LOG_HELPER.getCode();
            objParams[1] = ex.getMessage();
            objParams[2] = stackTrace;
            objParams[3] = android_id;
            new WebAPIAsyncTaskService(null).execute(objParams);

        } else {

            return;
        }
    }
}
