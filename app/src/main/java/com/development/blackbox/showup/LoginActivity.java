package com.development.blackbox.showup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.UserUIModel;

import java.io.Serializable;

public class LoginActivity extends PresentationLayerBase implements ICallbackable {

    EditText etLoginname, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences userInfoShPref = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
        long id = userInfoShPref.getLong(Config.USER_ID_KEY, -1);
        if (id > 0) {
            UserUIModel ui = new UserUIModel();
            ui.ID = id;
            ui.UserName = userInfoShPref.getString(Config.USER_NAME_KEY, "");
            ui.LoginName = userInfoShPref.getString(Config.USER_LOGIN_NAME_KEY, "");
            ui.Age = userInfoShPref.getInt(Config.USER_AGE_KEY, Config.USER_AGE_DEFAULT);
            ui.GenderType = GenderEnumType.ParseInt(userInfoShPref.getInt(Config.USER_GENDER_KEY, 0));

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("1", (Serializable) ui);
            startActivity(intent);
            finish();
        }

        etLoginname = (EditText) findViewById(R.id.etLoginname);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    @Override
    public void CallbackToContext(Object result) {

        if (_ProgressDialog != null) {
            _ProgressDialog.dismiss();
        }

        if (result instanceof Exception) {

        } else if (result instanceof UserUIModel) {

            UserUIModel ui = (UserUIModel)result;
            if(ui.ID == -1) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("Wrong login name or password")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

                return;

            }


            SharedPreferences languageprefT = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
            SharedPreferences.Editor editorT = languageprefT.edit();
            editorT.putLong(Config.USER_ID_KEY, ui.ID);
            editorT.putString(Config.USER_NAME_KEY, ui.UserName);
            editorT.putString(Config.USER_LOGIN_NAME_KEY, ui.LoginName);
            editorT.putInt(Config.USER_AGE_KEY, ui.Age);
            editorT.putInt(Config.USER_GENDER_KEY, ui.GenderType.getCode());
            editorT.commit();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("1", (Serializable) ui);
            startActivity(intent);
            finish();
        }

    }

    public void onLogin(View view) {

        String loginName = etLoginname.getText().toString();
        String pwd = etPassword.getText().toString();
        if(loginName.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Login name is empty")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;

        }

        if(pwd.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Password is empty")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;

        }

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            _ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.LOG_IN.getCode();
            objParams[1] = loginName;
            objParams[2] = pwd;
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

    public void onRegister(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        //intent.putExtra("1", (Serializable) categoryModel);
        startActivity(intent);
        finish();
    }

}
