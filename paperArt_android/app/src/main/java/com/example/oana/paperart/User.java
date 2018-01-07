package com.example.oana.paperart;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by oana on 1/5/2018.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String role;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

