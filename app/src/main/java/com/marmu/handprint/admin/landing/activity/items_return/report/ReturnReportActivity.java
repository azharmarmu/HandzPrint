package com.marmu.handprint.admin.landing.activity.items_return.report;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

@SuppressWarnings({"unchecked", "deprecation"})
public class ReturnReportActivity extends AppCompatActivity {

    TableLayout tableLayout;

    HashMap<String, Object> returnDetails = new HashMap<>();

    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);

    String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_return_report);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);

        String date = getIntent().getStringExtra("date").replace("-", "/");
        route = getIntent().getStringExtra("route");

        getData(date, route);
    }

    private void getData(final String date, final String route) {
        takenDBRef.addValueEventListener(new ValueEventListener() {
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

    private void evaluate(DataSnapshot dataSnapshot, String date, String route) {
        HashMap<String, Object> listTaken = (HashMap<String, Object>) dataSnapshot.getValue();

        TextView noReturn = (TextView) findViewById(R.id.no_return);

        tableLayout.setVisibility(View.GONE);
        noReturn.setVisibility(View.GONE);
        assert listTaken != null;
        for (String key : listTaken.keySet()) {
            HashMap<String, Object> takenDetails = (HashMap<String, Object>) listTaken.get(key);
            try {
                Date dbDate = new SimpleDateFormat("dd/MM/yyyy").parse(takenDetails.get("sales_date").toString());
                Date localDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);

                String dbRoute = takenDetails.get("sales_route").toString();

                if (localDate.compareTo(dbDate) == 0 && dbRoute.equalsIgnoreCase(route)) {
                    returnDetails.put(route, takenDetails);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (returnDetails.size() > 0) {
            tableLayout.setVisibility(View.VISIBLE);
            updateTableHeader();
            updateTableBody();
        } else {
            noReturn.setVisibility(View.VISIBLE);
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


        /* Product Name --> TextView */
        TextView productNameHead = new TextView(this);
        productNameHead.setLayoutParams(params);

        productNameHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productNameHead.setPadding(16, 16, 16, 16);
        productNameHead.setText("Product");
        productNameHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productNameHead.setTypeface(null, Typeface.BOLD);
        productNameHead.setGravity(Gravity.CENTER);
        tr.addView(productNameHead);

        /* Product Price --> TextView */
        TextView productQTYHead = new TextView(this);
        productQTYHead.setLayoutParams(params);

        productQTYHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productQTYHead.setPadding(16, 16, 16, 16);
        productQTYHead.setText("Return-QTY");
        productQTYHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productQTYHead.setTypeface(null, Typeface.BOLD);
        productQTYHead.setGravity(Gravity.CENTER);
        tr.addView(productQTYHead);

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        final HashMap<String, Object> routeDetails = (HashMap<String, Object>) returnDetails.get(route);
        final HashMap<String, Object> productDetails = (HashMap<String, Object>) routeDetails.get("sales_order_qty_left");

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


            /* Product Date --> TextView */
            TextView productName = new TextView(this);
            productName.setLayoutParams(params);

            productName.setTextColor(getResources().getColor(R.color.colorAccent));
            productName.setPadding(16, 16, 16, 16);
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productName.setText(prodKey);
            productName.setGravity(Gravity.CENTER);
            tr.addView(productName);

            /* Product Party-Name --> EditText */
            TextView productQTY = new TextView(this);
            productQTY.setLayoutParams(params);

            productQTY.setTextColor(getResources().getColor(R.color.colorAccent));
            productQTY.setPadding(16, 16, 16, 16);
            productQTY.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productQTY.setText(productDetails.get(prodKey).toString());
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
