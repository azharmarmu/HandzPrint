package com.marmu.handprint.sales_man.landing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.landing.activity.billing.BillingActivity;
import com.marmu.handprint.sales_man.landing.activity.order.OrderActivity;
import com.marmu.handprint.sales_man.landing.activity.price.PriceActivity;
import com.marmu.handprint.sales_man.landing.activity.view_stock.ViewStockActivity;
import com.marmu.handprint.z_common.Constants;
import com.marmu.handprint.z_common.login.LoginActivity;

public class SalesManLandingActivity extends AppCompatActivity {

    DatabaseReference takenDBRef = Constants.DATABASE.getReference(Constants.ADMIN_TAKEN);

    private String salesKey;
    private String salesMan;
    private String salesRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_landing);

        salesKey = getIntent().getStringExtra("sales_key");
        salesMan = getIntent().getStringExtra("sales_man_name");
        salesRoute = getIntent().getStringExtra("sales_route");
    }

    public void billingClick(View view) {
        Intent billing = new Intent(SalesManLandingActivity.this, BillingActivity.class);
        billing.putExtra("sales_key", salesKey);
        billing.putExtra("sales_man_name", salesMan);
        billing.putExtra("sales_route", salesRoute);
        startActivity(billing);
    }

    public void priceClick(View view) {
        Intent price = new Intent(SalesManLandingActivity.this, PriceActivity.class);
        price.putExtra("sales_key", salesKey);
        price.putExtra("sales_man_name", salesMan);
        price.putExtra("sales_route", salesRoute);
        startActivity(price);
    }

    public void viewStockClick(View view) {
        Intent viewStock = new Intent(SalesManLandingActivity.this, ViewStockActivity.class);
        viewStock.putExtra("sales_key", salesKey);
        viewStock.putExtra("sales_man_name", salesMan);
        viewStock.putExtra("sales_route", salesRoute);
        startActivity(viewStock);
    }

    public void orderClick(View view) {
        Intent order = new Intent(SalesManLandingActivity.this, OrderActivity.class);
        order.putExtra("sales_key", salesKey);
        order.putExtra("sales_man_name", salesMan);
        order.putExtra("sales_route", salesRoute);
        startActivity(order);
    }

    public void closeClick(View view) {
        takenDBRef.child(salesKey).child("process").setValue("closed");
        finish();
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void signOut(View view) {
        Constants.AUTH.signOut();
        startActivity(new Intent(SalesManLandingActivity.this, LoginActivity.class));
        finish();
    }
}
