package com.shouxin.shouxin.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import com.shouxin.shouxin.DataModel.Word;

import java.util.List;

@Dao
public interface WordDao {

    /**
     * 向数据库中插入数据
     * @param word 将要被插入到数据库中的词语
     */
    @Insert
    Completable insert(Word word);

    /**
     * 删除数据库中的全部数据
     */
    @Query("delete from WORD")
    Completable deleteAll();

    /**
     * 从数据库中获取数据
     * @return 数据库中的全部Word数据
     */
    @Query("select * from WORD")
    Maybe<List<Word>> getAllWords();

    /**
     * 更改数据库中的某个word值
     * @param word 即将呗更新的词语
     */
    @Update
    Completable updateWordCollectedStatus(Word word);

    /**
     * 获取数据库中被收藏的词语
     * @return 被用户收藏的词语
     */
    @Query("select * from WORD where collected = 1")
    Flowable<List<Word>> getFavoriteWords();

}
