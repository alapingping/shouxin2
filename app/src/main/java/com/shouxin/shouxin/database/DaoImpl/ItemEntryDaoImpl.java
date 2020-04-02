package com.shouxin.shouxin.database.DaoImpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shouxin.shouxin.DataModel.ItemEntry;
import com.shouxin.shouxin.database.Dao.ItemEntryDao;
import com.shouxin.shouxin.database.util.DBHelper;

public class ItemEntryDaoImpl implements ItemEntryDao {

    private DBHelper helper;

    public ItemEntryDaoImpl(Context context){
        helper = new DBHelper(context);
    }


    @Override
    public void add(ItemEntry itemEntry) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", itemEntry.getName());
        values.put("description", itemEntry.getDescription());
        values.put("pictureUrl", itemEntry.getPictureUrl());
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
    public void update(ItemEntry itemEntry) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", itemEntry.getName());
        values.put("description", itemEntry.getDescription());
        values.put("pictureUrl", itemEntry.getPictureUrl());
        db.update("ItemEntry", values, "name=?", new String[]{itemEntry.getName()});
        db.close();
    }

    @Override
    public ItemEntry findById(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ItemEntry item = null;
        Cursor cursor = db.query("ItemEntry", null, "name=?", new String[]{name}, null, null, null);
        if(cursor.moveToNext()){
            item = new ItemEntry();
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
