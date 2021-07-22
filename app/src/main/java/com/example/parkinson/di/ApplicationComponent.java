package com.example.parkinson.di;

import com.example.parkinson.features.notification.NotificationActivity;
import com.example.parkinson.features.splash.SplashActivity;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component()
public interface ApplicationComponent {

    OnBoardingComponent.Factory onBoardingComponent();
    MainComponent.Factory mainComponent();
    // This tells Dagger that Activity requests injection so the graph needs to
    // satisfy all the dependencies of the fields that LoginActivity is injecting.
    void inject(SplashActivity splashActivity);
    void inject(NotificationActivity notificationActivity);

//    void inject(NotifManager notifManager);
//    void inject(NotifService notifService);
}
