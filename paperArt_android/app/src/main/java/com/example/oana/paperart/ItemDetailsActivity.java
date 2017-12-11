package com.example.oana.paperart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oana.paperart.database.AppDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by oana on 11/7/2017.
 */

public class ItemDetailsActivity extends FragmentActivity {
    private AppDatabase mdb;
    private static TextView editText_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        mdb = AppDatabase.getAppDatabase(getApplicationContext());

        final PaperItem item = (PaperItem) getIntent().getExtras().getSerializable("item");

        final EditText editText_name = (EditText) findViewById(R.id.edit_name);
        editText_name.setText(item.getName());

        final EditText editText_paper = (EditText) findViewById(R.id.edit_paper);
        editText_paper.setText(item.getPaperType());

        final EditText editText_color = (EditText) findViewById(R.id.edit_color);
        editText_color.setText(item.getColor());

        final EditText editText_duration = (EditText) findViewById(R.id.edit_duration);
        editText_duration.setText(item.getDuration().toString());

        editText_date = (TextView) findViewById(R.id.edit_date);
        final DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        editText_date.setText(dateFormat.format(item.getCreatedAt()));

        Button saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PaperItem newItem = null;
                try {
                    newItem = new PaperItem(item.getId(), item.getCategoryId(),
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
