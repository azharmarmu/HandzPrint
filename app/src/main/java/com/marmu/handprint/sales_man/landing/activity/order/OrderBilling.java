package com.marmu.handprint.sales_man.landing.activity.order;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.model.OrderList;
import com.marmu.handprint.z_common.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings({"deprecation", "unchecked"})
public class OrderBilling extends AppCompatActivity {

    OrderList orderList;

    DatabaseReference priceDBRef = Constants.DATABASE.getReference(Constants.ADMIN_PRODUCT_PRICE);
    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);
    DatabaseReference orderDBRef = Constants.DATABASE.getReference(Constants.ADMIN_ORDER);
    DatabaseReference billingDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN_BILLING);

    HashMap<String, Object> priceProd = new HashMap<>();
    HashMap<String, Object> leftQty = new HashMap<>();
    TableLayout tableLayout;
    TextView totalBill;

    private String salesKey;
    private String salesMan;
    private String salesRoute;


    HashMap<String, Object> products = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_order_biliing);
        tableLayout = findViewById(R.id.table_layout);
        totalBill = findViewById(R.id.tv_billing_total);

        orderList = (OrderList) getIntent().getSerializableExtra("orderBill");

        salesKey = getIntent().getStringExtra("sales_key");
        salesMan = getIntent().getStringExtra("sales_man_name");
        salesRoute = getIntent().getStringExtra("sales_route");

        getLeftQty();
        fetchPriceOfProd();
    }

    private void fetchPriceOfProd() {
        priceDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                priceProd = (HashMap<String, Object>) dataSnapshot.getValue();
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    private void updateUI() {
        EditText partyName = findViewById(R.id.et_party_name);
        partyName.setText(orderList.getPartyName());
        partyName.setSelection(partyName.getText().toString().length());
        updateTableHeader();
        updateTableBody();
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
        HashMap<String, Object> productDetails = orderList.getSalesOrderQTY();
        int total = 0;
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

            String prodQTY = productDetails.get(prodKey).toString();
            /* Product Price --> EditText */
            EditText productQTY = new EditText(this);
            productQTY.setLayoutParams(params);

            productQTY.setTextColor(getResources().getColor(R.color.colorAccent));
            productQTY.setPadding(16, 16, 16, 16);
            productQTY.setText(prodQTY);
            productQTY.setGravity(Gravity.CENTER);
            productQTY.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productQTY);

            String prodPrice = priceProd.get(prodKey).toString();
            int subTotal = Integer.parseInt(prodPrice) * Integer.parseInt(prodQTY);
            total = total + subTotal;

            String left = leftQty.get(prodKey).toString();
            leftQty.put(prodKey, String.valueOf(Integer.parseInt(left) - Integer.parseInt(prodQTY)));

            /* Product Price --> TextView */
            TextView productPrice = new TextView(this);
            productPrice.setLayoutParams(params);

            productPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            productPrice.setPadding(16, 16, 16, 16);
            productPrice.setText(String.valueOf(subTotal));
            productPrice.setGravity(Gravity.CENTER);
            productPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productPrice);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);

            HashMap<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("prod_name", prodKey);
            itemDetails.put("prod_price", priceProd.get(prodKey));
            itemDetails.put("prod_qty", prodQTY);
            itemDetails.put("prod_sub_total", subTotal);
            products.put(prodKey, itemDetails);
        }


        totalBill.setText(String.valueOf(total));

    }

    private void getLeftQty() {
        takenDBRef.child(salesKey).child("sales_order_qty_left").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    leftQty = (HashMap<String, Object>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void generateBill(View view) {
        final EditText partyName = findViewById(R.id.et_party_name);

        if (TextUtils.isEmpty(partyName.getText())) {
            partyName.setError("Party Name is Mandatory");
            partyName.requestFocus();
        } else {


            HashMap<String, Object> billItem = new HashMap<>();
            billItem.put("total", totalBill.getText().toString());
            billItem.put("sales_key", salesKey);
            billItem.put("product", products);
            billItem.put("sales_man_name", salesMan);
            billItem.put("sales_route", salesRoute);
            billItem.put("party_name", partyName.getText().toString());
            billItem.put("date", new SimpleDateFormat("dd/MM/yyyy")
                    .format(new Date(System.currentTimeMillis())));
            billingDBRef.child(salesKey).child(partyName.getText().toString()).updateChildren(billItem);
            takenDBRef.child(salesKey).child("sales_order_qty_left").updateChildren(leftQty);
            orderDBRef.child(orderList.getKey()).child("process").setValue("close");
            finish();
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
