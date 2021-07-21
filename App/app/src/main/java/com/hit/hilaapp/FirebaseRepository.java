package com.hit.hilaapp;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseRepository {

    private final DatabaseReference mDatabase;
    private final FirebaseAuth mFirebaseAuth;

    private FireBaseRepositoryListener listener;

    public interface FireBaseRepositoryListener {

        void OnSignInSuccessful();

    }

    @Inject
    public FirebaseRepository(DatabaseReference db, FirebaseAuth auth) {
        mDatabase = db;
        mFirebaseAuth = auth;
    }

    public void emailPasswordSignIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        if (listener != null) {
                            listener.OnSignInSuccessful();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    public void setListener(FireBaseRepositoryListener listener) {
        this.listener = listener;
    }

//    public void getRealtimeUser() {
//        mDatabase.child("users").child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserModel user = snapshot.getValue(UserModel.class);
//
//                if (listener != null) {
//                    listener.OnRealtimeUserReceived(user);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    public void getRealtimeUserByUID(String UID) {
//        mDatabase.child("users").child(UID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserModel seller = snapshot.getValue(UserModel.class);
//
//                if (listener != null) {
//                    listener.OnRealtimeSellerReceived(seller);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
