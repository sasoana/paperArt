package com.example.oana.paperart.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by oana on 12/12/2017.
 */

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long longDate) {
        return longDate == null ? null : new Date(longDate);
    }

    @TypeConverter
    public static Long toString(Date date) {
        return date == null ? null : date.getTime();
    }
}

