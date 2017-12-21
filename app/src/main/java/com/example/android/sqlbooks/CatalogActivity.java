package com.example.android.sqlbooks;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.android.sqlbooks.data.ProductContract;
import com.example.android.sqlbooks.data.ProductDbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = "CatalogActivity";

    // This is the Adapter being used to display the list's data
    ProductCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find ListView to populate
        ListView productsListView = (ListView) findViewById(R.id.list_view_product);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        productsListView.setEmptyView(emptyView);

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new ProductCursorAdapter(this,null);
        // Attach cursor adapter to the ListView
        productsListView.setAdapter(mAdapter);
    }

    private void insertData(){

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        ProductDbHelper mDbHelper = new ProductDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Initialize the ContentValues object
        ContentValues values = new ContentValues();

        // Setup the values to insert into the table
        values.put(ProductContract.ProductEntry.COLUMN_NAME, "Learn Android the Udacity Way");
        values.put(ProductContract.ProductEntry.COLUMN_USD_PRICE, "44.99");
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, "100");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, "Udacity");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL, "pub@udacity.com");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, "111-111-2222");

        long newRowId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);

        Log.d(TAG, "New row id: " + newRowId);

    }


    private Cursor queryData() {

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        ProductDbHelper mDbHelper = new ProductDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_NAME,
                ProductContract.ProductEntry.COLUMN_USD_PRICE,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ProductContract.ProductEntry._ID + " DESC";

        Cursor cursor = db.query(
                ProductContract.ProductEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                            // The columns for the WHERE clause
                null,                        // The values for the WHERE clause
                null,                            // don't group the rows
                null,                             // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // Figure out the index of each column
        int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME);
        int usdPriceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_USD_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME);
        int supplierEmailColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL);
        int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE);

        // Iterate through all the returned rows in the cursor
        while (cursor.moveToNext()) {
            // Use that index to extract the String or Int value of the word
            // at the current row the cursor is on.
            int currentID = cursor.getInt(idColumnIndex);
            String currentName = cursor.getString(nameColumnIndex);
            float currentUsdPrice = cursor.getFloat(usdPriceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            String currentSupplierName = cursor.getString(supplierNameColumnIndex);
            String currentSupplierEmail = cursor.getString(supplierEmailColumnIndex);
            String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

            Log.d(TAG, ("\n" + currentID + " - "
            + currentName + " - "
            + currentUsdPrice + " - "
            + currentQuantity + " - "
            + currentSupplierName + " - "
            + currentSupplierEmail + " - "
            + currentSupplierPhone));

        }
        return cursor;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
