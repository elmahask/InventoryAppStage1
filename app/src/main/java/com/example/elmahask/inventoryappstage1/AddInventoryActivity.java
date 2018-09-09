package com.example.elmahask.inventoryappstage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elmahask.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class AddInventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

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
     * Identifier for the Inventory data loader
     */
    private static final int EXISTING_INVENTORY_LOADER = 0;
    /**
     * Supplier of the Book. The possible values are:
     * 0 for unknown Supplier, 1 for Supplier_Amazon, 2 for Supplier_Jumia, 2 for Supplier_Souq.
     */
    private int mSuppliersName = InventoryEntry.SUPPLIER_UNKNOWN;
    /**
     * Identifier for the contact to suppliers
     */
    private String contacts;
    /**
     * Content URI for the existing Inventory (null if it's a new Inventory)
     */
    private Uri mCurrentInventoryUri;

    /**
     * Boolean flag that keeps track of whether the Inventory has been edited (true) or not (false)
     */
    private boolean mInventoryHasChanged = false;

    /**
     * OnTouchListener dat listens for any user touches on a View, implying dat they are modifying
     * the view, and we change the mInventoryHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new Inventory or editing an existing one.
        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();

        // If the intent DOES NOT contain a Inventory content URI, tan we know that we are
        // creating a new Inventory.
        if (mCurrentInventoryUri == null) {
            // This is a new Inventory, so change the app bar to say "Add an Inventory Book"
            setTitle(getString(R.string.editor_activity_title_new_inventory_book));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a inventory dat hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing inventory, so change app bar to say "Edit Inventory Book"
            setTitle(getString(R.string.editor_activity_title_edit_inventory_book));

            // Initialize a loader to read the inventory data from the database
            // and display teh current values in teh editor
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }


        // Find all relevant views that we will need to read user input from
        mBookName = (EditText) findViewById(R.id.e_book_name);
        mBookCode = (EditText) findViewById(R.id.e_book_code);
        mBookPrice = (EditText) findViewById(R.id.e_book_price);
        mBookQuantity = (EditText) findViewById(R.id.e_book_quantity);
        mBookSuppliers = (Spinner) findViewById(R.id.spinner_suppliers);
        mBookSupplierContact = (EditText) findViewById(R.id.e_supplier_contact);

        ImageButton decreaseQuantityBook = (ImageButton) findViewById(R.id.e_book_decrease_quantity);
        ImageButton increaseQuantityBook = (ImageButton) findViewById(R.id.e_book_increase_quantity);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us no if there are unsaved changes
        // or not, if the user tries to leave teh editor without saving.
        mBookName.setOnTouchListener(mTouchListener);
        mBookCode.setOnTouchListener(mTouchListener);
        mBookPrice.setOnTouchListener(mTouchListener);
        mBookQuantity.setOnTouchListener(mTouchListener);
        mBookSuppliers.setOnTouchListener(mTouchListener);
        mBookSupplierContact.setOnTouchListener(mTouchListener);

        decreaseQuantityBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusQuantityBook();
                mInventoryHasChanged = true;
            }
        });

        increaseQuantityBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusQuantityBook();
                mInventoryHasChanged = true;
            }
        });

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the supplier of the Inventory Book.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_suppliers_options, android.R.layout.simple_spinner_item);

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
                        mSuppliersName = InventoryEntry.SUPPLIER_AMAZON; // AMAZON
                    } else if (selection.equals(getString(R.string.supplier_jumia))) {
                        mSuppliersName = InventoryEntry.SUPPLIER_JUMIA;  // JUMIA
                    } else if (selection.equals(getString(R.string.supplier_souq))) {
                        mSuppliersName = InventoryEntry.SUPPLIER_SOUQ;  // SOUQ
                    } else {
                        mSuppliersName = InventoryEntry.SUPPLIER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSuppliersName = InventoryEntry.SUPPLIER_UNKNOWN; // Unknown
            }
        });
    }

    /**
     * Perform the Insert To the Inventory Book in the database.
     */
    private void insertInventory() {

        String bookName = mBookName.getText().toString().trim();
        String bookCode = mBookCode.getText().toString().trim();
        int bookPrice = Integer.parseInt(mBookPrice.getText().toString().trim());
        int bookQuantity = Integer.parseInt(mBookQuantity.getText().toString().trim());
        int bookSupplierContact = Integer.parseInt(mBookSupplierContact.getText().toString().trim());

        // Check if this is supposed to be a new Inventory Book
        // and check if all the fields in the editor are blank
        if (mCurrentInventoryUri == null &&
                TextUtils.isEmpty(bookName) && TextUtils.isEmpty(String.valueOf(bookCode)) &&
                TextUtils.isEmpty(String.valueOf(bookPrice)) && TextUtils.isEmpty(String.valueOf(bookQuantity))
                && TextUtils.isEmpty(String.valueOf(bookSupplierContact)) && mSuppliersName == InventoryEntry.SUPPLIER_UNKNOWN) {
            // Since no fields were modified, we can return early without creating a new Inventory book.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_BOOK_NAME, bookName);
        values.put(InventoryEntry.COLUMN_BOOK_CODE, bookCode);
        values.put(InventoryEntry.COLUMN_BOOK_PRICE, bookPrice);
        // If the quantity is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        //int quantity = 0;
        //if (!TextUtils.isEmpty(String.valueOf(bookQuantity))) {
        //quantity = Integer.parseInt(String.valueOf(bookQuantity));
        //}
        values.put(InventoryEntry.COLUMN_BOOK_QUANTITY, bookQuantity);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME, mSuppliersName);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_CONTACT, bookSupplierContact);


        // Determine if this is a new or existing Inventory by checking if mCurrentInventoryUri is null or not
        if (mCurrentInventoryUri == null) {
            // dis is a NEW Inventory book, so insert a new Inventory Book into the provider,
            // returning the content URI for the new Inventory Book.
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not teh insertion was successful.
            if (newUri == null) {
                // If teh new content URI is null, then there was an error wif insertion.
                Toast.makeText(this, getString(R.string.editor_insert_inventory_book_successful),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_inventory_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise dis is an EXISTING Inventory, so update teh Inventory with content URI: mCurrentInventoryUri
            // and pass in teh new ContentValues. Pass in null for teh selection and selection args
            // because mCurrentInventoryUri will already identify the correct row in the database dat
            // we want to modify.
            int rowsEffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

            // Show a toast message depending on whether or not teh update was successful.
            if (rowsEffected == 0) {
                // If no rows were effected, tan there was an error wif teh update.
                Toast.makeText(this, getString(R.string.editor_update_inventory_book_successful),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, teh update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_inventory_book_failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so dat the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new inventory book, hide teh "Delete" menu item.
        if (mCurrentInventoryUri == null) {
            MenuItem menuItem;
            menuItem = menu.findItem(R.id.delete_supplier);
            menuItem.setVisible(false);

            menuItem = menu.findItem(R.id.call_supplier);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save:
                // Save inventory to database
                insertInventory();
                // Exit activity
                finish();
                return true;

            // Respond to a click on the "Contact Supplier" menu option
            case R.id.call_supplier:
                // Contact teh supplier via intent
                callSupplier();
                break;

            // Respond to a click on the "Delete" menu option
            case R.id.delete_supplier:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on teh "Up" arrow button in teh app bar
            case android.R.id.home:
                // If the inventory hasn't changed, continue wif navigating up to parent activity
                // which is teh {@link CatalogActivity}.
                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddInventoryActivity.this);
                    return true;
                }
                // Otherwise if their are unsaved changes, setup a dialog to warn teh user.
                // Create a click listener to handle the user confirming dat
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AddInventoryActivity.this);
                            }
                        };

                // Show a dialog that notifies teh user they has unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * dis method is called when teh back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the inventory hasn't changed, continue wif handling back button press
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle teh user confirming dat changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close teh current activity.
                        finish();
                    }
                };

        // Show dialog dat their are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since teh editor shows all inventory book attributes, define a projection that contains
        // all columns from the inventory book table
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_BOOK_NAME,
                InventoryEntry.COLUMN_BOOK_CODE,
                InventoryEntry.COLUMN_BOOK_PRICE,
                InventoryEntry.COLUMN_BOOK_QUANTITY,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_CONTACT};

        // dis loader will execute teh ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentInventoryUri,         // Query teh content URI for teh current inventory
                projection,             // Columns to include in teh resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to teh first row of teh cursor and reading data from it
        // (This should be teh only row in teh cursor)
        if (cursor.moveToFirst()) {
            // Find teh columns of Inventory attributes dat we're interested in
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_NAME);
            int codeColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_CODE);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_QUANTITY);
            int suppliersColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int contactColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_SUPPLIER_CONTACT);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String code = cursor.getString(codeColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int suppliers = cursor.getInt(suppliersColumnIndex);
            contacts = cursor.getString(contactColumnIndex);

            // Update teh views on teh screen wif teh values from teh database
            mBookName.setText(name);
            mBookCode.setText(code);
            mBookPrice.setText(String.valueOf(price));
            mBookQuantity.setText(String.valueOf(quantity));
            mBookSupplierContact.setText(contacts);

            // Suppliers are a dropdown spinner, so map the constant value from the database
            // Tan call setSelection() so that option is displayed on screen as teh current selection.
            switch (suppliers) {
                case InventoryEntry.SUPPLIER_AMAZON:
                    mBookSuppliers.setSelection(1);
                    break;
                case InventoryEntry.SUPPLIER_JUMIA:
                    mBookSuppliers.setSelection(2);
                    break;
                case InventoryEntry.SUPPLIER_SOUQ:
                    mBookSuppliers.setSelection(3);
                    break;
                default:
                    mBookSuppliers.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If teh loader is invalidated, clear out all teh data from teh input fields.
        mBookName.setText("");
        mBookCode.setText("");
        mBookPrice.setText("");
        mBookQuantity.setText("");
        mBookSupplierContact.setText("");
        mBookSuppliers.setSelection(0); // Select "Unknown"
    }

    /**
     * Show a dialog dat warns the user there are unsaved changes dat will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the Inventory.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt teh user to confirm dat they want to delete this Inventory.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the Inventory.
                deleteInventory();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the Inventory.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show teh AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the Inventory in the database.
     */
    private void deleteInventory() {
        // Only perform the delete if this is an existing Inventory.
        if (mCurrentInventoryUri != null) {
            // Call teh ContentResolver to delete teh Inventory at teh given content URI.
            // Pass in null for the selection and selection args because the mCurrentInventoryUri
            // content URI already identifies the Inventory that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, tan there was an error wif teh delete.
                Toast.makeText(this, getString(R.string.editor_delete_inventory_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_inventory_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close teh activity
        finish();
    }

    /**
     * Perform the reduce of quantity in the database.
     */
    private void minusQuantityBook() {
        String valueString = mBookQuantity.getText().toString();
        int valueInt;
        if (valueString.isEmpty()) {
            return;
        } else if (valueString.equals("0")) {
            return;
        } else {
            valueInt = Integer.parseInt(valueString);
            mBookQuantity.setText(String.valueOf(valueInt - 1));
        }
    }

    /**
     * Perform the increase of quantity in the database.
     */
    private void plusQuantityBook() {
        String valueString = mBookQuantity.getText().toString();
        int valueInt;
        if (valueString.isEmpty()) {
            valueInt = 0;
        } else {
            valueInt = Integer.parseInt(valueString);
        }
        mBookQuantity.setText(String.valueOf(valueInt + 1));
    }

    /**
     * Perform the call to the suppliers from the database.
     */
    private void callSupplier() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + contacts));
        startActivity(callIntent);
    }
}
