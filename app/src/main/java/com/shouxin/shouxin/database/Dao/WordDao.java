package com.shouxin.shouxin.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.shouxin.shouxin.DataModel.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(Word word);

    @Query("delete from WORD")
    void deleteAll();

    @Query("select * from WORD")
    List<Word> getAllWords();

}
