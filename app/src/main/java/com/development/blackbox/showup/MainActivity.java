package com.development.blackbox.showup;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.development.blackbox.showup.Activities.MyProfileActivity;
import com.development.blackbox.showup.Activities.ReceivePhotoActivity;
import com.development.blackbox.showup.Activities.SendPhotoActivity;
import com.development.blackbox.showup.Activities.TryActivity;
import com.development.blackbox.showup.Fragments.ActiveUsersFragment;
import com.development.blackbox.showup.Fragments.CategoriesFragment;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Helpers.Enums.OrderEnumType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Helpers.MyFirebaseInstanceIDService;
import com.development.blackbox.showup.Helpers.Network.GcmKeepAlive;
import com.development.blackbox.showup.Models.ActiveUsersResponse;
import com.development.blackbox.showup.Models.CategoryModel;
import com.development.blackbox.showup.Models.UserUIModel;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.Serializable;

public class MainActivity extends PresentationLayerBase
        implements NavigationView.OnNavigationItemSelectedListener, ICallbackable {

    protected Fragment activeFragment;
    UserUIModel _Me = null;
    TextView _txtProfileName;
    private ActiveUsersResponse _ActiveUsersResponse = null;
    GcmKeepAlive gcmKeepAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MyFirebaseInstanceIDService.storeRegIdInPref(this, refreshedToken);
        MyFirebaseInstanceIDService.sendRegistrationToServer(this, refreshedToken);
        /*if(MyFirebaseInstanceIDService.storeRegIdInPref(this, refreshedToken)) {
            MyFirebaseInstanceIDService.sendRegistrationToServer(this, refreshedToken);
        }*/

        gcmKeepAlive = new GcmKeepAlive(this);

        /*GoogleApiAvailability.makeGooglePlayServicesAvailable();
        if(isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
        }*/


        //android.support.v7.app.ActionBar bar = getSupportActionBar();
        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0d0da5")));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        _Me = (UserUIModel) intent.getSerializableExtra("1");

        if (_Me == null) {

            SharedPreferences userInfoShPref = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
            long id = userInfoShPref.getLong(Config.USER_ID_KEY, -1);
            if(id == -1) {

                Intent intentLogin = new Intent(this, LoginExpressActivity.class);
                startActivity(intentLogin);
                finish();

                return;

            } else {

                _Me = new UserUIModel();
                _Me.ID = id;
                _Me.UserName = userInfoShPref.getString(Config.USER_NAME_KEY, "");
                _Me.LoginName = userInfoShPref.getString(Config.USER_LOGIN_NAME_KEY, "");
                _Me.Age = userInfoShPref.getInt(Config.USER_AGE_KEY, Config.USER_AGE_DEFAULT);
                _Me.GenderType = GenderEnumType.ParseInt(userInfoShPref.getInt(Config.USER_GENDER_KEY, 0));

            }

        }

        setTitle("Hi, " + _Me.UserName + "!");


        navigationView.getMenu().getItem(0).setChecked(true);

        activeFragment = new ActiveUsersFragment().newInstance(_Me, "");
        setCurrentFragment(R.id.content_main, activeFragment);

        View navHeader = navigationView.getHeaderView(0);
        _txtProfileName = (TextView) navHeader.findViewById(R.id.tvName);
        _txtProfileName.setText(_Me.UserName);


        try {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.cancel(getIntent().getIntExtra(Config.NOTIFICATION_ID, Config.NotificationId));
        } catch (Exception ex) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            gcmKeepAlive.isAliveRequest(this, _Me.ID, true);
        } catch (Exception ex) {

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_log_out) {

            SharedPreferences settings = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
            settings.edit().clear().commit();
            //settings.edit().remove("KeyName").commit();


            Intent intent = new Intent(this, LoginExpressActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            activeFragment = new ActiveUsersFragment().newInstance(_Me, "");
            setCurrentFragment(R.id.content_main, activeFragment);

        } else if (id == R.id.nav_gallery) {

            activeFragment = new CategoriesFragment().newInstance("", "");
            setCurrentFragment(R.id.content_main, activeFragment);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // TODO: Profile
    public void imgMyProfileClick(View view) {

        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.putExtra("1", (Serializable) _Me);
        startActivityForResult(intent, CHANGE_PROFILE_REQUEST_CODE);
    }
    public void tvMyProfileClick(View view) {

        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.putExtra("1", (Serializable) _Me);
        startActivityForResult(intent, CHANGE_PROFILE_REQUEST_CODE);
    }
    // TODO: Profile

    // TODO: Sorting
    public void onSortingName(View view) {

        //((ActiveUsersFragment)activeFragment).clickedToName();
        //setSortingSettings(OrderEnumType.NAME);
        ((ActiveUsersFragment)activeFragment).sortByName();
    }
    public void onSortingDownload(View view) {

        //((ActiveUsersFragment)activeFragment).clickedToDownload();
        //setSortingSettings(OrderEnumType.SEE_PHOTO);
        ((ActiveUsersFragment)activeFragment).sortByDownload();
    }
    public void onSortingMakePhoto(View view) {

        //((ActiveUsersFragment)activeFragment).clickedToMakePhoto();
        //setSortingSettings(OrderEnumType.MAKE_PHOTO);
        ((ActiveUsersFragment)activeFragment).sortByMakePhoto();
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


    /*private void setSortingSettings(OrderEnumType order) {

        SharedPreferences languageprefT = getSharedPreferences(Config.SORTING_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editorT = languageprefT.edit();
        editorT.putLong(Config.SORT_BY_KEY, order.getCode());
        editorT.commit();

        ((ActiveUsersFragment)activeFragment).FillActiveUsers(_ActiveUsersResponse);
    }*/
    // TODO: Sorting

    @Override
    public void CallbackToContext(Object result) {

        try {
            if(_ProgressDialog != null) {
                _ProgressDialog.dismiss();
            }
        } catch (Exception ex) {

        }

        if (result instanceof Exception) {

        } else if( result instanceof ActiveUsersResponse) {

            ((ActiveUsersFragment)activeFragment).CallbackFromActivity(((ActiveUsersResponse)result));

            /*_ActiveUsersResponse = ((ActiveUsersResponse)result);

            SharedPreferences userInfoShPref = getSharedPreferences(Config.SORTING_SETTINGS, MODE_PRIVATE);
            long orderBy = userInfoShPref.getLong(Config.SORT_BY_KEY, -1);
            if(orderBy == -1) {

                RadioButton rbName = (RadioButton) findViewById(R.id.rbName);
                rbName.setChecked(true);
                setSortingSettings(OrderEnumType.NAME);

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

                setSortingSettings(orderEnumType);
            }*/
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            switch (requestCode) {

                case CHANGE_PROFILE_REQUEST_CODE:

                    SharedPreferences userInfoShPref = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
                    _Me.UserName = userInfoShPref.getString(Config.USER_NAME_KEY, "");
                    _Me.Age = userInfoShPref.getInt(Config.USER_AGE_KEY, Config.USER_AGE_DEFAULT);
                    _txtProfileName.setText(_Me.UserName);

                    break;

                case RECEIVE_PHOTO_REQUEST_CODE:

                    activeFragment = new ActiveUsersFragment().newInstance(_Me, "");
                    setCurrentFragment(R.id.content_main, activeFragment);

                    break;
                case SEND_PHOTO_REQUEST_CODE:

                    activeFragment = new ActiveUsersFragment().newInstance(_Me, "");
                    setCurrentFragment(R.id.content_main, activeFragment);

                    break;
                case TRY_REQUEST_CODE:

                    activeFragment = new ActiveUsersFragment().newInstance(_Me, "");
                    setCurrentFragment(R.id.content_main, activeFragment);

                    break;
            }

        } catch (Exception ex) {

        }
    }


    private final int CHANGE_PROFILE_REQUEST_CODE = 10;
    private final int RECEIVE_PHOTO_REQUEST_CODE = 11;
    private final int SEND_PHOTO_REQUEST_CODE = 12;
    private final int TRY_REQUEST_CODE = 13;
    public void onReceivePhoto(View view) {

        Button btnTry = (Button) view;
        UserUIModel um = (UserUIModel) btnTry.getTag();

        Intent intent = new Intent(this, ReceivePhotoActivity.class);
        intent.putExtra("1", (Serializable) _Me);
        intent.putExtra("2", (Serializable) um);
        //startActivity(intent);
        startActivityForResult(intent, RECEIVE_PHOTO_REQUEST_CODE);
    }

    public void onSendPhoto(View view) {

        Button btnTry = (Button) view;
        UserUIModel um = (UserUIModel) btnTry.getTag();

        Intent intent = new Intent(this, SendPhotoActivity.class);
        intent.putExtra("1", (Serializable) _Me);
        intent.putExtra("2", (Serializable) um);
        //startActivity(intent);
        startActivityForResult(intent, SEND_PHOTO_REQUEST_CODE);
    }

    public void onTry(View view) {

        Button btnTry = (Button) view;
        UserUIModel um = (UserUIModel) btnTry.getTag();

        Intent intent = new Intent(this, TryActivity.class);
        intent.putExtra("1", (Serializable) um);
        //startActivity(intent);
        startActivityForResult(intent, TRY_REQUEST_CODE);
    }

    public void onOpenCategory(View view) {

        RelativeLayout rlTest = (RelativeLayout) view;
        CategoryModel cm = (CategoryModel) rlTest.getTag();


        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setTitle("Alert")
                .setMessage(cm.Name)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

    }

    /*public void setupNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = 1;
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notifyID, mBuilder.build());
    }*/


    /*private void isAliveRequest() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            //_ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.IS_ALIVE.getCode();
            objParams[1] = _Me.ID;
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

    }*/

}
