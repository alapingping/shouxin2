package com.shouxin.shouxin.database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.database.Dao.WordDao;
import com.shouxin.shouxin.database.Room.WordRoomDatabase;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private List<Word> mAllWords;

    public WordRepository(Application application){
        WordRoomDatabase database = WordRoomDatabase.getDatabase(application);
        mWordDao = database.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    public List<Word> getAllWords(){
        return mAllWords;
    }

    public void insert(Word word){
        new insertAsyncTask(mWordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao wordDao){
            this.mAsyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            mAsyncTaskDao.insert(words[0]);
            return null;
        }
    }

}
