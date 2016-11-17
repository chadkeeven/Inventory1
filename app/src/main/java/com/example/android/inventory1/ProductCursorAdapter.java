package com.example.android.inventory1;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory1.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {


    private Uri mCurrentProductUri;


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final int productId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry._ID));
        final int productQty = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY));
        final int productSold = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SOLD));
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView productSummary = (TextView) view.findViewById(R.id.summary);
        // Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
        // Read the product attributes from the Cursor for the current item
        String productNameString = cursor.getString(nameColumnIndex);
        String productPriceString = cursor.getString(priceColumnIndex);
        String productQuantityString = cursor.getString(quantityColumnIndex);
        String productSoldString = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SOLD));

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productNameString);
        productSummary.setText("Price $" + productPriceString + " Quantity: " + productQuantityString + " Sold: " + productSoldString);

        //Process a sale when the button is clicked and subtract one from the current inventory
        Button saleButton = (Button) view.findViewById(R.id.sell_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              ContentValues values = new ContentValues();
                                              if (productQty > 0) {
                                                  int mItemQty;
                                                  mItemQty = (productQty - 1);
                                                  int mProductSold;
                                                  mProductSold = productSold + 1;
                                                  values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, mItemQty);
                                                  values.put(ProductContract.ProductEntry.COLUMN_SOLD, mProductSold);
                                                  Uri uri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productId);
                                                  context.getContentResolver().update(uri, values, null, null);
                                              }
                                              context.getContentResolver().notifyChange(ProductContract.ProductEntry.CONTENT_URI, null);
                                          }
                                      }
        );
    }
}