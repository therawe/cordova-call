package com.therawe.cordovacall;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import capacitor.android.plugins.R;

public class LockScreenActivity extends AppCompatActivity {

    static final String ACCEPT_CALL_ACTION = "accept_call_action";
    static final String DECLINE_CALL_ACTION = "decline_call_action";

    private static ResourceUtilities resourceUtilities;

    private FloatingActionButton acceptActionFab;
    private FloatingActionButton declineActionFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resourceUtilities = new ResourceUtilities(this);

        setContentView(R.layout.activity_lock_screen);

        acceptActionFab = findViewById(resourceUtilities.getId("accept_action_fab"));
        declineActionFab = findViewById(resourceUtilities.getId("decline_action_fab"));

        acceptActionFab.show();
        acceptActionFab.setOnClickListener(acceptCallListener());
        declineActionFab.show();
        declineActionFab.setOnClickListener(declineCallListener());

        String callerName = getIntent().getStringExtra("CALLER_NAME");
        TextView callerNameText = findViewById(resourceUtilities.getId("caller_name_text"));
        if (callerName != null && !callerName.isEmpty()) {
            callerNameText.setText(callerName);
            callerNameText.setVisibility(View.VISIBLE);
        } else {
            callerNameText.setVisibility(View.INVISIBLE);
        }

        turnScreenOnAndKeyguardOff();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        turnScreenOffAndKeyguardOn();
    }

    private View.OnClickListener acceptCallListener() {
        return v -> {
            Intent acceptIntent = new Intent(ACCEPT_CALL_ACTION);
            acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            sendBroadcast(acceptIntent);
            finish();
        };
    }

    private View.OnClickListener declineCallListener() {
        return v -> {
            Intent declineIntent = new Intent(DECLINE_CALL_ACTION);
            declineIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            sendBroadcast(declineIntent);
            finish();
        };
    }

    private void turnScreenOnAndKeyguardOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }

        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.requestDismissKeyguard(this, null);
        }
    }

    private void turnScreenOffAndKeyguardOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false);
            setTurnScreenOn(false);
        } else {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }
    }
}
