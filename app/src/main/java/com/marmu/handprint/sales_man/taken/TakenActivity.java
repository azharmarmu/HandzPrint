package com.marmu.handprint.sales_man.taken;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.landing.activity.SalesManLandingActivity;
import com.marmu.handprint.z_common.Constants;
import com.marmu.handprint.z_common.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public class TakenActivity extends AppCompatActivity {

    TableLayout tableLayout;
    private String salesMan, saleKey;
    List<String> salesProcess = new ArrayList<>();
    List<String> salesRoute = new ArrayList<>();
    List<String> salesKey = new ArrayList<>();
    List<HashMap<String, Object>> salesOrderQty = new ArrayList<>();
    Spinner sp_route;
    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_taken);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        sp_route = (Spinner) findViewById(R.id.sp_route);
        salesMan = getIntent().getStringExtra("sales_man_name");
        progressDialog = new ProgressDialog(TakenActivity.this);
        progressDialog.setTitle("Loading...");
        setRoute();
        sp_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tableLayout.removeAllViews();
                updateTableHeader();
                updateTableBody();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private void setRoute() {
        //get salesman_name
        takenDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> salesManDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                LinearLayout routeContainer = (LinearLayout) findViewById(R.id.route_container);
                TextView noSales = (TextView) findViewById(R.id.no_sales);
                routeContainer.setVisibility(View.GONE);
                noSales.setVisibility(View.GONE);

                assert salesManDetails != null;
                for (String key : salesManDetails.keySet()) {
                    HashMap<String, Object> sales = (HashMap<String, Object>) salesManDetails.get(key);
                    String currentDate = new SimpleDateFormat("dd/MM/yyyy")
                            .format(new Date(System.currentTimeMillis()));
                    String salesDate = sales.get("sales_date").toString();
                    if (sales.get("sales_man_name").toString().equalsIgnoreCase(salesMan)
                            && salesDate.equals(currentDate)) {
                        salesKey.add(key);
                        salesProcess.add((String) sales.get("process"));
                        salesRoute.add((String) sales.get("sales_route"));
                        salesOrderQty.add((HashMap<String, Object>) sales.get("sales_order_qty_left"));
                    }
                }

                if (salesRoute.size() > 0) {
                    routeContainer.setVisibility(View.VISIBLE);
                    //sp_route
                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(TakenActivity.this,
                            android.R.layout.simple_spinner_item, salesRoute);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_route.setAdapter(nameAdapter);

                    buttonContext();
                } else {
                    noSales.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    private void buttonContext() {
        int i = salesRoute.indexOf(sp_route.getSelectedItem().toString());
        TextView start = (TextView) findViewById(R.id.tv_submit);
        if (salesProcess.get(i).equalsIgnoreCase("start")) {
            start.setText("Start");
        } else if (salesProcess.get(i).equalsIgnoreCase("closed")) {
            start.setText("Closed");
            start.setClickable(false);
        } else {
            start.setText("Continue");
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


        /* Product Name --> EditText */
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

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        int i = salesRoute.indexOf(sp_route.getSelectedItem().toString());
        HashMap<String, Object> productDetails = salesOrderQty.get(i);
        saleKey = salesKey.get(i);

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

            /* Product Price --> TextView */
            TextView productQTY = new TextView(this);
            productQTY.setLayoutParams(params);

            productQTY.setTextColor(getResources().getColor(R.color.colorAccent));
            productQTY.setPadding(16, 16, 16, 16);
            productQTY.setText(productDetails.get(prodKey).toString());
            productQTY.setGravity(Gravity.CENTER);
            productQTY.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(productQTY);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }
    }

    public void startTaken(View view) {
        TextView start = (TextView) findViewById(R.id.tv_submit);
        if (start.getText().toString().equalsIgnoreCase("start")) {
            takenDBRef.child(saleKey).child("process").setValue("started");
        }
        Intent landing = new Intent(TakenActivity.this, SalesManLandingActivity.class);
        landing.putExtra("sales_key", saleKey);
        landing.putExtra("sales_man_name", salesMan);
        landing.putExtra("sales_route", sp_route.getSelectedItem().toString());
        startActivity(landing);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void signOut(View view) {
        Constants.AUTH.signOut();
        startActivity(new Intent(TakenActivity.this, LoginActivity.class));
        finish();
    }
}
