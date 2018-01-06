package com.example.oana.paperart;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by oana on 11/7/2017.
 */

@IgnoreExtraProperties
public class PaperItem implements Serializable{

    int id;
    Integer categoryId;
    String name;
    String paperType;
    String color;
    Integer duration;
    Date createdAt;
    String uid;

    public PaperItem() {

    }

    public PaperItem(int id, Integer categoryId, String name, String paperType, String color, Integer duration, Date createdAt, String uid) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.paperType = paperType;
        this.color = color;
        this.duration = duration;
        this.createdAt = createdAt;
        this.uid = uid;
    }

    public PaperItem(int categoryId, String uid){
        Integer newId = 0;
        this.id = newId;
        this.categoryId = categoryId;
        this.name = "";
        this.paperType = "";
        this.color = "";
        this.duration = 0;
        this.createdAt = new Date();
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "PaperItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", paperType='" + paperType + '\'' +
                ", color='" + color + '\'' +
                ", duration=" + duration +
                ", createdAt=" + createdAt.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaperItem)) return false;

        PaperItem paperItem = (PaperItem) o;

        return id == paperItem.id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
