package com.example.parkinson.features.brodacsts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.parkinson.features.notification.NotificationActivity;

public class ReportBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent reportActivity = new Intent(context, NotificationActivity.class);
        reportActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reportActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
       // App.setCurrentActivityIntent(currentActivityIntent);
        context.startActivity(reportActivity);
    }
}
