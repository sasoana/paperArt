package com.example.oana.paperart.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.oana.paperart.PaperItem;

import java.util.List;

/**
 * Created by oana on 12/6/2017.
 */

@Dao
public interface PaperItemDAO {
    @Query("select * from items")
    List<ItemWithRatings> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PaperItem> items);

    @Update
    void update(PaperItem item);

    @Query("select * from items where id=:id")
    ItemWithRatings findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addItem(PaperItem item);

    @Delete
    void delete(PaperItem item);

    @Query("delete from items")
    void deleteAll();

}
