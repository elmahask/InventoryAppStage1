package com.example.elmahask.inventoryappstage1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elmahask.inventoryappstage1.data.InventoryContract;
import com.example.elmahask.inventoryappstage1.data.InventoryDBHelper;

public class AddInventoryActivity extends AppCompatActivity {

    /**
     * EditText field to enter the Book's Name
     */
    private EditText mBookName;

    /**
     * EditText field to enter the Book's Code
     */
    private EditText mBookCode;

    /**
     * EditText field to enter the Book's Quantity
     */
    private EditText mBookQuantity;

    /**
     * EditText field to enter the Book's Price
     */
    private EditText mBookPrice;

    /**
     * EditText field to enter the Book's Suppliers
     */
    private Spinner mBookSuppliers;

    /**
     * EditText field to enter the Contact's Suppliers
     */
    private EditText mBookSupplierContact;

    /**
     * Supplier of the Book. The possible values are:
     * 0 for unknown Supplier, 1 for Supplier_Amazon, 2 for Supplier_Jumia, 2 for Supplier_Souq.
     */
    private int mSuppliersName = InventoryContract.SUPPLIER_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        // Find all relevant views that we will need to read user input from
        mBookName = (EditText) findViewById(R.id.e_book_name);
        mBookCode = (EditText) findViewById(R.id.e_book_code);
        mBookPrice = (EditText) findViewById(R.id.e_book_price);
        mBookQuantity = (EditText) findViewById(R.id.e_book_quantity);
        mBookSuppliers = (Spinner) findViewById(R.id.spinner_suppliers);
        mBookSupplierContact = (EditText) findViewById(R.id.e_supplier_contact);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mBookSuppliers.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mBookSuppliers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_amazon))) {
                        mSuppliersName = InventoryContract.SUPPLIER_AMAZON; // AMAZON
                    } else if (selection.equals(getString(R.string.supplier_jumia))) {
                        mSuppliersName = InventoryContract.SUPPLIER_JUMIA;  // JUMIA
                    } else if (selection.equals(getString(R.string.supplier_souq))) {
                        mSuppliersName = InventoryContract.SUPPLIER_SOUQ;  // SOUQ
                    } else {
                        mSuppliersName = InventoryContract.SUPPLIER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSuppliersName = InventoryContract.SUPPLIER_UNKNOWN; // Unknown
            }
        });
    }

    private void insertProduct() {

        String bookName = mBookName.getText().toString().trim();
        int bookCode = Integer.parseInt(mBookCode.getText().toString().trim());
        int bookPrice = Integer.parseInt(mBookPrice.getText().toString().trim());
        int bookQuantity = Integer.parseInt(mBookQuantity.getText().toString().trim());
        int bookSupplierContact = Integer.parseInt(mBookSupplierContact.getText().toString().trim());

        InventoryDBHelper mDbHelper = new InventoryDBHelper(this);
        SQLiteDatabase dB = mDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(InventoryContract.COLUMN_BOOK_NAME, bookName);
        values.put(InventoryContract.COLUMN_BOOK_CODE, bookCode);
        values.put(InventoryContract.COLUMN_BOOK_PRICE, bookPrice);
        values.put(InventoryContract.COLUMN_BOOK_QUANTITY, bookQuantity);
        values.put(InventoryContract.COLUMN_BOOK_SUPPLIER_NAME, mSuppliersName);
        values.put(InventoryContract.COLUMN_BOOK_SUPPLIER_CONTACT, bookSupplierContact);

        long rowID = dB.insert(InventoryContract.TABLE_NAME, null, values);

        if (rowID == -1) {
            Toast.makeText(this, "Error with saving", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved with ID: " + rowID, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save:
                // Do nothing for now
                insertProduct();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.delete:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
