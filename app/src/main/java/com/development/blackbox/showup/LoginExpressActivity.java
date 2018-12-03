package com.development.blackbox.showup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.development.blackbox.showup.Fragments.ActiveUsersFragment;
import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.ActiveUsersResponse;
import com.development.blackbox.showup.Models.UserUIModel;

import java.io.Serializable;

import customfonts.MyRegulerText;
import customfonts.MyTextView;

public class LoginExpressActivity extends PresentationLayerBase implements ICallbackable {

    TextView tvNickName;
    LinearLayout llStart;
    MyRegulerText btnLoginExpress;

    UserUIModel _UserModel = null;
    private ActiveUsersResponse _ActiveUsersResponse = null;

    protected Fragment activeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen


        setContentView(R.layout.activity_login_express);

        SharedPreferences userInfoShPref = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
        long id = userInfoShPref.getLong(Config.USER_ID_KEY, -1);
        if (id > 0) {
            UserUIModel ui = new UserUIModel();
            ui.ID = id;
            ui.UserName = userInfoShPref.getString(Config.USER_NAME_KEY, "");
            ui.LoginName = userInfoShPref.getString(Config.USER_LOGIN_NAME_KEY, "");
            ui.Age = userInfoShPref.getInt(Config.USER_AGE_KEY, 0);
            ui.GenderType = GenderEnumType.ParseInt(userInfoShPref.getInt(Config.USER_GENDER_KEY, 0));

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("1", (Serializable) ui);
            startActivity(intent);
            finish();
        }

        llStart = (LinearLayout) findViewById(R.id.llStart);
        tvNickName = (TextView) findViewById(R.id.tvNickName);
        btnLoginExpress = (MyRegulerText) findViewById(R.id.btnLoginExpress);

        _UserModel = new UserUIModel();

        if(IsNeverLoggedIn()) {
            final RadioButton rbMale = (RadioButton)findViewById(R.id.rbMale);
            //final MyTextView sinMale = (MyTextView)findViewById(R.id.sin);
            Animation mAnimation = new AlphaAnimation(1, 0);
            mAnimation.setDuration(250);
            mAnimation.setInterpolator(new LinearInterpolator());
            mAnimation.setRepeatCount(7);
            mAnimation.setRepeatMode(Animation.REVERSE);
            mAnimation.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationStart(Animation arg0) {
                    //sinMale.setTextColor(getResources().getColor(R.color.colorAccent));
                    //rbMale.setTooltipText("Click here !");
                }

                public void onAnimationRepeat(Animation arg0) {
                    if(rbMale.isChecked())
                        rbMale.setChecked(false);
                    else
                        rbMale.setChecked(true);
                }

                public void onAnimationEnd(Animation anim) {
                    //sinMale.setTextColor(Color.parseColor("#85ffffff"));
                    rbMale.setChecked(false);

                    // TODO: Start next RadioButton blinking
                    final RadioButton rbFeMale = (RadioButton)findViewById(R.id.rbFeMale);
                    //final MyTextView sinnFeMale = (MyTextView)findViewById(R.id.sinn);
                    Animation mAnimation2 = new AlphaAnimation(1, 0);
                    mAnimation2.setDuration(250);
                    mAnimation2.setInterpolator(new LinearInterpolator());
                    mAnimation2.setRepeatCount(7);
                    mAnimation2.setRepeatMode(Animation.REVERSE);
                    mAnimation2.setAnimationListener(new Animation.AnimationListener() {

                        public void onAnimationStart(Animation arg0) {
                            //sinnFeMale.setTextColor(getResources().getColor(R.color.colorAccent));
                        }

                        public void onAnimationRepeat(Animation arg0) {
                            if(rbFeMale.isChecked())
                                rbFeMale.setChecked(false);
                            else
                                rbFeMale.setChecked(true);
                        }

                        public void onAnimationEnd(Animation anim) {
                            //sinnFeMale.setTextColor(Color.parseColor("#85ffffff"));
                            rbFeMale.setChecked(false);
                        }

                    });

                    //sinnFeMale.startAnimation(mAnimation2);
                    rbFeMale.startAnimation(mAnimation2);
                }

            });
            //sinMale.startAnimation(mAnimation);
            rbMale.startAnimation(mAnimation);
        }
    }

    /*private void setSortingSettings(OrderEnumType order) {

        SharedPreferences languageprefT = getSharedPreferences(Config.SORTING_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editorT = languageprefT.edit();
        editorT.putLong(Config.SORT_BY_KEY, order.getCode());
        editorT.commit();

        ((ActiveUsersFragment)activeFragment).FillActiveUsers(_ActiveUsersResponse);
    }*/
    @Override
    public void CallbackToContext(Object result) {

        if (_ProgressDialog != null) {
            _ProgressDialog.dismiss();
        }

        if (result instanceof Exception) {

        } else if(result instanceof String) {

            tvNickName.setText(String.valueOf(result));
            toggleLoginExpress_EnabledDisabled(true);

            if(llStart.getVisibility() == View.INVISIBLE) {
                llStart.setVisibility(View.VISIBLE);

                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                if(android_id.equals("d3c7aae9ae306515")) {
                    ((ImageButton) findViewById(R.id.imgbtnChangeNickName)).setVisibility(View.VISIBLE);
                }

                //showUsersList();
            }

        } else if( result instanceof ActiveUsersResponse) {

            ((ActiveUsersFragment)activeFragment).CallbackFromActivity(((ActiveUsersResponse)result));

            /*_ActiveUsersResponse = ((ActiveUsersResponse)result);

            SharedPreferences userInfoShPref = getSharedPreferences(Config.SORTING_SETTINGS, MODE_PRIVATE);
            long orderBy = userInfoShPref.getLong(Config.SORT_BY_KEY, -1);
            if(orderBy == -1) {

                RadioButton rbName = (RadioButton) findViewById(R.id.rbName);
                rbName.setChecked(true);
                ((ActiveUsersFragment)activeFragment).setSortingSettings(OrderEnumType.NAME, _ActiveUsersResponse);

            } else {

                OrderEnumType orderEnumType = OrderEnumType.ParseInt((int)orderBy);
                switch (orderEnumType) {
                    case NAME:
                        ((ActiveUsersFragment)activeFragment).clickedToName();
                        break;
                    case SEE_PHOTO:
                        ((ActiveUsersFragment)activeFragment).clickedToDownload();
                        break;
                    case MAKE_PHOTO:
                        ((ActiveUsersFragment)activeFragment).clickedToMakePhoto();
                        break;
                }

                ((ActiveUsersFragment)activeFragment).setSortingSettings(orderEnumType, _ActiveUsersResponse);
            }*/
        } else if(result instanceof UserUIModel) {

            UserUIModel ui = (UserUIModel)result;

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
        } else if(result instanceof Bitmap) {

            ProgressBar pgsBar = (ProgressBar) findViewById(R.id.pBar);
            pgsBar.setVisibility(View.GONE);

            Bitmap bitmap = (Bitmap)result;

            try {
                ImageView imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
                imgAvatar.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

/*
    private void clickedToName() {

        RadioButton rbName = (RadioButton) findViewById(R.id.rbName);
        RadioButton rbDownload = (RadioButton) findViewById(R.id.rbDownload);
        ImageView imgDownload = (ImageView) findViewById(R.id.imgDownload);
        RadioButton rbMakePhoto = (RadioButton) findViewById(R.id.rbMakePhoto);
        ImageView imgMakePhoto = (ImageView) findViewById(R.id.imgMakePhoto);

        rbName.setChecked(true);
        rbDownload.setChecked(false);
        imgDownload.setImageResource(R.drawable.download_disabled);
        rbMakePhoto.setChecked(false);
        imgMakePhoto.setImageResource(R.drawable.make_photo_disabled);
    }

    private void clickedToDownload() {

        RadioButton rbName = (RadioButton) findViewById(R.id.rbName);
        RadioButton rbDownload = (RadioButton) findViewById(R.id.rbDownload);
        ImageView imgDownload = (ImageView) findViewById(R.id.imgDownload);
        RadioButton rbMakePhoto = (RadioButton) findViewById(R.id.rbMakePhoto);
        ImageView imgMakePhoto = (ImageView) findViewById(R.id.imgMakePhoto);

        rbName.setChecked(false);
        rbDownload.setChecked(true);
        imgDownload.setImageResource(R.drawable.download_enabled);
        rbMakePhoto.setChecked(false);
        imgMakePhoto.setImageResource(R.drawable.make_photo_disabled);
    }

    private void clickedToMakePhoto() {

        RadioButton rbName = (RadioButton) findViewById(R.id.rbName);
        RadioButton rbDownload = (RadioButton) findViewById(R.id.rbDownload);
        RadioButton rbMakePhoto = (RadioButton) findViewById(R.id.rbMakePhoto);
        ImageView imgDownload = (ImageView) findViewById(R.id.imgDownload);
        ImageView imgMakePhoto = (ImageView) findViewById(R.id.imgMakePhoto);

        rbName.setChecked(false);
        rbDownload.setChecked(false);
        rbMakePhoto.setChecked(true);
        imgDownload.setImageResource(R.drawable.download_disabled);

        imgMakePhoto.setImageResource(R.drawable.make_photo_enabled);
    }
*/
    public void receivePhoto() {

        if(_MaleChecked == null) {
            return;
        }

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            ProgressBar pgsBar = (ProgressBar) findViewById(R.id.pBar);
            pgsBar.setVisibility(View.VISIBLE);

            Object objParams[] = new Object[2];
            objParams[0] = AsyncCallType.SHUFFLE_PHOTO.getCode();
            objParams[1] = _MaleChecked;
            new WebAPIAsyncTaskService(this).execute(objParams);

        } else {

            return;
        }
    }

    public void onChangeAvatarPhoto(View view) {
        //receivePhoto();
    }

    Boolean _MaleChecked = null;
    public void onMaleClicked(View view) {

        _MaleChecked = true;

        RadioButton rbFeMale = (RadioButton) findViewById(R.id.rbFeMale);
        rbFeMale.setChecked(false);

        toggleLoginExpress_EnabledDisabled(false);

        ImageView imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        //imgAvatar.setImageResource(R.drawable.meninblack);
        imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.meninblack));

        getNickName(GenderEnumType.MALE.getCode());
    }

    public void onFemaleClicked(View view) {

        _MaleChecked = false;

        RadioButton rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbMale.setChecked(false);

        toggleLoginExpress_EnabledDisabled(false);

        ImageView imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        //imgAvatar.setImageResource(R.drawable.woman_face);
        imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.woman_face));

        getNickName(GenderEnumType.FEMALE.getCode());
    }

    public void onChangeNickName(View view) {

        TextView tvNickName = (TextView) findViewById(R.id.tvNickName);
        EditText etNickName = (EditText) findViewById(R.id.etNickName);
        ImageButton imgbtnChangeNickName = (ImageButton) findViewById(R.id.imgbtnChangeNickName);
        Button btnSaveChangedNickName = (Button) findViewById(R.id.btnSaveChangedNickName);

        etNickName.setText(tvNickName.getText());
        tvNickName.setVisibility(View.GONE);
        etNickName.setVisibility(View.VISIBLE);
        imgbtnChangeNickName.setVisibility(View.GONE);
        btnSaveChangedNickName.setVisibility(View.VISIBLE);
        toggleLoginExpress_EnabledDisabled(false);
    }

    public void onSaveChangedNickName(View view) {

        TextView tvNickName = (TextView) findViewById(R.id.tvNickName);
        EditText etNickName = (EditText) findViewById(R.id.etNickName);
        ImageButton imgbtnChangeNickName = (ImageButton) findViewById(R.id.imgbtnChangeNickName);
        Button btnSaveChangedNickName = (Button) findViewById(R.id.btnSaveChangedNickName);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        //                          etNickName.getWindowToken()

        tvNickName.setText(etNickName.getText());
        etNickName.setVisibility(View.GONE);
        tvNickName.setVisibility(View.VISIBLE);
        imgbtnChangeNickName.setVisibility(View.VISIBLE);
        btnSaveChangedNickName.setVisibility(View.GONE);
        toggleLoginExpress_EnabledDisabled(true);
    }

    public void onLoginExpress(View view) {

        String userName = tvNickName.getText().toString();

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            _ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            _UserModel.UserName = userName;
            _UserModel.LoginName = userName;
            _UserModel.Password = userName;
            _UserModel.Age = Integer.parseInt("25");
            _UserModel.GenderType = (_MaleChecked) ? GenderEnumType.MALE : GenderEnumType.FEMALE;
            _UserModel.ImageURL = "";

            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.LOG_IN_EXPRESS.getCode();
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

    private void getNickName(int gender) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            if(llStart.getVisibility() == View.INVISIBLE) {
                //_ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");
            }

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.GET_NICK_NAME.getCode();
            objParams[1] = gender;
            objParams[2] = tvNickName.getText().toString();
            new WebAPIAsyncTaskService(this).execute(objParams);
        }
    }

    private void showUsersList() {
        //activeFragment = new ActiveUsersFragment().newInstance(new UserUIModel(), "");
        //((ActiveUsersFragment) activeFragment).setDisabledScreen();
        //setCurrentFragment(R.id.llUsersListFragment, activeFragment);
    }

    private void toggleLoginExpress_EnabledDisabled(boolean isEnabled) {
        if(isEnabled) {
            btnLoginExpress.setEnabled(true);
            btnLoginExpress.setTextColor(getResources().getColor(R.color.colorAccent));
            btnLoginExpress.setTextColor(Color.rgb(255, 255, 255));
        } else {
            btnLoginExpress.setEnabled(false);
            btnLoginExpress.setTextColor(Color.rgb(169,169,169));
        }
    }

    private boolean IsNeverLoggedIn() {

        SharedPreferences pref = getSharedPreferences(Config.REG_TOKEN_SETTINGS, MODE_PRIVATE);
        String existsToken = pref.getString(Config.REG_TOKEN_KEY, "");

        if (existsToken.equals("")) {
            return true;
        }
        return false;
    }
}
