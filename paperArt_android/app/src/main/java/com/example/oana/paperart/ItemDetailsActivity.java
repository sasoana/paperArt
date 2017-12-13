package com.example.oana.paperart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oana.paperart.database.AppDatabase;
import com.example.oana.paperart.database.ItemWithRatings;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by oana on 11/7/2017.
 */

public class ItemDetailsActivity extends FragmentActivity {
    private AppDatabase mdb;
    private static TextView editText_date;
    private ItemWithRatings item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        mdb = AppDatabase.getAppDatabase(getApplicationContext());

        item = (ItemWithRatings) getIntent().getExtras().getSerializable("item");

        //avoid focus on edit text
        TextView name = (TextView) findViewById(R.id.tv_name);
        name.getParent().requestChildFocus(name,name);

        final EditText editText_name = (EditText) findViewById(R.id.edit_name);
        editText_name.setText(item.item.getName());

        final EditText editText_paper = (EditText) findViewById(R.id.edit_paper);
        editText_paper.setText(item.item.getPaperType());

        final EditText editText_color = (EditText) findViewById(R.id.edit_color);
        editText_color.setText(item.item.getColor());

        final EditText editText_duration = (EditText) findViewById(R.id.edit_duration);
        editText_duration.setText(item.item.getDuration().toString());

        editText_date = (TextView) findViewById(R.id.edit_date);
        final DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        editText_date.setText(dateFormat.format(item.item.getCreatedAt()));

        computeAverage();

        //save details button
        Button saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PaperItem newItem = null;
                try {
                    newItem = new PaperItem(item.item.getId(), item.item.getCategoryId(),
                            editText_name.getText().toString(),
                            editText_paper.getText().toString(),
                            editText_color.getText().toString(),
                            Integer.parseInt(editText_duration.getText().toString()),
                            dateFormat.parse(editText_date.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newItem", newItem);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        //add rating button
        Button addRating = (Button) findViewById(R.id.addRating);
        addRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ItemDetailsActivity.this);
                dialog.setContentView(R.layout.rating_details);
                dialog.setTitle("Give rating for " + item.getItem().getName());

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
                        String msg = message.getText().toString();
                        Spinner value = (Spinner) dialog.findViewById(R.id.valueSpinner);
                        String val = value.getSelectedItem().toString();
                        Integer newId = 0;
                        Rating newRating = new Rating(newId, item.item.getId(), Integer.parseInt(val), msg, new Date());
                        mdb.ratingDAO().add(newRating);
                        Toast.makeText(ItemDetailsActivity.this, "The rating has been added.", Toast.LENGTH_SHORT).show();
                        item = mdb.paperItemDAO().findById(item.item.getId());
                        computeAverage();
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
        Button showChart = (Button) findViewById(R.id.showChartButton);
        showChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Dialog dialog = new Dialog(ItemDetailsActivity.this);
                dialog.setContentView(R.layout.rating_history);
                dialog.setTitle("Rating history for " + item.getItem().getName());

                GraphView graph = (GraphView) dialog.findViewById(R.id.graph);
                Cursor averageCursor = mdb.ratingDAO().getAverageGroupedByDayForItem(item.item.getId());

                if (averageCursor.getCount() == 0) {
                    graph.setVisibility(View.GONE);
                    TextView noRatings = (TextView) dialog.findViewById(R.id.noRatings);
                    noRatings.setText("There are no ratings for this item.");
                    noRatings.setVisibility(View.VISIBLE);
                }
                DataPoint[] data = new DataPoint[averageCursor.getCount()];
                int i = 0;
                Log.wtf("xxx", Integer.toString(averageCursor.getCount()));
                Date min = new Date();
                Date max = new Date(1);
                while (averageCursor.moveToNext()) {
                    if (i == 0) averageCursor.moveToFirst();
                    DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                    Date date = null;
                    try {
                        Log.wtf("xxx", averageCursor.getString(0));
                        date = format.parse(averageCursor.getString(0));
                        if (date.compareTo(min) < 0) min = date;
                        if (date.compareTo(max) > 0) max = date;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Double d = averageCursor.getDouble(1);
                    Log.wtf("xxx", d.toString());
                    data[i] = new DataPoint(date, averageCursor.getDouble(1));
                    i++;
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
                graph.addSeries(series);

                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ItemDetailsActivity.this));
                graph.getGridLabelRenderer().setNumHorizontalLabels(averageCursor.getCount());

                // set manual x bounds to have nice steps
                graph.getViewport().setMinX(min.getTime());
                graph.getViewport().setMaxX(max.getTime());
                graph.getViewport().setXAxisBoundsManual(true);

                // as we use dates as labels, the human rounding to nice readable numbers
                // is not necessary
                graph.getGridLabelRenderer().setHumanRounding(false);

                dialog.show();
            }
        });


        //don't show the button for the chart if we add new item
        String type = getIntent().getExtras().getString("type");
        if (type.equals("add")) {
            showChart.setVisibility(View.GONE);
            addRating.setVisibility(View.GONE);
        }
    }

    public void computeAverage() {
        //compute average rating
        TextView averageRating = (TextView) findViewById(R.id.tv_rating);

        final Double average;
        Integer sum = 0;
        Log.wtf("xxx", item.ratings.toString());
        if(!item.ratings.isEmpty()) {
            for (Rating rating : item.ratings) {
                sum += rating.getValue();
            }
            average =  sum.doubleValue() / item.ratings.size();
        }
        else average = sum*1.0;
        averageRating.setText("Current average rating is " + average.toString());
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