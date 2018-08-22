package com.example.elmahask.inventoryappstage1.data;

import android.provider.BaseColumns;

public class InventoryContract implements BaseColumns {

    public InventoryContract() {
    }

    //Name of table DB
    public final static String TABLE_NAME = "inventory_book";

    //Column of table
    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_BOOK_NAME = "book_name";
    public final static String COLUMN_BOOK_CODE = "book_code";
    public final static String COLUMN_BOOK_PRICE = "book_price";
    public final static String COLUMN_BOOK_QUANTITY = "book_quantity";
    public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
    public final static String COLUMN_BOOK_SUPPLIER_CONTACT = "supplier_contact";

    // SUPPLIER_NAME LIST VALUES
    public final static int SUPPLIER_UNKNOWN = 0;
    public final static int SUPPLIER_AMAZON = 1;
    public final static int SUPPLIER_JUMIA = 2;
    public final static int SUPPLIER_SOUQ = 3;
}
