package com.zomindianew.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.zomindianew.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zomindianew.comman.activity.UnderReviewActivty;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.HomeActivityProvider;
import com.zomindianew.user.activity.HomeActivityUser;


import java.util.List;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("message", "onMessageReceived: " + remoteMessage.getData());


        if (remoteMessage.getData().size() > 0) {

            Log.v("new-message>>>>>>", "ORIGINAL MESSAGE DATA PAYLOAD NOTIFICATION==>" + remoteMessage.getData());
            Map<String, String> mapNotification = remoteMessage.getData();


            takeAction(mapNotification);
        }


    }


    private void takeAction(Map<String, String> data) {


        if (isAppIsInBackground(getApplicationContext())) {

            createNotification(data);
        } else {
            createNotification(data);
        }
    }


    private void createNotification(Map<String, String> data) {

        Intent intent = null;
        String message1 = data.get("message");
        if (data.get("type").equals(Constants.ACCOUNT_APPROVED)) {
            intent = new Intent(this, UnderReviewActivty.class);
        } else {
            if (MySharedPreferances.getInstance(MyFirebaseMessagingService.this).getString(Constants.USER_ROLE).equals("provider")) {
                intent = new Intent(this, HomeActivityProvider.class);
            } else {
                intent = new Intent(this, HomeActivityUser.class);
            }

            intent.putExtra("NAVIGATE_SCREEN", data.get("type"));

        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int notification_tag = (int) (System.currentTimeMillis() & 0xfffffff);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        String CHANNEL_ID = String.valueOf(System.currentTimeMillis());// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //.setContentTitle(newRequestBean.getType())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message1))
                .setContentText("ZOMINDIA")
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(pendingIntent);
        mNotificationBuilder.setSmallIcon(R.drawable.transparent_logo);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(notification_tag, mNotificationBuilder.build());
    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
