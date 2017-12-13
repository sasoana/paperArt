package com.example.oana.paperart;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.oana.paperart.database.DateConverter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by oana on 12/4/2017.
 */

@Entity(tableName = "ratings")
public class Rating implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    Integer modelId;
    Integer value;
    String message;
    @TypeConverters({DateConverter.class})
    Date createdAt;

    @Ignore
    public Rating(int id, Integer modelId, Integer value, String message, Date date) {
        this.id = id;
        this.modelId = modelId;
        this.value = value;
        this.message = message;
        this.createdAt = date;
    }

    public Rating() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;

        Rating rating = (Rating) o;

        return id== rating.id;

    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", modelId=" + modelId +
                ", value=" + value +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt.toString() +
                '}';
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
