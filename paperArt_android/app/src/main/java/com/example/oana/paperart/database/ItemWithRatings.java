package com.example.oana.paperart.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.example.oana.paperart.PaperItem;
import com.example.oana.paperart.Rating;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oana on 12/12/2017.
 */

public class ItemWithRatings implements Serializable{

    @Embedded
    public PaperItem item;

    @Relation(parentColumn = "id", entityColumn = "modelId", entity = Rating.class)
    public List<Rating> ratings;

    @Ignore
    public ItemWithRatings(PaperItem item, List<Rating> ratings) {
        this.item = item;
        this.ratings = ratings;
    }

    public ItemWithRatings(PaperItem item) {
        this.item = item;
        this.ratings = new ArrayList<>();
    }

    public PaperItem getItem() {
        return item;
    }

    public void setItem(PaperItem item) {
        this.item = item;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemWithRatings)) return false;

        ItemWithRatings that = (ItemWithRatings) o;

        return item.equals(that.item);

    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }

    @Override
    public String toString() {
        return "ItemWithRatings{" +
                "item=" + item +
                ", ratings=" + ratings +
                '}';
    }
}
