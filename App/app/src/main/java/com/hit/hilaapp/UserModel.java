package com.hit.hilaapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class UserModel implements Serializable {

    private String email;
    private String username;


    public UserModel() {
    }

    public UserModel(String email,
                     String username,
                     List<String> answers,
                     String bio,
                     String profileImage,
                     Map<String, String> chats) {
        this.email = email;
        this.username = username;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
