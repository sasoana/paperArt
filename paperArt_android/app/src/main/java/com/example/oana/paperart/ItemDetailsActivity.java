package com.example.oana.paperart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by oana on 11/7/2017.
 */

public class ItemDetailsActivity extends AppCompatActivity {

    private static TextView editText_date;
    private PaperItem item;
    private ArrayList<Rating> ratings = new ArrayList<>();

    private static final String TAG = "ItemDetails";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    final DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final Button showChart = (Button) findViewById(R.id.showChartButton);
        showChart.setVisibility(View.GONE);

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

        item = (PaperItem) getIntent().getExtras().getSerializable("item");

        //show up-to-date rating average
        if(getIntent().getExtras().getString("type").equals("update")) {
            mDatabase.child("item-ratings").child(item.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ratings.clear();
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        Rating rating = categorySnapshot.getValue(Rating.class);
                        rating.setKey(categorySnapshot.getKey());
                        ratings.add(rating);
                        computeAverage();
                        showChart.setVisibility(View.VISIBLE);
                        Log.wtf("ratings updated", "category: " + rating.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //avoid focus on edit text
        TextView name = (TextView) findViewById(R.id.tv_name);
        name.getParent().requestChildFocus(name, name);

        final EditText editText_name = (EditText) findViewById(R.id.edit_name);
        editText_name.setText(item.getName());

        final EditText editText_paper = (EditText) findViewById(R.id.edit_paper);
        editText_paper.setText(item.getPaperType());

        final EditText editText_color = (EditText) findViewById(R.id.edit_color);
        editText_color.setText(item.getColor());

        final EditText editText_duration = (EditText) findViewById(R.id.edit_duration);
        editText_duration.setText(item.getDuration().toString());

        editText_date = (TextView) findViewById(R.id.edit_date);
        editText_date.setText(dateFormat.format(item.getCreatedAt()));

        //save details button
        Button saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();
            }
        });
        //hide update button if not author
        if (!item.getUid().equals(userId)) {
            editText_color.setEnabled(false);
            editText_duration.setEnabled(false);
            editText_name.setEnabled(false);
            editText_paper.setEnabled(false);
            Button changeDate = (Button) findViewById(R.id.change_date);
            changeDate.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }

        //add rating button
        Button addRating = (Button) findViewById(R.id.addRating);
        addRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ItemDetailsActivity.this);
                dialog.setContentView(R.layout.rating_details);
                dialog.setTitle("Give rating for " + item.getName());

                Spinner spinner = (Spinner) dialog.findViewById(R.id.valueSpinner);
                List<Integer> list = new ArrayList<Integer>();
                list.add(1); list.add(2); list.add(3); list.add(4); list.add(5);
                ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(ItemDetailsActivity.this,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);

                Button save = (Button) dialog.findViewById(R.id.saveRating);
                save.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText message = (EditText) dialog.findViewById(R.id.edit_rating_message);
                        final String msg = message.getText().toString();
                        final Spinner value = (Spinner) dialog.findViewById(R.id.valueSpinner);
                        final Integer val = Integer.parseInt(value.getSelectedItem().toString());

                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        User user = dataSnapshot.getValue(User.class);

                                        if (user == null) {
                                            // User is null, error out
                                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                                            Toast.makeText(ItemDetailsActivity.this, "Error: could not fetch user.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            String key = mDatabase.child("ratings").push().getKey();
                                            Rating rating = new Rating(userId, user.getUsername(), item.getKey(),
                                                    val, msg, new Date());
                                            rating.setKey(key);
                                            Map<String, Object> postValues = rating.toMap();

                                            Map<String, Object> childUpdates = new HashMap<>();
                                            childUpdates.put("/ratings/" + key, postValues);
                                            childUpdates.put("/item-ratings/" + item.getKey() + "/" + key, postValues);
                                            childUpdates.put("/user-ratings/" + userId + "/" + key, postValues);
                                            mDatabase.updateChildren(childUpdates);
                                            ratings.add(rating);
                                            Toast.makeText(ItemDetailsActivity.this, "The rating has been added.", Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent();
                                        setResult(Activity.RESULT_OK, intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                    }
                                });

                        //computeAverage();
                        dialog.dismiss();
                    }
                });
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        //show chart button
        Button showChart1 = (Button) findViewById(R.id.showChartButton);
        showChart1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ItemDetailsActivity.this);
                dialog.setContentView(R.layout.rating_history);
                dialog.setTitle("Rating history for " + item.getName());

                final GraphView graph = (GraphView) dialog.findViewById(R.id.graph);

                final ArrayList<Rating> ratings2 = new ArrayList();
                mDatabase.child("item-ratings").child(item.getKey()).orderByChild("createdAt").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ratings2.clear();
                        final GraphView graph = (GraphView) dialog.findViewById(R.id.graph);
                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                            Rating rating = categorySnapshot.getValue(Rating.class);
                            rating.setKey(categorySnapshot.getKey());
                            ratings2.add(rating);
                        }
                        /*if (ratings2.size() == 0) {
                            graph.setVisibility(View.GONE);
                            TextView noRatings = (TextView) dialog.findViewById(R.id.noRatings);
                            noRatings.setText("There are no ratings for this item.");
                            noRatings.setVisibility(View.VISIBLE);
                            dialog.show();
                            return;
                        }*/
                        ArrayList<DataPoint> data = new ArrayList<DataPoint>();
                        Date min = new Date();
                        Date max = new Date(1);
                        Rating prev = ratings2.get(0);
                        int j = 0;
                        int pos = 0;
                        for (int i = 1; i <=  ratings2.size(); i++) {
                            String datePrev = dateFormat.format(prev.getCreatedAt());
                            String currentDate = "";
                            if (i == ratings2.size()) {
                                currentDate = dateFormat.format(ratings2.get(i-1).getCreatedAt());
                            }
                            else {
                                currentDate = dateFormat.format(ratings2.get(i).getCreatedAt());
                            }
                            if (!datePrev.equals(currentDate) || (pos == (ratings2.size()-1))) {
                                Double average = 0.0;
                                Integer sum = 0;
                                for (j = 0; j <= pos; j++) {
                                    sum += ratings2.get(j).getValue();
                                }
                                average = sum / (1.0 * (pos + 1));
                                Date date = prev.getCreatedAt();
                                if (date.compareTo(min) < 0) min = date;
                                if (date.compareTo(max) > 0) max = date;
                                data.add(new DataPoint(date, average));
                                Log.wtf("graphdata", date.toString() + " " + average);

                                if (i < ratings2.size()) {
                                    prev = ratings2.get(i);
                                }
                            } else {
                                pos = i;
                            }
                        }
                        if ((j-1) != ratings2.size()-1) {
                            Integer sum = 0, count = 0;
                            Double average = 0.0;
                            Date currentDate = ratings2.get(ratings2.size()-1).getCreatedAt();
                            for (int i = 0; i < ratings2.size(); i++) {
                                sum += ratings2.get(i).getValue();
                                count++;
                            }
                            average = sum / (1.0 * count);
                            Date date = ratings2.get(j).getCreatedAt();
                            if (date.compareTo(min) < 0) min = date;
                            if (date.compareTo(max) > 0) max = date;
                            data.add(new DataPoint(date, average));
                            Log.wtf("graphdata", date.toString() + " " + average);
                        }

                        DataPoint[] finalData = new DataPoint[data.size()];
                        int i = 0;
                        for(DataPoint point : data) {
                            finalData[i] = point;
                            i++;
                        }
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(finalData);
                        graph.addSeries(series);

                        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ItemDetailsActivity.this));
                        graph.getGridLabelRenderer().setNumHorizontalLabels(finalData.length);

                        // set manual x bounds to have nice steps
                        graph.getViewport().setMinX(min.getTime());
                        graph.getViewport().setMaxX(max.getTime());
                        graph.getViewport().setXAxisBoundsManual(true);

                        // as we use dates as labels, the human rounding to nice readable numbers
                        // is not necessary
                        graph.getGridLabelRenderer().setHumanRounding(false);

                        dialog.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //don't show the button for the chart and add rating if we add new item
        String type = getIntent().getExtras().getString("type");
        if (type.equals("add")) {
            showChart.setVisibility(View.GONE);
            addRating.setVisibility(View.GONE);
        }
    }

    public void addItem() {
        EditText editText_name = (EditText) findViewById(R.id.edit_name);
        EditText editText_paper = (EditText) findViewById(R.id.edit_paper);
        EditText editText_color = (EditText) findViewById(R.id.edit_color);
        EditText editText_duration = (EditText) findViewById(R.id.edit_duration);
        editText_date = (TextView) findViewById(R.id.edit_date);

        try {
        final String name = editText_name.getText().toString();
        final String paperType = editText_paper.getText().toString();
        final String color = editText_color.getText().toString();
        final Integer duration = Integer.parseInt(editText_duration.getText().toString());
        final Date createdAt = dateFormat.parse(editText_date.getText().toString());

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(ItemDetailsActivity.this, "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (getIntent().getExtras().getString("type").equals("add")) {
                                writeItem(userId, user.getUsername(), item.getCategoryId(), name,
                                        paperType, color, duration, createdAt);
                            }
                            else {
                                updateItem(userId, user.getUsername(), item.getCategoryId(), name,
                                        paperType, color, duration, createdAt);
                            }
                        }
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void writeItem(String uid, String author, String categoryId, String name,
                          String paperType, String color, Integer duration, Date createdAt) {

        String key = mDatabase.child("items").push().getKey();
        PaperItem item = new PaperItem(uid, author, categoryId, name, paperType, color, duration, createdAt);
        item.setKey(key);
        Log.wtf("newItem", item.toString());
        Map<String, Object> postValues = item.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/items/" + key, postValues);
        childUpdates.put("/category-items/" + categoryId + "/" + key, postValues);
        childUpdates.put("/user-items/" + uid + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(ItemDetailsActivity.this, "The item has been added.", Toast.LENGTH_SHORT).show();
    }

    public void updateItem(String uid, String author, String categoryId, String name,
                          String paperType, String color, Integer duration, Date createdAt) {
        String key = item.getKey();
        PaperItem item = new PaperItem(uid, author, categoryId, name, paperType, color, duration, createdAt);
        Log.wtf("newItem", item.toString());
        Map<String, Object> postValues = item.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/items/" + key, postValues);
        childUpdates.put("/category-items/" + categoryId + "/" + key, postValues);
        childUpdates.put("/user-items/" + uid + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(ItemDetailsActivity.this, "The item has been updated.", Toast.LENGTH_SHORT).show();
    }

    public void computeAverage() {
        //compute average rating
        TextView averageRating = (TextView) findViewById(R.id.tv_rating);
        DecimalFormat df2 = new DecimalFormat(".##");

        final Double average;
        Integer sum = 0;
        if(!ratings.isEmpty()) {
            for (Rating rating : ratings) {
                sum += rating.getValue();
            }
            average =  sum.doubleValue() / ratings.size();
        }
        else average = sum*1.0;
        averageRating.setText("Current average rating is " + df2.format(average).toString());
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            Date date = c.getTime();
            DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            editText_date.setText(dateFormat.format(date));
        }
    }
}