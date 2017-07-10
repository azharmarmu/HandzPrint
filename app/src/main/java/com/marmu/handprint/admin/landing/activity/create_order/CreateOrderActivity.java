package com.marmu.handprint.admin.landing.activity.create_order;

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
import android.widget.AdapterView;
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
public class CreateOrderActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TextView noProduct;
    DatabaseReference priceDBRef = Constants.DATABASE.getReference(Constants.ADMIN_PRODUCT_PRICE);
    DatabaseReference orderDBRef = Constants.DATABASE.getReference(Constants.ADMIN_ORDER);
    DatabaseReference salesManDBRef = Constants.DATABASE.getReference(Constants.SALES_MAN);
    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);

    ProgressDialog progressDialog;

    List<String> salesMan = new ArrayList<>();

    Spinner sp_name;
    Spinner sp_route;

    HashMap<String, Object> priceProd = new HashMap<>();
    HashMap<String, Object> productDetails = new HashMap<>();

    List<HashMap<String, Object>> salesOrderQty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_order);
        tableLayout = findViewById(R.id.table_layout);
        noProduct = findViewById(R.id.no_product);
        progressDialog = new ProgressDialog(CreateOrderActivity.this);
        progressDialog.setTitle("Loading...");

        sp_name = findViewById(R.id.sp_name);
        sp_route = findViewById(R.id.sp_route);

        fetchPriceOfProd();

        getSalesManDetails();
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

    private void getSalesManDetails() {
        salesManDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(CreateOrderActivity.this,
                            android.R.layout.simple_spinner_item, salesMan);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_name.setAdapter(nameAdapter);

                    sp_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            setRoute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void setRoute() {
        //get salesman_name
        takenDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    noProduct.setVisibility(View.GONE);
                    List<String> salesRoute = new ArrayList<>();
                    HashMap<String, Object> salesManDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : salesManDetails.keySet()) {
                        HashMap<String, Object> sales = (HashMap<String, Object>) salesManDetails.get(key);
                        String currentDate = new SimpleDateFormat("dd/MM/yyyy")
                                .format(new Date(System.currentTimeMillis()));
                        String salesDate = sales.get("sales_date").toString();
                        if (sales.get("sales_man_name").toString().equalsIgnoreCase(sp_name.getSelectedItem().toString())
                                && salesDate.equals(currentDate)) {
                            salesRoute.add(sales.get("sales_route").toString());
                            salesOrderQty.add((HashMap<String, Object>) sales.get("sales_order_qty_left"));
                        }
                    }

                    if (salesRoute.size() > 0) {
                        //sp_route
                        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(CreateOrderActivity.this,
                                android.R.layout.simple_spinner_item, salesRoute);
                        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_route.setAdapter(nameAdapter);

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
                } else {
                    noProduct.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        productDetails = salesOrderQty.get(sp_route.getSelectedItemPosition());

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

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }

        addQTy();
    }

    private void addQTy() {
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            if (tableRow != null) {
                final TextView productName = (TextView) tableRow.getChildAt(0);
                final String prodName = productName.getText().toString().replace("/", "_");
                final EditText productQTY = (EditText) tableRow.getChildAt(1);
                productQTY.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String prodQTY = productQTY.getText().toString();
                        if (priceProd.containsKey(prodName) &&
                                !prodQTY.isEmpty() &&
                                Integer.parseInt(prodQTY) > Integer.parseInt(productDetails.get(prodName).toString())) {
                            productQTY.setError("Taken good less");
                            productQTY.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void submitTaken(View view) {

        final EditText partyName = findViewById(R.id.et_party_name);


        if (sp_route.getSelectedItem() == null ||
                sp_name.getSelectedItem() == null ||
                TextUtils.isEmpty(partyName.getText())) {
            if (TextUtils.isEmpty(partyName.getText())) {
                partyName.setError("Party Name is Mandatory");
                partyName.requestFocus();
            } else if (sp_route.getSelectedItem() == null) {
                Toast.makeText(getApplicationContext(), "Add sales route", Toast.LENGTH_SHORT).show();
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
                HashMap<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("sales_man_name", sp_name.getSelectedItem().toString());
                orderDetails.put("sales_route", sp_route.getSelectedItem().toString());
                orderDetails.put("sales_order_qty", itemsQTY);
                orderDetails.put("partyName", partyName.getText().toString());
                orderDetails.put("process", "start");
                orderDetails.put("sales_date", new SimpleDateFormat("dd/MM/yyyy")
                        .format(new Date(System.currentTimeMillis())));
                orderDBRef.push().updateChildren(orderDetails);
                finish();
            }
        }
    }

    public void setProduct(View view) {
        startActivity(new Intent(CreateOrderActivity.this, SetAmountActivity.class));
    }

    public void backPress(View view) {
        onBackPressed();
    }

}
