package com.example.xiamentourismapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{
    //    Declare variable for database, table and column
    public static final String DATABASE_NAME = "XiamenTourism.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COLUMN_1 = "id";
    public static final String COLUMN_2 = "username";
    public static final String COLUMN_3 = "email";
    public static final String COLUMN_4 = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, password INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        if not exist, we can parse the onCreate method to run and create table
        onCreate(sqLiteDatabase);
    }

    public boolean createUser(String username, String email, String password){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_2, username);
        contentValues.put(COLUMN_3, email);
        contentValues.put(COLUMN_4, password);

        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) { //data inserted failed
            return false;
        } else { //data inserted successfully
            return true;
        }

    }

    public Boolean checkEmail(String email) //check user exists
    {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from user_table where email = ?", new String[]{email});

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean authenticateUser(String email, String password){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from user_table where email = ? and password=?", new String[]{email, password});

        if(cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
     }
}
