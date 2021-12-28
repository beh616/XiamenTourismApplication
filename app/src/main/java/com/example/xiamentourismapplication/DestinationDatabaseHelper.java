package com.example.xiamentourismapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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

    public Cursor getRecommendDestination(ArrayList<Integer> number_list){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME + " where destination_id in ("
                + number_list.get(0) + "," + number_list.get(1) + "," + number_list.get(2) + "," + number_list.get(3) + "," + number_list.get(4) + ")";
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public int getNumberOfRecords(){
        int limit = 0;
        String query = "Select COUNT(*) from " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
//        get number of records
        Cursor numberOfRecord = null;
        if(database != null){
            numberOfRecord = database.rawQuery(query, null);
        }
        if(numberOfRecord.moveToFirst()) {
            limit = numberOfRecord.getInt(0);
        }
        return limit;
    }
}
