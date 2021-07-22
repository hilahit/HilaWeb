package com.example.parkinson.di;

import com.example.parkinson.features.on_boarding.OnBoardingActivity;
import com.example.parkinson.features.on_boarding.login.LoginFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Subcomponent;

@OnBoardingScope
@Subcomponent
public interface OnBoardingComponent {

    @Subcomponent.Factory
    interface Factory {
        OnBoardingComponent create();
    }

    void inject(OnBoardingActivity onBoardingActivity);
    void inject(LoginFragment loginFragment);
}
