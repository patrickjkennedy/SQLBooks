package com.example.android.sqlbooks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sqlbooks.data.ProductContract;
import com.example.android.sqlbooks.data.ProductDbHelper;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    private static final String TAG = "CatalogActivity";

    private TextView quantityTextView;
    private String quantity;

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        Log.d(TAG, "Bind view was called");
        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        quantityTextView = (TextView) view.findViewById(R.id.quantity);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_USD_PRICE));
        quantity = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_QUANTITY));

        // Populate fields with extracted properties
        nameTextView.setText(name);
        priceTextView.setText(context.getString(R.string.usd_sign)+price);
        quantityTextView.setText(context.getString(R.string.quantity) + quantity);

        // Setup an On Click listener on the button, and get the correct position on button click
        Button saleButton = (Button) view.findViewById(R.id.sale_button);

        final int position = cursor.getPosition();
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move cursor to clicked position
                cursor.moveToPosition(position);

                int currentQuantity = Integer.parseInt(quantity);

                if (currentQuantity == 0) {
                    Toast.makeText(view.getContext(), "No product available", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentQuantity--;
                quantityTextView.setText(context.getString(R.string.quantity) + currentQuantity);

                ProductDbHelper dbHelper = new ProductDbHelper(view.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, currentQuantity);

                long id = cursor.getLong(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
                db.update(ProductContract.ProductEntry.TABLE_NAME, values, "_id=" + id, null);
                db.close();
                quantityTextView.setText(context.getString(R.string.quantity) + currentQuantity);

            }
        });
    }
}