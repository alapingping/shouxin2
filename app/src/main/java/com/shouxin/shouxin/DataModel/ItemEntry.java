package com.shouxin.shouxin.DataModel;

import java.io.Serializable;
import java.net.URL;

public class ItemEntry extends Entry implements Serializable {

    public ItemEntry(){}

    public ItemEntry(String name, String description, String pictureUrl){
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    private String name;

    private String description;

    private String pictureUrl;

    @Override
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