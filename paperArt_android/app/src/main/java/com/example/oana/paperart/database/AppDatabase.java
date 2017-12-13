package com.example.oana.paperart.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.oana.paperart.Category;
import com.example.oana.paperart.PaperItem;
import com.example.oana.paperart.Rating;

@Database(entities = {Category.class, PaperItem.class, Rating.class}, version = 8)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CategoryDAO categoryDAO();
    public abstract PaperItemDAO paperItemDAO();
    public abstract RatingDAO ratingDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "paperArtDatabase")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}