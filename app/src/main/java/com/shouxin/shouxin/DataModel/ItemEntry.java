package com.shouxin.shouxin.DataModel;

import java.io.Serializable;

public class ItemEntry implements Serializable {

    public ItemEntry(){}

    public ItemEntry(String name, String description, String pictureUrl){
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    private String name;

    private String description;

    private String pictureUrl;

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