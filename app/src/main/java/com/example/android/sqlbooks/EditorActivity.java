package com.example.android.sqlbooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by pkennedy on 12/21/17.
 */

public class EditorActivity extends AppCompatActivity {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    /** Content URI for the existing pet (null if it's a new pet) */
    private Uri PRODUCT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        PRODUCT_URI = intent.getParcelableExtra("PRODUCT_URI");

        Log.d("EditorActivity", "URI: " + PRODUCT_URI);

        if (PRODUCT_URI == null){
            getSupportActionBar().setTitle(R.string.add_product);
        } else{
            getSupportActionBar().setTitle(R.string.edit_product);
        }
}

}
