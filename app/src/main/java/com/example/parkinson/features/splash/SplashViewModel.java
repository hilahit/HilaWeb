package com.example.parkinson.features.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkinson.data.UserRepository;
import com.example.parkinson.fcm.MessagingManager;
import com.example.parkinson.fcm.MyFirebaseMessagingService;

import javax.inject.Inject;

public class SplashViewModel implements UserRepository.InitUserListener {
    private final UserRepository userRepository;
    private final MessagingManager messagingManager;

    public enum NavigationEvent{
        OPEN_ON_BOARDING_ACTIVITY,
        OPEN_MAIN_ACTIVITY
    }

    MutableLiveData<NavigationEvent> navigationEvent;

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public SplashViewModel(UserRepository userRepository, MessagingManager messagingManager) {
        this.userRepository = userRepository;
        this.messagingManager = messagingManager;
        navigationEvent = new MutableLiveData<NavigationEvent>();
    }

    public void init(){
        fetchUser();
    }

    private void fetchUser(){
        if(userRepository.getCurrentUser() == null){
            navigationEvent.postValue(NavigationEvent.OPEN_ON_BOARDING_ACTIVITY);
        } else {
            userRepository.initUserDetails(this);
        }
    }

    @Override
    public void finishedLoadingUser() {
        messagingManager.refreshPushNotificationToken();
        navigationEvent.postValue(NavigationEvent.OPEN_MAIN_ACTIVITY);
    }

}
