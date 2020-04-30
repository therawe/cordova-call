package com.therawe.cordovacall;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.Manifest;
import android.telecom.Connection;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CordovaCall extends CordovaPlugin {

    private static String TAG = "CordovaCall";
    public static final int CALL_PHONE_REQ_CODE = 0;
    public static final int REAL_PHONE_CALL = 1;
    private static final String CHANNEL_ID = "therawe_notification_channel";
    static final int NOTIFICATION_ID = 1;
    private int permissionCounter = 0;
    private String pendingAction;
    private TelecomManager tm;
    private PhoneAccountHandle handle;
    private PhoneAccount phoneAccount;
    private CallbackContext callbackContext;
    private String appName;
    private String from;
    private String to;
    private String realCallTo;
    private static HashMap<String, ArrayList<CallbackContext>> callbackContextMap = new HashMap<String, ArrayList<CallbackContext>>();
    private static CordovaInterface cordovaInterface;
    private static CordovaWebView cordovaWebView;
    private static Icon icon;
    private static CordovaCall instance;

    static final String INCOMING_CALL_ACTION = "incoming_call_action";
    static final String ACCEPT_CALL_ACTION = "accept_call_action";
    static final String REJECT_CALL_ACTION = "REJECT_CALL_ACTION";

    public static HashMap<String, ArrayList<CallbackContext>> getCallbackContexts() {
        return callbackContextMap;
    }

    public static CordovaInterface getCordova() {
        return cordovaInterface;
    }

    public static CordovaWebView getWebView() {
        return cordovaWebView;
    }

    public static Icon getIcon() {
        return icon;
    }

    public static CordovaCall getInstance() {
        return instance;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        cordovaInterface = cordova;
        cordovaWebView = webView;
        super.initialize(cordova, webView);
        appName = getApplicationName(this.cordova.getActivity().getApplicationContext());
//        handle = new PhoneAccountHandle(new ComponentName(this.cordova.getActivity().getApplicationContext(),MyConnectionService.class),appName);
//        tm = (TelecomManager)this.cordova.getActivity().getApplicationContext().getSystemService(this.cordova.getActivity().getApplicationContext().TELECOM_SERVICE);
//        if(android.os.Build.VERSION.SDK_INT >= 26) {
//          phoneAccount = new PhoneAccount.Builder(handle, appName)
//                  .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
//                  .build();
//          tm.registerPhoneAccount(phoneAccount);
//        }
//        if(android.os.Build.VERSION.SDK_INT >= 23) {
//          phoneAccount = new PhoneAccount.Builder(handle, appName)
//                   .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
//                   .build();
//          tm.registerPhoneAccount(phoneAccount);
//        }
        callbackContextMap.put("answer",new ArrayList<CallbackContext>());
        callbackContextMap.put("reject",new ArrayList<CallbackContext>());
        callbackContextMap.put("hangup",new ArrayList<CallbackContext>());
        callbackContextMap.put("sendCall",new ArrayList<CallbackContext>());
        callbackContextMap.put("receiveCall",new ArrayList<CallbackContext>());

        instance = this;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("receiveCall")) {
//            Connection conn = MyConnectionService.getConnection();
//            if(conn != null) {
//                if(conn.getState() == Connection.STATE_ACTIVE) {
//                    this.callbackContext.error("You can't receive a call right now because you're already in a call");
//                } else {
//                    this.callbackContext.error("You can't receive a call right now");
//                }
//            } else {
//                from = args.getString(0);
//                permissionCounter = 2;
//                pendingAction = "receiveCall";
//                this.checkCallPermission();
//            }

            this.receiveCall(args.getString(0));
            return true;
        } else if (action.equals("sendCall")) {
//            Connection conn = MyConnectionService.getConnection();
//            if(conn != null) {
//                if(conn.getState() == Connection.STATE_ACTIVE) {
//                    this.callbackContext.error("You can't make a call right now because you're already in a call");
//                } else if(conn.getState() == Connection.STATE_DIALING) {
//                    this.callbackContext.error("You can't make a call right now because you're already trying to make a call");
//                } else {
//                    this.callbackContext.error("You can't make a call right now");
//                }
//            } else {
//                to = args.getString(0);
//                permissionCounter = 2;
//                pendingAction = "sendCall";
//                this.checkCallPermission();
//                /*cordova.getThreadPool().execute(new Runnable() {
//                    public void run() {
//                        getCallPhonePermission();
//                    }
//                });*/
//            }
            this.callbackContext.error("sendCall not supported");
            return true;
        } else if (action.equals("connectCall")) {
//            Connection conn = MyConnectionService.getConnection();
//            if(conn == null) {
//                this.callbackContext.error("No call exists for you to connect");
//            } else if(conn.getState() == Connection.STATE_ACTIVE) {
//                this.callbackContext.error("Your call is already connected");
//            } else {
//                conn.setActive();
//                Intent intent = new Intent(this.cordova.getActivity().getApplicationContext(), this.cordova.getActivity().getClass());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                this.cordova.getActivity().getApplicationContext().startActivity(intent);
//                this.callbackContext.success("Call connected successfully");
//            }
            this.callbackContext.error("connectCall not supported");
            return true;
        } else if (action.equals("endCall")) {
//            Connection conn = MyConnectionService.getConnection();
//            if(conn == null) {
//                this.callbackContext.error("No call exists for you to end");
//            } else {
//                DisconnectCause cause = new DisconnectCause(DisconnectCause.LOCAL);
//                conn.setDisconnected(cause);
//                conn.destroy();
//                MyConnectionService.deinitConnection();
//                ArrayList<CallbackContext> callbackContexts = CordovaCall.getCallbackContexts().get("hangup");
//                for (final CallbackContext cbContext : callbackContexts) {
//                    cordova.getThreadPool().execute(new Runnable() {
//                        public void run() {
//                            PluginResult result = new PluginResult(PluginResult.Status.OK, "hangup event called successfully");
//                            result.setKeepCallback(true);
//                            cbContext.sendPluginResult(result);
//                        }
//                    });
//                }
//                this.callbackContext.success("Call ended successfully");
//            }
            this.callbackContext.error("endCall not supported");
            return true;
        } else if (action.equals("registerEvent")) {
            String eventType = args.getString(0);
            ArrayList<CallbackContext> callbackContextList = callbackContextMap.get(eventType);
            callbackContextList.add(this.callbackContext);
            return true;
        } else if (action.equals("setAppName")) {
//            String appName = args.getString(0);
//            handle = new PhoneAccountHandle(new ComponentName(this.cordova.getActivity().getApplicationContext(),MyConnectionService.class),appName);
//            if(android.os.Build.VERSION.SDK_INT >= 26) {
//              phoneAccount = new PhoneAccount.Builder(handle, appName)
//                  .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
//                  .build();
//              tm.registerPhoneAccount(phoneAccount);
//            }
//            if(android.os.Build.VERSION.SDK_INT >= 23) {
//              phoneAccount = new PhoneAccount.Builder(handle, appName)
//                   .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
//                   .build();
//              tm.registerPhoneAccount(phoneAccount);
//            }
//            this.callbackContext.success("App Name Changed Successfully");
            this.callbackContext.error("setAppName not supported");
            return true;
        } else if (action.equals("setIcon")) {
//            String iconName = args.getString(0);
//            int iconId = this.cordova.getActivity().getApplicationContext().getResources().getIdentifier(iconName, "drawable", this.cordova.getActivity().getPackageName());
//            if(iconId != 0) {
//                icon = Icon.createWithResource(this.cordova.getActivity(), iconId);
//                this.callbackContext.success("Icon Changed Successfully");
//            } else {
//                this.callbackContext.error("This icon does not exist. Make sure to add it to the res/drawable folder the right way.");
//            }
            this.callbackContext.error("setIcon not supported");
            return true;
        } else if (action.equals("mute")) {
//            this.mute();
            this.callbackContext.error("mute not supported");
            return true;
        } else if (action.equals("unmute")) {
//            this.unmute();
            this.callbackContext.error("unmute not supported");
            return true;
        } else if (action.equals("speakerOn")) {
//            this.speakerOn();
            this.callbackContext.error("speakerOn not supported");
            return true;
        } else if (action.equals("speakerOff")) {
//            this.speakerOff();
            this.callbackContext.error("speakerOff not supported");
            return true;
        } else if (action.equals("callNumber")) {
//            realCallTo = args.getString(0);
//            if(realCallTo != null) {
//              cordova.getThreadPool().execute(new Runnable() {
//                  public void run() {
//                      callNumberPhonePermission();
//                  }
//              });
//              this.callbackContext.success("Call Successful");
//            } else {
//              this.callbackContext.error("Call Failed. You need to enter a phone number.");
//            }
            this.callbackContext.error("callNumber not supported");
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException
    {
        for(int r:grantResults)
        {
            if(r == PackageManager.PERMISSION_DENIED)
            {
                this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "CALL_PHONE Permission Denied"));
                return;
            }
        }
        switch(requestCode)
        {
            case CALL_PHONE_REQ_CODE:
                this.sendCall();
                break;
            case REAL_PHONE_CALL:
                this.callNumber();
                break;
        }
    }

    private void checkCallPermission() {
//        if(permissionCounter >= 1) {
//            PhoneAccount currentPhoneAccount = tm.getPhoneAccount(handle);
//            if(currentPhoneAccount.isEnabled()) {
//                if(pendingAction == "receiveCall") {
//                    this.receiveCall();
//                } else if(pendingAction == "sendCall") {
//                    this.sendCall();
//                }
//            } else {
//                if(permissionCounter == 2) {
//                    Intent phoneIntent = new Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS);
//                    phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    this.cordova.getActivity().getApplicationContext().startActivity(phoneIntent);
//                } else {
//                    this.callbackContext.error("You need to accept phone account permissions in order to send and receive calls");
//                }
//            }
//        }
//        permissionCounter--;
    }

    private void receiveCall(String callFrom) {
        Context context = this.cordova.getActivity();
        Window window = this.cordova.getActivity().getWindow();

        IntentFilter filter = new IntentFilter();
        filter.addAction(INCOMING_CALL_ACTION);
        filter.addAction(ACCEPT_CALL_ACTION);
        filter.addAction(REJECT_CALL_ACTION);
        this.cordova.getActivity().registerReceiver(notificationBroadcastReceiver, filter);

        Runnable runnable = () -> {

            createNotificationChannel(context);

            //set flags so activity is showed when phone is off (on lock screen)
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            CordovaCall.getInstance().createFullScreenNotification(context, callFrom);
        };
        this.cordova.getActivity().runOnUiThread(runnable);

        this.callbackContext.success("Incoming call successful");
    }

    private void createFullScreenNotification(Context context, String callFrom) {
        Intent acceptIntent = new Intent(ACCEPT_CALL_ACTION);
        acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this.cordova.getActivity(), 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent declineIntent = new Intent(REJECT_CALL_ACTION);
        declineIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(this.cordova.getActivity(), 0, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent fullScreenIntent = new Intent(this.cordova.getActivity(), LockScreenActivity.class);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent fullScreenPending = PendingIntent.getActivity(this.cordova.getActivity(), 0, fullScreenIntent, 0);
        
        int iconId = this.cordova.getActivity().getApplicationContext().getResources().getIdentifier("icon", "drawable", this.cordova.getActivity().getPackageName());
        int acceptIconId = this.cordova.getActivity().getApplicationContext().getResources().getIdentifier("ic_videocam_green_24px", "drawable", this.cordova.getActivity().getPackageName());
        int declineIconId = this.cordova.getActivity().getApplicationContext().getResources().getIdentifier("ic_videocam_off_red_24px", "drawable", this.cordova.getActivity().getPackageName());

        NotificationCompat.Action acceptAction = new NotificationCompat.Action.Builder(acceptIconId, "Accept", acceptPendingIntent).build();
        NotificationCompat.Action declineAction = new NotificationCompat.Action.Builder(declineIconId, "Decline", declinePendingIntent).build();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(iconId)
                        .setContentTitle("Video Call Incoming!")
                        .setContentText(String.format("%s is attempting to call you!", callFrom))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setContentIntent(fullScreenPending)
                        .setFullScreenIntent(fullScreenPending, true)
                        .addAction(acceptAction)
                        .addAction(declineAction);

        Notification notification = notificationBuilder.build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }

    private BroadcastReceiver notificationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ArrayList<CallbackContext> callbackContexts = new ArrayList<>();
            switch (action) {
                case INCOMING_CALL_ACTION:
                    callbackContexts = CordovaCall.getCallbackContexts().get("receiveCall");
                    break;
                case ACCEPT_CALL_ACTION:
                    callbackContexts = CordovaCall.getCallbackContexts().get("answer");
                    break;
                case REJECT_CALL_ACTION:
                    callbackContexts = CordovaCall.getCallbackContexts().get("reject");
                    break;
            }

            for (final CallbackContext cbContext : callbackContexts) {
                cordova.getThreadPool().execute(() -> {
                    PluginResult result = new PluginResult(PluginResult.Status.OK);
                    result.setKeepCallback(true);
                    cbContext.sendPluginResult(result);
                });
            }

            NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID);
        }
    };

    public void sendEvent(String eventName) {
        ArrayList<CallbackContext> callbackContexts = CordovaCall.getCallbackContexts().get(eventName);
        for (final CallbackContext cbContext : callbackContexts) {
            cordova.getThreadPool().execute(() -> {
                PluginResult result = new PluginResult(PluginResult.Status.OK);
                result.setKeepCallback(true);
                cbContext.sendPluginResult(result);
            });
        }
    }

    private void sendCall() {
//        Uri uri = Uri.fromParts("tel", to, null);
//        Bundle callInfoBundle = new Bundle();
//        callInfoBundle.putString("to",to);
//        Bundle callInfo = new Bundle();
//        callInfo.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS,callInfoBundle);
//        callInfo.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle);
//        callInfo.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, true);
//        tm.placeCall(uri, callInfo);
//        permissionCounter = 0;
        this.callbackContext.error("Outgoing call not supported");
    }

    private void mute() {
        AudioManager audioManager = (AudioManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(true);
    }

    private void unmute() {
        AudioManager audioManager = (AudioManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(false);
    }

    private void speakerOn() {
        AudioManager audioManager = (AudioManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);
    }

    private void speakerOff() {
        AudioManager audioManager = (AudioManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(false);
    }

    protected void getCallPhonePermission() {
        cordova.requestPermission(this, CALL_PHONE_REQ_CODE, Manifest.permission.CALL_PHONE);
    }

    protected void callNumberPhonePermission() {
        cordova.requestPermission(this, REAL_PHONE_CALL, Manifest.permission.CALL_PHONE);
    }

    private void callNumber() {
        try {
          Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", realCallTo, null));
          this.cordova.getActivity().getApplicationContext().startActivity(intent);
        } catch(Exception e) {
          this.callbackContext.error("Call Failed");
        }
        this.callbackContext.success("Call Successful");
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

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
