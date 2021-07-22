package com.example.parkinson.di;

import com.example.parkinson.fcm.MyFirebaseMessagingService;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
class ServiceModule {

    MyFirebaseMessagingService mService;

    ServiceModule(MyFirebaseMessagingService service) {
        mService = service;
    }

    @Provides
    MyFirebaseMessagingService provideMyService() {
        return mService;
    }
}

@Component(modules=ServiceModule.class)
interface MyServiceComponent {
    void inject(MyFirebaseMessagingService service);
}