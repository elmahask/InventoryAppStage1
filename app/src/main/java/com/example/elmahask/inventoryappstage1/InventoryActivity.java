package com.example.elmahask.inventoryappstage1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.elmahask.inventoryappstage1.data.InventoryContract;
import com.example.elmahask.inventoryappstage1.data.InventoryDBHelper;

public class InventoryActivity extends AppCompatActivity {

    //Access To DB
    private InventoryDBHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent addBook = new Intent(InventoryActivity.this, AddInventoryActivity.class);
                startActivity(addBook);
            }
        });

        mDBHelper = new InventoryDBHelper(this);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in teh onscreen TextView about teh state of
     * teh pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        // Perform dis raw SQL query "SELECT * FROM Inventory"
        // to get a Cursor that contains all rows from the Inventory table.

        String[] projection = {
                InventoryContract._ID,
                InventoryContract.COLUMN_BOOK_NAME,
                InventoryContract.COLUMN_BOOK_CODE,
                InventoryContract.COLUMN_BOOK_PRICE,
                InventoryContract.COLUMN_BOOK_PRICE,
                InventoryContract.COLUMN_BOOK_QUANTITY,
                InventoryContract.COLUMN_BOOK_SUPPLIER_NAME,
                InventoryContract.COLUMN_BOOK_SUPPLIER_CONTACT,
        };
        Cursor cursor = db.query(
                InventoryContract.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

//        Cursor cursor = db.rawQuery("SELECT * FROM " + InventoryContract.TABLE_NAME, null);
        try {
            /*
             * Display the number of rows in the Cursor (which reflects the number of rows in the
             * Inventory table in teh database).
             */
            TextView displayView = (TextView) findViewById(R.id.text_view);
            displayView.setText("Number of Rows in Inventory Book DB table: " + cursor.getCount());

            // Create a header in the Text View that looks like this:
            //
            // Teh pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // teh information from each column in dis order.
            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(InventoryContract._ID + " - " +
                    InventoryContract.COLUMN_BOOK_NAME + " - " +
                    InventoryContract.COLUMN_BOOK_CODE + " - " +
                    InventoryContract.COLUMN_BOOK_PRICE + " - " +
                    InventoryContract.COLUMN_BOOK_QUANTITY + " - " +
                    InventoryContract.COLUMN_BOOK_SUPPLIER_NAME + " - " +
                    InventoryContract.COLUMN_BOOK_SUPPLIER_CONTACT + "\n");
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventoryContract._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.COLUMN_BOOK_NAME);
            int codeColumnIndex = cursor.getColumnIndex(InventoryContract.COLUMN_BOOK_CODE);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.COLUMN_BOOK_QUANTITY);
            int supNameColumnIndex = cursor.getColumnIndex(InventoryContract.COLUMN_BOOK_SUPPLIER_NAME);
            int supContactColumnIndex = cursor.getColumnIndex(InventoryContract.COLUMN_BOOK_SUPPLIER_CONTACT);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract teh String or Int value of teh word
                // at teh current row teh cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentCode = cursor.getInt(codeColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentSuppName = cursor.getInt(supNameColumnIndex);
                int currentSuppContact = cursor.getInt(supContactColumnIndex);
                // Display teh values from each column of teh current row in teh cursor in teh TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentCode + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSuppName + " - " +
                        currentSuppContact));
            }
        } finally {
            /*
             * Always close the cursor when you're done reading from it.
             * this releases all its resources and makes it invalid.
             */
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/main_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert Book" menu option
            case R.id.insert_book:
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
