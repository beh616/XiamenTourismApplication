package com.example.xiamentourismapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DestinationDatabaseHelper extends SQLiteOpenHelper {
    //    Declare variable for database, table and column
    public static final String DATABASE_NAME = "XiamenTourism.db";
    public static final String TABLE_NAME = "destination";

    public DestinationDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(destination_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, description TEXT, operation_hours TEXT, phone INTEGER, website TEXT, latitude REAL, longitude REAL, image BLOB, type_id INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor getAllDestination(){
        String query = "Select * from " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }
}
