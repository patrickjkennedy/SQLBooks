package com.example.android.sqlbooks.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pkennedy on 12/13/17.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "product.db";

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " (" +
                    ProductContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ProductContract.ProductEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    ProductContract.ProductEntry.COLUMN_USD_PRICE + " REAL NOT NULL," +
                    ProductContract.ProductEntry.COLUMN_QUANTITY + " INTEGER NOT NULL," +
                    ProductContract.ProductEntry.COLUMN_IMAGE + " BLOB," +
                    ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                    ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL," +
                    ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProductContract.ProductEntry.TABLE_NAME;
}
