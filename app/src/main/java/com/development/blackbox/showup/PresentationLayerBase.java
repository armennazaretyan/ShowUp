package com.development.blackbox.showup;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.development.blackbox.showup.Helpers.LogHelper;

public class PresentationLayerBase extends AppCompatActivity {

    protected ProgressDialog _ProgressDialog;
    protected LogHelper _LogHelper;
    protected Handler mHandler;

    /*protected final String LOGIN_SETTINGS = "loginInfo";
    protected final String USER_ID_KEY = "LoggedInUserID";
    protected final String USER_NAME_KEY = "UserName";
    protected final String USER_LOGIN_NAME_KEY = "LoginName";
    protected final String USER_AGE_KEY = "Age";
    protected final String USER_GENDER_KEY = "Gender";*/





    public PresentationLayerBase() {
        if (mHandler == null) {
            mHandler = new Handler();
        }

        if (_LogHelper == null) {
            _LogHelper = new LogHelper(this);
        }
    }


    protected void setCurrentFragment(@IdRes int layoutResID, Fragment fragment) {

        final Fragment selectedFragment = fragment;
        final @IdRes int currentLayoutResID = layoutResID;

        if (selectedFragment != null) {
            // Sometimes, when fragment has huge data, screen seems hanging
            // when switching between navigation menus
            // So using runnable, the fragment is loaded with cross fade effect
            // This effect can be seen in GMail app
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    // update the main content by replacing fragments
                    Fragment fragment = selectedFragment; // getSelectedFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(currentLayoutResID, fragment, "CURRENT_TAG");
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };

            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler.postDelayed(mPendingRunnable, 300);
            }
        }
    }
}
