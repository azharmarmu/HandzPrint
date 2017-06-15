package com.marmu.handprint.sales_man.landing.activity.price;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.Constants;

import java.util.HashMap;

@SuppressWarnings({"unchecked", "deprecation"})
public class PriceActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TextView noProd;

    DatabaseReference productDBRef = Constants.DATABASE.getReference(Constants.ADMIN_PRODUCT_PRICE);

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_price);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        noProd = (TextView) findViewById(R.id.tv_no_prod);
        progressDialog = new ProgressDialog(PriceActivity.this);
        progressDialog.setTitle("Loading...");
        updateUI();
    }

    private void updateUI() {
        progressDialog.show();
        productDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.getValue() == null) {
                    noProd.setVisibility(View.VISIBLE);
                } else {
                    updateTableHeader();
                    updateTableBody(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    private void updateTableHeader() {
        /* Create a TableRow dynamically */
        TableRow tr = new TableRow(this);
        tr.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border_ripple));
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        tr.setWeightSum(2);

            /*Params*/
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;


        /* Product Name --> EditText */
        TextView productNameHead = new TextView(this);
        productNameHead.setLayoutParams(params);

        productNameHead.setTextColor(getResources().getColor(R.color.colorAccent));
        productNameHead.setPadding(16, 16, 16, 16);
        productNameHead.setText("Product");
        productNameHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productNameHead.setTypeface(null, Typeface.BOLD);
        productNameHead.setGravity(Gravity.CENTER);
        tr.addView(productNameHead);

        /* Product Price --> EditText */
        EditText productPriceHead = new EditText(this);
        productPriceHead.setLayoutParams(params);

        productPriceHead.setTextColor(getResources().getColor(R.color.colorAccent));
        productPriceHead.setPadding(16, 16, 16, 16);
        productPriceHead.setText("Price");
        productPriceHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productPriceHead.setTypeface(null, Typeface.BOLD);
        productPriceHead.setGravity(Gravity.CENTER);
        productPriceHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productPriceHead); // Adding textView to table-row.

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody(DataSnapshot dataSnapshot) {
        HashMap<String, Object> productDetails = (HashMap<String, Object>) dataSnapshot.getValue();
        noProd.setVisibility(View.GONE);

        assert productDetails != null;
        for (String prodKey : productDetails.keySet()) {
        /* Create a TableRow dynamically */
            TableRow tr = new TableRow(this);
            tr.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border_ripple));
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tr.setWeightSum(2);

            /*Params*/
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;


        /* Product Name --> EditText */
            TextView productName = new TextView(this);
            productName.setLayoutParams(params);

            productName.setTextColor(getResources().getColor(R.color.colorAccent));
            productName.setPadding(16, 16, 16, 16);
            productName.setText(prodKey);
            productName.setGravity(Gravity.CENTER);
            tr.addView(productName);

        /* Product Price --> EditText */
            TextView productPrice = new TextView(this);
            productPrice.setLayoutParams(params);

            productPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            productPrice.setPadding(16, 16, 16, 16);
            productPrice.setText(productDetails.get(prodKey).toString());
            productPrice.setGravity(Gravity.CENTER);
            productPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productPrice); // Adding textView to table-row.

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
