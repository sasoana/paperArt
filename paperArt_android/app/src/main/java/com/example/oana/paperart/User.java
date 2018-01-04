package com.example.oana.paperart;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by oana on 1/5/2018.
 */

// [START blog_user_class]
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

}
// [END blog_user_class]
