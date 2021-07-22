package com.example.parkinson.features.brodacsts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.parkinson.R;
import com.example.parkinson.features.notification.NotificationActivity;

public class ReportService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Intent reportActivity = new Intent(this, NotificationActivity.class);
//        reportActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        reportActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        //App.setCurrentActivityIntent(currentActivityIntent);
//        //setCurrentActivityIntent(currentActivityIntent);
//        startActivity(reportActivity);

         createNotificationChannel();
        NotificationCompat.Builder builder;
        NotificationManager notificationManager;
        Intent reportActivity = new Intent(this, NotificationActivity.class);
        reportActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //reportActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, reportActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("איך אתה מרגיש?")
                .setContentText("לחץ עליי לדיווח")
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(Color.BLACK)
                .setOngoing(true);


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(10, builder.build());
        //startForeground(10,builder.build() );
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL";
            String description = "CHANNEL_DESC";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
