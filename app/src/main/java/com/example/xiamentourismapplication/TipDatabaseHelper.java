package com.example.xiamentourismapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TipDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "XiamenTourism.db";
    public static final String TABLE_NAME = "travel_tips";

    public TipDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(tip_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT, url TEXT, type TEXT, image BLOB, author TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

//    get all tips
    public Cursor getAllTips(){
        String query = "Select * from " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

}
