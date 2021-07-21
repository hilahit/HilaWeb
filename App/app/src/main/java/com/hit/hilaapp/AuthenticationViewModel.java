package com.hit.hilaapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthenticationViewModel extends ViewModel
        implements FirebaseRepository.FireBaseRepositoryListener {

    private final FirebaseRepository mFirebaseRepository;

    private final MutableLiveData<FirebaseUser> mUser = new MutableLiveData<>();

    private AuthListener listener;

    public interface AuthListener {


    }

    public void setListener(AuthListener listener) {
        this.listener = listener;
    }

    @Inject
    public AuthenticationViewModel(FirebaseRepository repository) {
        this.mFirebaseRepository = repository;

        mFirebaseRepository.setListener(this);
    }

    public LiveData<FirebaseUser> getUser() {
        return mUser;
    }


    public void signIn(String email, String password) {
        mFirebaseRepository.emailPasswordSignIn(email, password);
    }


    @Override
    public void OnSignInSuccessful() {
        mUser.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }

//    public void getUserByUID(String UID){
//        mFirebaseRepository.getRealtimeUserByUID(UID);
//    }
//
//    public void getRealtimeUserFromDB() {
//        mFirebaseRepository.getRealtimeUser();
//    }


    public void onSignOut() {
        mUser.setValue(null);
    }
}
