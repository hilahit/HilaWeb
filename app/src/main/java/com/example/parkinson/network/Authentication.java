package com.example.parkinson.network;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;

/** Singleton class that stores Authentication instance.
 *  And current logged user instance
 */
@Singleton
public class Authentication {
    FirebaseAuth AuthenticationServer;
    FirebaseUser currentUser;

    @Inject
    public Authentication() {
        AuthenticationServer = FirebaseAuth.getInstance();
        currentUser = getAuthentication().getCurrentUser();
    }

    public FirebaseAuth getAuthentication() {
        return AuthenticationServer;
    }

    public FirebaseUser getCurrentUser() { return currentUser; }

    public void updateCurrentUser(){
        currentUser = getAuthentication().getCurrentUser();
    }
}
