package com.marmu.handprint.sales_man.landing.activity.view_stock;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"deprecation", "unchecked"})
public class ViewStockActivity extends AppCompatActivity {

    TableLayout tableLayout;
    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);

    ProgressDialog progressDialog;

    String salesKey, salesMan, salesRoute;

    List<HashMap<String, Object>> salesTakenQTY = new ArrayList<>();
    List<HashMap<String, Object>> salesSold = new ArrayList<>();
    List<HashMap<String, Object>> salesleft = new ArrayList<>();
    List<String> salesRoutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_view_stock);

        salesKey = getIntent().getStringExtra("sales_key");
        salesMan = getIntent().getStringExtra("sales_man_name");
        salesRoute = getIntent().getStringExtra("sales_route");

        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        progressDialog = new ProgressDialog(ViewStockActivity.this);
        progressDialog.setTitle("Loading...");

        getTakenQTY();
    }

    private void getTakenQTY() {
        progressDialog.show();
        takenDBRef.child(salesKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                HashMap<String, Object> salesDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                if (salesDetails != null && salesDetails.get("sales_man_name").toString().equalsIgnoreCase(salesMan)) {
                    salesTakenQTY.add((HashMap<String, Object>) salesDetails.get("sales_order_qty"));
                    salesSold.add((HashMap<String, Object>) salesDetails.get("sales_order_qty_sold"));
                    salesleft.add((HashMap<String, Object>) salesDetails.get("sales_order_qty_left"));
                    salesRoutes.add((String) salesDetails.get("sales_route"));

                    updateTableHeader();
                    updateTableBody();
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
        tr.setWeightSum(4);

            /*Params*/
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;


        /* Product Name --> TextView */
        TextView productNameHead = new TextView(this);
        productNameHead.setLayoutParams(params);

        productNameHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productNameHead.setPadding(16, 16, 16, 16);
        productNameHead.setText("Product");
        productNameHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productNameHead.setTypeface(null, Typeface.BOLD);
        productNameHead.setGravity(Gravity.CENTER);
        tr.addView(productNameHead);

        /* Product Price --> TextView */
        TextView productTakenHead = new TextView(this);
        productTakenHead.setLayoutParams(params);

        productTakenHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productTakenHead.setPadding(16, 16, 16, 16);
        productTakenHead.setText("Taken");
        productTakenHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productTakenHead.setTypeface(null, Typeface.BOLD);
        productTakenHead.setGravity(Gravity.CENTER);
        productTakenHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productTakenHead);

        /* Product Price --> TextView */
        TextView productSoldHead = new TextView(this);
        productSoldHead.setLayoutParams(params);

        productSoldHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productSoldHead.setPadding(16, 16, 16, 16);
        productSoldHead.setText("Sold");
        productSoldHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productSoldHead.setTypeface(null, Typeface.BOLD);
        productSoldHead.setGravity(Gravity.CENTER);
        productSoldHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productSoldHead);

        /* Product Price --> TextView */
        TextView productBalHead = new TextView(this);
        productBalHead.setLayoutParams(params);

        productBalHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productBalHead.setPadding(16, 16, 16, 16);
        productBalHead.setText("Balance");
        productBalHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productBalHead.setTypeface(null, Typeface.BOLD);
        productBalHead.setGravity(Gravity.CENTER);
        productBalHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productBalHead);

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        HashMap<String, Object> productsTakenQTY = salesTakenQTY.get(salesRoutes.indexOf(salesRoute));
        HashMap<String, Object> productsleft = salesleft.get(salesRoutes.indexOf(salesRoute));

        for (String prodKey : productsTakenQTY.keySet()) {
            /* Create a TableRow dynamically */
            TableRow tr = new TableRow(this);
            tr.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border_ripple));
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tr.setWeightSum(4);

            String prodQTY = productsTakenQTY.get(prodKey).toString();
            String prodBal = productsleft.get(prodKey).toString();
            String prodSold = String.valueOf(Integer.parseInt(prodQTY)-Integer.parseInt(prodBal));

            /*Params*/
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;


            /* Product Name --> TextView */
            TextView productName = new TextView(this);
            productName.setLayoutParams(params);

            productName.setTextColor(getResources().getColor(R.color.colorAccent));
            productName.setPadding(16, 16, 16, 16);
            productName.setText(prodKey);
            productName.setGravity(Gravity.CENTER);
            tr.addView(productName);

            /* Product Taken --> EditText */
            EditText productTaken = new EditText(this);
            productTaken.setLayoutParams(params);

            productTaken.setTextColor(getResources().getColor(R.color.colorAccent));
            productTaken.setPadding(16, 16, 16, 16);
            productTaken.setText(prodQTY);
            productTaken.setGravity(Gravity.CENTER);
            productTaken.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productTaken);

            /* Product Sold --> TextView */
            TextView productSold = new TextView(this);
            productSold.setLayoutParams(params);

            productSold.setTextColor(getResources().getColor(R.color.colorAccent));
            productSold.setPadding(16, 16, 16, 16);
            productSold.setText(prodSold);
            productSold.setGravity(Gravity.CENTER);
            productSold.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productSold);

            /* Product Balance --> TextView */
            TextView productBal = new TextView(this);
            productBal.setLayoutParams(params);

            productBal.setTextColor(getResources().getColor(R.color.colorAccent));
            productBal.setPadding(16, 16, 16, 16);
            productBal.setText(prodBal);
            productBal.setGravity(Gravity.CENTER);
            productBal.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productBal);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
