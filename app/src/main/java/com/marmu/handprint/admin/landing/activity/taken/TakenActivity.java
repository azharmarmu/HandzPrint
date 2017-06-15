package com.marmu.handprint.admin.landing.activity.taken;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.admin.landing.activity.price_setup.SetAmountActivity;
import com.marmu.handprint.z_common.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@SuppressWarnings({"deprecation", "unchecked"})
public class TakenActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TextView noProduct;
    DatabaseReference prodDBRef = Constants.DATABASE.getReference(Constants.ADMIN_PRODUCT_PRICE);
    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);
    DatabaseReference salesManDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN);

    ProgressDialog progressDialog;

    List<String> salesMan = new ArrayList<>();
    Spinner sp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_taken);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        noProduct = (TextView) findViewById(R.id.no_product);
        progressDialog = new ProgressDialog(TakenActivity.this);
        progressDialog.setTitle("Loading...");

        getSalesManDetails();
        updateUI();
    }

    private void getSalesManDetails() {
        salesManDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sp_name = (Spinner) findViewById(R.id.sp_name);
                if (dataSnapshot.getValue() != null) {
                    HashMap<String, Object> salesManDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : salesManDetails.keySet()) {
                        HashMap<String, Object> sales = (HashMap<String, Object>) salesManDetails.get(key);
                        String name = sales.get("name").toString();
                        if (!salesMan.contains(name)) {
                            salesMan.add(name);
                        }
                    }

                    //sp_name
                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(TakenActivity.this,
                            android.R.layout.simple_spinner_item, salesMan);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_name.setAdapter(nameAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    private void updateUI() {
        progressDialog.show();
        prodDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.getValue() == null) {
                    noProduct.setVisibility(View.VISIBLE);
                } else {
                    noProduct.setVisibility(View.GONE);
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
        productQTYHead.setText("QTY");
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


        /* Product Name --> TextView */
            TextView productName = new TextView(this);
            productName.setLayoutParams(params);

            productName.setTextColor(getResources().getColor(R.color.colorAccent));
            productName.setPadding(16, 16, 16, 16);
            productName.setText(prodKey);
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            productName.setGravity(Gravity.CENTER);
            tr.addView(productName);

        /* Product QTY --> EditText */
            EditText productQTY = new EditText(this);
            productQTY.setLayoutParams(params);

            productQTY.setTextColor(getResources().getColor(R.color.colorAccent));
            productQTY.setPadding(16, 16, 16, 16);
            productQTY.setText("0");
            productQTY.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            productQTY.setGravity(Gravity.CENTER);
            productQTY.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productQTY); // Adding textView to table-row.

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void submitTaken(View view) {

        final EditText salesRoute = (EditText) findViewById(R.id.et_route);


        if (sp_name.getSelectedItem() == null || TextUtils.isEmpty(salesRoute.getText())) {
            if (TextUtils.isEmpty(salesRoute.getText())) {
                salesRoute.setError("Sales Route is Mandatory");
                salesRoute.requestFocus();
            } else {
                Toast.makeText(getApplicationContext(), "Add sales Man", Toast.LENGTH_SHORT).show();
            }
        } else {
            HashMap<String, Object> itemsQTY = new HashMap<>();
            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                if (tableRow != null) {
                    TextView productName = (TextView) tableRow.getChildAt(0);
                    String prodName = productName.getText().toString().replace("/", "_");
                    EditText productQTY = (EditText) tableRow.getChildAt(1);
                    String prodQTY = productQTY.getText().toString();
                    if (!prodName.isEmpty() && !prodQTY.isEmpty() && Integer.parseInt(prodQTY) > 0) {
                        itemsQTY.put(prodName, prodQTY);
                    }
                }
            }
            if (itemsQTY.size() > 0) {
                HashMap<String, Object> takenDetails = new HashMap<>();
                takenDetails.put("sales_man_name", sp_name.getSelectedItem().toString());
                takenDetails.put("sales_route", salesRoute.getText().toString());
                takenDetails.put("sales_order_qty", itemsQTY);
                takenDetails.put("sales_order_qty_left", itemsQTY);
                takenDetails.put("process", "start");
                takenDetails.put("sales_date", new SimpleDateFormat("dd/MM/yyyy")
                        .format(new Date(System.currentTimeMillis())));
                takenDBRef.push().updateChildren(takenDetails);
                finish();
            }
        }
    }

    public void setProduct(View view) {
        startActivity(new Intent(TakenActivity.this, SetAmountActivity.class));
    }

    public void backPress(View view) {
        onBackPressed();
    }

}
