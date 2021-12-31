package com.example.xiamentourismapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WishlistDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "XiamenTourism.db";
    public static final String TABLE_NAME = "user_wishlist";
    public static final String TABLE_DESTINATION = "destination";

    public static final String COLUMN_1 = "wish_id";
    public static final String COLUMN_2 = "destination_id";
    public static final String COLUMN_3 = "user_id";
    public static final String COLUMN_4 = "comment";

    public WishlistDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(wish_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, destination_id INTEGER NOT NULL, user_id INTEGER NOT NULL, comment TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

//    Add to wishlist function
    public boolean insertWishlist(int destination_id, int user_id, String comment){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_2, destination_id);
        contentValues.put(COLUMN_3, user_id);
        contentValues.put(COLUMN_4, comment);

        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) { //data inserted failed
            return false;
        } else { //data inserted successfully
            return true;
        }
    }

//    remove from wishlist using destination_id and user_id
    public boolean removeList(int destination_id, int user_id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, COLUMN_2 + "=" + destination_id + " and " + COLUMN_3 + "=" + user_id, null) > 0;
    }

    //    remove from wishlist using wish_id
    public boolean removeListById(int wish_id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, COLUMN_1 + "=" + wish_id, null) > 0;
    }

//    retrieve all the destination that added to wishlist of a user
    public Cursor getAllWish(int user_id){
        SQLiteDatabase database = this.getReadableDatabase();

        String query = "Select * from " + TABLE_NAME + " where " + COLUMN_3 + " =" + user_id;
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

//    retrieve destination information using destination_id
    public Cursor getAllDestinationAdded(int user_id)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        String query = "select tbl1.* from " + TABLE_DESTINATION + " tbl1 left join " + TABLE_NAME + " tbl2 " +
                "on tbl1." + COLUMN_2 + "=" + "tbl2." + COLUMN_2 +
                " where " + COLUMN_3 + "=" + user_id + " order by " + "tbl2." + COLUMN_1;

        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

//    Edit comment function
    public int editComment(int wish_id, String comment){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_4, comment);

        return database.update(TABLE_NAME, contentValues, COLUMN_1 + "=?", new String[]{String.valueOf(wish_id)} );
    }

    public String viewComment(int wish_id)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        String query = "select " + COLUMN_4 + " from " + TABLE_NAME + " where " + COLUMN_1 + "=" + wish_id;
        Cursor cursor = null;
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        cursor.moveToFirst();
        return cursor.getString(0);
    }
}
