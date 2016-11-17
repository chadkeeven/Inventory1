package com.example.android.inventory1;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory1.data.ProductContract;



public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Uri mCurrentProductUri;
    private TextView mQuantity;
    private TextView mPrice;
    private TextView mSold;
    private String name;
    private ImageView mImageView;
    private Button soldButton;
    private Button increment;
    private Button decrement;
    private Button email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mQuantity = (TextView) findViewById(R.id.detail_product_quantity);
        mPrice = (TextView) findViewById(R.id.detail_product_price);
        mSold = (TextView) findViewById(R.id.detail_product_sold);
        mImageView = (ImageView) findViewById(R.id.detail_image);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        Log.v(DetailActivity.class.getSimpleName(),mCurrentProductUri.toString());
        setTitle(name);
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

        soldButton = (Button) findViewById(R.id.sold_button);
        increment = (Button) findViewById(R.id.increment);
        decrement = (Button) findViewById(R.id.decrement);
        email = (Button) findViewById(R.id.order_more);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    private boolean editProduct(){
        Intent intent = new Intent(DetailActivity.this,EditorActivity.class);
//        Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
        intent.setData(mCurrentProductUri);
        startActivity(intent);
        return true;
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
                NavUtils.navigateUpFromSameTask(DetailActivity.this);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
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
     * Perform the deletion of the product in the database.
     */
    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int deleteUri = getContentResolver().delete(mCurrentProductUri, null, null);
            // Show a toast message depending on whether or not the deletion was successful
            if (deleteUri == 0) {
                // If the new content URI is null, then there was an error with deletion.
                Toast.makeText(this, R.string.error_with_deletion, Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, R.string.deletion_success,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_edit:
                editProduct();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRICE,
                ProductContract.ProductEntry.COLUMN_PICTURE,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_SOLD};


        return new CursorLoader(this, mCurrentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            Log.v(EditorActivity.class.getSimpleName(),"return null");
            return;
        }
        String curse;
        int rows = cursor.getCount();

        curse = "amount of rows:" + rows;
        Log.v(DetailActivity.class.getSimpleName(),curse);


// Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {

            // Find the columns of product attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
            int soldColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SOLD);
            int pictureColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PICTURE);


            int iDColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
            final String rowId = cursor.getString(iDColumnIndex);
            // Extract out the value from the Cursor for the given column index
            name = cursor.getString(nameColumnIndex);
            final int price = cursor.getInt(priceColumnIndex);
            final int quantity = cursor.getInt(quantityColumnIndex);
            int sold = cursor.getInt(soldColumnIndex);
            String pictureLink = cursor.getString(pictureColumnIndex);
            // Update the views on the screen with the values from the database;
            mImageView.setImageURI(Uri.parse(pictureLink));
            mPrice.setText("Price: $" + Integer.toString(price));
            mQuantity.setText(Integer.toString(quantity));
            mSold.setText(Integer.toString(sold));
            setTitle(name);

            soldButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String quantityString = mQuantity.getText().toString();
                    String soldString = mSold.getText().toString();
                    int sold = Integer.parseInt(soldString);
                    int quantity = Integer.parseInt(quantityString);
                    if (quantity >0) {
                        quantity = quantity - 1;
                        sold = sold + 1;
                        mQuantity.setText(Integer.toString(quantity));
                        mSold.setText(Integer.toString(sold));
                        ContentValues values = new ContentValues();
                        Integer itemId = Integer.parseInt(rowId);
                        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantity);
                        values.put(ProductContract.ProductEntry.COLUMN_SOLD,sold);
                        ContentResolver resolver = view.getContext().getContentResolver();
                        mCurrentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,itemId);
                        resolver.update(mCurrentProductUri, values, null, null);
                    }
                }
            });
            increment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String quantityString = mQuantity.getText().toString();
                    int quantity = Integer.parseInt(quantityString);
                    quantity = quantity + 1;
                    mQuantity.setText(Integer.toString(quantity));
                    ContentValues values = new ContentValues();
                    Integer itemId = Integer.parseInt(rowId);
                    values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantity);
                    ContentResolver resolver = view.getContext().getContentResolver();
                    mCurrentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,itemId);
                    resolver.update(mCurrentProductUri, values, null, null);
                }
            });
            decrement.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String quantityString = mQuantity.getText().toString();
                    int quantity = Integer.parseInt(quantityString);
                    if (quantity > 0) {
                        quantity = quantity - 1;
                        mQuantity.setText(Integer.toString(quantity));
                        ContentValues values = new ContentValues();
                        Integer itemId = Integer.parseInt(rowId);
                        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantity);
                        ContentResolver resolver = view.getContext().getContentResolver();
                        mCurrentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, itemId);
                        resolver.update(mCurrentProductUri, values, null, null);
                    }
                }
            });
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_TEXT, name + "\nPrice: " + price +"\nQuantity: " + quantity);
                    if (intent.resolveActivity(getPackageManager()) !=null){
                        startActivity(intent);
                    }
                }
            });
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPrice.setText("");
        mQuantity.setText("");
        mSold.setText("");
    }
}