package com.development.blackbox.showup.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.UserUIModel;
import com.development.blackbox.showup.PresentationLayerBase;
import com.development.blackbox.showup.R;

public class MyProfileActivity extends PresentationLayerBase implements ICallbackable {

    private UserUIModel _UserUIModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Intent intent = getIntent();
        _UserUIModel = (UserUIModel) intent.getSerializableExtra("1");

        EditText etName = (EditText) findViewById(R.id.etEditMyName);
        etName.setText(_UserUIModel.UserName);
        etName.requestFocus();

        EditText etEditMyAge = (EditText) findViewById(R.id.etEditMyAge);
        etEditMyAge.setText(String.valueOf(_UserUIModel.Age));
    }

    public void onSaveMyName(View view) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            _ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            EditText etName = (EditText) findViewById(R.id.etEditMyName);
            String name = etName.getText().toString();

            EditText etEditMyAge = (EditText) findViewById(R.id.etEditMyAge);
            //int age = Integer.parseInt(etEditMyAge.getText().toString());
            int age = _UserUIModel.Age;
            String sage = etEditMyAge.getText().toString();
            if(sage != "") {
                age = Integer.parseInt(etEditMyAge.getText().toString());
            }

            if (!name.trim().isEmpty()) {

                _UserUIModel.UserName = name;
                _UserUIModel.Age = age;

                Object objParamsTrans[] = new Object[2];
                objParamsTrans[0] = AsyncCallType.CHANGE_PROFILE.getCode();
                objParamsTrans[1] = _UserUIModel;
                new WebAPIAsyncTaskService(this).execute(objParamsTrans);

            } else {

                try {
                    if (_ProgressDialog != null) {
                        _ProgressDialog.dismiss();
                    }
                } catch (Exception ex) {

                }

            }

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

    public void onCancel(View view) {

        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void CallbackToContext(Object result) {

        try {
            if (_ProgressDialog != null) {
                _ProgressDialog.dismiss();
            }
        } catch (Exception ex) {

        }

        if(result instanceof Exception) {

            // TODO: Error message
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Connection is closed.")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        } else {

            SharedPreferences languageprefT = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
            SharedPreferences.Editor editorT = languageprefT.edit();
            editorT.putString(Config.USER_NAME_KEY, _UserUIModel.UserName);
            editorT.putInt(Config.USER_AGE_KEY, _UserUIModel.Age);
            editorT.commit();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }

    }

}
