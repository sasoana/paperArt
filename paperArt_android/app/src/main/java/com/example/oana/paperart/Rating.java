package com.example.oana.paperart;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oana on 12/4/2017.
 */

@IgnoreExtraProperties
public class Rating implements Serializable {

    String key;

    String uid;
    String author;
    String modelId;
    Integer value;
    String message;
    Date createdAt;

    public Rating() {}

    public Rating(String uid, String author, String modelId, Integer value, String message, Date createdAt) {
        this.uid = uid;
        this.author = author;
        this.modelId = modelId;
        this.value = value;
        this.message = message;
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

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "key='" + key + '\'' +
                ", uid='" + uid + '\'' +
                ", author='" + author + '\'' +
                ", modelId='" + modelId + '\'' +
                ", value=" + value +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("modelId", modelId);
        result.put("value", value);
        result.put("message", message);
        result.put("createdAt", createdAt);

        return result;
    }
}
