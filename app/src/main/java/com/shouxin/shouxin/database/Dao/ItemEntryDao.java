package com.shouxin.shouxin.database.Dao;

import android.content.ClipData;

import com.shouxin.shouxin.DataModel.ItemEntry;

public interface ItemEntryDao {

    void add(ItemEntry itemEntry);
    void remove(ItemEntry itemEntry);
    void update(ItemEntry itemEntry);
    ItemEntry findById(ItemEntry itemEntry);

}
