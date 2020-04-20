package com.shouxin.shouxin.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.shouxin.shouxin.DataModel.Word;

import java.util.List;

@Dao
public interface WordDao {

    /**
     * 向数据库中插入数据
     * @param word
     */
    @Insert
    void insert(Word word);

    /**
     * 删除数据库中的全部数据
     */
    @Query("delete from WORD")
    void deleteAll();

    /**
     * 从数据库中获取数据
     * @return 数据库中的全部Word数据
     */
    @Query("select * from WORD")
    List<Word> getAllWords();

    /**
     * 更改数据库中的某个word值
     * @param word
     */
    @Update
    void updateWordCollectedStatus(Word word);

}
