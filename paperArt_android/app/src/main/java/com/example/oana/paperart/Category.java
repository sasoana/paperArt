package com.example.oana.paperart;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oana on 12/4/2017.
 */

@IgnoreExtraProperties
public class Category {
    public String uid;
    public String author;
    String name;
    String description;
    String imageName;

    public Category() {

    }

    public Category(String uid, String author, String name, String description, String imageName) {
        this.uid = uid;
        this.author = author;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
    }

    // [START category_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("name", name);
        result.put("description", description);
        result.put("imageName", imageName);

        return result;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "uid='" + uid + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
    // [END category_to_map]

}
