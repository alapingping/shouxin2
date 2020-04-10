package com.shouxin.shouxin.DataModel;

import java.io.Serializable;

public class Word implements Serializable {


    public String category;
    public String name;
    public String description;
    public String pictureUrl;

    public Word(){}

    public Word(String name, String description, String pictureUrl){
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}