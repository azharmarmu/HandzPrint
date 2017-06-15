package com.marmu.handprint.admin.landing.activity.price_setup;

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
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.Constants;

import java.util.HashMap;

@SuppressWarnings({"deprecation", "unchecked"})
public class SetAmountActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TextView addProd;
    DatabaseReference productDBRef = Constants.DATABASE.getReference(Constants.ADMIN_PRODUCT_PRICE);
    DatabaseReference qtyDBRef = Constants.DATABASE.getReference(Constants.ADMIN_PRODUCT_QTY);

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_price_set_amount);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        addProd = (TextView) findViewById(R.id.tv_add_prod);
        progressDialog = new ProgressDialog(SetAmountActivity.this);
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
                    addProd.setText("No Product --> ADD");
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
        tr.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border));
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        tr.setWeightSum(2);

            /*Params*/
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;


        /* Product Product --> TextView */
        TextView productNameHead = new TextView(this);
        productNameHead.setLayoutParams(params);

        productNameHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productNameHead.setPadding(16, 16, 16, 16);
        productNameHead.setText("Product");
        productNameHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productNameHead.setTypeface(null, Typeface.BOLD);
        productNameHead.setGravity(Gravity.CENTER);
        tr.addView(productNameHead);

        /* Product QTY --> TextView */
        TextView productQTYHead = new TextView(this);
        productQTYHead.setLayoutParams(params);

        productQTYHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productQTYHead.setPadding(16, 16, 16, 16);
        productQTYHead.setText("Price");
        productQTYHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productQTYHead.setTypeface(null, Typeface.BOLD);
        productQTYHead.setGravity(Gravity.CENTER);
        productQTYHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productQTYHead);

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody(DataSnapshot dataSnapshot) {
        HashMap<String, Object> productDetails = (HashMap<String, Object>) dataSnapshot.getValue();
        addProd.setText("Add More");

        assert productDetails != null;
        for (String prodKey : productDetails.keySet()) {
        /* Create a TableRow dynamically */
            TableRow tr = new TableRow(this);
            tr.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border));
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            tr.setWeightSum(2);

            /*Params*/
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;


        /* Product Name --> EditText */
            EditText productName = new EditText(this);
            productName.setLayoutParams(params);

            productName.setTextColor(getResources().getColor(R.color.colorAccent));
            productName.setPadding(16, 16, 16, 16);
            productName.setText(prodKey);
            productName.setGravity(Gravity.CENTER);
            tr.addView(productName);

        /* Product Price --> EditText */
            EditText productPrice = new EditText(this);
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

    public void addMore(View view) {
        addProd.setText("Add More");
        /* Create a TableRow dynamically */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        tr.setWeightSum(2);

        /* Product Name --> EditText */
        EditText productName = new EditText(this);
        productName.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f
        ));
        productName.setTextColor(getResources().getColor(R.color.colorAccent));
        productName.setPadding(16, 16, 16, 16);
        productName.setGravity(View.TEXT_ALIGNMENT_CENTER);
        productName.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border));
        tr.addView(productName);  // Adding textView to table-row.

        /* Product Price --> EditText */
        EditText productPrice = new EditText(this);
        productPrice.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f
        ));
        productPrice.setTextColor(getResources().getColor(R.color.colorAccent));
        productPrice.setPadding(16, 16, 16, 16);
        productPrice.setGravity(View.TEXT_ALIGNMENT_CENTER);
        productPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        productPrice.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border));
        tr.addView(productPrice); // Adding textView to table-row.

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    public void setAmount(View view) {
        progressDialog.show();
        productDBRef.removeValue();
        qtyDBRef.removeValue();
        HashMap<String, Object> itemsPrice = new HashMap<>();
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            if (tableRow != null) {
                EditText productName = (EditText) tableRow.getChildAt(0);
                String prodName = productName.getText().toString().replace("/", "_");
                EditText productPrice = (EditText) tableRow.getChildAt(1);
                String prodPrice = productPrice.getText().toString();
                if (!prodName.isEmpty() && !prodPrice.isEmpty() && Integer.parseInt(prodPrice) > 0) {
                    itemsPrice.put(prodName, prodPrice);
                }
            }
        }
        //update to DB
        if (itemsPrice.size() > 0) {
            productDBRef.updateChildren(itemsPrice);
        }
        progressDialog.dismiss();
        finish();
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
