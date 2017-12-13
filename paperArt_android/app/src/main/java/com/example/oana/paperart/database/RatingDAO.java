package com.example.oana.paperart.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.example.oana.paperart.Rating;

import java.util.List;

/**
 * Created by oana on 12/12/2017.
 */

@Dao
public interface RatingDAO {
    @Query("select * from ratings")
    List<Rating> getAll();

    @Query("select * from ratings where createdAt like :date")
    List<Rating> getByDate(String date);

    @Query("select * from ratings where id=:id")
    Rating getOne(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Rating rating);

    @Query("DELETE FROM ratings")
    void deleteAll();

    @Delete
    void delete(Rating rating);

    @Query("select createdAt, avg(value) from ratings where modelId=:id group by createdAt order by createdAt asc")
    Cursor getAverageGroupedByDayForItem(long id);
}
