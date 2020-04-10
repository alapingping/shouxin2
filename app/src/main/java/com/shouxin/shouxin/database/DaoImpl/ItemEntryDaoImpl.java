package com.shouxin.shouxin.database.DaoImpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.database.Dao.ItemEntryDao;
import com.shouxin.shouxin.database.util.DBHelper;

public class ItemEntryDaoImpl implements ItemEntryDao {

    private DBHelper helper;

    public ItemEntryDaoImpl(Context context){
        helper = new DBHelper(context);
    }


    @Override
    public void add(Word word) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", word.getName());
        values.put("description", word.getDescription());
        values.put("pictureUrl", word.getPictureUrl());
        db.insert("ItemEntry", null, values);
        db.close();

    }

    @Override
    public void remove(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("ItemEntry", "name=?", new String[]{name});
        db.close();
    }

    @Override
    public void update(Word word) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", word.getName());
        values.put("description", word.getDescription());
        values.put("pictureUrl", word.getPictureUrl());
        db.update("ItemEntry", values, "name=?", new String[]{word.getName()});
        db.close();
    }

    @Override
    public Word findById(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Word item = null;
        Cursor cursor = db.query("ItemEntry", null, "name=?", new String[]{name}, null, null, null);
        if(cursor.moveToNext()){
            item = new Word();
            item.setName(cursor.getString(1));
            item.setDescription(cursor.getString(2));
            item.setPictureUrl(cursor.getString(3));
        }
        db.close();
        return item;
    }

    public int getCount(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from ItemEntry", null);
        int count = -1;
        if( cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        db.close();
        return count;
    }

}
