package com.shouxin.shouxin.database.Dao;

import com.shouxin.shouxin.DataModel.Word;

public interface ItemEntryDao {

    void add(Word word);
    void remove(String name);
    void update(Word word);
    Word findById(String name);

}
