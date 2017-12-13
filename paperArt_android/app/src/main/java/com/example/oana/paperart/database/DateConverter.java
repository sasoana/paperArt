package com.example.oana.paperart.database;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by oana on 12/12/2017.
 */

public class DateConverter {
    static DateFormat output = new SimpleDateFormat("MMMM dd, yyyy");
    @TypeConverter
    public static Date toDate(String longDate) {

        try {
            return longDate == null ? null : output.parse(longDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String toString(Date date) {

        return date == null ? null : output.format(date);
    }
}

