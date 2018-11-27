package com.development.blackbox.showup.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.UserUIModel;
import com.development.blackbox.showup.PresentationLayerBase;
import com.development.blackbox.showup.R;
import com.squareup.picasso.Picasso;

public class TryActivity extends PresentationLayerBase implements ICallbackable {

    private UserUIModel _MeUserInfo = null;
    private UserUIModel _ActiveUserModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);


        Intent intent = getIntent();
        _ActiveUserModel = (UserUIModel) intent.getSerializableExtra("1");

        ImageView userImage = (ImageView) findViewById(R.id.userImage);
        TextView nick = (TextView) findViewById(R.id.userName);
        TextView age = (TextView) findViewById(R.id.userAge);
        TextView userGender = (TextView) findViewById(R.id.userGender);

        if(_ActiveUserModel.ImageURL.isEmpty()) {

            if(_ActiveUserModel.GenderType == GenderEnumType.MALE) {
                userImage.setImageResource(R.drawable.ic_empty_male);
            } else {
                userImage.setImageResource(R.drawable.ic_empty_female);
            }
        } else {
            Picasso.with(this).load(_ActiveUserModel.ImageURL).into(userImage);
        }


        nick.setText(_ActiveUserModel.UserName);
        age.setText(String.valueOf(_ActiveUserModel.Age));
        userGender.setText(GenderEnumType.toString(_ActiveUserModel.GenderType));

        _MeUserInfo = new UserUIModel();
        _MeUserInfo.ID = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE).getLong(Config.USER_ID_KEY, -1);
        _MeUserInfo.UserName = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE).getString(Config.USER_NAME_KEY, "");
    }

    @Override
    public void CallbackToContext(Object result) {

        if (_ProgressDialog != null) {
            _ProgressDialog.dismiss();
        }

        if (result instanceof Exception) {

        } else if(result.toString().equals("true")) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Request sent successfully")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent();
                            //intent.putExtra("adition", "1");
                            setResult(RESULT_OK, intent);
                            finish();

                        }
                    }).show();
        }
    }

    public void onCancel(View view) {

        Intent intent = new Intent();
        //intent.putExtra("adition", "1");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onShowUp(View view) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            _ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.TRY_REQUEST.getCode();
            objParams[1] = _MeUserInfo.ID;
            objParams[2] = _ActiveUserModel.ID;
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
