package com.development.blackbox.showup.Helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.development.blackbox.showup.MainActivity;
import com.development.blackbox.showup.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        /*if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        } else if(remoteMessage.getData() != null) {
            sendNotification(remoteMessage.getData().get("title"));
        }*/
        if(remoteMessage.getData() != null) {
            sendNotification(remoteMessage.getData());
        }
    }

    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

    long[] llPattern = new long[2];
    private void sendNotification(Map<String, String> dataMessage) {

        /*SharedPreferences userInfoShPref = getSharedPreferences(Config.LOGIN_SETTINGS, MODE_PRIVATE);
        long userID = userInfoShPref.getLong(Config.USER_ID_KEY, -1);
        if(userID != Integer.parseInt(dataMessage.get("user_id"))) {
            return;
        }*/


        llPattern[0] = 100;
        llPattern[1] = 300;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle(dataMessage.get("title"))
                .setContentText(dataMessage.get("body"))
                .setAutoCancel(true)
                .setLights(Color.RED, 300, 5000)
                .setSound(defaultSoundUri)
                .setVibrate(llPattern)
                .setContentIntent(pendingIntent);

        notificationBuilder.setLights(Color.RED, 300, 5000); //0xff00ff00  Color.RED
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Config.NotificationId /* ID of notification */, notificationBuilder.build());
    }

}
