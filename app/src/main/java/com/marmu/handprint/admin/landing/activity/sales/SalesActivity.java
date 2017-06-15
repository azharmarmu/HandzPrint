package com.marmu.handprint.admin.landing.activity.sales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.marmu.handprint.R;
import com.marmu.handprint.admin.landing.activity.sales.party.PartyWiseActivity;
import com.marmu.handprint.admin.landing.activity.sales.product.ProductWiseActivity;

public class SalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sales);
    }

    public void partyWise(View view) {
        startActivity(new Intent(SalesActivity.this, PartyWiseActivity.class));
    }

    public void productWise(View view) {
        startActivity(new Intent(SalesActivity.this, ProductWiseActivity.class));
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
