package com.example.oana.paperart;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oana on 12/10/2017.
 */

public class CategoryDetalis extends AppCompatActivity {

    private static final String TAG = "CategoryDetails";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_details);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //show currently logged in user and role
        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setSubtitle(user.getUsername() + ": " + user.getRole());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        Spinner spinner = (Spinner) findViewById(R.id.imageSpinner);
        List<String> list = new ArrayList<String>();
        list.add("kusudama");
        list.add("pure");
        list.add("modular");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Button saveButton = (Button) findViewById(R.id.button_save_cat);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addCategory();
            }
        });
    }

    private void addCategory() {
        TextView nameView = (TextView) findViewById(R.id.edit_cat_name);
        TextView descriptionView = (TextView) findViewById(R.id.edit_description);
        Spinner spinner = (Spinner) findViewById(R.id.imageSpinner);

        final String name = nameView.getText().toString();
        final String desc = descriptionView.getText().toString();
        final String image = spinner.getSelectedItem().toString();

        // Name is required
        if (TextUtils.isEmpty(name)) {
            nameView.setError(REQUIRED);
            return;
        }

        // Description is required
        if (TextUtils.isEmpty(desc)) {
            descriptionView.setError(REQUIRED);
            return;
        }

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(CategoryDetalis.this, "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeCategory(userId, user.username, name, desc, image);
                        }

                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void writeCategory(String uid, String username, String name, String description, String image) {
        String key = mDatabase.child("categories").push().getKey();
        Category category = new Category(uid, username, name, description, image);
        Map<String, Object> postValues = category.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/categories/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(CategoryDetalis.this, "The category has been added.", Toast.LENGTH_SHORT).show();
    }
}
