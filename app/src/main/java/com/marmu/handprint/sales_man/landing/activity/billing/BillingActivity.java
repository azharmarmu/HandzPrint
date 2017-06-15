package com.marmu.handprint.sales_man.landing.activity.billing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.landing.activity.billing.add.AddBillingActivity;
import com.marmu.handprint.sales_man.landing.activity.billing.view_delete_edit.ViewDeleteEditBillingActivity;

public class BillingActivity extends AppCompatActivity {

    String salesKey, salesMan, salesRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_billing);

        salesKey = getIntent().getStringExtra("sales_key");
        salesMan = getIntent().getStringExtra("sales_man_name");
        salesRoute = getIntent().getStringExtra("sales_route");
    }

    public void addClick(View view) {
        Intent addBill = new Intent(BillingActivity.this, AddBillingActivity.class);
        addBill.putExtra("sales_key", salesKey);
        addBill.putExtra("sales_man_name", salesMan);
        addBill.putExtra("sales_route", salesRoute);
        startActivity(addBill);
    }

    public void viewDeleteEditClick(View view) {
        Intent billList = new Intent(BillingActivity.this, ViewDeleteEditBillingActivity.class);
        billList.putExtra("sales_key", salesKey);
        billList.putExtra("sales_man_name", salesMan);
        billList.putExtra("sales_route", salesRoute);
        startActivity(billList);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
