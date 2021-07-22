package com.example.parkinson.features.main;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ParkinsonApplication;
import com.example.parkinson.R;
import com.example.parkinson.common.LoadingScreen;
import com.example.parkinson.di.MainComponent;
import com.example.parkinson.features.brodacsts.NotifService;
import com.example.parkinson.features.brodacsts.ReportBroadcast;
import com.example.parkinson.features.brodacsts.ReportService;
import com.example.parkinson.features.notification.NotifServiceForground;
import com.example.parkinson.features.on_boarding.OnBoardingActivity;

import java.util.Calendar;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    public MainComponent mainComponent;

    @Inject
    MainViewModel mainViewModel;
    LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainComponent = ((ParkinsonApplication) getApplicationContext()).appComponent.mainComponent().create();
        mainComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        initObservers();

//        Intent intentService = new Intent(this, NotifServiceForground.class);
//        startService(intentService);
//
//        Intent intent = new Intent(this, ReportService.class);
//        intent.putExtra("title", "תזכורת נטילת תרופות");
//        intent.putExtra("id", 1);
//        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 22);
//        calendar.set(Calendar.MINUTE,59);
//
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        //alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, 3000, AlarmManager.INTERVAL_DAY, pintent);
//        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 60 * 1, pintent);
//        //alarm.setExact()
//// Set the alarm to start at 8:30 a.m.
////        Calendar calendar = Calendar.getInstance();
////        calendar.setTimeInMillis(System.currentTimeMillis());
////        calendar.set(Calendar.HOUR_OF_DAY, 22);
////        calendar.set(Calendar.MINUTE, 59);
//
//// setRepeating() lets you specify a precise custom interval--in this case,
//// 20 minutes.
////
//
//        Intent medicineIntent = new Intent(this, NotifService.class);
//        medicineIntent.putExtra("title", "תזכורת נטילת תרופות");
//        medicineIntent.putExtra("id", 0);
//        PendingIntent medicineIntentPintent = PendingIntent.getService(this, 0, intent, 0);
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTimeInMillis(System.currentTimeMillis());
//        calendar2.set(Calendar.HOUR_OF_DAY,0);
//        calendar2.set(Calendar.MINUTE,20);
//
//        AlarmManager alarm2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        //alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, 3000, AlarmManager.INTERVAL_DAY, pintent);
//        alarm2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
//                1000 * 60 * 1, medicineIntentPintent);
    }

    private void initUi() {
        loadingScreen = findViewById(R.id.loadingScreen);
    }

    private void initObservers(){
        mainViewModel.openActivityEvent.observe(this, navigationEvent -> {
            switch (navigationEvent){
                case OPEN_ON_BOARDING_ACTIVITY:
                    openOnBoarding();
                    break;
            }
        });
        mainViewModel.isLoading.observe(this, isLoading ->{
            updateLoadingScreen(isLoading);
        });
    }

    /** Show / hide loading animation
     * @param isLoading is true when waiting for data from server
     */
    public void updateLoadingScreen(Boolean isLoading) {
        if(isLoading){
            loadingScreen.show();
        } else {
            loadingScreen.hide();
        }
    }

    private void openOnBoarding(){
        Intent intent = new Intent(this, OnBoardingActivity.class);
        startActivity(intent);
        finish();
    }
}