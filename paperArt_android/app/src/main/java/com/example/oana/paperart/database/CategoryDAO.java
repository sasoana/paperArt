package com.example.oana.paperart.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.oana.paperart.Category;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Query("select * from categories")
    List<Category> getAll();

    @Insert
    void addCategory(Category category);

    @Query("DELETE FROM categories")
    void deleteAll();
}