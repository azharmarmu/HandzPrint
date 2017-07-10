package com.marmu.handprint.admin.landing.activity.sales_man;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.marmu.handprint.R;

public class SalesManActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sales_man);
    }

    public void addSalesManClick(View view) {
        startActivity(new Intent(SalesManActivity.this, AddSalesManActivity.class));
    }

    public void viewSalesManClick(View view) {
        startActivity(new Intent(SalesManActivity.this, ViewSalesManActivity.class));
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
