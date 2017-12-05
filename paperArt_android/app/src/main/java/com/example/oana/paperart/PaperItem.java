package com.example.oana.paperart;

import java.io.Serializable;

/**
 * Created by oana on 11/7/2017.
 */

public class PaperItem implements Serializable{
    Integer id;
    String name;
    String paperType;
    String color;
    Integer duration;

    public PaperItem(Integer id, String name, String paperType, String color, Integer duration) {
        this.id = id;
        this.name = name;
        this.paperType = paperType;
        this.color = color;
        this.duration = duration;
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

    @Override
    public String toString() {
        return "PaperItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", paperType='" + paperType + '\'' +
                ", color='" + color + '\'' +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaperItem)) return false;

        PaperItem paperItem = (PaperItem) o;

        return id != null ? id.equals(paperItem.id) : paperItem.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
