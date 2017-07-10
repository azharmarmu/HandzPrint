package com.marmu.handprint.admin.landing.activity.sales.party.report;

import android.annotation.SuppressLint;
import android.content.Intent;
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
public class PartyReportActivity extends AppCompatActivity {

    TableLayout tableLayout;

    HashMap<String, Object> partyWiseDetails = new HashMap<>();
    DatabaseReference billingDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN_BILLING);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_party_report);
        tableLayout = findViewById(R.id.table_layout);

        String date = getIntent().getStringExtra("date").replace("-", "/");
        String route = getIntent().getStringExtra("route");

        getData(date, route);
    }

    private void getData(final String date, final String route) {
        billingDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    evaluate(dataSnapshot, date, route);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void evaluate(DataSnapshot dataSnapshot, String date, String route) {
        HashMap<String, Object> listParty = (HashMap<String, Object>) dataSnapshot.getValue();

        TextView noSale = findViewById(R.id.no_sales);

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

                    String dbRoute = partyDetails.get("sales_route").toString();
                    String name = partyDetails.get("party_name").toString();

                    if (localDate.compareTo(dbDate) == 0 && dbRoute.equalsIgnoreCase(route)) {
                        partyWiseDetails.put(name, partyDetails);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (partyWiseDetails.size() > 0) {
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
        tr.setWeightSum(3);

            /*Params*/
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;


        /* Product Date --> TextView */
        TextView productNameHead = new TextView(this);
        productNameHead.setLayoutParams(params);

        productNameHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productNameHead.setPadding(16, 16, 16, 16);
        productNameHead.setText("Date");
        productNameHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productNameHead.setTypeface(null, Typeface.BOLD);
        productNameHead.setGravity(Gravity.CENTER);
        tr.addView(productNameHead);

        /* Product Price --> TextView */
        TextView productQTYHead = new TextView(this);
        productQTYHead.setLayoutParams(params);

        productQTYHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productQTYHead.setPadding(16, 16, 16, 16);
        productQTYHead.setText("Party");
        productQTYHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productQTYHead.setTypeface(null, Typeface.BOLD);
        productQTYHead.setGravity(Gravity.CENTER);
        productQTYHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productQTYHead);

        /* Product Price --> TextView */
        TextView productPriceHead = new TextView(this);
        productPriceHead.setLayoutParams(params);

        productPriceHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productPriceHead.setPadding(16, 16, 16, 16);
        productPriceHead.setText("Amount");
        productPriceHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productPriceHead.setTypeface(null, Typeface.BOLD);
        productPriceHead.setGravity(Gravity.CENTER);
        productPriceHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productPriceHead);

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        for (String prodKey : partyWiseDetails.keySet()) {
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


            final HashMap<String, Object> details = (HashMap<String, Object>) partyWiseDetails.get(prodKey);

            /* Product Date --> TextView */
            TextView productName = new TextView(this);
            productName.setLayoutParams(params);

            productName.setTextColor(getResources().getColor(R.color.colorAccent));
            productName.setPadding(16, 16, 16, 16);
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productName.setText(details.get("date").toString());
            productName.setGravity(Gravity.CENTER);
            tr.addView(productName);

            /* Product Party-Name --> EditText */
            TextView productQTY = new TextView(this);
            productQTY.setLayoutParams(params);

            productQTY.setTextColor(getResources().getColor(R.color.colorAccent));
            productQTY.setPadding(16, 16, 16, 16);
            productQTY.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productQTY.setText(details.get("party_name").toString());
            productQTY.setGravity(Gravity.CENTER);
            tr.addView(productQTY);

            /* Product Amount --> TextView */
            TextView productPrice = new TextView(this);
            productPrice.setLayoutParams(params);

            productPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            productPrice.setPadding(16, 16, 16, 16);
            productPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productPrice.setText(details.get("total").toString());
            productPrice.setGravity(Gravity.CENTER);
            tr.addView(productPrice);

            //onclick
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent report = new Intent(PartyReportActivity.this, ReportParty.class);
                    report.putExtra("product", details);
                    report.putExtra("total", details.get("total").toString());
                    startActivity(report);
                }
            });

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
