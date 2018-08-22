package com.example.elmahask.inventoryappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + InventoryContract.TABLE_NAME + " ("
                + InventoryContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + InventoryContract.COLUMN_BOOK_CODE + " INTEGER NOT NULL, "
                + InventoryContract.COLUMN_BOOK_PRICE + " INTEGER NOT NULL, "
                + InventoryContract.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, "
                + InventoryContract.COLUMN_BOOK_SUPPLIER_NAME + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryContract.COLUMN_BOOK_SUPPLIER_CONTACT + " INTEGER );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);

        Log.d("successfully message" , "DB is Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
