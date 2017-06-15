package com.marmu.handprint.admin.landing.activity.sales.product.report;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.z_common.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings({"deprecation", "unchecked"})
public class ProductReportActivity extends AppCompatActivity {

    TableLayout tableLayout;

    HashMap<String, Object> productWiseDetails = new HashMap<>();
    DatabaseReference billingDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN_BILLING);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_report);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);

        String date = getIntent().getStringExtra("date").replace("-", "/");

        getData(date);
    }

    private void getData(final String date) {
        billingDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    evaluate(dataSnapshot, date);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    private void evaluate(DataSnapshot dataSnapshot, String date) {
        HashMap<String, Object> listParty = (HashMap<String, Object>) dataSnapshot.getValue();

        TextView noSale = (TextView) findViewById(R.id.no_sales);

        tableLayout.setVisibility(View.GONE);
        noSale.setVisibility(View.GONE);
        assert listParty != null;
        for (String key : listParty.keySet()) {
            HashMap<String, Object> partyName = (HashMap<String, Object>) listParty.get(key);
            for (String partyKey : partyName.keySet()) {
                HashMap<String, Object> partyDetails = (HashMap<String, Object>) partyName.get(partyKey);
                try {
                    Date dbDate = new SimpleDateFormat("dd/MM/yyyy").parse(partyDetails.get("date").toString());
                    Date localDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);

                    if (localDate.compareTo(dbDate) == 0) {
                        HashMap<String, Object> product = (HashMap<String, Object>) partyDetails.get("product");
                        for (String prodKey : product.keySet()) {
                            HashMap<String, Object> productDetails = (HashMap<String, Object>) product.get(prodKey);
                            int i = 0;
                            if (productWiseDetails.containsKey(prodKey)) {
                                i = Integer.parseInt(productWiseDetails.get(prodKey).toString());
                            }
                            i = i + Integer.parseInt(productDetails.get("prod_qty").toString());

                            productWiseDetails.put(prodKey, String.valueOf(i));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (productWiseDetails.size() > 0) {
            tableLayout.setVisibility(View.VISIBLE);
            updateTableHeader();
            updateTableBody();
        } else {
            noSale.setVisibility(View.VISIBLE);
        }
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
        productQTYHead.setText("Sales QTY");
        productQTYHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productQTYHead.setTypeface(null, Typeface.BOLD);
        productQTYHead.setGravity(Gravity.CENTER);
        productQTYHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productQTYHead);

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        for (String prodKey : productWiseDetails.keySet()) {
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
            TextView productName = new TextView(this);
            productName.setLayoutParams(params);

            productName.setTextColor(getResources().getColor(R.color.colorAccent));
            productName.setPadding(16, 16, 16, 16);
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productName.setText(prodKey);
            productName.setGravity(Gravity.CENTER);
            tr.addView(productName);

            /* Product QTY --> EditText */
            TextView productQTY = new TextView(this);
            productQTY.setLayoutParams(params);

            productQTY.setTextColor(getResources().getColor(R.color.colorAccent));
            productQTY.setPadding(16, 16, 16, 16);
            productQTY.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productQTY.setText(productWiseDetails.get(prodKey).toString());
            productQTY.setGravity(Gravity.CENTER);
            tr.addView(productQTY);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
