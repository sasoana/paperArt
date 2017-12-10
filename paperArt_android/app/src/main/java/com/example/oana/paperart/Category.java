package com.example.oana.paperart;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by oana on 12/4/2017.
 */

@Entity(tableName = "categories")
public class Category implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String description;
    String imageName;

    public Category(int id, String name, String description, String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        return id == category.id;

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
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
