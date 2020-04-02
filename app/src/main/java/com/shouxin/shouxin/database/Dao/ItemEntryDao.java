package com.shouxin.shouxin.database.Dao;

import android.content.ClipData;

import com.shouxin.shouxin.DataModel.ItemEntry;

public interface ItemEntryDao {

    void add(ItemEntry itemEntry);
    void remove(String name);
    void update(ItemEntry itemEntry);
    ItemEntry findById(String name);

}
