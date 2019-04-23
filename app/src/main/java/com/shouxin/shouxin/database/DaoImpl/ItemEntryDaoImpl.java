package com.shouxin.shouxin.database.DaoImpl;

import android.content.ContentValues;
import android.content.Context;
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
    public void remove(ItemEntry itemEntry) {

    }

    @Override
    public void update(ItemEntry itemEntry) {

    }

    @Override
    public ItemEntry findById(ItemEntry itemEntry) {
        return null;
    }
}
