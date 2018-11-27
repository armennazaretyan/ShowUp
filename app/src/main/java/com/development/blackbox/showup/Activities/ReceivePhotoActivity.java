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
import android.view.WindowManager;
import android.widget.ImageView;

import com.development.blackbox.showup.DataAccessLayer.WebAPIServiceProvider;
import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.UserUIModel;
import com.development.blackbox.showup.PresentationLayerBase;
import com.development.blackbox.showup.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class ReceivePhotoActivity extends PresentationLayerBase implements ICallbackable {

    private UserUIModel _MeUserInfo = null;
    private UserUIModel _ActiveUserModel = null;
    //private String _PphotoURL = "";

    Timer _Timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_receive_photo);

        Intent intent = getIntent();
        _MeUserInfo = (UserUIModel) intent.getSerializableExtra("1");
        _ActiveUserModel = (UserUIModel) intent.getSerializableExtra("2");

        receivePhoto();
    }

    @Override
    public void CallbackToContext(Object result) {

        if (result.toString().equals("true")) {

            ImageView imgReceive = (ImageView) findViewById(R.id.imgReceive);
            imgReceive.setVisibility(View.VISIBLE);

            // TODO: Start Timer
            _Timer = new Timer();
            TimerTask updateProfile = new TimerTask() {
                @Override
                public void run() {
                    try {
                        UpdateGUI();
                    } catch (Exception ex) {
                        String sex = ex.getMessage();
                    }
                }
            };
            //_Timer.scheduleAtFixedRate(updateProfile, 0, 3000);
            _Timer.schedule(updateProfile, 3000);


        } else if (result.toString().lastIndexOf(".jpg") > 0) {

            String photoName = result.toString();
            photoName = photoName.substring(1);
            photoName = photoName.substring(0, photoName.length() - 1);

            String photoURL = WebAPIServiceProvider.imageUrl + photoName;

            ImageView imgReceive = (ImageView) findViewById(R.id.imgReceive);
            imgReceive.setVisibility(View.INVISIBLE);

            Picasso.with(this).load(photoURL)
                    //.resize(50, 50)
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    //.transform(new CropSquareTransformation())
                    .into(imgReceive, new Callback() {
                        @Override
                        public void onSuccess() {

                            String sss = "";
                            deletePhoto();
                        }

                        @Override
                        public void onError() {

                            String sss = "";
                        }
                    });

            return;
        }


        try {
            if (_ProgressDialog != null) {
                _ProgressDialog.dismiss();
            }
        } catch (Exception ex) {

        }
    }


    private void UpdateGUI() {
        try {

            Intent intent = new Intent();
            //intent.putExtra("adition", "1");
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception ex) {
            String sex = ex.getMessage();
        }
    }

    public void receivePhoto() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            _ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.LOAD_PHOTO.getCode();
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

    public void deletePhoto() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            //_ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.DELETE_PHOTO.getCode();
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
