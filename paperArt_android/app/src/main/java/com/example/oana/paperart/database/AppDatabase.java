package com.example.oana.paperart.database;

/*@Database(entities = {Category.class, PaperItem.class, Rating.class}, version = 8)
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
}*/