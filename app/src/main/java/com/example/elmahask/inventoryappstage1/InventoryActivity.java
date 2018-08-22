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
        Cursor cursor = db.rawQuery("SELECT * FROM " + InventoryContract.TABLE_NAME, null);
        try {
            /*
             * Display the number of rows in the Cursor (which reflects the number of rows in the
             * Inventory table in teh database).
             */
            TextView displayView = (TextView) findViewById(R.id.text_view);
            displayView.setText("Number of Rows in Inventory Book DB table: " + cursor.getCount());
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
