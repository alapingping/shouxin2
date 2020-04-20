package com.shouxin.shouxin.database.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.database.Dao.WordDao;

@Database(entities = {Word.class}, version = 2, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

    private static volatile WordRoomDatabase INSTANCE;

    public static WordRoomDatabase getDatabase(final Context context){

        if(INSTANCE == null){
            synchronized (WordRoomDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateAsync(INSTANCE).execute();
                }

            };

    private static class PopulateAsync extends AsyncTask<Void,Void,Void> {

        private WordDao mWordDao;

        public PopulateAsync(WordRoomDatabase db){
            this.mWordDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

//            mWordDao.deleteAll();
//            Word word = new Word("称谓", "我们", "哈哈哈", "http");
//            mWordDao.insert(word);
            return null;
        }
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE WORD "
                    + " ADD COLUMN collected INTEGER");
        }
    };

}
