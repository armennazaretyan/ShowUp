package com.development.blackbox.showup.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.UserUIModel;
import com.development.blackbox.showup.PresentationLayerBase;
import com.development.blackbox.showup.R;

public class RateAppActivity extends PresentationLayerBase implements ICallbackable {

    private UserUIModel _MeUserInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_app);

        setTitle("Rate application");

        _MeUserInfo = new UserUIModel();
        _MeUserInfo.ID = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE).getLong(Config.USER_ID_KEY, -1);
        _MeUserInfo.UserName = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE).getString(Config.USER_NAME_KEY, "");
    }

    @Override
    public void CallbackToContext(Object result) {

    }


    public void onRate(View view) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            _ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            int stars = -1;

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.TRY_REQUEST.getCode();
            objParams[1] = _MeUserInfo.ID;
            objParams[2] = stars;
            new WebAPIAsyncTaskService(this).execute(objParams);

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Check internet connection.")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;
        }

    }
}
