package com.example.parkinson.features.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ParkinsonApplication;
import com.example.parkinson.R;
import com.example.parkinson.di.ApplicationComponent;
import com.example.parkinson.model.enums.EStatus;

import javax.inject.Inject;


public class NotificationActivity extends AppCompatActivity {
    public ApplicationComponent applicationComponent;

    EStatus chosenStatus;

    @Inject
    NotificationViewModel notificationViewModel;

    FrameLayout notificationBtn;
    ConstraintLayout background;
    SensorManager manager;
    Sensor sensor;
    RadioButton onnBtn, offBtn, dyskinesiaBtn;
    TextView reportBtn,isHallucinations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((ParkinsonApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification);
        //manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initUi();
    }

    private void initUi() {
        //notificationBtn = findViewById(R.id.notificationFrame);
        background = findViewById(R.id.notification_background);
        initBtnListenrs();
        //initSwipeListener();
    }

    private void initBtnListenrs() {
        reportBtn = findViewById(R.id.notificationReportBtn);
        offBtn = findViewById(R.id.notificationOffBtn);
        onnBtn = findViewById(R.id.notificationOnBtn);
        dyskinesiaBtn = findViewById(R.id.notificationDyskinesiaBtn);

        RadioGroup radioGroup = findViewById(R.id.reportRG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == onnBtn.getId()) {
                    chosenStatus = EStatus.On;
                } else if (checkedId == offBtn.getId()) {
                    chosenStatus = EStatus.Off;
                } else if (checkedId == dyskinesiaBtn.getId()) {
                    chosenStatus = EStatus.Dyskinesia;
                }
            }
        });


        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportToServer();
            }
        });

        isHallucinations = findViewById(R.id.notificationHallucinationsBtn);

        isHallucinations.setOnClickListener(v->{
            isHallucinations.setSelected(!isHallucinations.isSelected());
        });
    }

    private void reportToServer() {
        if (chosenStatus == null)
            return;

        switch (chosenStatus) {
            case On:
                notificationViewModel.updateReport(EStatus.On, isHallucinations.isSelected());
                break;
            case Off:
                notificationViewModel.updateReport(EStatus.Off, isHallucinations.isSelected());
                break;
            case Dyskinesia:
                notificationViewModel.updateReport(EStatus.Dyskinesia, isHallucinations.isSelected());
                break;
        }
        Intent intentService = new Intent(this, NotifServiceForground.class);
        startService(intentService);
        onBackPressed();
    }

    @SuppressLint("ClickableViewAccessibility")
//    private void initSwipeListener() {
//        notificationBtn.setOnTouchListener(new OnSwipeTouchListener(this) {
//            public void onSwipeTop() {
//                upReport();
//            }
//
//            public void onSwipeRight() {
//                rightReport();
//            }
//
//            public void onSwipeLeft() {
//                leftReport();
//            }
//
//            public void onSwipeBottom() {
//                downReport();
//            }
//        });
//    }

    private void upReport() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                notificationViewModel.updateReport(EStatus.Dyskinesia, isHallucinations.isSelected());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
        });
        //hideDescription();
        //notificationBtn.startAnimation(animation);
    }

    private void downReport() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                notificationViewModel.updateReport(EStatus.Hallucination, isHallucinations.isSelected());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
        });
        //hideDescription();
        //notificationBtn.startAnimation(animation);

    }

    private void leftReport() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_left_exit);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                notificationViewModel.updateReport(EStatus.Off, isHallucinations.isSelected());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
        });
        //hideDescription();
        //notificationBtn.startAnimation(animation);

    }

    private void rightReport() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_right_exit);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                notificationViewModel.updateReport(EStatus.On, isHallucinations.isSelected());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
        });
        //hideDescription();
        //notificationBtn.startAnimation(animation);
    }

    private void hideDescription() {
        //findViewById(R.id.notificationHallucinationBtn).setVisibility(View.GONE);
        findViewById(R.id.notificationOffBtn).setVisibility(View.GONE);
        findViewById(R.id.notificationOnBtn).setVisibility(View.GONE);
        findViewById(R.id.notificationDyskinesiaBtn).setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
