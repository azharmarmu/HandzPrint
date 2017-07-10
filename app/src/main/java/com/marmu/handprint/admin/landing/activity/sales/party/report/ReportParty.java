package com.marmu.handprint.admin.landing.activity.sales.party.report;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.marmu.handprint.R;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "deprecation", "ConstantConditions"})
public class ReportParty extends AppCompatActivity {

    TableLayout tableLayout;

    Map<String, Object> partyDetails = new HashMap<>();
    Map<String, Object> report = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_party);
        tableLayout = findViewById(R.id.table_layout);

        report = (Map<String, Object>) getIntent().getSerializableExtra("product");
        partyDetails = (HashMap<String, Object>) report.get("product");

        TextView billTotal = findViewById(R.id.tv_billing_total);
        billTotal.setText(getIntent().getExtras().getString("total"));

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
        productNameHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productNameHead.setTypeface(null, Typeface.BOLD);
        productNameHead.setGravity(Gravity.CENTER);
        tr.addView(productNameHead);

        /* Product Price --> TextView */
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

        /* Product Price --> TextView */
        TextView productPriceHead = new TextView(this);
        productPriceHead.setLayoutParams(params);

        productPriceHead.setTextColor(getResources().getColor(R.color.colorBlack));
        productPriceHead.setPadding(16, 16, 16, 16);
        productPriceHead.setText("Price");
        productPriceHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        productPriceHead.setTypeface(null, Typeface.BOLD);
        productPriceHead.setGravity(Gravity.CENTER);
        productPriceHead.setInputType(InputType.TYPE_CLASS_NUMBER);
        tr.addView(productPriceHead);

        // Add the TableRow to the TableLayout
        tableLayout.addView(tr);
    }

    private void updateTableBody() {
        for (String prodKey : partyDetails.keySet()) {
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


            HashMap<String, Object> localProdDetails = (HashMap<String, Object>) partyDetails.get(prodKey);

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
            productQTY.setText(localProdDetails.get("prod_qty").toString());
            productQTY.setGravity(Gravity.CENTER);
            tr.addView(productQTY);

            /* Product Amount --> TextView */
            TextView productPrice = new TextView(this);
            productPrice.setLayoutParams(params);

            productPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            productPrice.setPadding(16, 16, 16, 16);
            productPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            productPrice.setText(localProdDetails.get("prod_sub_total").toString());
            productPrice.setGravity(Gravity.CENTER);
            tr.addView(productPrice);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tr);
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
