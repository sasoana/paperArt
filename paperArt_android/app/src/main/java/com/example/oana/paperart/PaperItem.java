package com.example.oana.paperart;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oana on 11/7/2017.
 */

@IgnoreExtraProperties
public class PaperItem implements Serializable{

    String key;

    String uid;
    String author;
    String categoryId;
    String name;
    String paperType;
    String color;
    Integer duration;
    Date createdAt;

    public PaperItem() {

    }

    public PaperItem(String uid, String author, String categoryId, String name, String paperType,
                     String color, Integer duration, Date createdAt) {

        this.uid = uid;
        this.author = author;
        this.categoryId = categoryId;
        this.name = name;
        this.paperType = paperType;
        this.color = color;
        this.duration = duration;
        this.createdAt = createdAt;
        this.uid = uid;
    }

    public PaperItem(String categoryId, String uid){
        this.categoryId = categoryId;
        this.name = "";
        this.paperType = "";
        this.color = "";
        this.duration = 0;
        this.createdAt = new Date();
        this.uid = uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("categoryId", categoryId);
        result.put("name", name);
        result.put("paperType", paperType);
        result.put("color", color);
        result.put("duration", duration);
        result.put("createdAt", createdAt);

        return result;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "PaperItem{" +
                "key='" + key + '\'' +
                ", uid='" + uid + '\'' +
                ", author='" + author + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", paperType='" + paperType + '\'' +
                ", color='" + color + '\'' +
                ", duration=" + duration +
                ", createdAt=" + createdAt +
                '}';
    }
}
