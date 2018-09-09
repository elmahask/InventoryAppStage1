package com.example.elmahask.inventoryappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.elmahask.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class InventoryDBHelper extends SQLiteOpenHelper {

    //Name of DB
    private static final String DATABASE_NAME = "inventory_book.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_CODE + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_BOOK_SUPPLIER_CONTACT + " INTEGER );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);

        Log.d("successfully message" , "DB is Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
