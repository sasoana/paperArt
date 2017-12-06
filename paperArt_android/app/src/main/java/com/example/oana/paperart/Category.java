package com.example.oana.paperart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oana on 12/4/2017.
 */

public class Category implements Serializable {
    Integer id;
    String name;
    String description;
    String imageName;
    ArrayList<PaperItem> items;

    public Category(Integer id, String name, String description, String imageName, ArrayList<PaperItem> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.items = items;
    }

    public Category(Integer id, String name, String description, String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.items = new ArrayList<>();
    }

    public void addItem(PaperItem item) {
        this.items.add(item);
    }

    public void removeItem(PaperItem item) {
        this.items.remove(item);
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

    public void setItems(ArrayList<PaperItem> items) {
        this.items = items;
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

    public ArrayList<PaperItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        return id.equals(category.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
