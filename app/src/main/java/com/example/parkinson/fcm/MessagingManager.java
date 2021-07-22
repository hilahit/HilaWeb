package com.example.parkinson.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.parkinson.data.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessagingManager {

    UserRepository userRepository;

    @Inject
    public MessagingManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void refreshPushNotificationToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        updateToken(token);
                    }
                });
    }

    public void updateToken(String token) {
        userRepository.updateUserToken(token);
    }

}
