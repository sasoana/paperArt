package com.example.oana.paperart.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.example.oana.paperart.Category;
import com.example.oana.paperart.PaperItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oana on 12/8/2017.
 */

public class CategoryWithItems implements Serializable{
    @Embedded
    public Category category;

    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = PaperItem.class)
    public List<PaperItem> items;

    @Ignore
    public CategoryWithItems(Category category, ArrayList<PaperItem> items) {
        this.category = category;
        this.items = items;
    }

    public CategoryWithItems() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryWithItems)) return false;

        CategoryWithItems that = (CategoryWithItems) o;

        return category.equals(that.category);

    }

    @Override
    public int hashCode() {
        return category.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryWithItems{" +
                "category=" + category.toString() +
                ", items=" + items.toString() +
                '}';
    }
}
