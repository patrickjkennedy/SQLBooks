package com.example.android.sqlbooks;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.sqlbooks.data.ProductContract;

import java.util.ArrayList;

/**
 * Created by pkennedy on 12/21/17.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = "EditorActivity";

    private static final int EXISTING_PRODUCT_LOADER = 0;

    /** Content URI for the existing product (null if it's a new product) */
    private Uri PRODUCT_URI;

    /**
     * EditText field to enter the product name
     */
    private EditText mProductNameEditText;

    /**
     * EditText field to enter the product price
     */
    private EditText mProductPriceEditText;

    /**
     * EditText field to enter the product quantity
     */
    private EditText mProductQuantityEditText;

    /**
     * EditText field to enter the supplier name
     */
    private EditText mSupplierNameEditText;

    /**
     * EditText field to enter the supplier email
     */
    private EditText mSupplierEmailEditText;

    /**
     * EditText field to enter the supplier phone
     */
    private EditText mSupplierPhoneEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();

        PRODUCT_URI = intent.getParcelableExtra("PRODUCT_URI");

        Log.d("EditorActivity", "URI: " + PRODUCT_URI);

        if (PRODUCT_URI == null){
            getSupportActionBar().setTitle(R.string.add_product);
        } else{
            getSupportActionBar().setTitle(R.string.edit_product);
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mProductPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mProductQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mSupplierEmailEditText = (EditText) findViewById(R.id.edit_supplier_email);


    }

    /**
     * Get user input from editor and save new product into database
     */
    private void saveProduct() {

        // Get the product's values from the user
        String productNameString = mProductNameEditText.getText().toString().trim();

        String productPriceString = mProductPriceEditText.getText().toString().trim();
        float productPriceFloat = Float.parseFloat(productPriceString);

        String productQuantityString = mProductQuantityEditText.getText().toString().trim();
        int productQuantityInt = Integer.parseInt(productQuantityString);

        String supplierNameString = mSupplierNameEditText.getText().toString().trim();

        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        String supplierEmailString = mSupplierEmailEditText.getText().toString().trim();


        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME, productNameString);
        values.put(ProductContract.ProductEntry.COLUMN_USD_PRICE, productPriceFloat);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL, supplierEmailString);

        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, productQuantityInt);

        if(PRODUCT_URI == null){

            // Save the new product
            getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

            //Show save message
            Toast.makeText(this, R.string.toast_product_saved, Toast.LENGTH_SHORT).show();

        } else{
            //Update the existing product
            getContentResolver().update(PRODUCT_URI, values, null,null);

            //Show edit message
            Toast.makeText(this, R.string.toast_product_edit, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save product to database
                saveProduct();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a selection
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_NAME,
                ProductContract.ProductEntry.COLUMN_USD_PRICE,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL
        };
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, PRODUCT_URI, projection,
                null, null, null);    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of product attributes that we're interested in
            int productNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_USD_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE);
            int supplierEmailColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL);

            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(productNameColumnIndex);
            float productPrice = cursor.getFloat(productPriceColumnIndex);
            int productQuantity = cursor.getInt(productQuantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
            String supplierEmail = cursor.getString(supplierEmailColumnIndex);


            // Update the views on the screen with the values from the database
            mProductNameEditText.setText(productName);
            mProductPriceEditText.setText(Float.toString(productPrice));
            mProductQuantityEditText.setText(Integer.toString(productQuantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(supplierPhone);
            mSupplierEmailEditText.setText(supplierEmail);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductNameEditText.setText("");
        mProductPriceEditText.setText(String.valueOf(0.00));
        mProductQuantityEditText.setText(String.valueOf(0));
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
        mSupplierEmailEditText.setText("");
    }

    public void increment(View view){
        String productQuantityString = mProductQuantityEditText.getText().toString().trim();
        int productQuantityInt = Integer.parseInt(productQuantityString);
        productQuantityInt++;
        mProductQuantityEditText.setText(Integer.toString(productQuantityInt));
    }

    public void decrement(View view){
        String productQuantityString = mProductQuantityEditText.getText().toString().trim();
        int productQuantityInt = Integer.parseInt(productQuantityString);
        productQuantityInt--;
        mProductQuantityEditText.setText(Integer.toString(productQuantityInt));
    }

    public void emailSupplier(View view) {
        String supplierEmail = mSupplierEmailEditText.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"", supplierEmail});
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void callSupplier(View view) {
        String supplierPhoneNumber = mSupplierPhoneEditText.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + supplierPhoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
