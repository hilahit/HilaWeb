package com.hit.hilaapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    public interface LoginListener {
        void onLogin();

    }

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    private LoginListener listener;
    private AuthenticationViewModel authenticationViewModel;

    private ProgressBar mProgressBar;
    private TextInputEditText mEmailEt;
    private TextInputEditText mPasswordEt;
    private Button mLoginBtn;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try{
        listener = (LoginListener) context;
        }
        catch(ClassCastException e){
            throw new ClassCastException("The activity must implement LoginListener interface");
        }
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        authenticationViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    hideProgressBar();
                    listener.onLogin();
                }
                else{
                    Snackbar.make(view, getString(R.string.user_error), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mEmailEt = view.findViewById(R.id.login_page_username_edit_text);
        mPasswordEt = view.findViewById(R.id.login_page_password_edit_text);
        mLoginBtn = view.findViewById(R.id.login_page_login_btn);
        mProgressBar = view.findViewById(R.id.login_page_progress_bar);

        setListeners(view);
    }

    private void setListeners(View view) {

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation progressBarSlideDown = AnimationUtils.loadAnimation(getContext(), R.anim.progress_slide_down);


                String email = Objects.requireNonNull(mEmailEt.getText()).toString();
                String password = Objects.requireNonNull(mPasswordEt.getText()).toString();

                if (!email.trim().isEmpty() && !password.trim().isEmpty()){
                    mProgressBar.startAnimation(progressBarSlideDown);
                    mProgressBar.setVisibility(View.VISIBLE);

                    // sign in
                    authenticationViewModel.signIn(email, password);
                }
                else{
                    Snackbar.make(view, getString(R.string.provide_email_and_password), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideProgressBar() {
        mProgressBar.clearAnimation();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("alih", "onDestroy: login fragment destroyed");
    }
}
