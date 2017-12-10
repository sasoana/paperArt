package com.example.oana.paperart;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oana on 11/7/2017.
 */

@Entity(tableName = "items")
public class PaperItem implements Serializable{
    @PrimaryKey(autoGenerate = true)
    int id;

    Integer categoryId;
    String name;
    String paperType;
    String color;
    Integer duration;
    @Ignore
    List<Rating> ratings;

    public PaperItem(int id, Integer categoryId, String name, String paperType, String color, Integer duration) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.paperType = paperType;
        this.color = color;
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    @Ignore
    public PaperItem(int categoryId){
        Integer newId = 0;
        this.id = newId;
        this.categoryId = categoryId;
        this.name = "";
        this.paperType = "";
        this.color = "";
        this.duration = 0;
        this.ratings = new ArrayList<>();
    }
    @Ignore
    public PaperItem(Integer id, Integer categoryId, String name, String paperType, String color, Integer duration, List<Rating> ratings) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.paperType = paperType;
        this.color = color;
        this.duration = duration;
        this.ratings = ratings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }

    public void removeRating(Rating rating) {
        this.ratings.remove(rating);
    }

    @Override
    public String toString() {
        return "PaperItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", paperType='" + paperType + '\'' +
                ", color='" + color + '\'' +
                ", duration=" + duration +
                ", ratings=" + ratings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaperItem)) return false;

        PaperItem paperItem = (PaperItem) o;

        return id == paperItem.id;

    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
