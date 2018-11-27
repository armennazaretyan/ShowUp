package com.development.blackbox.showup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.UserUIModel;

import java.io.Serializable;

public class RegisterActivity extends PresentationLayerBase implements ICallbackable {

    EditText etUserName, etLoginName, etPassword, etAge;
    UserUIModel _UserModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etLoginName = (EditText) findViewById(R.id.etLoginName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAge = (EditText) findViewById(R.id.etAge);

        _UserModel = new UserUIModel();
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
                        .setMessage("Login is already registered")
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
            //intent.putExtra("2", 1);
            startActivity(intent);
            finish();
        }
    }

    Boolean _MaleChecked = null;
    public void onMaleClicked(View view) {

        _MaleChecked = true;

        RadioButton rbFeMale = (RadioButton) findViewById(R.id.rbFeMale);
        rbFeMale.setChecked(false);
    }

    public void onFemaleClicked(View view) {

        _MaleChecked = false;

        RadioButton rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbMale.setChecked(false);
    }

    public void onRegister(View view) {

        String userName = etUserName.getText().toString();
        String loginName = etLoginName.getText().toString();
        String pwd = etPassword.getText().toString();
        String age = etAge.getText().toString();
        if(userName.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("User name is empty")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;
        }

        if(loginName.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Login is empty")
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

        if(age.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Age is empty")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;
        }

        if(_MaleChecked == null) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Gender is not checked")
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

            _UserModel.UserName = userName;
            _UserModel.LoginName = loginName;
            _UserModel.Password = pwd;
            _UserModel.Age = Integer.parseInt(age);
            _UserModel.GenderType = (_MaleChecked) ? GenderEnumType.MALE : GenderEnumType.FEMALE;
            _UserModel.ImageURL = "";

            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.REGISTER.getCode();
            objParams[1] = _UserModel;
            objParams[2] = android_id;
            new WebAPIAsyncTaskService(this).execute(objParams);

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Check internet")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;
        }
    }

}
