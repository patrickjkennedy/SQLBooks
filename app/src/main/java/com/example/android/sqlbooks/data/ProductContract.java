package com.example.android.sqlbooks.data;

import android.provider.BaseColumns;

/**
 * Created by pkennedy on 12/13/17.
 */

public final class ProductContract {

    public static abstract class ProductEntry implements BaseColumns{

        public static final String TABLE_NAME = "product";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USD_PRICE = "usd_price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";
        public static final String COLUMN_SUPPLIER_PHONE = "supplier_price";

    }
}


