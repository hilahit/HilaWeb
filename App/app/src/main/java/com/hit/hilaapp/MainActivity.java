package com.hit.hilaapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements HomeFragment.HomeListener, LoginFragment.LoginListener, ChatFragment.ChatListener {


    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("message");
        mFragmentManager = getSupportFragmentManager();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {

            LoginFragment loginFragment = LoginFragment.newInstance();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("Alih", ContactListFragment.CONTACT_NAME);
            bundle.putString("1", ContactListFragment.CONTACT_ID);
            transaction.
                    add(R.id.main_container, loginFragment, LoginFragment.class.getName()).
                    addToBackStack(LoginFragment.class.getName())
                    .commit();
        }
        else {

            HomeFragment homeFragment = HomeFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.
                    add(R.id.main_container, homeFragment, HomeFragment.class.getName()).
                    commit();
        }
    }

    @Override
    public void onChatClick() {

        ChatFragment chatFragment = ChatFragment.newInstance("Alih", 1);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("Alih", ContactListFragment.CONTACT_NAME);
        bundle.putString("1", ContactListFragment.CONTACT_ID);
        transaction.
                add(R.id.main_container, chatFragment).
                addToBackStack(ChatFragment.class.getName())
                .commit();
    }

    @Override
    public void onLogin() {
        mFragmentManager.popBackStack();
        Log.d("alih", "onLogin: logged in successfully!");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
//        FirebaseAuth.getInstance().signOut();
    }

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {

            }
        }
    };

    @Override
    public void onContentReceived() {

    }
}