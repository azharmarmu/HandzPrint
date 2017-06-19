package com.marmu.handprint.sales_man.landing.activity.billing.add;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.marmu.handprint.sales_man.landing.activity.billing.view_delete_edit.ViewDeleteEditBillingActivity;
import com.marmu.handprint.z_common.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public class AddBillingActivity extends AppCompatActivity {

    TableLayout tableLayout;
    DatabaseReference priceDBRef = Constants.DATABASE.getReference(Constants.ADMIN_PRODUCT_PRICE);
    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);
    DatabaseReference billingDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN_BILLING);

    ProgressDialog progressDialog;

    HashMap<String, Object> priceProd = new HashMap<>();
    HashMap<String, Object> billItem = new HashMap<>();

    private int total;

    private String salesKey;
    private String salesMan;
    private String salesRoute;

    HashMap<String, Object> productDetails = new HashMap<>();
    List<String> salesRoutes = new ArrayList<>();
    List<HashMap<String, Object>> salesOrderQty = new ArrayList<>();

    HashMap<String, Object> leftQty = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_add_billing);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        progressDialog = new ProgressDialog(AddBillingActivity.this);
        progressDialog.setTitle("Loading...");

        salesKey = getIntent().getStringExtra("sales_key");
        salesMan = getIntent().getStringExtra("sales_man_name");
        salesRoute = getIntent().getStringExtra("sales_route");

        fetchPriceOfProd();
        getRoute();
    }

    private void fetchPriceOfProd() {
        priceDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                priceProd = (HashMap<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    private void getRoute() {
        progressDialog.show();
        takenDBRef.child(salesKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                HashMap<String, Object> salesDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                if (salesDetails != null && salesDetails.get("sales_man_name").toString().equalsIgnoreCase(salesMan)) {
                    salesRoutes.add((String) salesDetails.get("sales_route"));
                    salesOrderQty.add((HashMap<String, Object>) salesDetails.get("sales_order_qty_left"));

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
        tr.setWeightSum(3);

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
        TextView productQTYHead = new TextView(this);
        productQTYHead.setLayoutParams(params);

        productQTYHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productQTYHead.setPadding(16, 16, 16, 16);
        productQTYHead.setText("QTY");
        productQTYHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productQTYHead.setTypeface(null, Typeface.BOLD);
        productQTYHead.setGravity(Gravity.CENTER);
        productQTYHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productQTYHead);

        /* Product Price --> TextView */
        TextView productPriceHead = new TextView(this);
        productPriceHead.setLayoutParams(params);

        productPriceHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productPriceHead.setPadding(16, 16, 16, 16);
        productPriceHead.setText("Sub Total");
        productPriceHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productPriceHead.setTypeface(null, Typeface.BOLD);
        productPriceHead.setGravity(Gravity.CENTER);
        productPriceHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productPriceHead);

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        productDetails = salesOrderQty.get(salesRoutes.indexOf(salesRoute));

        for (String prodKey : productDetails.keySet()) {
            /* Create a TableRow dynamically */
            TableRow tr = new TableRow(this);
            tr.setBackground(getResources().getDrawable(R.drawable.box_white_thick_border_ripple));
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tr.setWeightSum(3);

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

            /* Product Price --> EditText */
            EditText productQTY = new EditText(this);
            productQTY.setLayoutParams(params);

            productQTY.setTextColor(getResources().getColor(R.color.colorAccent));
            productQTY.setPadding(16, 16, 16, 16);
            productQTY.setText("0");
            productQTY.setGravity(Gravity.CENTER);
            productQTY.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productQTY);

            /* Product Price --> TextView */
            TextView productPrice = new TextView(this);
            productPrice.setLayoutParams(params);

            productPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            productPrice.setPadding(16, 16, 16, 16);
            productPrice.setText("0");
            productPrice.setGravity(Gravity.CENTER);
            productPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productPrice);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }

        addQTy();
    }

    private void addQTy() {
        final HashMap<String, Object> prodDetails = new HashMap<>();
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            final HashMap<String, Object> itemDetails = new HashMap<>();
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            if (tableRow != null) {
                final TextView productName = (TextView) tableRow.getChildAt(0);
                final String prodName = productName.getText().toString().replace("/", "_");
                final EditText productQTY = (EditText) tableRow.getChildAt(1);
                final TextView productPrice = (TextView) tableRow.getChildAt(2);
                productQTY.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        String prodQTY = productQTY.getText().toString();
                        TextView totalBill = (TextView) findViewById(R.id.tv_billing_total);
                        if (priceProd.containsKey(prodName) &&
                                !prodQTY.isEmpty() &&
                                Integer.parseInt(prodQTY) <= Integer.parseInt(productDetails.get(prodName).toString())) {
                            int price = Integer.parseInt(prodQTY) *
                                    Integer.parseInt(priceProd.get(prodName).toString());
                            if (!prodName.isEmpty() && !prodQTY.isEmpty() && Integer.parseInt(prodQTY) > 0) {
                                billItem.remove("product");
                                total -= price;
                                totalBill.setText(String.valueOf(total));
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String prodQTY = productQTY.getText().toString();
                        if (priceProd.containsKey(prodName) &&
                                !prodQTY.isEmpty() &&
                                Integer.parseInt(prodQTY) > Integer.parseInt(productDetails.get(prodName).toString())) {
                            productQTY.setError("Taken good less");
                            productQTY.requestFocus();
                            productPrice.setText(String.valueOf(0));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String prodQTY = productQTY.getText().toString();
                        TextView totalBill = (TextView) findViewById(R.id.tv_billing_total);

                        if (priceProd.containsKey(prodName) &&
                                !prodQTY.isEmpty() &&
                                Integer.parseInt(prodQTY) <= Integer.parseInt(productDetails.get(prodName).toString())) {
                            int price = Integer.parseInt(prodQTY) *
                                    Integer.parseInt(priceProd.get(prodName).toString());
                            productPrice.setText(String.valueOf(price));

                            if (!prodName.isEmpty() && !prodQTY.isEmpty() && Integer.parseInt(prodQTY) > 0) {
                                leftQty.put(prodName, String.valueOf(
                                        Integer.parseInt(productDetails.get(prodName).toString()) -
                                                Integer.parseInt(prodQTY)));
                                itemDetails.put("prod_qty", prodQTY);
                                itemDetails.put("prod_name", prodName);
                                itemDetails.put("prod_sub_total", productPrice.getText().toString());
                                prodDetails.put(prodName, itemDetails);
                                billItem.put("product", prodDetails);
                                total += price;
                                totalBill.setText(String.valueOf(total));
                            }
                        } else {
                            productPrice.setText("");
                        }
                    }
                });
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void generateBill(View view) {

        final EditText partyName = (EditText) findViewById(R.id.et_party_name);

        if (TextUtils.isEmpty(partyName.getText())) {
            partyName.setError("Party Name is Mandatory");
            partyName.requestFocus();
        } else {
            if (billItem.size() > 0) {
                billItem.put("total", String.valueOf(total));
                billItem.put("sales_key", salesKey);
                billItem.put("sales_man_name", salesMan);
                billItem.put("sales_route", salesRoute);
                billItem.put("party_name", partyName.getText().toString());
                billItem.put("date", new SimpleDateFormat("dd/MM/yyyy")
                        .format(new Date(System.currentTimeMillis())));
                billingDBRef.child(salesKey).child(partyName.getText().toString()).updateChildren(billItem);
                takenDBRef.child(salesKey).child("sales_order_qty_left").updateChildren(leftQty);


                Intent billList = new Intent(AddBillingActivity.this, ViewDeleteEditBillingActivity.class);
                billList.putExtra("sales_key", salesKey);
                billList.putExtra("sales_man_name", salesMan);
                billList.putExtra("sales_route", salesRoute);
                startActivity(billList);
                finish();
            }
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }

}
