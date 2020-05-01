package com.therawe.cordovacall;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import capacitor.android.plugins.R;

import static com.therawe.cordovacall.CordovaCall.*;

public class TheraweNotificationService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "therawe_notification_channel";
    static final int NOTIFICATION_ID = 1;
    static final String INCOMING_CALL_ACTION = "incoming_call_action";
    static final String ACCEPT_CALL_ACTION = "accept_call_action";
    static final String REJECT_CALL_ACTION = "reject_call_action";


    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        if (data.containsKey("extra")) {
            try {
                JSONObject extra = new JSONObject(data.get("extra"));
                String notificationType = extra.getString("type");
                if (notificationType.equals("VIDEO_CALL_INCOMING")) {
                    String callerName = extra.getString("callingUserName");

                    IntentFilter filter = new IntentFilter();
                    filter.addAction(INCOMING_CALL_ACTION);
                    filter.addAction(ACCEPT_CALL_ACTION);
                    filter.addAction(REJECT_CALL_ACTION);
                    this.registerReceiver(notificationBroadcastReceiver, filter);

//                    Intent acceptIntent = new Intent(ACCEPT_CALL_ACTION);
//                    acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//                    Intent notificationIntent = new Intent(this, LockScreenActivity.class);
//                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                    createNotificationChannel(this);

//                    Intent notificationIntent = getPackageManager()
//                            .getLaunchIntentForPackage(getPackageName())
//                            .setPackage(null)
//                            .setAction(Intent.ACTION_VIEW)
//                            .setData(Uri.parse(
//                                    getResources().getString(R.string.custom_url_scheme) +
//                                            "://events/"))
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//
//                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1234,
//                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent acceptIntent = new Intent(ACCEPT_CALL_ACTION);
                    acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent declineIntent = new Intent(REJECT_CALL_ACTION);
                    declineIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent declinePendingIntent = PendingIntent.getBroadcast(this, 0, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent fullScreenIntent = new Intent(this, LockScreenActivity.class);
                    fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    fullScreenIntent.putExtra("CALLER_NAME", callerName);
                    PendingIntent fullScreenPending = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    int iconId = this.getApplicationContext().getResources().getIdentifier("icon", "drawable", this.getPackageName());
                    int acceptIconId = this.getApplicationContext().getResources().getIdentifier("ic_videocam_green_24px", "drawable", this.getPackageName());
                    int declineIconId = this.getApplicationContext().getResources().getIdentifier("ic_videocam_off_red_24px", "drawable", this.getPackageName());

                    NotificationCompat.Action acceptAction = new NotificationCompat.Action.Builder(acceptIconId, "Accept", acceptPendingIntent).build();
                    NotificationCompat.Action declineAction = new NotificationCompat.Action.Builder(declineIconId, "Decline", declinePendingIntent).build();

                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(this, CHANNEL_ID)
                                    .setSmallIcon(iconId)
                                    .setContentTitle("Incoming Video Call")
                                    .setContentText(String.format("%s is attempting to call you!", callerName))
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                    .addAction(acceptAction)
                                    .addAction(declineAction)

                                    // Use a full-screen intent only for the highest-priority alerts where you
                                    // have an associated activity that you would like to launch after the user
                                    // interacts with the notification. Also, if your app targets Android 10
                                    // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                                    // order for the platform to invoke this notification.
                                    .setFullScreenIntent(fullScreenPending, true);

                    Notification incomingCallNotification = notificationBuilder.build();
                    NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, incomingCallNotification);

//                    startForeground(NOTIFICATION_ID, incomingCallNotification);
                }
            } catch (JSONException e) { }
        }
    }

    private BroadcastReceiver notificationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACCEPT_CALL_ACTION:
                    Intent notificationIntent = getPackageManager()
                            .getLaunchIntentForPackage(getPackageName())
                            .setPackage(null)
                            .setAction(Intent.ACTION_VIEW)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(notificationIntent);
                    CordovaCall.getInstance().sendEvent("answer");
                    break;
                case REJECT_CALL_ACTION:
                    CordovaCall.getInstance().sendEvent("reject");
                    break;
            }
//            stopForeground(true);
            NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID);
        }
    };

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "TheraWe Notification Channel", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Custom Notifications");
                channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}