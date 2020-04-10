package com.shouxin.shouxin.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "WORD")
public class Word implements Serializable {

    @NonNull
    @ColumnInfo(name = "category")
    public String category;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    public String name;
    @NonNull
    @ColumnInfo(name = "description")
    public String description;
    @NonNull
    @ColumnInfo(name = "pictureUrl")
    public String pictureUrl;

    public Word(){}

    @Ignore
    public Word(String name, String description, String pictureUrl){
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    @Ignore
    public Word(String category, String name, String description, String pictureUrl){
        this.category = category;
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