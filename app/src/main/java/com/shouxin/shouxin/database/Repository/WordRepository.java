package com.shouxin.shouxin.database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import io.reactivex.Completable;
import io.reactivex.Maybe;

import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.database.Dao.WordDao;
import com.shouxin.shouxin.database.Room.WordRoomDatabase;

import java.util.List;

public class WordRepository {

    private static WordDao sWordDao;
    private static volatile WordRepository sRepository;

    private WordRepository(Application application){
        WordRoomDatabase database = WordRoomDatabase.getDatabase(application);
        sWordDao = database.wordDao();
    }

    public static WordRepository getWordRepository() {
        return sRepository;
    }

    public static void init(Application application) {
        sRepository = new WordRepository(application);
    }

    public Maybe<List<Word>> getAllWords(){
        return sWordDao.getAllWords();
    }

    public Completable deleteAll() {
        return sWordDao.deleteAll();
    }

    public Completable insert(Word word) {
        return sWordDao.insert(word);
    }

    public Completable insert(List<Word> words) {
        return sWordDao.insert(words);
    }

    public Completable update(Word word) {
        return sWordDao.updateWordCollectedStatus(word);
    }
}
