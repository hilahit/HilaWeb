package com.example.parkinson.features.on_boarding.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.parkinson.data.UserRepository;
import com.example.parkinson.fcm.MessagingManager;
import com.example.parkinson.features.splash.SplashViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

public class LoginViewModel implements UserRepository.InitUserListener {

    private final UserRepository userRepository;
    private final MessagingManager messagingManager;

    String email = "";
    String password = "";
    MutableLiveData<ErrorEvent> errorEvent;
    MutableLiveData<Boolean> loginEvent;
    MutableLiveData<NextButtonState> nextButtonState;

    enum ErrorEvent {
        UN_VALID_EMAIL,
        UN_VALID_PASSWORD,
        LOGIN_FAIL
    }

    enum NextButtonState {
        ENABLE,
        DISABLE
    }

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public LoginViewModel(UserRepository userRepository, MessagingManager messagingManager) {
        this.userRepository = userRepository;
        errorEvent = new MutableLiveData<>();
        loginEvent = new MutableLiveData<>();
        nextButtonState = new MutableLiveData<>();
        this.messagingManager = messagingManager;
    }

    public void setEmail(String email) {
        this.email = email;
        validateNextButton();
    }

    public void setPassword(String password) {
        this.password = password;
        validateNextButton();
    }

    public void validateNextButton(){
        if(!email.isEmpty() && !password.isEmpty()){
            nextButtonState.postValue(NextButtonState.ENABLE);
        } else {
            nextButtonState.postValue(NextButtonState.DISABLE);
        }
    }

    public void onLoginClick() {
        if (email.isEmpty()) {
            errorEvent.postValue(ErrorEvent.UN_VALID_EMAIL);
        } else if (password.isEmpty()) {
            errorEvent.postValue(ErrorEvent.UN_VALID_PASSWORD);
        } else {
            userRepository.login(email, password, setLoginListener());
        }
    }

    public OnCompleteListener setLoginListener() {
        return (OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful()) {
                Log.d("wowLoginVM", "sign in successful");
                userRepository.updateCurrentUser();
                userRepository.initUserDetails(this);
            } else {
                Log.d("wowLoginVM", "sign in Not successful");
                errorEvent.postValue(ErrorEvent.LOGIN_FAIL);
            }
        };
    }

    @Override
    public void finishedLoadingUser() {
        messagingManager.refreshPushNotificationToken();
        loginEvent.postValue(true);
    }

}
