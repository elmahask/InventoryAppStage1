package com.example.elmahask.inventoryappstage1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class InventoryContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.elmahask.inventoryappstage1";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.elmahask.inventoryappstage1/inventory_book/ is a valid path for
     * looking at pet data. content://com.example.elmahask.inventoryappstage1/book/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_INVENTORY = "inventory_book";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public InventoryContract() {
    }

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */

    public static final class InventoryEntry implements BaseColumns {

        /**
         * The content URI to access the Inventory data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of Inventory.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single Inventory.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        //Name of table DB
        /**
         * Name of database table for
         */
        public final static String TABLE_NAME = "inventory_book";

        //Column of table

        /**
         * Unique ID number for the pet (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Book.
         * Type: TEXT
         */
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

        /**
         * Returns whether or not the given suppliers
         */
        public static boolean isValidSupplier(int supplier) {
            return supplier == SUPPLIER_UNKNOWN || supplier == SUPPLIER_AMAZON || supplier == SUPPLIER_JUMIA || supplier == SUPPLIER_SOUQ;
        }
    }
}
