package com.example;

import android.app.Application;

import com.example.parkinson.di.ApplicationComponent;
import com.example.parkinson.di.DaggerApplicationComponent;
import com.google.firebase.FirebaseApp;

public class ParkinsonApplication extends Application {

    public ApplicationComponent appComponent = DaggerApplicationComponent.create();

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }


}
