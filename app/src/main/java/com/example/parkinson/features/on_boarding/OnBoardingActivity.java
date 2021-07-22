package com.example.parkinson.features.on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import com.example.ParkinsonApplication;
import com.example.parkinson.R;
import com.example.parkinson.di.OnBoardingComponent;
import com.example.parkinson.features.main.MainActivity;

import java.util.Observable;

import javax.inject.Inject;

public class OnBoardingActivity extends AppCompatActivity {

    public OnBoardingComponent onBoardingComponent;

    @Inject
    OnBoardingViewModel onBoardingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onBoardingComponent = ((ParkinsonApplication) getApplicationContext())
                .appComponent.onBoardingComponent().create();
        onBoardingComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        initObservers();
    }

    private void initObservers() {
        onBoardingViewModel.navigationEvent.observe(this, new Observer<OnBoardingViewModel.NavigationEvent>() {
            @Override
            public void onChanged(OnBoardingViewModel.NavigationEvent navigationEvent) {
                switch (navigationEvent) {
                    case OPEN_ON_MAIN_ACTIVITY:
                        openMainActivity();
                        break;
                }
            }
        });

    }


    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}