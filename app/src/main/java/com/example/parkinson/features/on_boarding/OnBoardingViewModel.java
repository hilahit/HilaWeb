package com.example.parkinson.features.on_boarding;

import androidx.lifecycle.MutableLiveData;

import com.example.parkinson.data.UserRepository;
import com.example.parkinson.di.OnBoardingScope;

import javax.inject.Inject;

@OnBoardingScope
public class OnBoardingViewModel {
    private final UserRepository userRepository;

    MutableLiveData<NavigationEvent> navigationEvent;
    enum NavigationEvent{
        OPEN_ON_MAIN_ACTIVITY
    }
    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public OnBoardingViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        navigationEvent = new MutableLiveData<>();
    }

    public void openMainActivity() {
        navigationEvent.postValue(NavigationEvent.OPEN_ON_MAIN_ACTIVITY);
    }
}
