package com.example.parkinson.network;

import com.google.firebase.database.FirebaseDatabase;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Singleton class that stores FirebaseDatabase instance.*/
@Singleton
public class DatabaseManager {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Inject
    public DatabaseManager() {
        database = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }
}