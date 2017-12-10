package com.example.oana.paperart.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.oana.paperart.Category;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Query("select * from categories")
    List<CategoryWithItems> getAll();

    @Query("select * from categories where id=:id")
    CategoryWithItems getOne(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addCategory(Category category);

    @Query("DELETE FROM categories")
    void deleteAll();

    @Delete
    void delete(Category category);
}