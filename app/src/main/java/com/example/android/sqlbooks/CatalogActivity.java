package com.example.android.sqlbooks;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.sqlbooks.data.ProductContract;
import com.example.android.sqlbooks.data.ProductDbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = "CatalogActivity";

    public static final int PRODUCT_LOADER = 0;

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

        // Setup item click listener
        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String PRODUCT_ID = Long.toString(id);
                final Uri PRODUCT_URI = Uri.withAppendedPath(ProductContract.ProductEntry.CONTENT_URI, PRODUCT_ID);

                view.findViewById(R.id.sale_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "Product URI: " + PRODUCT_URI);
                    }
                });

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Pass URI to intent
                intent.putExtra("PRODUCT_URI", PRODUCT_URI);

                // Start the activity
                startActivity(intent);
            }
        });

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertData(){

        // Initialize the ContentValues object
        ContentValues values = new ContentValues();

        // Setup the values to insert into the table
        values.put(ProductContract.ProductEntry.COLUMN_NAME, "Learn Android the Udacity Way");
        values.put(ProductContract.ProductEntry.COLUMN_USD_PRICE, "44.99");
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, "100");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME, "Udacity");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL, "pub@udacity.com");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE, "111-111-2222");

        getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Define a projection
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_NAME,
                ProductContract.ProductEntry.COLUMN_USD_PRICE,
                ProductContract.ProductEntry.COLUMN_QUANTITY
        };

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ProductContract.ProductEntry.CONTENT_URI, projection,
                null, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}
